package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Persistencia offline en JSON simple (sin dependencias externas).
 * Formato: {@code [{"username":"Ada","score":100}, ...]}
 */
public class FileScoreRepository implements ScoreRepository {

    private static final Logger LOGGER = Logger.getLogger(FileScoreRepository.class.getName());

    private final Path scoresPath;
    private final Path legacyHighscorePath;

    public FileScoreRepository() {
        this(Path.of("scores.json"), Path.of("Highscore.txt"));
    }

    public FileScoreRepository(Path scoresPath) {
        this(scoresPath, Path.of("Highscore.txt"));
    }

    public FileScoreRepository(Path scoresPath, Path legacyHighscorePath) {
        this.scoresPath = scoresPath;
        this.legacyHighscorePath = legacyHighscorePath;
        migrateLegacyHighscoreIfNeeded();
    }

    @Override
    public synchronized boolean save(ScoreEntry entry) {
        List<ScoreEntry> all = loadAll();
        all.add(entry);
        return writeAll(all);
    }

    @Override
    public synchronized List<ScoreEntry> top(int limit) {
        int safeLimit = Math.max(0, limit);
        return loadAll().stream()
                .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
                .limit(safeLimit)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized boolean clear() {
        return writeAll(new ArrayList<>());
    }

    public Path getScoresPath() {
        return scoresPath;
    }

    private List<ScoreEntry> loadAll() {
        if (!Files.exists(scoresPath)) {
            return new ArrayList<>();
        }
        try {
            String content = Files.readString(scoresPath, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return new ArrayList<>();
            }
            return parseJsonArray(content);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudo leer " + scoresPath, ex);
            return new ArrayList<>();
        }
    }

    private boolean writeAll(List<ScoreEntry> entries) {
        try {
            Path parent = scoresPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            StringBuilder json = new StringBuilder();
            json.append('[');
            for (int i = 0; i < entries.size(); i++) {
                ScoreEntry e = entries.get(i);
                if (i > 0) {
                    json.append(',');
                }
                json.append("{\"username\":\"")
                        .append(escape(e.getUsername()))
                        .append("\",\"score\":")
                        .append(e.getScore())
                        .append('}');
            }
            json.append(']');
            Files.writeString(scoresPath, json.toString(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "No se pudo escribir " + scoresPath, ex);
            return false;
        }
    }

    private void migrateLegacyHighscoreIfNeeded() {
        if (Files.exists(scoresPath)) {
            return;
        }
        Path legacy = legacyHighscorePath;
        if (!Files.exists(legacy)) {
            return;
        }
        try (Scanner scanner = new Scanner(legacy, StandardCharsets.UTF_8)) {
            if (scanner.hasNextInt()) {
                int legacyScore = scanner.nextInt();
                if (legacyScore > 0) {
                    writeAll(List.of(new ScoreEntry("Legacy", legacyScore)));
                    LOGGER.info("Migrado Highscore.txt -> scores.json (Legacy=" + legacyScore + ")");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudo migrar Highscore.txt", ex);
        }
    }

    static List<ScoreEntry> parseJsonArray(String content) {
        List<ScoreEntry> result = new ArrayList<>();
        String body = content.trim();
        if (body.startsWith("[")) {
            body = body.substring(1);
        }
        if (body.endsWith("]")) {
            body = body.substring(0, body.length() - 1);
        }
        body = body.trim();
        if (body.isEmpty()) {
            return result;
        }

        // Divide objetos { ... } de primer nivel
        int depth = 0;
        int start = -1;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    ScoreEntry entry = parseObject(body.substring(start, i + 1));
                    if (entry != null) {
                        result.add(entry);
                    }
                    start = -1;
                }
            }
        }
        return result;
    }

    private static ScoreEntry parseObject(String objectJson) {
        String username = extractString(objectJson, "username");
        Integer score = extractInt(objectJson, "score");
        if (username == null || score == null) {
            return null;
        }
        try {
            return new ScoreEntry(username, Math.max(0, score));
        } catch (IllegalArgumentException ex) {
            // Nombres históricos no válidos: conservar el puntaje bajo "Legacy"
            return new ScoreEntry("Legacy", Math.max(0, score));
        }
    }

    private static String extractString(String json, String key) {
        String marker = "\"" + key + "\"";
        int keyIdx = json.indexOf(marker);
        if (keyIdx < 0) {
            return null;
        }
        int colon = json.indexOf(':', keyIdx + marker.length());
        if (colon < 0) {
            return null;
        }
        int firstQuote = json.indexOf('"', colon + 1);
        if (firstQuote < 0) {
            return null;
        }
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (secondQuote < 0) {
            return null;
        }
        return unescape(json.substring(firstQuote + 1, secondQuote));
    }

    private static Integer extractInt(String json, String key) {
        String marker = "\"" + key + "\"";
        int keyIdx = json.indexOf(marker);
        if (keyIdx < 0) {
            return null;
        }
        int colon = json.indexOf(':', keyIdx + marker.length());
        if (colon < 0) {
            return null;
        }
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }
        int start = i;
        while (i < json.length() && (Character.isDigit(json.charAt(i)) || json.charAt(i) == '-')) {
            i++;
        }
        if (start == i) {
            return null;
        }
        return Integer.parseInt(json.substring(start, i));
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}

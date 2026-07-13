package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia local embebida en SQLite: top scores + historial de partidas.
 * Archivo por defecto: {@code data/space-chemistry.db}.
 */
public class SqliteScoreRepository implements ScoreRepository {

    private static final Logger LOGGER = Logger.getLogger(SqliteScoreRepository.class.getName());

    private final Path databasePath;
    private final Path legacyJsonPath;
    private final Path legacyHighscorePath;

    public SqliteScoreRepository() {
        this(Path.of("data", "space-chemistry.db"), Path.of("scores.json"), Path.of("Highscore.txt"));
    }

    public SqliteScoreRepository(Path databasePath) {
        this(databasePath, databasePath.getParent() != null
                ? databasePath.getParent().resolve("scores.json")
                : Path.of("scores.json"),
                databasePath.getParent() != null
                        ? databasePath.getParent().resolve("Highscore.txt")
                        : Path.of("Highscore.txt"));
    }

    public SqliteScoreRepository(Path databasePath, Path legacyJsonPath, Path legacyHighscorePath) {
        this.databasePath = databasePath;
        this.legacyJsonPath = legacyJsonPath;
        this.legacyHighscorePath = legacyHighscorePath;
        initialize();
    }

    public Path getDatabasePath() {
        return databasePath;
    }

    private void initialize() {
        try {
            Path parent = databasePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Connection cn = open(); Statement st = cn.createStatement()) {
                st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS runs (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT NOT NULL,
                            score INTEGER NOT NULL,
                            level_reached INTEGER NOT NULL DEFAULT 1,
                            difficulty INTEGER NOT NULL DEFAULT 1,
                            won INTEGER NOT NULL DEFAULT 0,
                            played_at TEXT NOT NULL
                        )
                        """);
                st.executeUpdate("""
                        CREATE INDEX IF NOT EXISTS idx_runs_score
                        ON runs(score DESC)
                        """);
                st.executeUpdate("""
                        CREATE INDEX IF NOT EXISTS idx_runs_played_at
                        ON runs(played_at DESC)
                        """);
            }
            migrateFromLegacyFilesIfEmpty();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "No se pudo inicializar SQLite en " + databasePath, ex);
            throw new IllegalStateException("No se pudo inicializar la base local de scores.", ex);
        }
    }

    private Connection open() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath.toAbsolutePath());
    }

    private void migrateFromLegacyFilesIfEmpty() {
        if (!isEmpty()) {
            return;
        }
        // Importa scores.json / Highscore.txt una sola vez.
        FileScoreRepository legacy = new FileScoreRepository(legacyJsonPath, legacyHighscorePath);
        List<ScoreEntry> old = legacy.top(1000);
        if (old.isEmpty()) {
            return;
        }
        for (ScoreEntry entry : old) {
            saveRun(new RunEntry(entry.getUsername(), entry.getScore(), 1, RunEntry.DIFFICULTY_EASY, false));
        }
        LOGGER.info("Migradas " + old.size() + " entradas legacy a SQLite (" + databasePath + ")");
    }

    private boolean isEmpty() {
        try (Connection cn = open();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM runs")) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (Exception ex) {
            return true;
        }
    }

    @Override
    public synchronized boolean save(ScoreEntry entry) {
        return saveRun(new RunEntry(
                entry.getUsername(),
                entry.getScore(),
                1,
                RunEntry.DIFFICULTY_EASY,
                false));
    }

    public synchronized boolean saveRun(RunEntry run) {
        String sql = """
                INSERT INTO runs(username, score, level_reached, difficulty, won, played_at)
                VALUES(?, ?, ?, ?, ?, ?)
                """;
        try (Connection cn = open(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, run.getUsername());
            ps.setInt(2, run.getScore());
            ps.setInt(3, run.getLevelReached());
            ps.setInt(4, run.getDifficulty());
            ps.setInt(5, run.isWon() ? 1 : 0);
            ps.setString(6, run.getPlayedAt().toString());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error guardando partida en SQLite", ex);
            return false;
        }
    }

    @Override
    public synchronized List<ScoreEntry> top(int limit) {
        List<ScoreEntry> result = new ArrayList<>();
        for (RunEntry run : topRuns(limit)) {
            result.add(run.toScoreEntry());
        }
        return result;
    }

    public synchronized List<RunEntry> topRuns(int limit) {
        int safeLimit = Math.max(0, limit);
        List<RunEntry> result = new ArrayList<>();
        if (safeLimit == 0) {
            return result;
        }
        String sql = """
                SELECT id, username, score, level_reached, difficulty, won, played_at
                FROM runs
                ORDER BY score DESC, played_at DESC
                LIMIT ?
                """;
        try (Connection cn = open(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, safeLimit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRun(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error leyendo top desde SQLite", ex);
        }
        return result;
    }

    public synchronized List<RunEntry> history(int limit) {
        int safeLimit = Math.max(0, limit);
        List<RunEntry> result = new ArrayList<>();
        if (safeLimit == 0) {
            return result;
        }
        String sql = """
                SELECT id, username, score, level_reached, difficulty, won, played_at
                FROM runs
                ORDER BY played_at DESC, id DESC
                LIMIT ?
                """;
        try (Connection cn = open(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, safeLimit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRun(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error leyendo historial desde SQLite", ex);
        }
        return result;
    }

    public synchronized List<RunEntry> historyFor(String username, int limit) {
        String validated = ScoreEntry.validateUsername(username);
        int safeLimit = Math.max(0, limit);
        List<RunEntry> result = new ArrayList<>();
        if (safeLimit == 0) {
            return result;
        }
        String sql = """
                SELECT id, username, score, level_reached, difficulty, won, played_at
                FROM runs
                WHERE username = ?
                ORDER BY played_at DESC, id DESC
                LIMIT ?
                """;
        try (Connection cn = open(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, validated);
            ps.setInt(2, safeLimit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRun(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error leyendo historial de jugador desde SQLite", ex);
        }
        return result;
    }

    @Override
    public synchronized boolean clear() {
        try (Connection cn = open(); Statement st = cn.createStatement()) {
            st.executeUpdate("DELETE FROM runs");
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error limpiando SQLite", ex);
            return false;
        }
    }

    private static RunEntry mapRun(ResultSet rs) throws Exception {
        Instant playedAt;
        String raw = rs.getString("played_at");
        try {
            playedAt = Instant.parse(raw);
        } catch (Exception ex) {
            try {
                playedAt = Timestamp.valueOf(raw).toInstant();
            } catch (Exception ignored) {
                playedAt = Instant.EPOCH;
            }
        }
        return new RunEntry(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getInt("score"),
                rs.getInt("level_reached"),
                rs.getInt("difficulty"),
                rs.getInt("won") == 1,
                playedAt);
    }
}

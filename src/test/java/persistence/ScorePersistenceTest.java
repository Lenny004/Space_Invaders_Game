package persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreEntryTest {

    @Test
    void acceptsValidUsername() {
        ScoreEntry entry = new ScoreEntry("Ada123", 50);
        assertEquals("Ada123", entry.getUsername());
        assertEquals(50, entry.getScore());
    }

    @Test
    void trimsUsername() {
        assertEquals("Lenny", new ScoreEntry("  Lenny  ", 1).getUsername());
    }

    @Test
    void rejectsEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> new ScoreEntry("   ", 10));
    }

    @Test
    void rejectsTooLongUsername() {
        assertThrows(IllegalArgumentException.class, () -> new ScoreEntry("ABCDEFGHIJKLMNOP", 10));
    }

    @Test
    void rejectsSpacesAndSymbols() {
        assertFalse(ScoreEntry.isValidUsername("Ada Lovelace"));
        assertFalse(ScoreEntry.isValidUsername("Ada!"));
        assertTrue(ScoreEntry.isValidUsername("Ada42"));
    }

    @Test
    void rejectsNegativeScore() {
        assertThrows(IllegalArgumentException.class, () -> new ScoreEntry("Ada", -1));
    }
}

class FileScoreRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAndTopOrdersDescending() {
        Path file = tempDir.resolve("scores.json");
        FileScoreRepository repo = new FileScoreRepository(file);

        assertTrue(repo.save(new ScoreEntry("Low", 10)));
        assertTrue(repo.save(new ScoreEntry("High", 100)));
        assertTrue(repo.save(new ScoreEntry("Mid", 50)));

        List<ScoreEntry> top = repo.top(2);
        assertEquals(2, top.size());
        assertEquals("High", top.get(0).getUsername());
        assertEquals(100, top.get(0).getScore());
        assertEquals("Mid", top.get(1).getUsername());
    }

    @Test
    void topRespectsLimit() {
        FileScoreRepository repo = new FileScoreRepository(tempDir.resolve("scores.json"));
        repo.save(new ScoreEntry("A", 1));
        repo.save(new ScoreEntry("B", 2));
        repo.save(new ScoreEntry("C", 3));
        assertEquals(1, repo.top(1).size());
        assertEquals(0, repo.top(0).size());
    }

    @Test
    void clearEmptiesFile() throws Exception {
        Path file = tempDir.resolve("scores.json");
        FileScoreRepository repo = new FileScoreRepository(file);
        repo.save(new ScoreEntry("Ada", 99));
        assertTrue(repo.clear());
        assertTrue(repo.top(5).isEmpty());
        assertTrue(Files.exists(file));
    }

    @Test
    void migratesLegacyHighscoreTxt() throws Exception {
        Path scores = tempDir.resolve("scores.json");
        Path legacy = tempDir.resolve("Highscore.txt");
        Files.writeString(legacy, "777");

        FileScoreRepository repo = new FileScoreRepository(scores, legacy);
        List<ScoreEntry> top = repo.top(1);
        assertEquals(1, top.size());
        assertEquals("Legacy", top.get(0).getUsername());
        assertEquals(777, top.get(0).getScore());
        assertTrue(Files.exists(scores));
    }

    @Test
    void parsesJsonArray() {
        List<ScoreEntry> parsed = FileScoreRepository.parseJsonArray(
                "[{\"username\":\"Legacy\",\"score\":777},{\"username\":\"Ada\",\"score\":10}]");
        assertEquals(2, parsed.size());
        assertEquals("Legacy", parsed.get(0).getUsername());
        assertEquals(777, parsed.get(0).getScore());
    }

    @Test
    void scoreServicePrefersFileWhenJdbcUnavailable() {
        Path file = tempDir.resolve("scores.json");
        FileScoreRepository fileRepo = new FileScoreRepository(file);
        ScoreRepository jdbcDown = new ScoreRepository() {
            @Override
            public boolean save(ScoreEntry entry) {
                return false;
            }

            @Override
            public List<ScoreEntry> top(int limit) {
                return List.of();
            }

            @Override
            public boolean clear() {
                return false;
            }

            @Override
            public boolean isAvailable() {
                return false;
            }
        };

        ScoreService service = new ScoreService(fileRepo, jdbcDown);
        assertTrue(service.save("Player1", 42));
        assertEquals(42, service.bestScore());
        assertEquals("Player1", service.top(1).get(0).getUsername());
        assertFalse(service.isJdbcAvailable());
    }
}

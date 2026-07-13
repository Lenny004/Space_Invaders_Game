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
        FileScoreRepository repo = new FileScoreRepository(file, tempDir.resolve("no-legacy.txt"));

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
        Path dir = tempDir.resolve("limit");
        FileScoreRepository repo = new FileScoreRepository(
                dir.resolve("scores.json"), dir.resolve("Highscore.txt"));
        repo.save(new ScoreEntry("A", 1));
        repo.save(new ScoreEntry("B", 2));
        repo.save(new ScoreEntry("C", 3));
        assertEquals(1, repo.top(1).size());
        assertEquals(0, repo.top(0).size());
    }

    @Test
    void clearEmptiesFile() throws Exception {
        Path file = tempDir.resolve("clear-scores.json");
        FileScoreRepository repo = new FileScoreRepository(file, tempDir.resolve("clear-legacy.txt"));
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
}

class SqliteScoreRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void saveRunAndTopOrdersDescending() {
        Path db = tempDir.resolve("game.db");
        SqliteScoreRepository repo = new SqliteScoreRepository(
                db, tempDir.resolve("scores.json"), tempDir.resolve("Highscore.txt"));

        assertTrue(repo.saveRun(new RunEntry("Low", 10, 2, RunEntry.DIFFICULTY_EASY, false)));
        assertTrue(repo.saveRun(new RunEntry("High", 100, 8, RunEntry.DIFFICULTY_HARD, true)));
        assertTrue(repo.saveRun(new RunEntry("Mid", 50, 4, RunEntry.DIFFICULTY_MEDIUM, false)));

        List<RunEntry> top = repo.topRuns(2);
        assertEquals(2, top.size());
        assertEquals("High", top.get(0).getUsername());
        assertEquals(100, top.get(0).getScore());
        assertTrue(top.get(0).isWon());
        assertEquals(8, top.get(0).getLevelReached());
        assertEquals("Mid", top.get(1).getUsername());
    }

    @Test
    void historyReturnsNewestFirst() {
        Path db = tempDir.resolve("history.db");
        SqliteScoreRepository repo = new SqliteScoreRepository(
                db, tempDir.resolve("no.json"), tempDir.resolve("no.txt"));

        repo.saveRun(new RunEntry("First", 10, 1, 1, false));
        repo.saveRun(new RunEntry("Second", 20, 2, 2, true));

        List<RunEntry> history = repo.history(5);
        assertEquals(2, history.size());
        assertEquals("Second", history.get(0).getUsername());
        assertEquals("First", history.get(1).getUsername());
    }

    @Test
    void historyForFiltersByPlayer() {
        Path db = tempDir.resolve("player.db");
        SqliteScoreRepository repo = new SqliteScoreRepository(
                db, tempDir.resolve("no.json"), tempDir.resolve("no.txt"));

        repo.saveRun(new RunEntry("Ada", 10, 1, 1, false));
        repo.saveRun(new RunEntry("Bob", 99, 5, 3, true));
        repo.saveRun(new RunEntry("Ada", 40, 3, 2, false));

        List<RunEntry> ada = repo.historyFor("Ada", 10);
        assertEquals(2, ada.size());
        assertTrue(ada.stream().allMatch(r -> r.getUsername().equals("Ada")));
    }

    @Test
    void migratesLegacyJsonIntoSqlite() throws Exception {
        Path dir = tempDir.resolve("migrate");
        Files.createDirectories(dir);
        Path json = dir.resolve("scores.json");
        Files.writeString(json, "[{\"username\":\"Legacy\",\"score\":777}]");

        SqliteScoreRepository repo = new SqliteScoreRepository(
                dir.resolve("space-chemistry.db"), json, dir.resolve("Highscore.txt"));

        List<ScoreEntry> top = repo.top(1);
        assertEquals(1, top.size());
        assertEquals("Legacy", top.get(0).getUsername());
        assertEquals(777, top.get(0).getScore());
    }

    @Test
    void scoreServiceUsesSqlite() {
        Path dir = tempDir.resolve("service");
        SqliteScoreRepository repo = new SqliteScoreRepository(
                dir.resolve("game.db"), dir.resolve("scores.json"), dir.resolve("Highscore.txt"));
        ScoreService service = new ScoreService(repo);

        assertTrue(service.saveRun(new RunEntry("Player1", 42, 3, RunEntry.DIFFICULTY_MEDIUM, false)));
        assertEquals(42, service.bestScore());
        assertEquals(1, service.history(5).size());
        assertEquals(3, service.history(5).get(0).getLevelReached());
        assertFalse(service.isJdbcAvailable());
    }

    @Test
    void clearRemovesRuns() {
        Path db = tempDir.resolve("clear.db");
        SqliteScoreRepository repo = new SqliteScoreRepository(
                db, tempDir.resolve("no.json"), tempDir.resolve("no.txt"));
        repo.saveRun(new RunEntry("Ada", 10, 1, 1, false));
        assertTrue(repo.clear());
        assertTrue(repo.top(5).isEmpty());
        assertTrue(repo.history(5).isEmpty());
    }
}

class RunEntryTest {

    @Test
    void normalizesInvalidDifficulty() {
        assertEquals(RunEntry.DIFFICULTY_EASY, RunEntry.normalizeDifficulty(0));
        assertEquals(RunEntry.DIFFICULTY_EASY, RunEntry.normalizeDifficulty(99));
        assertEquals("Medio", RunEntry.difficultyLabel(2));
    }
}

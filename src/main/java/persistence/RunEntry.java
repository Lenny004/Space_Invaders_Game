package persistence;

import java.time.Instant;
import java.util.Objects;

/**
 * Partida guardada localmente (score + metadatos de historial).
 */
public final class RunEntry {

    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;

    private final long id;
    private final String username;
    private final int score;
    private final int levelReached;
    private final int difficulty;
    private final boolean won;
    private final Instant playedAt;

    public RunEntry(String username, int score, int levelReached, int difficulty, boolean won) {
        this(0, username, score, levelReached, difficulty, won, Instant.now());
    }

    public RunEntry(
            long id,
            String username,
            int score,
            int levelReached,
            int difficulty,
            boolean won,
            Instant playedAt) {
        this.id = id;
        this.username = ScoreEntry.validateUsername(username);
        if (score < 0) {
            throw new IllegalArgumentException("El puntaje no puede ser negativo.");
        }
        this.score = score;
        this.levelReached = Math.max(1, levelReached);
        this.difficulty = normalizeDifficulty(difficulty);
        this.won = won;
        this.playedAt = playedAt != null ? playedAt : Instant.now();
    }

    public static int normalizeDifficulty(int difficulty) {
        if (difficulty < DIFFICULTY_EASY || difficulty > DIFFICULTY_HARD) {
            return DIFFICULTY_EASY;
        }
        return difficulty;
    }

    public static String difficultyLabel(int difficulty) {
        return switch (normalizeDifficulty(difficulty)) {
            case DIFFICULTY_MEDIUM -> "Medio";
            case DIFFICULTY_HARD -> "Dificil";
            default -> "Facil";
        };
    }

    public ScoreEntry toScoreEntry() {
        return new ScoreEntry(username, score);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean isWon() {
        return won;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RunEntry that)) {
            return false;
        }
        return id == that.id
                && score == that.score
                && levelReached == that.levelReached
                && difficulty == that.difficulty
                && won == that.won
                && Objects.equals(username, that.username)
                && Objects.equals(playedAt, that.playedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, score, levelReached, difficulty, won, playedAt);
    }

    @Override
    public String toString() {
        return username + "=" + score + " L" + levelReached + (won ? " W" : "");
    }
}

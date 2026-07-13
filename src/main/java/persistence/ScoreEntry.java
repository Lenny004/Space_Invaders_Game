package persistence;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entrada de puntaje inmutable con validación básica de nombre.
 */
public final class ScoreEntry {

    public static final int MAX_USERNAME_LENGTH = 15;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    private final String username;
    private final int score;

    public ScoreEntry(String username, int score) {
        this.username = validateUsername(username);
        if (score < 0) {
            throw new IllegalArgumentException("El puntaje no puede ser negativo.");
        }
        this.score = score;
    }

    public static String validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        String trimmed = username.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (trimmed.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar " + MAX_USERNAME_LENGTH + " caracteres.");
        }
        if (!USERNAME_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y números.");
        }
        return trimmed;
    }

    public static boolean isValidUsername(String username) {
        try {
            validateUsername(username);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScoreEntry)) {
            return false;
        }
        ScoreEntry that = (ScoreEntry) o;
        return score == that.score && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, score);
    }

    @Override
    public String toString() {
        return username + "=" + score;
    }
}

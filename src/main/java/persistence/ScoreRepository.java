package persistence;

import java.util.List;

/**
 * Contrato de persistencia de puntajes (archivo, JDBC, memoria, etc.).
 */
public interface ScoreRepository {

    /**
     * Guarda una entrada de puntaje.
     *
     * @return {@code true} si se persistió correctamente
     */
    boolean save(ScoreEntry entry);

    /**
     * Devuelve los mejores puntajes ordenados de mayor a menor.
     */
    List<ScoreEntry> top(int limit);

    /**
     * Elimina o reinicia los registros de high score gestionados por este repositorio.
     */
    boolean clear();

    /**
     * Indica si el backend está disponible (siempre true para archivo; JDBC solo con conexión).
     */
    default boolean isAvailable() {
        return true;
    }
}

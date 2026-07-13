package persistence;

import java.util.List;
import java.util.logging.Logger;

/**
 * Fachada de puntajes e historial para la UI.
 * Persistencia local con SQLite ({@code data/space-chemistry.db}).
 */
public class ScoreService {

    private static final Logger LOGGER = Logger.getLogger(ScoreService.class.getName());

    private final SqliteScoreRepository repository;

    private static final class Holder {
        private static final ScoreService INSTANCE = new ScoreService();
    }

    public static ScoreService getInstance() {
        return Holder.INSTANCE;
    }

    public ScoreService() {
        this(new SqliteScoreRepository());
    }

    public ScoreService(SqliteScoreRepository repository) {
        this.repository = repository;
    }

    /**
     * Guarda una partida mínima (compatibilidad). Preferir {@link #saveRun(RunEntry)}.
     */
    public boolean save(String username, int score) {
        return saveRun(new RunEntry(username, score, 1, RunEntry.DIFFICULTY_EASY, false));
    }

    public boolean saveRun(RunEntry run) {
        boolean ok = repository.saveRun(run);
        if (!ok) {
            LOGGER.warning("No se pudo guardar la partida en SQLite.");
        }
        return ok;
    }

    public List<ScoreEntry> top(int limit) {
        return repository.top(limit);
    }

    public List<RunEntry> topRuns(int limit) {
        return repository.topRuns(limit);
    }

    /** Historial reciente (más nuevas primero). */
    public List<RunEntry> history(int limit) {
        return repository.history(limit);
    }

    public List<RunEntry> historyFor(String username, int limit) {
        return repository.historyFor(username, limit);
    }

    public int bestScore() {
        List<ScoreEntry> best = top(1);
        return best.isEmpty() ? 0 : best.get(0).getScore();
    }

    public boolean clearHighScores() {
        return repository.clear();
    }

    /**
     * @deprecated Ya no hay SQL Server; siempre local.
     */
    @Deprecated
    public boolean isJdbcAvailable() {
        return false;
    }

    public boolean isAvailable() {
        return repository.isAvailable();
    }
}

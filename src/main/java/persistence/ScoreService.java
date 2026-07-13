package persistence;

import java.util.List;
import java.util.logging.Logger;

/**
 * Fachada de puntajes para la UI.
 * Siempre persiste en archivo; JDBC es opcional cuando hay conexión.
 */
public class ScoreService {

    private static final Logger LOGGER = Logger.getLogger(ScoreService.class.getName());

    private final ScoreRepository fileRepository;
    private final ScoreRepository jdbcRepository;

    private static final class Holder {
        private static final ScoreService INSTANCE = new ScoreService();
    }

    public static ScoreService getInstance() {
        return Holder.INSTANCE;
    }

    public ScoreService() {
        this(new FileScoreRepository(), new JdbcScoreRepository());
    }

    public ScoreService(ScoreRepository fileRepository, ScoreRepository jdbcRepository) {
        this.fileRepository = fileRepository;
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Guarda siempre en archivo; también en SQL si está disponible.
     */
    public boolean save(String username, int score) {
        ScoreEntry entry = new ScoreEntry(username, score);
        boolean fileOk = fileRepository.save(entry);
        boolean jdbcOk = false;
        if (jdbcRepository != null && jdbcRepository.isAvailable()) {
            jdbcOk = jdbcRepository.save(entry);
            if (!jdbcOk) {
                LOGGER.warning("No se pudo guardar el score en SQL Server; quedó en archivo local.");
            }
        }
        return fileOk || jdbcOk;
    }

    /**
     * Prefiere el top de SQL si hay datos; si no, usa el archivo local.
     */
    public List<ScoreEntry> top(int limit) {
        if (jdbcRepository != null && jdbcRepository.isAvailable()) {
            List<ScoreEntry> fromDb = jdbcRepository.top(limit);
            if (!fromDb.isEmpty()) {
                return fromDb;
            }
        }
        return fileRepository.top(limit);
    }

    /**
     * Mejor puntaje conocido (0 si no hay registros).
     */
    public int bestScore() {
        List<ScoreEntry> best = top(1);
        return best.isEmpty() ? 0 : best.get(0).getScore();
    }

    /**
     * Reinicia high scores locales (y SQL si está disponible).
     */
    public boolean clearHighScores() {
        boolean fileOk = fileRepository.clear();
        boolean jdbcOk = true;
        if (jdbcRepository != null && jdbcRepository.isAvailable()) {
            jdbcOk = jdbcRepository.clear();
        }
        return fileOk && jdbcOk;
    }

    public boolean isJdbcAvailable() {
        return jdbcRepository != null && jdbcRepository.isAvailable();
    }
}

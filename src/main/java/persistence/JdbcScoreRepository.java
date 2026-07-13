package persistence;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @deprecated El juego es local; usar {@link SqliteScoreRepository}.
 * Se mantiene vacío para no romper referencias antiguas.
 */
@Deprecated
public class JdbcScoreRepository implements ScoreRepository {

    private static final Logger LOGGER = Logger.getLogger(JdbcScoreRepository.class.getName());

    public JdbcScoreRepository() {
        LOGGER.fine("JdbcScoreRepository está deprecado; no conecta a SQL Server.");
    }

    public JdbcScoreRepository(Connection connection) {
        this();
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean save(ScoreEntry entry) {
        return false;
    }

    @Override
    public List<ScoreEntry> top(int limit) {
        return Collections.emptyList();
    }

    @Override
    public boolean clear() {
        return false;
    }
}

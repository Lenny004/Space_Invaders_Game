package persistence;

import ClaseConexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de puntajes en SQL Server.
 * La conexión se abre de forma perezosa; si falla, {@link #isAvailable()} es {@code false}.
 */
public class JdbcScoreRepository implements ScoreRepository {

    private static final Logger LOGGER = Logger.getLogger(JdbcScoreRepository.class.getName());

    private Connection connection;
    private boolean connectAttempted;
    private final boolean useDefaultConnector;

    public JdbcScoreRepository() {
        this.connection = null;
        this.connectAttempted = false;
        this.useDefaultConnector = true;
    }

    public JdbcScoreRepository(Connection connection) {
        this.connection = connection;
        this.connectAttempted = true;
        this.useDefaultConnector = false;
    }

    private synchronized void ensureConnected() {
        if (connectAttempted || !useDefaultConnector) {
            return;
        }
        connectAttempted = true;
        try {
            connection = new Conexion().conectar();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudo inicializar JDBC para scores", ex);
            connection = null;
        }
    }

    @Override
    public boolean isAvailable() {
        ensureConnected();
        try {
            return connection != null && !connection.isClosed();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean save(ScoreEntry entry) {
        if (!isAvailable()) {
            return false;
        }
        String sql = "INSERT INTO highscore(username, score) VALUES(?, ?)";
        try (PreparedStatement cmd = connection.prepareStatement(sql)) {
            cmd.setString(1, entry.getUsername());
            cmd.setInt(2, entry.getScore());
            return cmd.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error guardando score en SQL Server", ex);
            return false;
        }
    }

    @Override
    public List<ScoreEntry> top(int limit) {
        List<ScoreEntry> result = new ArrayList<>();
        if (!isAvailable() || limit <= 0) {
            return result;
        }
        int safeLimit = Math.min(limit, 100);
        String sql = "SELECT TOP " + safeLimit + " username, score FROM highscore ORDER BY score DESC";
        try (PreparedStatement cmd = connection.prepareStatement(sql);
             ResultSet rs = cmd.executeQuery()) {
            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                try {
                    result.add(new ScoreEntry(username, score));
                } catch (IllegalArgumentException ex) {
                    result.add(new ScoreEntry("Legacy", Math.max(0, score)));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error leyendo top scores de SQL Server", ex);
        }
        return result;
    }

    @Override
    public boolean clear() {
        if (!isAvailable()) {
            return false;
        }
        try (PreparedStatement cmd = connection.prepareStatement("DELETE FROM highscore")) {
            cmd.executeUpdate();
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error limpiando scores en SQL Server", ex);
            return false;
        }
    }
}

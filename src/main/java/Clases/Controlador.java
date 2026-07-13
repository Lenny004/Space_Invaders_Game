package Clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import persistence.JdbcScoreRepository;
import persistence.ScoreEntry;
import persistence.ScoreService;

/**
 * @deprecated Usar {@link persistence.ScoreService}. Se mantiene por compatibilidad temporal.
 */
@Deprecated
public class Controlador {

    private final ScoreService scoreService = ScoreService.getInstance();
    private final JdbcScoreRepository jdbc = new JdbcScoreRepository();
    private Integer idU;
    private String username;
    private Integer score;

    public Connection getCn() {
        return null;
    }

    public void setCn(Connection cn) {
        // no-op: la conexión la gestiona JdbcScoreRepository
    }

    public Integer getIdU() {
        return idU;
    }

    public void setidU(Integer idU) {
        this.idU = idU;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer scoreU() {
        return score;
    }

    public void setScoreU(Integer score) {
        this.score = score;
    }

    public Controlador() {
    }

    public boolean consultarId() {
        return false;
    }

    public boolean consultarTopScores() {
        List<ScoreEntry> top = scoreService.top(1);
        if (top.isEmpty()) {
            return false;
        }
        username = top.get(0).getUsername();
        score = top.get(0).getScore();
        return true;
    }

    public boolean guardarScore() {
        if (username == null || score == null) {
            return false;
        }
        return scoreService.save(username, score);
    }

    /**
     * @deprecated Preferir {@link ScoreService#top(int)}.
     */
    @Deprecated
    public ResultSet getTabla() {
        return null;
    }

    public boolean isJdbcAvailable() {
        return jdbc.isAvailable();
    }
}

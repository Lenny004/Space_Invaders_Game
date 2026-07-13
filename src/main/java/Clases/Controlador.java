package Clases;

import java.sql.Connection;
import java.util.List;
import persistence.ScoreEntry;
import persistence.ScoreService;

/**
 * @deprecated Usar {@link persistence.ScoreService}. Se mantiene por compatibilidad temporal.
 */
@Deprecated
public class Controlador {

    private final ScoreService scoreService = ScoreService.getInstance();
    private Integer idU;
    private String username;
    private Integer score;

    public Connection getCn() {
        return null;
    }

    public void setCn(Connection cn) {
        // no-op: persistencia local vía ScoreService / SQLite
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

    public boolean GuardarScore() {
        if (username == null || score == null) {
            return false;
        }
        return scoreService.save(username, score);
    }

    public boolean InsertarScore(String username, int score) {
        return scoreService.save(username, score);
    }

    /**
     * @deprecated Preferir {@link ScoreService#top(int)}.
     */
    @Deprecated
    public List<ScoreEntry> ListarTop(int limit) {
        return scoreService.top(limit);
    }

    public boolean BorrarScores() {
        return scoreService.clearHighScores();
    }
}

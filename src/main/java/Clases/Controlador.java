package Clases;

import ClaseConexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Acceso a puntajes en SQL Server.
 */
public class Controlador {

    private Connection cn;
    private Integer idU;
    private String username;
    private Integer score;

    public Connection getCn() {
        return cn;
    }

    public void setCn(Connection cn) {
        this.cn = cn;
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
        Conexion con = new Conexion();
        cn = con.conectar();
    }

    public boolean consultarId() {
        boolean res = false;
        if (cn == null) {
            return false;
        }
        String sql = "SELECT id, username, score FROM highscore WHERE id = (SELECT MAX(id) FROM highscore)";
        try (PreparedStatement cmd = cn.prepareStatement(sql);
             ResultSet rs = cmd.executeQuery()) {
            if (rs.next()) {
                res = true;
                idU = rs.getInt(1);
                username = rs.getString(2);
                score = rs.getInt(3);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return res;
    }

    public boolean consultarTopScores() {
        boolean res = false;
        if (cn == null) {
            return false;
        }
        String sql = "SELECT TOP 5 username, score FROM highscore ORDER BY score DESC";
        try (PreparedStatement cmd = cn.prepareStatement(sql);
             ResultSet rs = cmd.executeQuery()) {
            if (rs.next()) {
                res = true;
                username = rs.getString(1);
                score = rs.getInt(2);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return res;
    }

    public boolean guardarScore() {
        boolean res = false;
        if (cn == null) {
            System.out.println("No hay conexión a la base de datos; el puntaje no se guardó.");
            return false;
        }
        String sql = "INSERT INTO highscore(username, score) VALUES(?, ?)";
        try (PreparedStatement cmd = cn.prepareStatement(sql)) {
            cmd.setString(1, username);
            cmd.setInt(2, score);
            res = cmd.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return res;
    }

    public ResultSet getTabla() {
        if (cn == null) {
            return null;
        }
        try {
            Statement st = cn.createStatement();
            return st.executeQuery("SELECT TOP 5 username, score FROM highscore ORDER BY score DESC");
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}

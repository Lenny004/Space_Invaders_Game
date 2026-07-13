package ClaseConexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Enlace JDBC a SQL Server.
 * La configuración se lee desde {@code db.properties} en el classpath
 * (ver {@code db.properties.example}).
 */
public class Conexion {

    private static final String PROPERTIES_FILE = "/db.properties";

    public Connection conectar() {
        Connection cn = null;
        try {
            Properties props = loadProperties();
            String url = buildUrl(props);
            boolean integrated = Boolean.parseBoolean(props.getProperty("db.integratedSecurity", "true"));

            if (integrated) {
                cn = DriverManager.getConnection(url);
            } else {
                String user = props.getProperty("db.user", "");
                String password = props.getProperty("db.password", "");
                cn = DriverManager.getConnection(url, user, password);
            }

            if (cn != null) {
                System.out.println("Conexión a SQL Server establecida.");
            }
        } catch (Exception ex) {
            System.out.println("Error de conexión a la base de datos: " + ex.getMessage());
            System.out.println("Revisa src/main/resources/db.properties (ver db.properties.example).");
        }
        return cn;
    }

    private Properties loadProperties() throws Exception {
        Properties props = new Properties();
        try (InputStream in = Conexion.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encontró " + PROPERTIES_FILE + ". Copia db.properties.example como db.properties.");
            }
            props.load(in);
        }
        return props;
    }

    private String buildUrl(Properties props) {
        String customUrl = props.getProperty("db.url");
        if (customUrl != null && !customUrl.isBlank()) {
            return customUrl.trim();
        }

        String host = props.getProperty("db.host", "localhost").trim();
        String instance = props.getProperty("db.instance", "").trim();
        String dbName = props.getProperty("db.name", "spaceInvaders").trim();
        boolean integrated = Boolean.parseBoolean(props.getProperty("db.integratedSecurity", "true"));

        StringBuilder url = new StringBuilder("jdbc:sqlserver://");
        url.append(host);
        if (!instance.isEmpty()) {
            url.append("\\").append(instance);
        }
        url.append(";databaseName=").append(dbName);
        url.append(";encrypt=true;trustServerCertificate=true");
        if (integrated) {
            url.append(";integratedSecurity=true");
        }
        return url.toString();
    }
}

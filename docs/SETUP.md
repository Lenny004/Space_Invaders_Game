# Guía de setup

## JDK 17

1. Descarga Temurin 17 o superior: https://adoptium.net/
2. Instala y verifica:

```bash
java -version
javac -version
```

3. (Windows) Define `JAVA_HOME` apuntando al JDK, no al JRE.

## Maven

1. Descarga: https://maven.apache.org/download.cgi
2. Añade `bin` al `PATH`.
3. Verifica: `mvn -version`.

## SQL Server (opcional)

Los records funcionan offline en `scores.json`. SQL Server solo es necesario si quieres sincronizar el top en una base compartida.

1. Instala SQL Server Express + SSMS.
2. Habilita TCP/IP y conoce tu instancia (`SQLEXPRESS`, etc.).
3. Ejecuta `sql/spaceInvaders.sql` **con cuidado**:
   - Usa solo el `CREATE DATABASE`, `USE` y `CREATE TABLE`.
   - No ejecutes `DROP DATABASE` en un entorno con datos.
4. Copia `db.properties.example` → `db.properties` y ajusta host/instancia.

### Autenticación integrada (Windows)

```properties
db.integratedSecurity=true
```

Puede requerir el DLL nativo de autenticación del driver MSSQL en el `PATH`.

### Usuario y contraseña

```properties
db.integratedSecurity=false
db.user=sa
db.password=TuPassword
```

## Primera ejecución

```bash
mvn clean package
mvn exec:java
```

Clase principal: `Game.Inicio`.

## IDE recomendados

| IDE | Cómo abrir |
|-----|------------|
| IntelliJ IDEA | Open → seleccionar `pom.xml` |
| VS Code / Cursor | Extensión Extension Pack for Java + abrir carpeta |
| Eclipse | Import → Existing Maven Project |
| NetBeans | Open Project (Maven); los `.form` siguen editables |

## Problemas frecuentes

| Síntoma | Causa probable | Solución |
|---------|----------------|----------|
| No se oye música | WAV no encontrado en classpath | Verificar `src/main/resources/Sonidos` |
| Scores no aparecen en Records | Primera partida / archivo vacío | Juega una partida; se crea `scores.json` |
| Scores no guardan en SQL | BD mal configurada | Revisar consola y `db.properties`; el archivo local sí guarda |
| `UnsupportedClassVersionError` | JDK < 17 | Actualizar JAVA_HOME |
| Pantalla en blanco / NPE en imágenes | Recurso mal nombrado | Rutas case-sensitive: `/Imagenes/...` |

## Assets

Las imágenes y sonidos son originales del equipo. No reutilizar fuera del proyecto sin autorización (ver README).

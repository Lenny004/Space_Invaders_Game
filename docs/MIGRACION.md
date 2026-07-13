# Migración a Java 17 y Maven

## Por qué Java 17

- LTS ampliamente soportada (y base común en 2024–2026).
- Suficiente para Swing + JDBC sin forzar Virtual Threads / Foreign Function API de Java 21.
- Compatible con el driver `mssql-jdbc` actual.

Si más adelante se desea Java 21 LTS, basta con cambiar `maven.compiler.release` en el `pom.xml`.

## Cambios realizados

### Build

| Antes | Después |
|-------|---------|
| Ant (`build.xml`) + `nbproject/` | `pom.xml` Maven |
| `javac.source=1.8` | `maven.compiler.release=17` |
| JAR NetBeans | `maven-shade-plugin` (JAR fat con main class) |

### Layout de fuentes

- Código → `src/main/java/...`
- Assets → `src/main/resources/Imagenes|Sonidos|Tipografia`
- SQL → `sql/spaceInvaders.sql`
- NetBeans antiguo → `legacy/netbeans/`

### Audio

- `sun.audio.*` → `Game.SoundEffect` (`javax.sound.sampled.Clip`)
- `java.applet.Applet.newAudioClip` → `Game.MusicPlayer` con bucle `Clip.LOOP_CONTINUOUSLY`
- Rutas normalizadas a classpath (`/Sonidos/...`); se aceptan rutas legacy `src/Sonidos/...`

### UI

- `com.sun.awt.AWTUtilities.setWindowOpaque` → `setBackground(new Color(0,0,0,0))` en ventana undecorated

### Base de datos

- Connection string hardcodeada → `db.properties`
- Plantilla versionada: `db.properties.example`
- Driver Maven en lugar de JAR suelto

## Cómo verificar la migración

```bash
java -version          # debe ser 17+
mvn -version
mvn clean compile
mvn exec:java
```

## Rollback

El árbol antiguo de NetBeans está en `legacy/netbeans/`. No es el camino recomendado, pero permite comparar comportamiento histórico.

## Notas de compatibilidad

1. En Linux/macOS, autenticación integrada de SQL Server no aplica igual que en Windows: usa `db.integratedSecurity=false` + usuario/contraseña.
2. Los `.form` de NetBeans siguen junto a las clases; editarlos requiere NetBeans. El código Java generado funciona sin el diseñador.
3. El juego puede arrancar sin BD; guardar scores fallará con mensaje en consola hasta configurar JDBC.

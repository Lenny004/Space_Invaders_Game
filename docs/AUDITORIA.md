# Auditoría técnica — Space Chemistry

Fecha de revisión: julio 2026. Estado previo: proyecto NetBeans Ant, `javac.source=1.8`, sin actualizaciones ~6 años.

## Resumen ejecutivo

El juego es funcional como prototipo académico Swing, pero no era portable ni compatible con JDK modernos sin cambios. Se modernizó el build a **Maven + Java 17**, se eliminaron APIs internas (`sun.*`, `java.applet`, `com.sun.awt`) y se externalizó la configuración JDBC.

## Hallazgos críticos (resueltos o mitigados)

| Severidad | Problema | Impacto | Acción tomada |
|-----------|----------|---------|---------------|
| Crítica | Uso de `sun.audio.AudioPlayer` / `AudioStream` | No compila en Java 9+ | Reemplazado por `javax.sound.sampled.Clip` (`SoundEffect`) |
| Crítica | Uso de `java.applet.Applet.newAudioClip` | API eliminada | Reescrito `MusicPlayer` con `Clip` |
| Crítica | Uso de `com.sun.awt.AWTUtilities` | API interna | Sustituido por `setBackground(Color(0,0,0,0))` |
| Alta | Driver JDBC con ruta absoluta de un PC (`C:\Users\lenny\...`) | Impide clonar y compilar | Dependencia Maven `mssql-jdbc` |
| Alta | Connection string hardcodeada (`LENNYX\SERVIDORSQL`) | No portable | `db.properties` + plantilla `.example` |
| Alta | Rutas de audio `src/Sonidos/...` como `File` | Fallan empaquetadas en JAR | Recursos en classpath `/Sonidos/...` |
| Media | Rutas `/imagenes/` vs `/Imagenes/` | Falla en Linux (case-sensitive) | Unificado a `/Imagenes/` |
| Media | Recarga de sonidos al final de cada frame | I/O y fugas de recursos | Carga única en inicialización de campos |
| Media | Bugs en `Controlador` (placeholders SQL, columnas incorrectas, cierre prematuro de conexión) | Scores rotos | Consultas corregidas; try-with-resources parcial |

## Problemas técnicos pendientes (no bloqueantes)

1. **God class `GamePanel` (~1500 líneas)** — mezcla UI, física, colisiones, power-ups, niveles, audio y persistencia.
2. **Duplicación `Bullet`–`Bullet7`** — siete clases casi idénticas; deberían ser una con parámetros (color, daño, velocidad).
3. **Estado global estático** — `CantidadBalas`, flags de power-ups, `FrmNombre.nombre`, instancias estáticas de frames.
4. **Nombres de paquetes poco convencionales** — `Game`, `Clases`, `ClaseConexion` en lugar de `com.spacechemistry.*`.
5. **Archivos `.form` de NetBeans** — útiles solo con el diseñador de NetBeans; el layout AbsoluteLayout acopla UI al IDE.
6. **Sin tests automatizados** — carpeta `src/test/java` preparada pero vacía.
7. **SQL Server como única persistencia** — acoplamiento fuerte; dificulta demos sin BD.
8. **Nombre de clase con tilde** — `Créditos.java` puede dar problemas en algunos sistemas de archivos/CI.
9. **Highscore dual** — archivo local `Highscore.txt` + tabla SQL sin estrategia unificada.
10. **Seguridad JDBC** — `encrypt=true;trustServerCertificate=true` es válido para desarrollo, no para producción.

## Dependencias

| Antes | Después |
|-------|---------|
| Java 8 | Java 17 (`maven.compiler.release`) |
| Ant + NetBeans (`build.xml`) | Maven (`pom.xml`) |
| `sqljdbc42.jar` local | `com.microsoft.sqlserver:mssql-jdbc:12.8.1.jre11` |
| AbsoluteLayout (NetBeans libs) | No requerido en runtime (código generado usa layouts estándar / null) |

El material NetBeans original se conservó en `legacy/netbeans/` como referencia histórica.

## Métricas aproximadas del código

| Métrica | Valor |
|---------|-------|
| Clases `.java` | ~36 |
| Líneas en `GamePanel` | ~1500 |
| Imágenes | ~65 |
| Sonidos WAV | ~22 |
| Formularios NetBeans `.form` | 11 |

## Compatibilidad Java 17

El código actual evita APIs eliminadas. Puntos a vigilar al seguir evolucionando:

- No reintroducir paquetes `sun.*` / `com.sun.*` internos.
- Preferir `try-with-resources` y NIO para I/O.
- Si se migra a módulos JPMS, declarar `requires java.desktop` y `requires java.sql`.

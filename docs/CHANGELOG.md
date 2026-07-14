# Changelog

## [1.5.2] — Drops honestos (2026-07-13)

### Cambiado
- Matar aliens ya no suma elementos al inventario de inmediato: sueltan un drop físico.
- Recoger el drop con la nave suma el elemento, reproduce SFX y muestra feedback (`+Fe`, etc.).
- Si el drop sale de pantalla sin recogerse, se pierde.
- Tasa de spawn: `GameBalance.ELEMENT_DROP_CHANCE_PERCENT` (50% en aliens); jefe siempre dropea al morir (`ELEMENT_DROP_ON_BOSS`).
- Lógica extraída a `DropSystem` (testeable); hitbox de `ElementoDrop` alineada al icono.

## [1.5.1] — Jugabilidad (2026-07-13)

### Corregido
- Flags de crafteo se resetean al reiniciar partida (`CraftingSystem.Flags` + `ResetearValores`).
- Vida del jefe unificada (`GameBalance.BOSS_HEALTH`) al entrar en nivel boss.
- Colisiones de beams sin `try/catch` silenciosos; degradación vía `CollisionSystem`.

### Cambiado
- Disparo, crafteo y caída de elementos movidos de `paint()` a `ActualizarEstadoJuego`.
- ESC abre `MenuEmergente` y pausa el `GameLoop`.
- Recetas de compuestos extraídas a `CraftingSystem` (testeable).

## [1.5.0] — Fase E (2026-07-13)

### Añadido
- `GameConfig`: preferencias persistidas en `data/user-settings.properties` (volumen, mute, idioma, fullscreen, dificultad).
- `Messages` + `i18n/messages_*.properties` (es/en) para menús y diálogos.
- Controles de audio, idioma y pantalla completa en `Configuracion`.
- Historial reciente en pantalla `Records` (además del Top 5).
- `GameBalance`: constantes de spawn, velocidades y puntuación.
- `MusicPlayer.setVolume` alineado con efectos.

### Cambiado
- Dificultad unificada con `RunEntry` (1=fácil, 2=medio, 3=difícil); metadatos de Records corregidos.
- Spawns de beams/bonus movidos de `paint()` a `ActualizarEstadoJuego`.
- `GameFrame` maximiza la ventana si fullscreen está activo.
- Versión del artefacto: `1.5.0-SNAPSHOT`.

### Corregido
- Tooltips de Records mostraban la dificultad invertida respecto a la UI.

## [1.4.0] — Persistencia local SQLite (2026-07-13)

### Eliminado
- Sistema de quizzes / preguntas de química (`Quizz`) al inicio de partida.

### Añadido
- `SqliteScoreRepository` con archivo `data/space-chemistry.db` (sin servidor).
- `RunEntry`: score + nivel, dificultad, victoria/derrota y fecha.
- Historial de partidas (`history` / `historyFor`) vía `ScoreService`.
- Migración automática desde `scores.json` / `Highscore.txt` a SQLite.
- Tooltips en Records con nivel, dificultad y resultado.

### Cambiado
- Persistencia principal: SQLite local (ya no SQL Server).
- Al perder se guarda la partida siempre (también si eliges reintentar).
- Dependencia Maven: `sqlite-jdbc` (eliminado `mssql-jdbc` del build).
- Versión del artefacto: `1.4.0-SNAPSHOT`.

### Deprecado
- `JdbcScoreRepository` (stub; el juego es local).

## [1.3.0] — Fase D (2026-07-12)

### Añadido
- CI con GitHub Actions (`mvn -B verify`) en JDK 17 y 21.
- Perfil Maven `jpackage` para generar instalador/app nativa (`mvn -P jpackage package`).
- Tests de `LevelManager`, `GameState`, colisiones con listener y `Projectile`.

### Cambiado
- `LevelManager` acepta dificultad por `int` (testeable sin Swing).
- `ScoreService` con inicialización perezosa; `JdbcScoreRepository` conecta solo al usarse.
- `FileScoreRepository(Path)` busca `Highscore.txt` junto al JSON, no en el CWD.
- Versión del artefacto: `1.3.0-SNAPSHOT`.

### Corregido
- BOM UTF-8 en `Inicio`, `GamePanel` y `Creditos` que rompía `javac`.
- Tests de persistencia contaminados por `Highscore.txt` del directorio de trabajo.

## [1.2.0] — Fase C (2026-07-12)

### Añadido
- `Projectile` unifica las 7 clases de balas.
- Sistemas: `PowerUpSystem`, `CollisionSystem`, `LevelManager`, `GameLoop`.
- `GameState` como modelo de estado compartido para seguir extrayendo lógica.
- Tests de combate (`CombatSystemsTest`).

### Cambiado
- `GamePanel` delega disparo, colisiones de proyectiles, setup de nivel y timers.
- `Bullet` queda deprecado como alias de `Projectile`; eliminados `Bullet2`…`Bullet7`.

### Notas
- `paint()` aún contiene spawns de beams/bonus y crafteo de power-ups (siguiente iteración).

## [1.1.0] — Fase B (2026-07-12)

### Añadido
- Paquete `persistence` con `ScoreEntry`, `ScoreRepository`, `FileScoreRepository`, `JdbcScoreRepository` y `ScoreService`.
- Persistencia offline en `scores.json` (el juego funciona sin SQL Server).
- Migración automática de `Highscore.txt` (valor numérico) a `scores.json` como entrada `Legacy`.
- Tests JUnit para validación de nombres y repositorio de archivo.
- Pantalla de créditos renombrada a `Creditos` (ASCII).

### Cambiado
- `GamePanel`, `Records` y `FrmNombre` usan `ScoreService` en lugar de JDBC directo / `Highscore.txt` por frame.
- `Clases.Controlador` queda deprecado y delega en `ScoreService`.
- Records muestra top 5 desde archivo o SQL (si hay conexión).

### Corregido
- Validación de nombre de jugador (1–15, alfanumérico) sin mensaje falso de error de BD.
- Eliminado árbol huérfano `src/Game/` de la migración Maven.

## [1.0.0] — Fase A (2026-07-12)

### Añadido
- Build Maven, Java 17, documentación en `/docs`, `.gitignore` moderno.
- Audio con `javax.sound.sampled` (`SoundEffect`, `MusicPlayer`).
- Configuración JDBC externa (`db.properties`).

# Changelog

## [1.6.0] — Integraciones de diversión P0+P1 (2026-07-15)

### Añadido
- **Combos:** rachas de kills con multiplicador de score (tope x5); se corta al recibir daño o por timeout.
- **Buffs temporales:** drops S/Z/D (escudo, slow, doble disparo) además del crafteo permanente.
- **Oleadas variadas:** formaciones GRID / V / escalonada según nivel; algunos aliens en zigzag.
- **Jefes con fases:** HP por jefe, patrones OPENING/RAGE/DESPERATE y minions en fase final; daño de proyectil aplicado al jefe.
- **Modo Infinito:** botón en menú; no hay victoria al pasar el 15; records con nivel alto se marcan como Infinito.

### Ya presente (1.5.2–1.5.4)
- Drops honestos, overlay de crafteo (`C` + `1`–`6`/`Q`–`Y`), tutorial interactivo.

## [1.5.4] — Tutorial interactivo (2026-07-13)

### Añadido
- Botón **Tutorial** en el menú `Inicio` (sin pedir nombre).
- `TutorialController`: 3 pasos (mover+disparar → recoger Fe → craftear Hierro con `1`).
- Oleada mínima en `LevelManager.createTutorialWave` (4 aliens lentos, sin disparos ni jefe).
- Banner de instrucciones i18n; al completar marca `tutorialCompleted` en `user-settings.properties`.
- No guarda score del tutorial; al morir o terminar vuelve al menú.

## [1.5.3] — UX de crafteo (2026-07-13)

### Añadido
- Tecla `C` abre/cierra overlay de crafteo (pausa lógica ligera; ESC también cierra).
- Overlay lista las 12 recetas con nombre, coste, efecto y tecla; deshabilita usadas / sin materiales.
- Atajos nuevos: `1`–`6` (disparo) y `Q`/`W`/`E`/`R`/`T`/`Y` (velocidad).
- i18n de nombres/efectos de compuestos en `messages_es/en.properties`.

### Cambiado
- Deprecados los hotkeys opacos F/W/O/U/Z/T/H/N/S/B/P/A como requisito de crafteo.
- `CraftingSystem` modela recetas explícitas (`Recipe` + flags por índice).
- Barra lateral PRESS muestra las teclas nuevas; hint `C: crafteo` en HUD.
- Nota: textos de `FrmAyuda` aún mencionan teclas antiguas (actualizar en iteración de ayuda).

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

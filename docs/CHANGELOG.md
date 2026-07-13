# Changelog

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

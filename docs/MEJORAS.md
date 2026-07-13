# Propuestas de mejora

Priorizadas para retomar el proyecto de forma sostenible.

## 1. Arquitectura (alto impacto)

### Extraer un motor de juego delgado

`GamePanel` hoy es un *God Object*. Separar:

| Clase / mÃ³dulo | Responsabilidad |
|----------------|-----------------|
| `GameLoop` | Timer / ticks / FPS |
| `LevelManager` | Oleadas, jefes, transiciÃ³n de nivel |
| `CollisionSystem` | DetecciÃ³n y respuesta |
| `PowerUpSystem` | Elementos, compuestos, timers |
| `ScoreService` | Puntaje local + persistencia |
| `GamePanel` | Solo pintar y reenviar input |

**Por quÃ©:** reduce bugs, permite tests unitarios del dominio sin Swing.

### Unificar proyectiles

Reemplazar `Bullet`â€¦`Bullet7` por:

```java
public class Projectile extends MovingGameObject {
    private final int damage;
    private final ProjectileType type;
}
```

**Por quÃ©:** menos duplicaciÃ³n, mÃ¡s fÃ¡cil aÃ±adir armas nuevas.

### Repository para scores

Persistencia local con SQLite (`SqliteScoreRepository`):

- Top scores
- Historial de partidas (`RunEntry`: nivel, dificultad, victoria, fecha)
- MigraciÃ³n desde `scores.json` / `Highscore.txt`

`FileScoreRepository` queda como puente de migraciÃ³n. `JdbcScoreRepository` estÃ¡ deprecado.

## 2. Patrones de diseÃ±o recomendados

| PatrÃ³n | DÃ³nde aplicarlo |
|--------|-----------------|
| **State** | MenÃº / Jugando / Pausa / GameOver / Victoria |
| **Strategy** | Dificultad (fÃ¡cil/medio/difÃ­cil) en vez de `switch` repartidos |
| **Factory** | CreaciÃ³n de enemigos y jefes por nivel |
| **Observer / Event bus** | Colisiones â†’ sonido, score |
| **Command** | Input de teclado mapeado a acciones |
| **Object Pool** | Balas y beams (menos GC en el loop) |
| **Singleton controlado** (o DI simple) | `AudioService`, `ConfigService` en lugar de estÃ¡ticos sueltos |

## 3. MetodologÃ­a de desarrollo moderna

1. **Git Flow ligero:** `main` estable, ramas `feature/*`, PRs pequeÃ±os.
2. **Issues + milestones:** bugs de audio/BD primero; refactor de `GamePanel` despuÃ©s.
3. **CI GitHub Actions:** `mvn -B verify` en JDK 17 y 21.
4. **Convenciones:** paquetes `com.spacechemistry.*`, nombres ASCII (`Creditos.java`), UTF-8 en todo el repo.
5. **Changelog** (`docs/CHANGELOG.md`) al publicar versiones jugables.

## 4. Mejoras de producto

- ~~Modo offline sin SQL~~ â†’ SQLite local con historial (`data/space-chemistry.db`).
- ~~Volumen y mute en configuraciÃ³n~~ â†’ `GameConfig` + sliders en `Configuracion`.
- ~~Escalado de resoluciÃ³n / fullscreen~~ â†’ maximizar ventana vÃ­a preferencia (playfield fijo 1200Ã—675).
- ~~InternacionalizaciÃ³n (es/en) de menÃºs~~ â†’ `Messages` / ResourceBundle (ayuda larga pendiente).
- Empaquetado con `jpackage` (instalador Windows/macOS/Linux).
- ~~Pantalla Records con historial reciente~~ (ademÃ¡s del top; tooltips con metadatos).

## 5. Calidad de cÃ³digo

- Introducir JUnit 5 + tests de colisiones y de `ScoreRepository`.
- Activar SpotBugs / Checkstyle o Error Prone en Maven.
- Eliminar `System.out` de producciÃ³n; usar `java.util.logging` o SLF4J.
- Corregir tipografÃ­a: `Fuente` debe cargar `/Tipografia/space_invaders.ttf` de forma explÃ­cita.

## 6. Seguridad y configuraciÃ³n

- Nunca versionar `db.properties` con secretos (ya en `.gitignore`).
- ParÃ¡metro `encrypt`/`trustServerCertificate` documentado para prod vs dev.
- Validar longitud/contenido del nombre de jugador antes del INSERT.

## Roadmap sugerido

```
Fase A (hecha)   Modernizar build + APIs rotas + docs + gitignore
Fase B (hecha)   ScoreRepository + modo offline + renombrar Creditos
Fase C (hecha)   Extraer sistemas de GamePanel + unificar Projectile
Fase D (hecha)   Tests + CI + jpackage
Fase E (hecha)   Pulido UX / i18n / balance de niveles
```

## Estado Fase B

Completada. Ver [CHANGELOG.md](CHANGELOG.md).

- Offline: `scores.json` vÃ­a `FileScoreRepository`
- SQL opcional: `JdbcScoreRepository` cuando `db.properties` conecta
- UI: `ScoreService` en `GamePanel` / `Records` / `FrmNombre`
- `Creditos.java` (sin tilde)

## Estado Fase C

Completada (extracciÃ³n incremental).

| MÃ³dulo | Responsabilidad |
|--------|-----------------|
| `Projectile` | Unifica Bulletâ€¦Bullet7 |
| `PowerUpSystem` | Disparo en abanico + velocidad de nave |
| `CollisionSystem` | Movimiento/colisiones de proyectiles + degradaciÃ³n de escudos |
| `LevelManager` | Oleadas, jefes, nave, vidas, escudos |
| `GameLoop` | Timers de tick y hitmarker |
| `GameState` | Modelo de estado compartido (base para seguir adelgazando `GamePanel`) |

Pendiente para iteraciones futuras: mover crafteo fuera de `paint()`, y migrar listas/contadores restantes a `GameState`.

## Estado Fase D

Completada.

| Entrega | Detalle |
|---------|---------|
| Tests | `LevelManager`, `GameState`, colisiones, proyectiles + suite previa (38 tests) |
| CI | `.github/workflows/ci.yml` â€” JDK 17 y 21, `mvn -B verify` |
| Empaquetado | Perfil Maven `-P jpackage` â†’ `target/dist/` |

## Estado Fase E

Completada.

| Entrega | Detalle |
|---------|---------|
| Preferencias | `GameConfig` → `data/user-settings.properties` |
| Audio | Sliders SFX/música + mute; `MusicPlayer.setVolume` |
| i18n | Menús / config / records / diálogos es+en |
| Records | Top 5 + historial reciente |
| Fullscreen | Maximizar ventana de juego |
| Balance | `GameBalance` + dificultad alineada con `RunEntry` |
| GamePanel | Spawns fuera de `paint()` |

Siguiente: i18n de `FrmAyuda`, extraer crafteo de `paint()`, escalado real del playfield (opcional).

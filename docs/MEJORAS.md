# Propuestas de mejora

Priorizadas para retomar el proyecto de forma sostenible.

## 1. Arquitectura (alto impacto)

### Extraer un motor de juego delgado

`GamePanel` hoy es un *God Object*. Separar:

| Clase / módulo | Responsabilidad |
|----------------|-----------------|
| `GameLoop` | Timer / ticks / FPS |
| `LevelManager` | Oleadas, jefes, transición de nivel |
| `CollisionSystem` | Detección y respuesta |
| `PowerUpSystem` | Elementos, compuestos, timers |
| `ScoreService` | Puntaje local + persistencia |
| `GamePanel` | Solo pintar y reenviar input |

**Por qué:** reduce bugs, permite tests unitarios del dominio sin Swing.

### Unificar proyectiles

Reemplazar `Bullet`…`Bullet7` por:

```java
public class Projectile extends MovingGameObject {
    private final int damage;
    private final ProjectileType type;
}
```

**Por qué:** menos duplicación, más fácil añadir armas nuevas.

### Repository para scores

Persistencia local con SQLite (`SqliteScoreRepository`):

- Top scores
- Historial de partidas (`RunEntry`: nivel, dificultad, victoria, fecha)
- Migración desde `scores.json` / `Highscore.txt`

`FileScoreRepository` queda como puente de migración. `JdbcScoreRepository` está deprecado.

## 2. Patrones de diseño recomendados

| Patrón | Dónde aplicarlo |
|--------|-----------------|
| **State** | Menú / Jugando / Pausa / GameOver / Victoria |
| **Strategy** | Dificultad (fácil/medio/difícil) en vez de `switch` repartidos |
| **Factory** | Creación de enemigos y jefes por nivel |
| **Observer / Event bus** | Colisiones → sonido, score, quizz |
| **Command** | Input de teclado mapeado a acciones |
| **Object Pool** | Balas y beams (menos GC en el loop) |
| **Singleton controlado** (o DI simple) | `AudioService`, `ConfigService` en lugar de estáticos sueltos |

## 3. Metodología de desarrollo moderna

1. **Git Flow ligero:** `main` estable, ramas `feature/*`, PRs pequeños.
2. **Issues + milestones:** bugs de audio/BD primero; refactor de `GamePanel` después.
3. **CI GitHub Actions:** `mvn -B verify` en JDK 17 y 21.
4. **Convenciones:** paquetes `com.spacechemistry.*`, nombres ASCII (`Creditos.java`), UTF-8 en todo el repo.
5. **Changelog** (`docs/CHANGELOG.md`) al publicar versiones jugables.

## 4. Mejoras de producto

- ~~Modo offline sin SQL~~ → SQLite local con historial (`data/space-chemistry.db`).
- Volumen y mute en configuración (API ya preparada en `SoundEffect.setVolume`).
- Escalado de resolución / fullscreen.
- Internacionalización (es/en) de menús y quizzes.
- Empaquetado con `jpackage` (instalador Windows/macOS/Linux).
- Pantalla Records con historial reciente (además del top; tooltips ya muestran metadatos).

## 5. Calidad de código

- Introducir JUnit 5 + tests de colisiones y de `ScoreRepository`.
- Activar SpotBugs / Checkstyle o Error Prone en Maven.
- Eliminar `System.out` de producción; usar `java.util.logging` o SLF4J.
- Corregir tipografía: `Fuente` debe cargar `/Tipografia/space_invaders.ttf` de forma explícita.

## 6. Seguridad y configuración

- Nunca versionar `db.properties` con secretos (ya en `.gitignore`).
- Parámetro `encrypt`/`trustServerCertificate` documentado para prod vs dev.
- Validar longitud/contenido del nombre de jugador antes del INSERT.

## Roadmap sugerido

```
Fase A (hecha)   Modernizar build + APIs rotas + docs + gitignore
Fase B (hecha)   ScoreRepository + modo offline + renombrar Creditos
Fase C (hecha)   Extraer sistemas de GamePanel + unificar Projectile
Fase D (hecha)   Tests + CI + jpackage
Fase E           Pulido UX / i18n / balance de niveles
```

## Estado Fase B

Completada. Ver [CHANGELOG.md](CHANGELOG.md).

- Offline: `scores.json` vía `FileScoreRepository`
- SQL opcional: `JdbcScoreRepository` cuando `db.properties` conecta
- UI: `ScoreService` en `GamePanel` / `Records` / `FrmNombre`
- `Creditos.java` (sin tilde)

## Estado Fase C

Completada (extracción incremental).

| Módulo | Responsabilidad |
|--------|-----------------|
| `Projectile` | Unifica Bullet…Bullet7 |
| `PowerUpSystem` | Disparo en abanico + velocidad de nave |
| `CollisionSystem` | Movimiento/colisiones de proyectiles + degradación de escudos |
| `LevelManager` | Oleadas, jefes, nave, vidas, escudos |
| `GameLoop` | Timers de tick y hitmarker |
| `GameState` | Modelo de estado compartido (base para seguir adelgazando `GamePanel`) |

Pendiente para iteraciones futuras: mover crafteo/spawns fuera de `paint()`, y migrar listas/contadores restantes a `GameState`.

## Estado Fase D

Completada.

| Entrega | Detalle |
|---------|---------|
| Tests | `LevelManager`, `GameState`, colisiones, proyectiles + suite previa (38 tests) |
| CI | `.github/workflows/ci.yml` — JDK 17 y 21, `mvn -B verify` |
| Empaquetado | Perfil Maven `-P jpackage` → `target/dist/` |

Siguiente: **Fase E** (UX, i18n, balance) y seguir adelgazando `GamePanel.paint()`.
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

```java
public interface ScoreRepository {
    void save(String username, int score);
    List<ScoreEntry> top(int limit);
}
```

Implementaciones: `JdbcScoreRepository`, `FileScoreRepository`, `InMemoryScoreRepository` (demos/tests).

**Por qué:** el menú y el juego no dependen de SQL Server para arrancar.

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

- Modo offline sin SQL (guardar solo en archivo o SQLite).
- Volumen y mute en configuración (API ya preparada en `SoundEffect.setVolume`).
- Escalado de resolución / fullscreen.
- Internacionalización (es/en) de menús y quizzes.
- Empaquetado con `jpackage` (instalador Windows/macOS/Linux).

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
Fase B           ScoreRepository + modo offline + renombrar Creditos
Fase C           Extraer sistemas de GamePanel + unificar Bullet
Fase D           Tests + CI + jpackage
Fase E           Pulido UX / i18n / balance de niveles
```

# Space Chemistry

Videojuego en Java, estilo *Space Invaders*, que combina acción arcade con química: elementos de la tabla periódica, compuestos y power-ups.

Proyecto originalmente desarrollado como trabajo académico (NetBeans / Java 8) y modernizado a **Java 17** con estructura **Maven** (`1.5.0-SNAPSHOT`).

## Descripción

Controlas una nave espacial, derrotas oleadas de enemigos y jefes a lo largo de **15 niveles**, recolectas elementos químicos y formas compuestos que mejoran el disparo y la velocidad. Los puntajes e historial de partidas se guardan **en local** con SQLite (`data/space-chemistry.db`).

Desarrollador:

- Lenny Adrián Elías Sánchez

## ¿Qué necesitas para correrlo?

**No.** No usa Docker, ni SQL Server, ni ningún servidor externo.

Solo hace falta:

| Herramienta | Para qué | Obligatorio |
|-------------|----------|-------------|
| **JDK 17+** | Compilar y ejecutar el juego | Sí |
| **Maven 3.9+** | Descargar dependencias y empaquetar | Sí (para compilar desde el repo) |

La base de datos es un archivo SQLite local (`data/space-chemistry.db`) que se crea solo al jugar. No hay que instalar nada más.

## Cómo correrlo

```bash
git clone https://github.com/Lenny004/Space_Invaders_Game.git
cd Space_Invaders_Game
mvn clean package
mvn exec:java
```

Alternativa con el JAR generado:

```bash
java -jar target/space-chemistry-1.5.0-SNAPSHOT.jar
```

Clase principal: `Game.Inicio`.

### Opcional (no hace falta para jugar)

```bash
mvn -B verify              # tests
mvn -P jpackage package    # instalador nativo (requiere jpackage en el JDK)
```
## Cómo se juega

### Flujo de pantallas

```text
Inicio → Nombre → Partida (GameFrame / GamePanel)
           ↘ Configuración → Records
           ↘ Ayuda
           ↘ Créditos
```

Al completar el nivel 15 ganas (`Victoria`). Si te quedas sin vidas, puedes reintentar o ir a `GameOver`. En ambos casos la partida se guarda en SQLite.

### Mecánica principal

| Aspecto | Detalle |
|---------|---------|
| **Vidas** | 3 al inicio |
| **Niveles** | 1–15; victoria al superar el 15 |
| **Oleadas** | Niveles normales: grilla de enemigos (30 aliens) |
| **Jefes** | Niveles 3, 6, 9, 12 y 15 (boss con más vida y disparo triple) |
| **Escudos** | 3 columnas de bloques destructibles delante de la nave |
| **Bonus** | Meteorito que cruza la pantalla (+5000 pts) |
| **Dificultad** | Fácil / Normal / Difícil (ajusta la velocidad enemiga) |

### Química y power-ups

Al destruir enemigos en oleadas normales obtienes elementos. Con las teclas de crafteo formas compuestos (una vez por partida cada uno):

**Disparo** (más balas en abanico):

| Tecla | Compuesto | Efecto |
|-------|-----------|--------|
| F | Fe | Nivel arma 1 |
| W | W | Nivel arma 2 |
| O | H₂O | Nivel arma 3 |
| U | U | Nivel arma 4 |
| Z | Cu₃Zn₂ | Nivel arma 5 |
| T | TNT (C₇H₅N₃O₆) | Nivel arma 6 |

**Velocidad** de la nave:

| Tecla | Compuesto | Efecto |
|-------|-----------|--------|
| H | He | Velocidad 1 |
| N | HNO₃ | Velocidad 2 |
| S | H₂SO₄ | Velocidad 3 |
| B | C₆H₆ | Velocidad 4 |
| P | C₃H₈ | Velocidad 5 |
| A | C₄H₁₀ | Velocidad 6 |

### Puntuación

| Acción | Puntos |
|--------|--------|
| Enemigo normal | +100 |
| Derrotar jefe | +9000 |
| Enemigo bonus | +5000 |

Cada partida guarda usuario, score, nivel alcanzado, dificultad, victoria/derrota y fecha. En **Records** se muestra el top 5 con tooltips de detalle.

## Controles

### En partida

| Tecla | Acción |
|-------|--------|
| ← / → | Mover nave |
| Espacio | Disparar |
| F W O U Z T | Craftear power-ups de disparo |
| H N S B P A | Craftear power-ups de velocidad |
| R | Reiniciar high scores (borra la BD local) |

### Menús

Navegación por clic: Iniciar, Configuración, Ayuda, Records, Créditos, volver al menú y cerrar.

## Estructura del proyecto

```
Space_Invaders_Game/
├── .github/workflows/         # CI (JDK 17 y 21)
├── docs/                      # Documentación técnica
├── sql/                       # Scripts históricos (referencia)
├── legacy/netbeans/           # Proyecto Ant/NetBeans original (referencia)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Game/          # UI Swing, loop, entidades y sistemas
│   │   │   ├── persistence/   # SQLite local (scores + historial)
│   │   │   ├── Controlador/   # Teclado
│   │   │   ├── Clases/        # Compatibilidad (Controlador deprecado)
│   │   │   ├── ClaseConexion/ # Legacy JDBC (no usado)
│   │   │   └── Tipografia/    # Fuentes personalizadas
│   │   └── resources/
│   │       ├── Imagenes/
│   │       ├── Sonidos/
│   │       └── Tipografia/
│   └── test/java/             # Tests JUnit 5
├── pom.xml
└── README.md
```

### Arquitectura de juego (resumen)

| Componente | Rol |
|------------|-----|
| `Inicio` | Menú principal (`main`) |
| `GamePanel` | Orquestador de partida (render, estado, crafteo) |
| `GameLoop` | Timers del juego |
| `LevelManager` | Oleadas, jefes, vidas y escudos |
| `CollisionSystem` | Colisiones de proyectiles del jugador |
| `PowerUpSystem` | Abanico de disparo y velocidad |
| `Projectile` / `Beam` | Balas del jugador / rayos enemigos |
| `ScoreService` | Fachada de persistencia SQLite |

Detalle de arquitectura y roadmap: ver carpeta [`docs/`](docs/).

## Stack técnico

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 17 |
| UI | Swing / AWT |
| Audio | `javax.sound.sampled` |
| Persistencia | SQLite (`sqlite-jdbc`) |
| Build | Maven (shade JAR, perfil `jpackage`) |
| Tests | JUnit 5 |
| CI | GitHub Actions (JDK 17 y 21) |

## Cómo contribuir / reportar problemas

1. Abre un *issue* describiendo el bug o la mejora (pasos para reproducir, JDK, SO).
2. Para cambios de código: crea una rama, haz commits claros y abre un Pull Request.
3. Respeta los assets gráficos: las imágenes fueron creadas para este juego y no deben reutilizarse sin autorización del autor.

## Licencia

Código disponible con fines educativos y de aprendizaje personal. Los assets (imágenes, sonidos, tipografía) permanecen bajo derechos del autor; no redistribuir ni reclamar autoría sin permiso.

Si publicas el repositorio de forma abierta, considera añadir un archivo `LICENSE` (por ejemplo MIT solo para el código fuente).

## Documentación adicional

- [Auditoría técnica](docs/AUDITORIA.md)
- [Arquitectura y estructura](docs/ARQUITECTURA.md)
- [Migración a Java 17 / Maven](docs/MIGRACION.md)
- [Propuestas de mejora](docs/MEJORAS.md)
- [Changelog](docs/CHANGELOG.md)
- [Guía de setup](docs/SETUP.md)

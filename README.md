# Space Chemistry

Videojuego educativo en Java, estilo *Space Invaders*, que combina acción arcade con química: elementos de la tabla periódica, compuestos, power-ups y quizzes.

Proyecto originalmente desarrollado como trabajo académico (NetBeans / Java 8) y modernizado a **Java 17** con estructura **Maven**.

## Descripción

Controlas una nave espacial, derrotas oleadas de enemigos y jefes, recolectas elementos químicos y formas compuestos que otorgan ventajas. Los puntajes e historial de partidas se guardan **en local** con SQLite (`data/space-chemistry.db`).

Autores originales:

- Lenny Adrián Elías Sánchez (coordinador)
- Josué Aristides Meléndez Alemán
- César Esaú Flores Martínez
- Miguel Alexander Hernández Martínez

## Requisitos previos

| Herramienta | Versión mínima | Notas |
|-------------|----------------|--------|
| **JDK** | 17+ | Temurin, Oracle, Microsoft Build of OpenJDK, etc. |
| **Apache Maven** | 3.9+ | Gestión de dependencias y build |
| **Windows / macOS / Linux** | — | Juego 100% local; no requiere base de datos externa |

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/<tu-usuario>/Space-Chemistry.git
cd Space-Chemistry
```

### 2. Persistencia local

No hace falta instalar ninguna base de datos. Al jugar se crea automáticamente:

```text
data/space-chemistry.db
```

Si existía `scores.json` o `Highscore.txt`, se migran una sola vez a SQLite.

### 3. Compilar

```bash
mvn clean package
```

### 4. Ejecutar

```bash
mvn exec:java
```

O bien:

```bash
java -jar target/space-chemistry-1.4.0-SNAPSHOT.jar
```

### Instalador (opcional)

Con un JDK que incluya `jpackage`:

```bash
mvn -P jpackage package
```

## Estructura del proyecto

```
Space-Chemistry/
├── .github/workflows/         # CI (JDK 17 y 21)
├── docs/                      # Documentación técnica
├── sql/                       # Scripts históricos (referencia)
├── legacy/netbeans/           # Proyecto Ant/NetBeans original (referencia)
├── src/
│   ├── main/
│   │   ├── java/              # Código fuente
│   │   │   ├── persistence/   # SQLite local (scores + historial)
│   │   │   ├── Game/          # UI, lógica de juego, entidades
│   │   │   ├── ClaseConexion/ # Legacy JDBC (no usado)
│   │   │   ├── Clases/        # Compatibilidad (Controlador deprecado)
│   │   │   ├── Controlador/   # Teclado
│   │   │   └── Tipografia/    # Fuentes personalizadas
│   │   └── resources/
│   │       ├── Imagenes/
│   │       ├── Sonidos/
│   │       └── Tipografia/
│   └── test/java/             # Tests JUnit 5
├── pom.xml
└── README.md
```

Detalle de arquitectura y roadmap: ver carpeta [`docs/`](docs/).

## Controles

| Tecla | Acción |
|-------|--------|
| ← / → | Mover nave |
| Espacio | Disparar |
| (menú) | Configuración, ayuda, records, créditos |

## Cómo contribuir / reportar problemas

1. Abre un *issue* describiendo el bug o la mejora (pasos para reproducir, JDK, SO).
2. Para cambios de código: crea una rama, haz commits claros y abre un Pull Request.
3. Respeta los assets gráficos: las imágenes fueron creadas para este juego y no deben reutilizarse sin autorización de los autores.

## Licencia

Código disponible con fines educativos y de aprendizaje personal. Los assets (imágenes, sonidos, tipografía) permanecen bajo derechos de sus autores; no redistribuir ni reclamar autoría sin permiso.

Si publicas el repositorio de forma abierta, considera añadir un archivo `LICENSE` (por ejemplo MIT solo para el código fuente).

## Documentación adicional

- [Auditoría técnica](docs/AUDITORIA.md)
- [Arquitectura y estructura](docs/ARQUITECTURA.md)
- [Migración a Java 17 / Maven](docs/MIGRACION.md)
- [Propuestas de mejora](docs/MEJORAS.md)
- [Changelog](docs/CHANGELOG.md)
- [Guía de setup](docs/SETUP.md)

# Space Chemistry

Videojuego educativo en Java, estilo *Space Invaders*, que combina acción arcade con química: elementos de la tabla periódica, compuestos, power-ups y quizzes.

Proyecto originalmente desarrollado como trabajo académico (NetBeans / Java 8) y modernizado a **Java 17** con estructura **Maven**.

## Descripción

Controlas una nave espacial, derrotas oleadas de enemigos y jefes, recolectas elementos químicos y formas compuestos que otorgan ventajas. Los puntajes se guardan **offline** en `scores.json`; **SQL Server es opcional** para sincronizar records.

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
| **SQL Server** | Express o superior | Opcional; records también funcionan offline |
| **Windows** (opcional) | — | Autenticación integrada JDBC; en otros SO usa usuario/contraseña |

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/<tu-usuario>/Space-Chemistry.git
cd Space-Chemistry
```

### 2. Configurar la base de datos (opcional)

Los records funcionan sin SQL Server (`scores.json` en el directorio de ejecución). Si quieres sincronizar con SQL Server:

1. Ejecuta el script `sql/spaceInvaders.sql` en SQL Server Management Studio (o `sqlcmd`), **omitendo** las líneas `DROP DATABASE` si solo quieres crear el esquema.
2. Copia la plantilla de configuración:

```bash
copy src\main\resources\db.properties.example src\main\resources\db.properties
```

3. Edita `db.properties` con tu host e instancia:

```properties
db.host=localhost
db.instance=SQLEXPRESS
db.name=spaceInvaders
db.integratedSecurity=true
```

> `db.properties` está en `.gitignore` para no subir credenciales ni rutas locales.

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
java -jar target/space-chemistry-1.0.0-SNAPSHOT.jar
```

Si usas autenticación integrada de Windows con SQL Server, asegúrate de tener el DLL nativo de autenticación en el `PATH` (consulta [mssql-jdbc](https://github.com/microsoft/mssql-jdbc)).

## Estructura del proyecto

```
Space-Chemistry/
├── docs/                      # Documentación técnica
├── sql/                       # Scripts de base de datos
├── legacy/netbeans/           # Proyecto Ant/NetBeans original (referencia)
├── src/
│   ├── main/
│   │   ├── java/              # Código fuente
│   │   │   ├── persistence/   # Scores offline + JDBC opcional
│   │   │   ├── Game/          # UI, lógica de juego, entidades
│   │   │   ├── ClaseConexion/ # JDBC
│   │   │   ├── Clases/        # Compatibilidad (Controlador deprecado)
│   │   │   ├── Controlador/   # Teclado
│   │   │   └── Tipografia/    # Fuentes personalizadas
│   │   └── resources/
│   │       ├── Imagenes/
│   │       ├── Sonidos/
│   │       ├── Tipografia/
│   │       └── db.properties.example
│   └── test/java/             # Tests (espacio preparado)
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

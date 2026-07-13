# Guía de setup

## JDK 17

1. Descarga Temurin 17 o superior: https://adoptium.net/
2. Instala y verifica:

```bash
java -version
javac -version
```

3. (Windows) Define `JAVA_HOME` apuntando al JDK, no al JRE.

## Maven

1. Descarga: https://maven.apache.org/download.cgi
2. Añade `bin` al `PATH`.
3. Verifica: `mvn -version`.

## Persistencia local (SQLite)

No se necesita SQL Server. Los records e historial viven en:

```text
data/space-chemistry.db
```

Se crea solo al guardar la primera partida. Si hay `scores.json` o `Highscore.txt` antiguos, se importan una vez.

## Primera ejecución

```bash
mvn clean package
mvn exec:java
```

Clase principal: `Game.Inicio`.

JAR ejecutable:

```bash
java -jar target/space-chemistry-1.4.0-SNAPSHOT.jar
```

## Tests y CI

```bash
mvn -B verify
```

En GitHub, el workflow `.github/workflows/ci.yml` ejecuta lo mismo en JDK 17 y 21 en cada push/PR a `main`.

## Instalador nativo (jpackage)

Requiere un JDK completo con la herramienta `jpackage` (no solo un JRE).

```bash
mvn -P jpackage package
```

Salida en `target/dist/` (app image / instalador según SO). En Windows habilita menú, acceso directo y selector de carpeta.

## IDE recomendados

| IDE | Cómo abrir |
|-----|------------|
| IntelliJ IDEA | Open → seleccionar `pom.xml` |
| VS Code / Cursor | Extensión Extension Pack for Java + abrir carpeta |
| Eclipse | Import → Existing Maven Project |
| NetBeans | Open Project (Maven); los `.form` siguen editables |

## Problemas frecuentes

| Síntoma | Causa probable | Solución |
|---------|----------------|----------|
| No se oye música | WAV no encontrado en classpath | Verificar `src/main/resources/Sonidos` |
| Scores no aparecen en Records | Primera partida / DB vacía | Juega una partida; se crea `data/space-chemistry.db` |
| Scores no guardan | Permisos en carpeta `data/` | Ejecutar desde un directorio escribible |
| `UnsupportedClassVersionError` | JDK < 17 | Actualizar JAVA_HOME |
| Pantalla en blanco / NPE en imágenes | Recurso mal nombrado | Rutas case-sensitive: `/Imagenes/...` |
| `illegal character: '\ufeff'` | BOM UTF-8 en fuentes | Guardar como UTF-8 sin BOM |

## Assets

Las imágenes y sonidos son originales del equipo. No reutilizar fuera del proyecto sin autorización (ver README).

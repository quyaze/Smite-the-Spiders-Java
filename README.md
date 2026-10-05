<div align="center">
    <img align="center" src="./assets/images/title.png"alt="Smite the Spiders"width="80%"></img>
    <p><strong>v1.0.2-alpha</strong></p>
    <img src="./.github/assets/cover.png"alt="Smite the Spiders"width="50%"></img>
</div>

<hr>

### Smite the Spiders (Java)

A remake of "Shoot the Spiders" in CSC 132 (Louisiana Tech University). Originally written in Python using [Pygame](https://www.pygame.org/docs/), it is now developed in Java using LibGDX.

Download the game under the latest GitHub release! Alternatively, you may generate a standlone application by running

```sh
./gradlew clean build jpackage
```

This creates a runnable application image in <code><a href="./lwjgl3/">lwjgl/</a>build/jpackage</code> that you can run (e.g. "Smite the Spiders.exe"). Consult the [Gradle tasks](#gradle) down below for guidance.

Built with [JDK 26.0](https://www.oracle.com/java/technologies/javase/jdk26-archive-downloads.html)

<hr>

## About Project

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

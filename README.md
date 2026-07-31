# Smite the Spiders (Java)

A remake of "Shoot the Spiders" in CSC 132 (Louisiana Tech University). Originally written in Python using Pygame, it is now developed in Java using LibGDX.

Built with [JDK 26](https://www.oracle.com/java/technologies/downloads/#java26)

## Controls

<div align="center">
    <span>
        <span>
            Move<br><code>[WASD] / [Arrows] / [Numpad Arrows]</code>
        </span>
        <span>
            Fire a spell<br><code>[SPACE]</code>
        </span>
        <span>
            Spawn spiders<br><code>[Q]</code>
        </span>
        <span>
            Quit to main menu<br><code>[ESCAPE], [SPACE]</code>
        </span>
        <span>
            Quit game (main menu)<br><code>[ESCAPE]</code>
        </span>
    </span>
</div>

## Application Build

To build a standalone application, run `./gradlew clean build jpackage`. This will generate the application image in the designated directory.

See [Gradle tasks](#gradle) down below for guidance.

<div align="center">
    <table>
        <tr>
            <th>Platform</th>
            <th>File</th>
            <th>Buildable</th>
            <th>Output Directory</th>
        </tr>
        <tr>
            <td>Windows</td>
            <td><code>.exe</code></td>
            <td>✅</td>
            <td><code>/lwjgl3/build/jpackage/Smite the Spiders/</code></td>
        </tr>
        <tr>
            <td>Linux</td>
            <td><code>file</code></td>
            <td>✅ WSL</td>
            <td><code>/lwjgl3/build/jpackage/Smite the Spiders/bin/</code></td>
        </tr>
        <tr>
            <td>macOS</td>
            <td><code>.app</code></td>
            <td>❌ unknown</td>
            <td>❌ unknown</td>
        </tr>
    </table>
</div>

## About Project (<i>Generated</i>)

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

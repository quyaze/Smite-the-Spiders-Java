package quyaze.stsj.lwjgl3;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import quyaze.stsj.GameMaster;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        
        /*  Pack textures into an atlas during development
        */
        if (1 == args.length && "--texture-packer".equals(args[0]))
        {
            TexturePacker.process("images", "packed", "packed");
        }
        
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new GameMaster(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        
        configuration.setTitle("Smite the Spiders");
        configuration.useVsync(true);
        configuration.setForegroundFPS(displayMode.refreshRate + 1);
        configuration.setFullscreenMode(displayMode);
        // configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon(
            "icon_16.png",
            "icon_24.png",
            "icon_32.png",
            "icon_48.png",
            "icon_64.png"
        );
        
        // implementation "com.badlogicgames.gdx:gdx-lwjgl3-angle:$gdxVersion"
        // configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);
        
        return configuration;
    }
}
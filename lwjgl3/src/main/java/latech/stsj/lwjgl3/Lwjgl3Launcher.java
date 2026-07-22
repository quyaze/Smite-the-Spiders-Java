package latech.stsj.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import latech.stsj.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        
        /*  TexturePacker will do its thing.
            Passing '--texture-packer' outside of development will cause directory issues.
        */
        if (args.length == 1 && args[0].equals("--texture-packer"))
        {
            Settings settings = new Settings();
            TexturePacker.process(settings, "./images", "./", "packed");
        }
        
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        
        configuration.setTitle("Smite the Spiders (Java)");
        configuration.setWindowIcon("icon_16.png", "icon_24.png", "icon_32.png", "icon_48.png", "icon_64.png");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        
        return configuration;
    }
}
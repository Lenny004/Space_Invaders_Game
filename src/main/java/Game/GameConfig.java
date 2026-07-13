package Game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistence.RunEntry;

/**
 * Preferencias de usuario persistidas en {@code data/user-settings.properties}.
 */
public final class GameConfig {

    private static final Logger LOG = Logger.getLogger(GameConfig.class.getName());
    private static final Path SETTINGS_PATH = Path.of("data", "user-settings.properties");

    private static final GameConfig INSTANCE = new GameConfig();

    private float sfxVolume = 0.8f;
    private float musicVolume = 0.7f;
    private boolean muted;
    private boolean fullscreen;
    private String language = "es";
    private int difficulty = RunEntry.DIFFICULTY_HARD;

    private GameConfig() {
        load();
        Configuracion.TipoDificultad = difficulty;
    }

    public static GameConfig getInstance() {
        return INSTANCE;
    }

    public float getSfxVolume() {
        return muted ? 0f : sfxVolume;
    }

    public float getMusicVolume() {
        return muted ? 0f : musicVolume;
    }

    public float getSfxVolumeRaw() {
        return sfxVolume;
    }

    public float getMusicVolumeRaw() {
        return musicVolume;
    }

    public boolean isMuted() {
        return muted;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public String getLanguage() {
        return language;
    }

    public Locale getLocale() {
        return "en".equalsIgnoreCase(language) ? Locale.ENGLISH : Locale.forLanguageTag("es");
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setSfxVolume(float volume) {
        this.sfxVolume = clamp01(volume);
        save();
        applyAudio();
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = clamp01(volume);
        save();
        applyAudio();
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        save();
        applyAudio();
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        save();
    }

    public void setLanguage(String language) {
        this.language = "en".equalsIgnoreCase(language) ? "en" : "es";
        save();
        Messages.reload(getLocale());
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = RunEntry.normalizeDifficulty(difficulty);
        Configuracion.TipoDificultad = this.difficulty;
        save();
    }

    /** Aplica volumen/mute a la música en reproducción. */
    public void applyAudio() {
        MusicPlayer.setVolume(getMusicVolume());
    }

    public void applyTo(SoundEffect... effects) {
        float volume = getSfxVolume();
        for (SoundEffect effect : effects) {
            if (effect != null) {
                effect.setVolume(volume);
            }
        }
    }

    private void load() {
        if (!Files.isRegularFile(SETTINGS_PATH)) {
            return;
        }
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(SETTINGS_PATH)) {
            props.load(in);
            sfxVolume = parseFloat(props.getProperty("sfxVolume"), sfxVolume);
            musicVolume = parseFloat(props.getProperty("musicVolume"), musicVolume);
            muted = Boolean.parseBoolean(props.getProperty("muted", "false"));
            fullscreen = Boolean.parseBoolean(props.getProperty("fullscreen", "false"));
            language = props.getProperty("language", language);
            if (!"en".equalsIgnoreCase(language)) {
                language = "es";
            }
            difficulty = RunEntry.normalizeDifficulty(
                    parseInt(props.getProperty("difficulty"), difficulty));
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "No se pudieron cargar preferencias: " + SETTINGS_PATH, ex);
        }
    }

    private void save() {
        try {
            Files.createDirectories(SETTINGS_PATH.getParent());
            Properties props = new Properties();
            props.setProperty("sfxVolume", Float.toString(sfxVolume));
            props.setProperty("musicVolume", Float.toString(musicVolume));
            props.setProperty("muted", Boolean.toString(muted));
            props.setProperty("fullscreen", Boolean.toString(fullscreen));
            props.setProperty("language", language);
            props.setProperty("difficulty", Integer.toString(difficulty));
            try (OutputStream out = Files.newOutputStream(SETTINGS_PATH)) {
                props.store(out, "Space Chemistry user settings");
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "No se pudieron guardar preferencias: " + SETTINGS_PATH, ex);
        }
    }

    private static float clamp01(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    private static float parseFloat(String raw, float fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return clamp01(Float.parseFloat(raw.trim()));
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private static int parseInt(String raw, int fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}

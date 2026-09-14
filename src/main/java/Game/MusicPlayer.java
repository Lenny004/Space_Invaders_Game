package Game;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * Reproductor de música de fondo en bucle.
 * Usa {@link SourceDataLine} (streaming) en lugar de {@link javax.sound.sampled.Clip}:
 * los temas de nivel pesan decenas de MB y Clip no es fiable con archivos grandes
 * ni cuando se cierra el {@link AudioInputStream} tras {@code open()}.
 */
public class MusicPlayer {

    private static final int BUFFER_SIZE = 16 * 1024;
    private static final Object LOCK = new Object();

    private static volatile boolean playing;
    private static volatile String lastSource;
    private static volatile float volume = 0.7f;
    private static Thread playerThread;
    private static SourceDataLine line;

    private MusicPlayer() {
    }

    /**
     * Reproduce música en bucle. Acepta rutas tipo {@code /Sonidos/TituloGame.wav}
     * o rutas legacy {@code src/Sonidos/TituloGame.wav}.
     */
    public static void IntentarMusica(String source) {
        try {
            String normalized = normalize(source);
            synchronized (LOCK) {
                if (normalized.equals(lastSource) && playing) {
                    return;
                }
                stopInternal();
                lastSource = normalized;
                playing = true;
                playerThread = new Thread(() -> runLoop(normalized), "MusicPlayer");
                playerThread.setDaemon(true);
                playerThread.start();
            }
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la canción: " + source + " (" + e.getMessage() + ")");
        }
    }

    public static void PonerMusica(String source) {
        IntentarMusica(source);
    }

    public static void PararMusica() {
        synchronized (LOCK) {
            lastSource = null;
            stopInternal();
        }
    }

    /**
     * Volumen lineal 0.0–1.0 (mapeado a MASTER_GAIN en dB).
     */
    public static void setVolume(float gainPercent) {
        volume = Math.max(0f, Math.min(1f, gainPercent));
        SourceDataLine current = line;
        if (current != null) {
            applyVolumeToLine(current);
        }
    }

    static String normalize(String source) {
        if (source == null) {
            return "";
        }
        String path = source.replace('\\', '/');
        if (path.startsWith("src/Sonidos/") || path.startsWith("src/sonidos/")) {
            path = path.substring(3);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    private static void stopInternal() {
        playing = false;
        SourceDataLine current = line;
        line = null;
        if (current != null) {
            try {
                current.stop();
                current.flush();
                current.close();
            } catch (Exception ignored) {
            }
        }
        Thread t = playerThread;
        playerThread = null;
        if (t != null && t != Thread.currentThread()) {
            t.interrupt();
            try {
                t.join(1500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void runLoop(String resource) {
        while (playing && resource.equals(lastSource)) {
            if (!streamOnce(resource)) {
                break;
            }
        }
    }

    private static boolean streamOnce(String resource) {
        try (InputStream raw = MusicPlayer.class.getResourceAsStream(resource)) {
            if (raw == null) {
                System.out.println("No se encontró el recurso de música: " + resource);
                playing = false;
                return false;
            }
            try (BufferedInputStream buffered = new BufferedInputStream(raw);
                 AudioInputStream stream = openPcmStream(buffered)) {
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
                sdl.open(format);
                synchronized (LOCK) {
                    if (!playing || !resource.equals(lastSource)) {
                        sdl.close();
                        return false;
                    }
                    line = sdl;
                }
                sdl.start();
                applyVolumeToLine(sdl);

                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while (playing && resource.equals(lastSource) && (n = stream.read(buf)) >= 0) {
                    int offset = 0;
                    while (offset < n && playing && resource.equals(lastSource)) {
                        int written = sdl.write(buf, offset, n - offset);
                        if (written <= 0) {
                            break;
                        }
                        offset += written;
                    }
                }

                if (playing && resource.equals(lastSource)) {
                    sdl.drain();
                }
                synchronized (LOCK) {
                    if (line == sdl) {
                        line = null;
                    }
                }
                try {
                    sdl.stop();
                    sdl.flush();
                    sdl.close();
                } catch (Exception ignored) {
                }
                return playing && resource.equals(lastSource);
            }
        } catch (Exception e) {
            if (playing) {
                System.out.println("No se pudo reproducir la canción: " + resource + " (" + e.getMessage() + ")");
            }
            playing = false;
            return false;
        }
    }

    private static AudioInputStream openPcmStream(BufferedInputStream buffered) throws Exception {
        AudioInputStream original = AudioSystem.getAudioInputStream(buffered);
        AudioFormat src = original.getFormat();
        if (AudioFormat.Encoding.PCM_SIGNED.equals(src.getEncoding()) && src.getSampleSizeInBits() == 16) {
            return original;
        }
        int channels = src.getChannels() <= 0 ? 2 : src.getChannels();
        AudioFormat pcm = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                src.getSampleRate(),
                16,
                channels,
                channels * 2,
                src.getSampleRate(),
                false);
        return AudioSystem.getAudioInputStream(pcm, original);
    }

    private static void applyVolumeToLine(SourceDataLine sdl) {
        if (sdl == null) {
            return;
        }
        try {
            if (sdl.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(linearToDb(volume, gain));
            } else if (sdl.isControlSupported(FloatControl.Type.VOLUME)) {
                FloatControl vol = (FloatControl) sdl.getControl(FloatControl.Type.VOLUME);
                float min = vol.getMinimum();
                float max = vol.getMaximum();
                vol.setValue(min + (max - min) * volume);
            }
        } catch (Exception ignored) {
        }
    }

    private static float linearToDb(float linear, FloatControl gain) {
        if (linear <= 0.0001f) {
            return gain.getMinimum();
        }
        float db = (float) (20.0 * Math.log10(linear));
        return Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), db));
    }
}

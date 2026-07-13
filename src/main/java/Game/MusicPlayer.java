package Game;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Reproductor de música de fondo con bucle.
 * Reemplaza {@code java.applet.Applet.newAudioClip}, eliminado en Java 9+.
 */
public class MusicPlayer {

    private static Clip musica;
    private static String lastSource;

    private MusicPlayer() {
    }

    /**
     * Reproduce música en bucle. Acepta rutas tipo {@code /Sonidos/TituloGame.wav}
     * o rutas legacy {@code src/Sonidos/TituloGame.wav}.
     */
    public static void IntentarMusica(String source) {
        try {
            String normalized = normalize(source);
            if (lastSource != null && lastSource.equals(normalized)) {
                return;
            }
            PararMusica();
            PonerMusica(normalized);
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la canción: " + source);
        }
    }

    public static void PonerMusica(String source) {
        try {
            String normalized = normalize(source);
            try (InputStream raw = MusicPlayer.class.getResourceAsStream(normalized)) {
                if (raw == null) {
                    System.out.println("No se encontró el recurso de música: " + normalized);
                    return;
                }
                try (BufferedInputStream buffered = new BufferedInputStream(raw);
                     AudioInputStream audioStream = AudioSystem.getAudioInputStream(buffered)) {
                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);
                    musica = (Clip) AudioSystem.getLine(info);
                    musica.open(audioStream);
                    lastSource = normalized;
                    musica.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la canción: " + source);
        }
    }

    public static void PararMusica() {
        try {
            if (musica != null) {
                musica.stop();
                musica.close();
                musica = null;
            }
            lastSource = null;
        } catch (Exception e) {
            System.out.println("No se pudo Detener la canción");
        }
    }

    private static String normalize(String source) {
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
}

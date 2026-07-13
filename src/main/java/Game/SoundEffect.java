package Game;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

/**
 * Reproductor de efectos de sonido compatible con Java 9+.
 * Reemplaza la API interna eliminada {@code sun.audio.AudioStream}/{@code AudioPlayer}.
 */
public class SoundEffect {

    private Clip clip;

    private SoundEffect(Clip clip) {
        this.clip = clip;
    }

    /**
     * Carga un WAV desde el classpath (ej: {@code /Sonidos/bulletSound.wav}).
     */
    public static SoundEffect load(String resourcePath) {
        try (InputStream raw = SoundEffect.class.getResourceAsStream(resourcePath)) {
            if (raw == null) {
                System.err.println("No se encontró el recurso de audio: " + resourcePath);
                return new SoundEffect(null);
            }
            try (BufferedInputStream buffered = new BufferedInputStream(raw);
                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(buffered)) {
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(audioStream);
                return new SoundEffect(clip);
            }
        } catch (Exception ex) {
            System.err.println("Error cargando audio '" + resourcePath + "': " + ex.getMessage());
            return new SoundEffect(null);
        }
    }

    public void play() {
        if (clip == null) {
            return;
        }
        try {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception ex) {
            System.err.println("Error reproduciendo efecto de sonido: " + ex.getMessage());
        }
    }

    public void stop() {
        if (clip == null) {
            return;
        }
        try {
            clip.stop();
            clip.setFramePosition(0);
        } catch (Exception ex) {
            System.err.println("Error deteniendo efecto de sonido: " + ex.getMessage());
        }
    }

    public void setVolume(float gainPercent) {
        if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float min = gain.getMinimum();
        float max = gain.getMaximum();
        float value = min + (max - min) * Math.max(0f, Math.min(1f, gainPercent));
        gain.setValue(value);
    }
}

package Game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MusicPlayerTest {

    @AfterEach
    void tearDown() {
        MusicPlayer.PararMusica();
    }

    @Test
    void normalizeAcceptsLegacyAndClasspathPaths() {
        assertEquals("/Sonidos/TituloGame.wav", MusicPlayer.normalize("src/Sonidos/TituloGame.wav"));
        assertEquals("/Sonidos/TituloGame.wav", MusicPlayer.normalize("/Sonidos/TituloGame.wav"));
        assertEquals("/Sonidos/Nivel1.wav", MusicPlayer.normalize("src/Sonidos/Nivel1.wav"));
    }

    @Test
    void missingResourceDoesNotThrow() {
        assertDoesNotThrow(() -> MusicPlayer.IntentarMusica("/Sonidos/__no_existe__.wav"));
        assertDoesNotThrow(MusicPlayer::PararMusica);
    }

    @Test
    void setVolumeAcceptsRange() {
        assertDoesNotThrow(() -> MusicPlayer.setVolume(0f));
        assertDoesNotThrow(() -> MusicPlayer.setVolume(0.65f));
        assertDoesNotThrow(() -> MusicPlayer.setVolume(1.5f));
    }
}

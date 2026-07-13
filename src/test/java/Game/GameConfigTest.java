package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {

    @Test
    void clampAndMuteZeroVolumes() {
        GameConfig config = GameConfig.getInstance();
        float previousSfx = config.getSfxVolumeRaw();
        float previousMusic = config.getMusicVolumeRaw();
        boolean previousMute = config.isMuted();
        try {
            config.setMuted(false);
            config.setSfxVolume(0.5f);
            config.setMusicVolume(0.4f);
            assertEquals(0.5f, config.getSfxVolume(), 0.001f);
            assertEquals(0.4f, config.getMusicVolume(), 0.001f);
            config.setMuted(true);
            assertEquals(0f, config.getSfxVolume(), 0.001f);
            assertEquals(0f, config.getMusicVolume(), 0.001f);
            assertEquals(0.5f, config.getSfxVolumeRaw(), 0.001f);
        } finally {
            config.setMuted(previousMute);
            config.setSfxVolume(previousSfx);
            config.setMusicVolume(previousMusic);
        }
    }

    @Test
    void languageNormalizedToEsOrEn() {
        GameConfig config = GameConfig.getInstance();
        String previous = config.getLanguage();
        try {
            config.setLanguage("en");
            assertEquals("en", config.getLanguage());
            config.setLanguage("fr");
            assertEquals("es", config.getLanguage());
        } finally {
            config.setLanguage(previous);
        }
    }
}

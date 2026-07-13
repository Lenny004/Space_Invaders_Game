package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Smoke tests del sistema de audio basado en javax.sound.sampled.
 */
class SoundEffectTest {

    @Test
    void loadMissingResourceDoesNotThrow() {
        SoundEffect effect = SoundEffect.load("/Sonidos/__no_existe__.wav");
        assertNotNull(effect);
        assertDoesNotThrow(effect::play);
        assertDoesNotThrow(effect::stop);
    }

    @Test
    void loadExistingBulletSound() {
        SoundEffect effect = SoundEffect.load("/Sonidos/bulletSound.wav");
        assertNotNull(effect);
        assertDoesNotThrow(effect::play);
    }
}

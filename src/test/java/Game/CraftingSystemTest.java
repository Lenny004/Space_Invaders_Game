package Game;

import Controlador.KeyboardController;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class CraftingSystemTest {

    @Test
    void craftIronConsumesElementAndRaisesBullets() {
        int[] elements = new int[12];
        elements[6] = 1;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();
        KeyboardController keys = pressed(KeyEvent.VK_F);

        CraftingSystem.Result result = CraftingSystem.tryCraft(keys, elements, flags);

        assertNotNull(result);
        assertEquals("F", result.getFeedbackKey());
        assertEquals(1, result.getMinBullets());
        assertNull(result.getSpeedLevel());
        assertEquals(0, elements[6]);
    }

    @Test
    void craftCannotBeRepeatedInSameRun() {
        int[] elements = new int[12];
        elements[6] = 2;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();
        KeyboardController keys = pressed(KeyEvent.VK_F);

        assertNotNull(CraftingSystem.tryCraft(keys, elements, flags));
        assertNull(CraftingSystem.tryCraft(keys, elements, flags));
        assertEquals(1, elements[6]);
    }

    @Test
    void craftHeliumSetsSpeed() {
        int[] elements = new int[12];
        elements[4] = 1;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();

        CraftingSystem.Result result = CraftingSystem.tryCraft(pressed(KeyEvent.VK_H), elements, flags);

        assertNotNull(result);
        assertEquals("H", result.getFeedbackKey());
        assertEquals(1, result.getSpeedLevel());
        assertNull(result.getMinBullets());
    }

    @Test
    void flagsResetAllowsCraftingAgain() {
        int[] elements = new int[12];
        elements[6] = 2;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();
        KeyboardController keys = pressed(KeyEvent.VK_F);

        assertNotNull(CraftingSystem.tryCraft(keys, elements, flags));
        flags.reset();
        assertNotNull(CraftingSystem.tryCraft(keys, elements, flags));
        assertEquals(0, elements[6]);
    }

    @Test
    void missingIngredientsDoesNothing() {
        int[] elements = new int[12];
        CraftingSystem.Flags flags = new CraftingSystem.Flags();

        assertNull(CraftingSystem.tryCraft(pressed(KeyEvent.VK_O), elements, flags));
    }

    private static KeyboardController pressed(int keyCode) {
        KeyboardController keys = new KeyboardController();
        keys.keyPressed(new KeyEvent(
                new java.awt.Label(),
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED));
        return keys;
    }
}

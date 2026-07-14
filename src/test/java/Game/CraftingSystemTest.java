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
        KeyboardController keys = pressed(KeyEvent.VK_1);

        CraftingSystem.Result result = CraftingSystem.tryCraft(keys, elements, flags);

        assertNotNull(result);
        assertEquals("Fe", result.getFeedbackKey());
        assertEquals(1, result.getMinBullets());
        assertNull(result.getSpeedLevel());
        assertEquals(0, elements[6]);
        assertTrue(flags.isUsed(0));
    }

    @Test
    void craftCannotBeRepeatedInSameRun() {
        int[] elements = new int[12];
        elements[6] = 2;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();
        KeyboardController keys = pressed(KeyEvent.VK_1);

        assertNotNull(CraftingSystem.tryCraft(keys, elements, flags));
        assertNull(CraftingSystem.tryCraft(keys, elements, flags));
        assertEquals(1, elements[6]);
    }

    @Test
    void craftHeliumSetsSpeed() {
        int[] elements = new int[12];
        elements[4] = 1;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();

        CraftingSystem.Result result = CraftingSystem.tryCraft(pressed(KeyEvent.VK_Q), elements, flags);

        assertNotNull(result);
        assertEquals("He", result.getFeedbackKey());
        assertEquals(1, result.getSpeedLevel());
        assertNull(result.getMinBullets());
    }

    @Test
    void flagsResetAllowsCraftingAgain() {
        int[] elements = new int[12];
        elements[6] = 2;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();
        KeyboardController keys = pressed(KeyEvent.VK_1);

        assertNotNull(CraftingSystem.tryCraft(keys, elements, flags));
        flags.reset();
        assertNotNull(CraftingSystem.tryCraft(keys, elements, flags));
        assertEquals(0, elements[6]);
    }

    @Test
    void missingIngredientsDoesNothing() {
        int[] elements = new int[12];
        CraftingSystem.Flags flags = new CraftingSystem.Flags();

        assertNull(CraftingSystem.tryCraft(pressed(KeyEvent.VK_3), elements, flags));
    }

    @Test
    void oldLetterHotkeysNoLongerCraft() {
        int[] elements = new int[12];
        elements[6] = 1;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();

        assertNull(CraftingSystem.tryCraft(pressed(KeyEvent.VK_F), elements, flags));
        assertEquals(1, elements[6]);
    }

    @Test
    void canCraftReflectsIngredientsAndFlags() {
        int[] elements = new int[12];
        elements[6] = 1;
        CraftingSystem.Flags flags = new CraftingSystem.Flags();
        CraftingSystem.Recipe iron = CraftingSystem.RECIPES[0];

        assertTrue(CraftingSystem.canCraft(iron, 0, elements, flags));
        CraftingSystem.tryCraftIndex(0, elements, flags);
        assertFalse(CraftingSystem.canCraft(iron, 0, elements, flags));
    }

    @Test
    void recipesHaveTwelveEntriesWithDistinctHotkeys() {
        assertEquals(12, CraftingSystem.RECIPES.length);
        assertEquals("1", CraftingSystem.RECIPES[0].getHotkeyLabel());
        assertEquals("6", CraftingSystem.RECIPES[5].getHotkeyLabel());
        assertEquals("Q", CraftingSystem.RECIPES[6].getHotkeyLabel());
        assertEquals("Y", CraftingSystem.RECIPES[11].getHotkeyLabel());
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

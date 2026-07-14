package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TutorialControllerTest {

    @Test
    void moveAndShootAdvancesToCollect() {
        TutorialController tutorial = new TutorialController();
        assertEquals(TutorialController.Step.MOVE_SHOOT, tutorial.getStep());

        tutorial.onMoved();
        assertEquals(TutorialController.Step.MOVE_SHOOT, tutorial.getStep());

        tutorial.onFired();
        assertEquals(TutorialController.Step.COLLECT, tutorial.getStep());
        assertTrue(tutorial.consumeDropSpawnRequest());
        assertFalse(tutorial.consumeDropSpawnRequest());
    }

    @Test
    void collectAdvancesToCraft() {
        TutorialController tutorial = new TutorialController();
        tutorial.onMoved();
        tutorial.onFired();
        tutorial.consumeDropSpawnRequest();

        tutorial.onDropCollected();
        assertEquals(TutorialController.Step.CRAFT, tutorial.getStep());
        assertTrue(tutorial.consumeCraftAssistRequest());
    }

    @Test
    void craftIronCompletesTutorial() {
        TutorialController tutorial = new TutorialController();
        tutorial.onMoved();
        tutorial.onFired();
        tutorial.onDropCollected();
        tutorial.consumeCraftAssistRequest();

        tutorial.onCrafted(1);
        assertFalse(tutorial.isDone());

        tutorial.onCrafted(0);
        assertTrue(tutorial.isDone());
        assertTrue(tutorial.consumeCompletion());
        assertFalse(tutorial.consumeCompletion());
    }

    @Test
    void bannerKeysMatchSteps() {
        TutorialController tutorial = new TutorialController();
        assertEquals("tutorial.step.move", tutorial.bannerKey());
        tutorial.onMoved();
        tutorial.onFired();
        assertEquals("tutorial.step.collect", tutorial.bannerKey());
        tutorial.onDropCollected();
        assertEquals("tutorial.step.craft", tutorial.bannerKey());
        tutorial.onCrafted(0);
        assertEquals("tutorial.done", tutorial.bannerKey());
    }
}

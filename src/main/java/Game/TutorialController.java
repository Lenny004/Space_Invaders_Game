package Game;

/**
 * Tutorial corto en 3 pasos: mover/disparar → recoger drop → craftear.
 * No persiste puntuación; el progreso de pasos es solo de la sesión.
 */
public final class TutorialController {

    public enum Step {
        MOVE_SHOOT,
        COLLECT,
        CRAFT,
        DONE
    }

    private Step step = Step.MOVE_SHOOT;
    private boolean moved;
    private boolean fired;
    private boolean dropSpawnRequested;
    private boolean craftAssistRequested;
    private boolean completionHandled;

    public Step getStep() {
        return step;
    }

    public boolean isActive() {
        return step != Step.DONE;
    }

    public boolean isDone() {
        return step == Step.DONE;
    }

    public String bannerKey() {
        return switch (step) {
            case MOVE_SHOOT -> "tutorial.step.move";
            case COLLECT -> "tutorial.step.collect";
            case CRAFT -> "tutorial.step.craft";
            case DONE -> "tutorial.done";
        };
    }

    public void onMoved() {
        if (step != Step.MOVE_SHOOT) {
            return;
        }
        moved = true;
        tryAdvanceFromMoveShoot();
    }

    public void onFired() {
        if (step != Step.MOVE_SHOOT) {
            return;
        }
        fired = true;
        tryAdvanceFromMoveShoot();
    }

    public void onDropCollected() {
        if (step == Step.COLLECT) {
            advanceTo(Step.CRAFT);
        }
    }

    public void onCrafted(int recipeIndex) {
        if (step == Step.CRAFT && recipeIndex == 0) {
            advanceTo(Step.DONE);
        }
    }

    /** True una vez al entrar en COLLECT: el panel debe spawnear un drop de Fe. */
    public boolean consumeDropSpawnRequest() {
        if (step == Step.COLLECT && dropSpawnRequested) {
            dropSpawnRequested = false;
            return true;
        }
        return false;
    }

    /** True una vez al entrar en CRAFT: dar Fe si falta y abrir overlay. */
    public boolean consumeCraftAssistRequest() {
        if (step == Step.CRAFT && craftAssistRequested) {
            craftAssistRequested = false;
            return true;
        }
        return false;
    }

    /** True una sola vez cuando el tutorial termina. */
    public boolean consumeCompletion() {
        if (step == Step.DONE && !completionHandled) {
            completionHandled = true;
            return true;
        }
        return false;
    }

    private void tryAdvanceFromMoveShoot() {
        if (moved && fired) {
            advanceTo(Step.COLLECT);
        }
    }

    private void advanceTo(Step next) {
        step = next;
        if (next == Step.COLLECT) {
            dropSpawnRequested = true;
        } else if (next == Step.CRAFT) {
            craftAssistRequested = true;
        }
    }
}

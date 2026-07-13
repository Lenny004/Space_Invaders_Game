package Game;

import Controlador.KeyboardController;

/**
 * Crafteo de compuestos a partir del inventario de elementos.
 * Cada receta solo puede usarse una vez por partida.
 */
public final class CraftingSystem {

    public static final class Flags {
        private int water;
        private int bronze;
        private int tnt;
        private int uranium;
        private int tungsten;
        private int iron;
        private int nitric;
        private int sulfuric;
        private int benzene;
        private int butane;
        private int helium;
        private int propane;

        public void reset() {
            water = bronze = tnt = uranium = tungsten = iron = 0;
            nitric = sulfuric = benzene = butane = helium = propane = 0;
        }
    }

    public static final class Result {
        private final String feedbackKey;
        private final Integer minBullets;
        private final Integer speedLevel;

        private Result(String feedbackKey, Integer minBullets, Integer speedLevel) {
            this.feedbackKey = feedbackKey;
            this.minBullets = minBullets;
            this.speedLevel = speedLevel;
        }

        public String getFeedbackKey() {
            return feedbackKey;
        }

        public Integer getMinBullets() {
            return minBullets;
        }

        public Integer getSpeedLevel() {
            return speedLevel;
        }
    }

    private CraftingSystem() {
    }

    /**
     * Intenta craftear según teclas pulsadas. Devuelve el primer craft exitoso del frame.
     */
    public static Result tryCraft(KeyboardController keys, int[] elements, Flags flags) {
        if (keys == null || elements == null || flags == null) {
            return null;
        }
        // Balas
        if (keys.getKeyStatus(79) && flags.water < 1 && elements[5] >= 2 && elements[8] >= 1) {
            elements[5] -= 2;
            elements[8]--;
            flags.water++;
            return new Result("O", 3, null);
        }
        if (keys.getKeyStatus(90) && flags.bronze < 1 && elements[3] >= 3 && elements[11] >= 2) {
            elements[3] -= 3;
            elements[11] -= 2;
            flags.bronze++;
            return new Result("Z", 5, null);
        }
        if (keys.getKeyStatus(84) && flags.tnt < 1
                && elements[2] >= 7 && elements[5] >= 5 && elements[7] >= 3 && elements[8] >= 6) {
            elements[2] -= 7;
            elements[5] -= 5;
            elements[7] -= 3;
            elements[8] -= 6;
            flags.tnt++;
            return new Result("T", 6, null);
        }
        if (keys.getKeyStatus(85) && flags.uranium < 1 && elements[9] >= 1) {
            elements[9]--;
            flags.uranium++;
            return new Result("U", 4, null);
        }
        if (keys.getKeyStatus(87) && flags.tungsten < 1 && elements[10] >= 1) {
            elements[10]--;
            flags.tungsten++;
            return new Result("W", 2, null);
        }
        if (keys.getKeyStatus(70) && flags.iron < 1 && elements[6] >= 1) {
            elements[6]--;
            flags.iron++;
            return new Result("F", 1, null);
        }
        // Velocidad
        if (keys.getKeyStatus(78) && flags.nitric < 1
                && elements[5] >= 1 && elements[7] >= 1 && elements[8] >= 3) {
            elements[5]--;
            elements[7]--;
            elements[8] -= 3;
            flags.nitric++;
            return new Result("N", null, 2);
        }
        if (keys.getKeyStatus(83) && flags.sulfuric < 1
                && elements[5] >= 2 && elements[1] >= 1 && elements[8] >= 4) {
            elements[5] -= 2;
            elements[1]--;
            elements[8] -= 4;
            flags.sulfuric++;
            return new Result("S", null, 3);
        }
        if (keys.getKeyStatus(66) && flags.benzene < 1 && elements[2] >= 6 && elements[5] >= 6) {
            elements[2] -= 6;
            elements[5] -= 6;
            flags.benzene++;
            return new Result("B", null, 4);
        }
        if (keys.getKeyStatus(65) && flags.butane < 1 && elements[2] >= 4 && elements[5] >= 10) {
            elements[2] -= 4;
            elements[5] -= 10;
            flags.butane++;
            return new Result("A", null, 6);
        }
        if (keys.getKeyStatus(72) && flags.helium < 1 && elements[4] >= 1) {
            elements[4]--;
            flags.helium++;
            return new Result("H", null, 1);
        }
        if (keys.getKeyStatus(80) && flags.propane < 1 && elements[2] >= 3 && elements[5] >= 8) {
            elements[2] -= 3;
            elements[5] -= 8;
            flags.propane++;
            return new Result("P", null, 5);
        }
        return null;
    }
}

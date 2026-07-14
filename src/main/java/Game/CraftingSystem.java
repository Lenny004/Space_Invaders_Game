package Game;

import Controlador.KeyboardController;

import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Crafteo de compuestos a partir del inventario de elementos.
 * Cada receta solo puede usarse una vez por partida.
 * Atajos: {@code 1}–{@code 6} (disparo) y {@code Q}–{@code Y} (velocidad), con overlay ({@code C}).
 */
public final class CraftingSystem {

    public static final class Cost {
        private final int elementIndex;
        private final int amount;

        public Cost(int elementIndex, int amount) {
            this.elementIndex = elementIndex;
            this.amount = amount;
        }

        public int getElementIndex() {
            return elementIndex;
        }

        public int getAmount() {
            return amount;
        }
    }

    public static final class Recipe {
        private final String id;
        private final String formula;
        private final String hotkeyLabel;
        private final int keyCode;
        private final Cost[] costs;
        private final Integer minBullets;
        private final Integer speedLevel;

        private Recipe(
                String id,
                String formula,
                String hotkeyLabel,
                int keyCode,
                Integer minBullets,
                Integer speedLevel,
                Cost... costs) {
            this.id = id;
            this.formula = formula;
            this.hotkeyLabel = hotkeyLabel;
            this.keyCode = keyCode;
            this.minBullets = minBullets;
            this.speedLevel = speedLevel;
            this.costs = costs;
        }

        public String getId() {
            return id;
        }

        public String getFormula() {
            return formula;
        }

        public String getHotkeyLabel() {
            return hotkeyLabel;
        }

        public int getKeyCode() {
            return keyCode;
        }

        public Cost[] getCosts() {
            return costs;
        }

        public Integer getMinBullets() {
            return minBullets;
        }

        public Integer getSpeedLevel() {
            return speedLevel;
        }

        public String getNameKey() {
            return "craft.recipe." + id + ".name";
        }

        public String getEffectKey() {
            return "craft.recipe." + id + ".effect";
        }

        public String formatCost() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < costs.length; i++) {
                if (i > 0) {
                    sb.append(" + ");
                }
                sb.append(costs[i].amount).append(' ').append(DropSystem.symbolFor(costs[i].elementIndex));
            }
            return sb.toString();
        }
    }

    /** Orden: 1–6 disparo, Q/W/E/R/T/Y velocidad. */
    public static final Recipe[] RECIPES = {
            new Recipe("iron", "Fe", "1", KeyEvent.VK_1, 1, null, new Cost(6, 1)),
            new Recipe("tungsten", "W", "2", KeyEvent.VK_2, 2, null, new Cost(10, 1)),
            new Recipe("water", "H2O", "3", KeyEvent.VK_3, 3, null, new Cost(5, 2), new Cost(8, 1)),
            new Recipe("uranium", "U", "4", KeyEvent.VK_4, 4, null, new Cost(9, 1)),
            new Recipe("bronze", "Cu3Zn2", "5", KeyEvent.VK_5, 5, null, new Cost(3, 3), new Cost(11, 2)),
            new Recipe("tnt", "C7H5N3O6", "6", KeyEvent.VK_6, 6, null,
                    new Cost(2, 7), new Cost(5, 5), new Cost(7, 3), new Cost(8, 6)),
            new Recipe("helium", "He", "Q", KeyEvent.VK_Q, null, 1, new Cost(4, 1)),
            new Recipe("nitric", "HNO3", "W", KeyEvent.VK_W, null, 2,
                    new Cost(5, 1), new Cost(7, 1), new Cost(8, 3)),
            new Recipe("sulfuric", "H2SO4", "E", KeyEvent.VK_E, null, 3,
                    new Cost(5, 2), new Cost(1, 1), new Cost(8, 4)),
            new Recipe("benzene", "C6H6", "R", KeyEvent.VK_R, null, 4, new Cost(2, 6), new Cost(5, 6)),
            new Recipe("propane", "C3H8", "T", KeyEvent.VK_T, null, 5, new Cost(2, 3), new Cost(5, 8)),
            new Recipe("butane", "C4H10", "Y", KeyEvent.VK_Y, null, 6, new Cost(2, 4), new Cost(5, 10)),
    };

    public static final class Flags {
        private final boolean[] used = new boolean[RECIPES.length];

        public void reset() {
            Arrays.fill(used, false);
        }

        public boolean isUsed(int recipeIndex) {
            return recipeIndex >= 0 && recipeIndex < used.length && used[recipeIndex];
        }

        public void markUsed(int recipeIndex) {
            if (recipeIndex >= 0 && recipeIndex < used.length) {
                used[recipeIndex] = true;
            }
        }
    }

    public static final class Result {
        private final String feedbackKey;
        private final Integer minBullets;
        private final Integer speedLevel;
        private final int recipeIndex;

        private Result(String feedbackKey, Integer minBullets, Integer speedLevel, int recipeIndex) {
            this.feedbackKey = feedbackKey;
            this.minBullets = minBullets;
            this.speedLevel = speedLevel;
            this.recipeIndex = recipeIndex;
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

        public int getRecipeIndex() {
            return recipeIndex;
        }
    }

    private CraftingSystem() {
    }

    public static boolean canCraft(Recipe recipe, int recipeIndex, int[] elements, Flags flags) {
        if (recipe == null || elements == null || flags == null || flags.isUsed(recipeIndex)) {
            return false;
        }
        return hasIngredients(recipe, elements);
    }

    public static boolean hasIngredients(Recipe recipe, int[] elements) {
        if (recipe == null || elements == null) {
            return false;
        }
        for (Cost cost : recipe.costs) {
            if (cost.elementIndex < 0 || cost.elementIndex >= elements.length
                    || elements[cost.elementIndex] < cost.amount) {
                return false;
            }
        }
        return true;
    }

    /**
     * Intenta craftear según teclas {@code 1}–{@code 6} / {@code Q}–{@code Y}.
     * Devuelve el primer craft exitoso del frame.
     */
    public static Result tryCraft(KeyboardController keys, int[] elements, Flags flags) {
        if (keys == null || elements == null || flags == null) {
            return null;
        }
        for (int i = 0; i < RECIPES.length; i++) {
            Recipe recipe = RECIPES[i];
            if (keys.getKeyStatus(recipe.keyCode) && canCraft(recipe, i, elements, flags)) {
                return craft(recipe, i, elements, flags);
            }
        }
        return null;
    }

    /** Craftea por índice de receta (p. ej. tests o UI). */
    public static Result tryCraftIndex(int recipeIndex, int[] elements, Flags flags) {
        if (elements == null || flags == null || recipeIndex < 0 || recipeIndex >= RECIPES.length) {
            return null;
        }
        Recipe recipe = RECIPES[recipeIndex];
        if (!canCraft(recipe, recipeIndex, elements, flags)) {
            return null;
        }
        return craft(recipe, recipeIndex, elements, flags);
    }

    private static Result craft(Recipe recipe, int recipeIndex, int[] elements, Flags flags) {
        for (Cost cost : recipe.costs) {
            elements[cost.elementIndex] -= cost.amount;
        }
        flags.markUsed(recipeIndex);
        return new Result(recipe.formula, recipe.minBullets, recipe.speedLevel, recipeIndex);
    }
}

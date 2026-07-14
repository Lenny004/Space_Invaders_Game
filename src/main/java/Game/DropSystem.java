package Game;

import java.util.List;
import java.util.Random;

/**
 * Spawns y recogida de drops de elementos (riesgo/recompensa).
 * El inventario solo aumenta al interceptar el drop con la nave.
 */
public final class DropSystem {

    public static final int MIN_ELEMENT = 1;
    public static final int MAX_ELEMENT = 11;
    public static final int DESPAWN_Y = 600;

    private static final String[] SYMBOLS = {
            null, "S", "C", "Cu", "He", "H", "Fe", "N", "O", "U", "W", "Zn"
    };

    private DropSystem() {
    }

    public static boolean isCollectible(int tipo) {
        return tipo >= MIN_ELEMENT && tipo <= MAX_ELEMENT;
    }

    /** Probabilidad de soltar un drop al matar un alien normal. */
    public static boolean shouldDrop(Random rng) {
        if (rng == null) {
            return false;
        }
        return rng.nextInt(100) < GameBalance.ELEMENT_DROP_CHANCE_PERCENT;
    }

    public static boolean shouldDropOnBossKill() {
        return GameBalance.ELEMENT_DROP_ON_BOSS;
    }

    /** Elemento coleccionable aleatorio en [{@link #MIN_ELEMENT}, {@link #MAX_ELEMENT}]. */
    public static int rollElement(Random rng) {
        if (rng == null) {
            return MIN_ELEMENT;
        }
        return rng.nextInt(MAX_ELEMENT - MIN_ELEMENT + 1) + MIN_ELEMENT;
    }

    public static String symbolFor(int tipo) {
        if (!isCollectible(tipo)) {
            return "?";
        }
        return SYMBOLS[tipo];
    }

    /**
     * Suma el elemento al inventario.
     *
     * @return feedback tipo {@code +Fe}, o {@code null} si no es coleccionable
     */
    public static String tryCollect(int[] inventory, int tipo) {
        if (inventory == null || !isCollectible(tipo) || tipo >= inventory.length) {
            return null;
        }
        inventory[tipo]++;
        return "+" + symbolFor(tipo);
    }

    /**
     * Avanza drops, resuelve colisión con la nave y elimina los que salen de pantalla.
     *
     * @return feedback del último pickup del frame, o {@code null}
     */
    public static String updateDrops(List<ElementoDrop> drops, Ship ship, int[] inventory) {
        if (drops == null || drops.isEmpty()) {
            return null;
        }

        String feedback = null;
        for (int index = drops.size() - 1; index >= 0; index--) {
            ElementoDrop drop = drops.get(index);
            if (drop == null) {
                drops.remove(index);
                continue;
            }

            drop.setYPosition(drop.getYPosition() + GameBalance.ELEMENT_FALL_SPEED);

            if (ship != null && drop.Colisionando(ship)) {
                String pickup = tryCollect(inventory, drop.getTipoElemento());
                if (pickup != null) {
                    feedback = pickup;
                }
                drops.remove(index);
            } else if (drop.getYPosition() > DESPAWN_Y) {
                drops.remove(index);
            }
        }
        return feedback;
    }
}

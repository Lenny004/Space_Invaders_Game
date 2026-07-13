package Game;

import java.awt.Color;

/**
 * @deprecated Usar {@link Projectile}. Se mantiene como alias de compatibilidad.
 */
@Deprecated
public class Bullet extends Projectile {

    public Bullet(int xPosition, int yPosition, int diameter, Color color) {
        super(xPosition, yPosition, color);
    }
}

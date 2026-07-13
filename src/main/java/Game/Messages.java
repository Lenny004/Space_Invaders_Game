package Game;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Acceso a cadenas i18n ({@code i18n/messages_*.properties}).
 */
public final class Messages {

    private static final String BUNDLE = "i18n.messages";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE, Locale.forLanguageTag("es"));

    private Messages() {
    }

    public static void reload(Locale locale) {
        Locale resolved = locale != null ? locale : Locale.forLanguageTag("es");
        bundle = ResourceBundle.getBundle(BUNDLE, resolved);
    }

    public static void reloadFromConfig() {
        reload(GameConfig.getInstance().getLocale());
    }

    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            return key;
        }
    }

    public static String format(String key, Object... args) {
        return String.format(get(key), args);
    }
}

package Game;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MessagesTest {

    @Test
    void loadsSpanishAndEnglishBundles() {
        Messages.reload(Locale.forLanguageTag("es"));
        assertEquals("Iniciar", Messages.get("menu.start"));
        Messages.reload(Locale.ENGLISH);
        assertEquals("Start", Messages.get("menu.start"));
        Messages.reload(Locale.forLanguageTag("es"));
    }
}

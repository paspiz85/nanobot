package it.paspiz85.nanobot.platform.win;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Keyboard mapping layout.
 *
 * @author paspiz85
 *
 */
public final class KeyboardMapping {

    /**
     * Single key mapping.
     *
     * @author paspiz85
     *
     */
    public static class Key {

        private final int code;

        private final boolean shifted;

        public Key(final int code, final boolean shifted) {
            this.code = code;
            this.shifted = shifted;
        }

        public int getCode() {
            return code;
        }

        public boolean isShifted() {
            return shifted;
        }
    }

    private static Map<Locale, KeyboardMapping> mappings = new HashMap<>();
    static {
        final KeyboardMapping it = new KeyboardMapping();
        it.putChar('\\', 0xDC);
        it.putChar('|', 0xDC, true);
        it.putChar('!', KeyEvent.VK_1, true);
        it.putChar('"', KeyEvent.VK_2, true);
        it.putChar('%', KeyEvent.VK_5, true);
        it.putChar('&', KeyEvent.VK_6, true);
        it.putChar('/', KeyEvent.VK_7, true);
        it.putChar('(', KeyEvent.VK_8, true);
        it.putChar(')', KeyEvent.VK_9, true);
        it.putChar('=', KeyEvent.VK_0, true);
        it.putChar('\'', 0xDB);
        it.putChar('?', 0xDB, true);
        it.putChar('+', 0xBB);
        it.putChar('*', 0xBB, true);
        it.putChar('-', 0xBD);
        it.putChar('_', 0xBD, true);
        it.putChar(' ', KeyEvent.VK_SPACE);
        it.putChar('.', 0xBE);
        it.putChar(':', 0xBE, true);
        it.putChar(',', 0xBC);
        it.putChar(';', 0xBC, true);
        it.putChar('<', 0xE2);
        it.putChar('>', 0xE2, true);
        it.putChar('à', 0xDE);
        it.putChar('°', 0xDE, true);
        it.putChar('è', 0xBA);
        it.putChar('é', 0xBA, true);
        it.putChar('ì', 0xDD);
        it.putChar('^', 0xDD, true);
        it.putChar('ò', 0xC0);
        it.putChar('ù', 0xBF);
        mappings.put(Locale.ITALY, it);
    }

    public static KeyboardMapping get(final Locale locale) {
        return mappings.get(locale);
    }

    private final Map<Character, Key> mapping;

    private KeyboardMapping() {
        mapping = new HashMap<>();
    }

    public Key getKey(final char ch) {
        return mapping.get(ch);
    }

    private void putChar(final char ch, final int code) {
        putChar(ch, code, false);
    }

    private void putChar(final char ch, final int code, final boolean shifted) {
        final Key key = new Key(code, shifted);
        mapping.put(ch, key);
    }
}

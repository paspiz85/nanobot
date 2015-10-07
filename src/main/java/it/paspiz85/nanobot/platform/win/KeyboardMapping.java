package it.paspiz85.nanobot.platform.win;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KeyboardMapping {

    public static class Key {

        private int code;

        private boolean shifted;

        public Key(int code, boolean shifted) {
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
        KeyboardMapping it = new KeyboardMapping();
        it.putChar('!', KeyEvent.VK_1, true);
        it.putChar('"', KeyEvent.VK_2, true);
        it.putChar('%', KeyEvent.VK_5, true);
        it.putChar('&', KeyEvent.VK_6, true);
        it.putChar('/', KeyEvent.VK_7, true);
        it.putChar('(', KeyEvent.VK_8, true);
        it.putChar(')', KeyEvent.VK_9, true);
        it.putChar('=', KeyEvent.VK_0, true);
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
        it.putChar('ù', 0xBF);

        it.putChar('\'', 0xDF);
        it.putChar('?', 0xDF, true);
        it.putChar('£', 0xDB);
        it.putChar('$', 0xDC);
        it.putChar('^', 0xDD);
        mappings.put(Locale.ITALY, it);
    }
    
    private final Map<Character, Key> mapping;

    public static KeyboardMapping get(Locale locale) {
        return mappings.get(locale);
    }

    private KeyboardMapping() {
        mapping = new HashMap<>();
    }

    public Key getKey(char ch) {
        return mapping.get(ch);
    }

    private void putChar(char ch, int code) {
        putChar(ch, code, false);
    }

    private void putChar(char ch, int code, boolean shifted) {
        Key key = new Key(code, shifted);
        mapping.put(ch, key);
    }
}

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
        it.putChar(' ', KeyEvent.VK_SPACE, false);
        it.putChar('.', KeyEvent.VK_PERIOD);
        it.putChar(',', KeyEvent.VK_COMMA); // screenshot
        it.putChar('-', KeyEvent.VK_MINUS);
        it.putChar('/', KeyEvent.VK_SLASH);
        it.putChar(';', KeyEvent.VK_SEMICOLON);
        it.putChar('=', KeyEvent.VK_EQUALS);
        it.putChar('\\', KeyEvent.VK_BACK_SLASH); // back
        it.putChar('*', KeyEvent.VK_ASTERISK);
        it.putChar(':', KeyEvent.VK_COLON);
        it.putChar('!', KeyEvent.VK_EXCLAMATION_MARK);
        it.putChar('_', KeyEvent.VK_UNDERSCORE);
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

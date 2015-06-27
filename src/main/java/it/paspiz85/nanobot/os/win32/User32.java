package it.paspiz85.nanobot.os.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends com.sun.jna.platform.win32.User32 {

    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean BlockInput(boolean block);

    boolean ClientToScreen(HWND hWnd, POINT lpPoint);

    @Override
    HWND FindWindow(String lpClassName, String lpWindowName);

    @Override
    short GetAsyncKeyState(int key);

    HWND GetDlgItem(HWND hDlg, int nIDDlgItem);

    short GetKeyState(int key);

    HDC GetWindowDC(HWND hWnd);

    int GetWindowRect(HWND handle, int[] rect);

    boolean ScreenToClient(HWND hWnd, POINT lpPoint);

    LRESULT SendMessage(HWND hWnd, int Msg, int wParam, int lParam);

    @Override
    HWND SetFocus(HWND hWnd);

    @Override
    boolean SetForegroundWindow(HWND hWnd);

    boolean SetWindowPos(HWND hWnd, int hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
}
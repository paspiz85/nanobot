package it.paspiz85.nanobot.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary {
	int ES_CONTINUOUS = 0x80000000;

	int ES_DISPLAY_REQUIRED = 0x00000002;

	int ES_SYSTEM_REQUIRED = 0x00000001;
	Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32",
			Kernel32.class);

	int SetThreadExecutionState(int EXECUTION_STATE);
}

package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.User32;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;

public class Robot {

	private static final Robot instance = new Robot();

	private static Random random = new Random();
	public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit()
			.getScreenSize().height;

	public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit()
			.getScreenSize().width;

	public static final String SYSTEM_OS = System.getProperty("os.name");
	public static final String USER_HOME_DIR = System.getProperty("user.home");
	public static final String USER_NAME = System.getProperty("user.name");
	public static final int VK_CONTROL = 0x11;
	public static final int VK_DOWN = 0x28;
	public static final int WM_COMMAND = 0x111;

	public static final int WM_KEYDOWN = 0x100;

	public static final int WM_KEYUP = 0x101;

	public static final int WM_LBUTTONDBLCLK = 0x203;

	public static final int WM_LBUTTONDOWN = 0x201;
	public static final int WM_LBUTTONUP = 0x202;
	public static final int WM_MOUSEWHEEL = 0x20A;
	// user32
	public static final int WM_NULL = 0x000;
	public static final int WM_RBUTTONDBLCLK = 0x206;
	public static final int WM_RBUTTONDOWN = 0x204;
	public static final int WM_RBUTTONUP = 0x205;
	public static final String WORKING_DIR = System.getProperty("user.dir");

	public static Robot instance() {
		return instance;
	}

	private static int makeParam(int low, int high) {
		// to work for negative numbers
		return high << 16 | low << 16 >>> 16;
	}

	private static void msgBox(String Text, String Title) {
		JOptionPane.showMessageDialog(null, Text, Title,
				JOptionPane.PLAIN_MESSAGE); // Show message box
	}

	public static Random random() {
		return random;
	}

	private HWND handler = null;

	// MY_CLIENT_61.638==MY_WINDOW.64.668
	protected final Logger logger = Logger.getLogger(getClass().getName());

	private java.awt.Robot r;

	private Robot() {
		try {
			r = new java.awt.Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public boolean clientToScreen(POINT clientPoint) {
		return User32.INSTANCE.ClientToScreen(handler, clientPoint);
	}

	public boolean compareColor(int c1, int c2, int var) {
		int r1 = c1 >> 16 & 0xFF;
		int r2 = c2 >> 16 & 0xFF;

		int g1 = c1 >> 8 & 0xFF;
		int g2 = c2 >> 8 & 0xFF;

		int b1 = c1 >> 0 & 0xFF;
		int b2 = c2 >> 0 & 0xFF;

		if (Math.abs(r1 - r2) > var || Math.abs(g1 - g2) > var
				|| Math.abs(b1 - b2) > var) {
			return false;
		} else {
			return true;
		}
	}

	public boolean confirmationBox(String msg, String title) {
		int result = JOptionPane.showConfirmDialog(null, msg, title,
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isClickableActive(Clickable clickable) {
		if (clickable.getColor() == null) {
			throw new IllegalArgumentException(clickable.name());
		}

		int tarColor = clickable.getColor().getRGB();
		int actualColor = pixelGetColor(clickable.getX(), clickable.getY())
				.getRGB();
		return compareColor(tarColor, actualColor, 5);
	}

	private boolean isCtrlKeyDown() {
		return User32.INSTANCE.GetKeyState(VK_CONTROL) < 0;
	}

	public void leftClick(Clickable clickable, int sleepInMs)
			throws InterruptedException {
		leftClickWin32(clickable.getX(), clickable.getY(), true);
		Thread.sleep(sleepInMs + random.nextInt(sleepInMs));
	}

	public void leftClick(int x, int y) {
		leftClickWin32(x, y, false);
	}

	public void leftClick(int x, int y, int sleepInMs)
			throws InterruptedException {
		leftClickWin32(x, y, false);
		Thread.sleep(sleepInMs + random.nextInt(sleepInMs));
	}

	private void leftClickWin32(int x, int y, boolean randomize) {
		// randomize coordinates little bit
		if (randomize) {
			x += -1 + random.nextInt(3);
			y += -1 + random.nextInt(3);
		}
		logger.finest("clicking " + x + " " + y);
		int lParam = makeParam(x, y);

		while (isCtrlKeyDown()) {
		}
		User32.INSTANCE
				.SendMessage(handler, WM_LBUTTONDOWN, 0x00000001, lParam);
		User32.INSTANCE.SendMessage(handler, WM_LBUTTONUP, 0x00000000, lParam);
	}

	public void msgBox(String Text) {
		msgBox(Text, "");
	}

	public Color pixelGetColor(int x, int y) {
		POINT point = new POINT(x, y);
		clientToScreen(point);
		Color pixel = r.getPixelColor(point.x, point.y);
		return pixel;
	}

	public File saveScreenShot(Area area, String filePathFirst,
			String... filePathRest) throws IOException {
		return saveScreenShot(area.getX1(), area.getY1(), area.getX2(),
				area.getY2(), filePathFirst, filePathRest);
	}

	public File saveScreenShot(int x1, int y1, int x2, int y2,
			String filePathFirst, String... filePathRest) throws IOException {
		Path path = Paths.get(filePathFirst, filePathRest).toAbsolutePath();
		String fileName = path.getFileName().toString();
		if (!path.getFileName().toString().toLowerCase().endsWith(".png")) {
			fileName = path.getFileName().toString() + ".png";
		}
		BufferedImage img = screenShot(x1, y1, x2, y2);
		File file = new File(path.getParent().toString(), fileName);
		if (!file.getParentFile().isDirectory()) {
			file.getParentFile().mkdirs();
		}
		ImageIO.write(img, "png", file);
		return file;
	}

	public BufferedImage screenShot(Area area) {
		return screenShot(area.getX1(), area.getY1(), area.getX2(),
				area.getY2());
	}

	public BufferedImage screenShot(int x1, int y1, int x2, int y2) {
		POINT point = new POINT(x1, y1);
		clientToScreen(point);
		return r.createScreenCapture(new Rectangle(point.x, point.y, x2 - x1,
				y2 - y1));
	}

	public void setupWin32(HWND handler) {
		this.handler = handler;
	}

	public void sleepRandom(int i) throws InterruptedException {
		Thread.sleep(i + random.nextInt(i));
	}

	public void sleepTillClickableIsActive(Clickable clickable)
			throws InterruptedException {
		while (true) {
			if (isClickableActive(clickable)) {
				return;
			}
			Thread.sleep(random.nextInt(250) + 750);
		}
	}

	public void zoomUp() throws InterruptedException {
		zoomUp(14);
	}

	public void zoomUp(int notch) throws InterruptedException {
		logger.info("Zooming out...");
		int lParam = 0x00000001 | 0x50 /* scancode */<< 16 | 0x01000000 /* extended */;

		WPARAM wparam = new WinDef.WPARAM(VK_DOWN);
		LPARAM lparamDown = new WinDef.LPARAM(lParam);
		LPARAM lparamUp = new WinDef.LPARAM(lParam | 1 << 30 | 1 << 31);

		for (int i = 0; i < notch; i++) {
			while (isCtrlKeyDown()) {
			}
			User32.INSTANCE
					.PostMessage(handler, WM_KEYDOWN, wparam, lparamDown);
			User32.INSTANCE.PostMessage(handler, WM_KEYUP, wparam, lparamUp);
			Thread.sleep(1000);
		}
	}

}
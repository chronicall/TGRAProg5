package tgra.prog5.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tgra.prog5.game.Prog5Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Programming Assignment 5 - Sario"; // or whatever you like
		config.height = 800;
		config.width = 1280;
		config.x = 150;
		config.y = 50;
		new LwjglApplication(new Prog5Game(), config);
	}
}

package tgra.prog5.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tgra.prog5.game.Prog5Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Programming Assignment 5 - This Is Not Mario 64";
		config.height = 720;
		config.width = 1280;
		new LwjglApplication(new Prog5Game(), config);
	}
}

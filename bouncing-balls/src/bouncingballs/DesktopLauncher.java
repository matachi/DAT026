package bouncingballs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Bouncing Balls";
		cfg.useGL20 = false;
		cfg.width = Constants.WIDTH;
		cfg.height = Constants.HEIGHT;
		
		new LwjglApplication(new Simulator(), cfg);
	}
}

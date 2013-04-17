package bouncingballs;

import java.util.Collection;
import java.util.HashSet;

public class Model {
	
	private Collection<Ball> balls;
	
	public Model() {
		balls = new HashSet<Ball>();
		balls.add(new Ball());
		balls.add(new Ball(220, 20));
	}
	
	public void update(float delta) {
		for (Ball ball : balls) {
			ball.getPosition().x += 60 * delta;
		}
	}

	public Collection<Ball> getBalls() {
		return balls;
	}
}

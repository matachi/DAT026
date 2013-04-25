package bouncingballs;

import java.util.Collection;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;

public class Model {
	
	private Collection<Ball> balls;
	
	public Model() {
		balls = new HashSet<Ball>();
		Ball ball = new Ball();
		ball.setVelocity(new Vector2(500, -50));
		balls.add(ball);
		ball = new Ball(250, 20);
		ball.setVelocity(new Vector2(-500, 50));
		balls.add(ball);
	}
	
	public void update(float delta) {
		// Update all balls positions
		for (Ball ball : balls) {
			ball.getPosition().x += ball.getVelocity().x * delta;
			ball.getPosition().y += ball.getVelocity().y * delta;
		}
		// Solve all collisions between balls
		for (Ball ball1 : balls) {
			// Check if the current ball collides with any other balls
			for (Ball ball2 : balls) {
				if (ball2 == ball1) {
					continue;
				}
				if (ball1.collidesWith(ball2)) {
					Vector2 velocity1 = ball1.getVelocity();
					Vector2 velocity2 = ball2.getVelocity();
					Vector2 position1 = ball1.getPosition();
					Vector2 position2 = ball2.getPosition();
					velocity1.x = 0;
					velocity1.y = 0;
					velocity2.x = 0;
					velocity2.y = 0;
				}
			}
		}
		// Make sure no ball is leaving the field
		for (Ball ball : balls) {
			float radius = ball.getRadius();
			// Right border
			if (ball.getPosition().x + radius > Constants.WIDTH / 2) {
				ball.getPosition().x = Constants.WIDTH / 2 - radius;
			}
			// Left border
			if (ball.getPosition().x - radius < -Constants.WIDTH / 2) {
				ball.getPosition().x = -Constants.WIDTH / 2 + radius;
			}
			// Top border
			if (ball.getPosition().y + radius > Constants.HEIGHT / 2) {
				ball.getPosition().y = Constants.HEIGHT / 2 - radius;
			}
			// Bottom border
			if (ball.getPosition().y - radius < -Constants.HEIGHT / 2) {
				ball.getPosition().y = -Constants.HEIGHT / 2 + radius;
			}
			ball.getPosition().y += ball.getVelocity().y * delta;
		}
	}

	public Collection<Ball> getBalls() {
		return balls;
	}
}

package bouncingballs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class Model {
	
	private Collection<Ball> balls;
	
	public Model() {
		balls = new HashSet<Ball>();
		Ball ball = new Ball(0, 0, 100, 70);
		ball.setVelocity(new Vector2(0, 0));
		balls.add(ball);
		ball = new Ball(250, 120, 100, 20);
		ball.setVelocity(new Vector2(-500, 10));
		balls.add(ball);
//		ball = new Ball(-250, 120, 100, 20);
//		ball.setVelocity(new Vector2(-500, 10));
//		balls.add(ball);
	}
	
	public void update(float delta) {
		// Update all balls positions
		for (Ball ball : balls) {
//			ball.getVelocity().y -= Constants.GRAVITY * delta;
			ball.getPosition().x += ball.getVelocity().x * delta;
			ball.getPosition().y += ball.getVelocity().y * delta;
		}
		
		// Keep track of which balls collision have already been checked between
		Map<Ball, Ball> checkedBalls = new HashMap<Ball, Ball>();
		// Solve all collisions between balls
		for (Ball ball1 : balls) {
			// Check if the current ball collides with any other balls
			for (Ball ball2 : balls) {
				if (ball2 == ball1) {
					// Trying to calculate collision with itself
					continue;
				} else if (checkedBalls.get(ball1) == ball2) {
					// Already have checked collision between these two balls
					continue;
				}
				if (ball1.collidesWith(ball2)) {
					// http://www.cse.chalmers.se/edu/year/2010/course/DAT026/CourseMaterial/lecture5.txt
					Vector2 velocity1 = ball1.getVelocity();
					Vector2 velocity2 = ball2.getVelocity();
					Vector2 position1 = ball1.getPosition();
					Vector2 position2 = ball2.getPosition();
					float angle = (float) Math.atan((position1.y - position2.y) / (position1.x - position2.x));
					float weight1 = ball1.getWeight();
					float weight2 = ball2.getWeight();
					float I = velocity1.len() * weight1 + velocity2.len() * weight2;
					float R = velocity2.len() - velocity1.len();
					float newVelocity1 = (I - weight2 * R) / (weight1 + weight2);
					float newVelocity2 = R + newVelocity1;
					
					System.out.println("Angle (collision): " + angle + "\n" +
							"Weight (1): " + weight1 + "\n" +
							"Weight (2): " + weight2 + "\n" +
							"I: " + I + "\n" +
							"R: " + R + "\n" +
							"v1: " + velocity1.len() + "\n" +
							"v2: " + velocity2.len() + "\n" +
							"Angle before (1): " + ball1.getVelocity().angle() + "\n" +
							"Angle before (2): " + ball2.getVelocity().angle() + "\n" +
							"u1: " + newVelocity1 + "\n" +
							"u2: " + newVelocity2
							);
					ball1.getVelocity().x = newVelocity1 * (float) Math.cos(angle);
					ball1.getVelocity().y = newVelocity1 * (float) Math.sin(angle);
					ball2.getVelocity().x = newVelocity2 * (float) Math.cos(angle);
					ball2.getVelocity().y = newVelocity2 * (float) Math.sin(angle);
					System.out.println(
							"Angle after (1): " + ball1.getVelocity().angle() + "\n" +
							"Angle after (2): " + ball2.getVelocity().angle() + "\n"
							);
				}
				checkedBalls.put(ball2, ball1);
			}
		}
		// Make sure no ball is leaving the field
		for (Ball ball : balls) {
			float radius = ball.getRadius();
			// Right border
			if (ball.getPosition().x + radius > Constants.WIDTH / 2) {
				ball.getPosition().x = Constants.WIDTH / 2 - radius;
				ball.getVelocity().x = -ball.getVelocity().x;
			}
			// Left border
			if (ball.getPosition().x - radius < -Constants.WIDTH / 2) {
				ball.getPosition().x = -Constants.WIDTH / 2 + radius;
				ball.getVelocity().x = -ball.getVelocity().x;
			}
			// Top border
			if (ball.getPosition().y + radius > Constants.HEIGHT / 2) {
				ball.getPosition().y = Constants.HEIGHT / 2 - radius;
				ball.getVelocity().y = -ball.getVelocity().y;
			}
			// Bottom border
			if (ball.getPosition().y - radius < -Constants.HEIGHT / 2) {
				ball.getPosition().y = -Constants.HEIGHT / 2 + radius;
				ball.getVelocity().y = -ball.getVelocity().y;
			}
		}
	}

	public Collection<Ball> getBalls() {
		return balls;
	}
}

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
		balls.add(ball);
		ball = new Ball(250, 120, 50, 20);
		ball.setVelocity(new Vector2(-500, 10));
		balls.add(ball);
		ball = new Ball(-250, 120, 100, 70);
		ball.setVelocity(new Vector2(-500, 10));
		balls.add(ball);
	}

	public void update(float delta) {
		// Update all balls positions
		for (Ball ball : balls) {
			ball.getVelocity().y -= Constants.GRAVITY * delta;
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
					/*
					 * 1) calculate the degree of the plane
					 * 2) convert the balls velocity to the new plane
					 * 3) handle collision (only x-velocity)
					 * 4) convert the new velocity back to the normal plane system
					 * 5) update balls position and velocity
					 */

					// 1)
					System.out.println(
							"Angle before (1): " + ball1.getVelocity().angle() + "\n" +
									"Angle before (2): " + ball2.getVelocity().angle()
							);
					Vector2 position1 = ball1.getPosition();
					Vector2 position2 = ball2.getPosition();
					// rotation = how much the new x axis is rotated from the normal x axis
					double rotation = Math.atan((position1.y - position2.y) / (position1.x - position2.x));
					if (position2.x > position1.x) {
						rotation = Math.PI + rotation;
					}
					// 2)
					/* 
					 * alfa = 90 - rotation
					 * beta = -rotation
					 * newX = oldY*cos(alfa)+oldX*cos(beta)
					 * newY = oldY*sin(alfa)+oldX*sin(beta)
					 */

					Vector2 oldVelocity1 = ball1.getVelocity();
					Vector2 oldVelocity2 = ball2.getVelocity();
					Vector2 newVelocity1 = getVectorNewPlane(oldVelocity1, rotation);
					Vector2 newVelocity2 = getVectorNewPlane(oldVelocity2, rotation);

					float weight1 = ball1.getWeight();
					float weight2 = ball2.getWeight();


					// 3) handle collision (only x-velocity)
					handleCollision(newVelocity1, newVelocity2, weight1, weight2);

					// 4) convert the new velocity back to the normal plane system
					Vector2 tmp = getVectorNewPlane(newVelocity1, -rotation);
					oldVelocity1.x = tmp.x;
					oldVelocity1.y = tmp.y;
					tmp = getVectorNewPlane(newVelocity2, -rotation);
					oldVelocity2.x = tmp.x;
					oldVelocity2.y = tmp.y;

					// 5) update balls position
					float collision = (ball1.getRadius() + ball2.getRadius() - ball1.getPosition().dst(ball2.getPosition()) + 0.0001f) / 2;
					ball1.getPosition().add(collision * (float)Math.cos(rotation), collision * (float)Math.sin(rotation));
					ball2.getPosition().add(-collision * (float)Math.cos(rotation), -collision * (float)Math.sin(rotation));
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

	public Vector2 getVectorNewPlane(Vector2 velocity, double rotation) {
		double alfa = Math.PI/2 - rotation;
		double beta = - rotation;
		Vector2 newVelocity = new Vector2();
		newVelocity.x = (float) (velocity.y * Math.cos(alfa) + velocity.x * Math.cos(beta));
		newVelocity.y = (float) (velocity.y * Math.sin(alfa) + velocity.x * Math.sin(beta));
		return newVelocity;
	}


	public void handleCollision(Vector2 v1, Vector2 v2, float weight1, float weight2) {
		/*
		 * v1 = ( (m1 - m2)*u1 + 2*m2*u2 ) / (m1 + m2)
		 * v2 = ( (m2 - m1)*u2 + 2*m1*u1 ) / (m1 + m2)
		 */
		float momentum = v1.x*weight1 + v2.x*weight2;
		float kinetic = v1.x*v1.x*weight1/2 + v2.x*v2.x*weight2/2;
		Vector2 tmp = new Vector2();
		tmp.x = ((weight1 - weight2)*v1.x + 2*weight2*v2.x) / (weight1 + weight2);
		v2.x = ((weight2 - weight1)*v2.x + 2*weight1*v1.x) / (weight1 + weight2);
		v1.x = tmp.x;
		System.out.println("Momentum: " + momentum + " vs " + (v1.x*weight1 + v2.x*weight2));
		System.out.println("Kinetic: " + kinetic + " vs " + (v1.x*v1.x*weight1/2 + v2.x*v2.x*weight2/2));
	}

	public Collection<Ball> getBalls() {
		return balls;
	}
}
package bouncingballs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Ball {
	
	private float weight;
	private float radius;
	private Vector2 position;
	private Vector2 velocity;
	private Color color;
	
	public Ball() {
		this(0, 0);
	}
	public Ball(float x, float y) {
		this(x, y, 100, 1);
	}
	public Ball(float x, float y, float radius, float weight) {
		this(x, y, radius, weight, new Color((float)Math.random() / 1.2f, (float)Math.random() / 1.2f, (float)Math.random() / 1.2f, 0));
	}
	public Ball(float x, float y, float radius, float weight, Color color) {
		position = new Vector2(x, y);
		velocity = new Vector2();
		this.color = color;
		this.radius = Math.abs(radius);
		this.weight = weight;
	}
	
	public float getWeight() {
		return weight;
	}
	public float getRadius() {
		return radius;
	}
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean collidesWith(Ball ball) {
		return position.dst(ball.position) < radius + ball.radius;
	}
}

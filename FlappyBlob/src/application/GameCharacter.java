package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameCharacter {

	private int posX, posY, velocity;
	private final int SIZE = 15;

	public GameCharacter() {
		setPos(100, 300);
		setVelocity(0);
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}

	public void addPos(int x, int y) {
		posX = x;
		posY = y;
	}

	public void updatePos() {
		posY += velocity;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int yVelocity) {
		velocity = yVelocity;
	}

	public void addVel(int yVelocity) {
		velocity += yVelocity;
	}

	public int getSize() {
		return SIZE;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.ORANGERED);
		gc.fillRect(posX, posY, SIZE, SIZE);
	}

	/**
	 * @return "Collisionbox" als ein Rectangle
	 */
	public Rectangle getCollision() {
		return new Rectangle(posX, posY, SIZE, SIZE);
	}
}

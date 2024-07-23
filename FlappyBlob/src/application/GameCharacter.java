package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameCharacter {

	private int posX, posY;
	private final int SIZE = 20;
	public static int velocity = 3;

	/**
	 * Konstruktor
	 */
	public GameCharacter() {
		setPos(100, 300);
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
		posX += x;
		posY += y;
	}

	public void updatePos() {
		posY += velocity;
	}

	public double getVelocity() {
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

	/**
	 * Render Methode welche den Charakter auf Canvas zeichnet
	 * 
	 * @param gc 2D Grafikkontext des Spiel-Canvas wird übergeben
	 */
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

package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class GameCharacter {

	private int posX, posY;
	private double velocity;
	private final int SIZE = 20;
	private final Image CHARACTER_IMG;
	public static final double INITIAL_VELOCITY = 3;

	/**
	 * Konstruktor
	 */
	public GameCharacter() {
		posX = 100;
		posY = 300;
		velocity = INITIAL_VELOCITY;
		CHARACTER_IMG = new Image(getClass().getResource("character.png").toExternalForm());
	}

	public void updatePos(double gravity) {
		velocity += gravity;
		posY += velocity;
	}

	public int getPosY() {
		return posY;
	}
	
	public void setVelocity(double velocity) {
		this.velocity = velocity;
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
		gc.drawImage(CHARACTER_IMG, posX, posY, SIZE, SIZE);
	}

	/**
	 * @return "Collisionbox" als ein Rectangle
	 */
	public Rectangle getCollision() {
		return new Rectangle(posX, posY, SIZE, SIZE);
	}
}

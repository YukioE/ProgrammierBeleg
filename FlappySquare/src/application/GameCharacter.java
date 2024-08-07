package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Klasse zur Verwaltung des GameCharacter Objekts
 * 
 * @author Timo Hoffmann
 * @version 1.0
 */
public class GameCharacter {

	/**
	 * globale ursprüngliche velocity
	 */
	public static final double INITIAL_VELOCITY = 3;

	/**
	 * aktuelle velocity
	 */
	private double velocity;

	/**
	 * Koordinaten
	 */
	private int posX, posY;

	/**
	 * Größe
	 */
	private final int size = 20;

	/**
	 * Bilder des GameCharacters
	 */
	private final Image characterImg, characterImg2;
	
	/**
	 * Konstruktor
	 */
	public GameCharacter() {
		posX = 100;
		posY = SquareGame.WINDOW_HEIGHT/2;
		velocity = INITIAL_VELOCITY;
		characterImg = new Image(getClass().getResource("character.png").toString());
		characterImg2 = new Image(getClass().getResource("character2.png").toString());		
	}

	/**
	 * aktualisiert die Position des Charakters basierend
	 * auf der velocity und der Schwerkraft
	 * 
	 * @param gravity
	 */
	public void updatePosition(double gravity) {
		velocity += gravity;
		posY += velocity;
	}

	/**
	 * @return y Koordinate
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * setzt aktuelle velocity
	 * 
	 * @param velocity
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * Render Methode welche den Charakter auf Canvas zeichnet
	 * falls der Charakter fliegt/springt werden Flügel auf seinem Rücken gerendert
	 * 
	 * @param gc 2D Grafikkontext des Spiel-Canvas wird übergeben
	 */
	public void render(GraphicsContext gc) {
		if (velocity > 0) {
			gc.drawImage(characterImg, posX, posY, size, size);
		} else {
			gc.drawImage(characterImg2, posX - characterImg2.getWidth() + size,
					posY, characterImg2.getWidth(), characterImg2.getHeight());
		}
	}

	/**
	 * @return "Collisionbox" als ein Rectangle
	 */
	public Rectangle getCollision() {
		return new Rectangle(posX, posY, size, size);
	}
}

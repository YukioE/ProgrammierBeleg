package application;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BlobGame {

	public static final double GRAVITY = 0.1;
	public static final int WINDOW_WIDTH = 700;
	public static final int WINDOW_HEIGHT = 700;
	public static final Image TOP_PIPE_IMG = new Image(BlobGame.class.getResource("pipe.png").toExternalForm());
	public static final Image BOTTOM_PIPE_IMG = new Image(BlobGame.class.getResource("pipe2.png").toExternalForm());
	private Score score, highscore;
	private GameCharacter character;
	private ArrayList<Obstacle> obstacles;

	/**
	 * Konstruktor welcher gleichzeitig als Spiel Initialisierung dient
	 */
	public BlobGame() {
		character = new GameCharacter();
		obstacles = new ArrayList<Obstacle>();
		score = new Score(false);
		highscore = new Score(true);
		obstacles.add(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT));
	}

	/**
	 * update Methode welche Position aller Objekte aktualisiert, Kollisionen prüft
	 * und neue Hindernisse hinzufügt
	 */
	public void update() {
		// Blob Position mit Schwerkraft updaten
		character.updatePos(GRAVITY);
		
		checkScoreCollision();

		// Hindernisse updaten + entfernen sobald sie nicht mehr zu sehen sind
		for (Obstacle obstacle : obstacles) {
			obstacle.updatePosition();

			// Hindernis wird entfernt sobald seine letzten Pixel nicht mehr zu sehen sind
			if (obstacle.getPosition() + obstacle.getWidth() <= 0) {
				obstacles.removeFirst();
				break;
			}
		}

		// neues Hindernis erzeugen
		// falls weniger als 4 Hindernisse auf dem Bildschirm sind und das neue
		// Hindernis mit genügend Abstand platziert wird
		if (obstacles.size() < 4 && obstacles.getLast().getPosition() + 300 <= WINDOW_WIDTH) {
			obstacles.addLast(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT, obstacles.getLast()));
		}
	}

	/**
	 * Render-Methode welche die verschiedenen Spiel-Objekte auf den Canvas zeichnet
	 * 
	 * @param gc 2D Grafikkontext des Spiel-Canvas wird übergeben
	 */
	public void render(GraphicsContext gc) {

		// Hintergrund rendern
		gc.setFill(Color.LIGHTBLUE);
		gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		// Charakter rendern
		character.render(gc);

		// Hindernisse rendern
		for (Obstacle obstacle : obstacles) {
			obstacle.render(gc);
		}

		// Scores rendern
		score.render(25, 45, 45, gc);
		highscore.render(25, 70, 25, gc);
	}

	/**
	 * Sprung-Methode welche den Charakter "Springen" lässt,
	 * falls er nicht über den oberen Fensterrand befinden wird.
	 */
	public void jump() {
		if ((character.getPosY() - 10) > 0) {
			character.setVelocity(-GRAVITY * 35);
		}
	}

	/**
	 * Methode welche auf Kollision von Charakter mit Hindernissen sowie dem Boden
	 * prüft
	 * 
	 * @return true falls Kollision, false falls nicht
	 */
	public boolean checkCollision() {
		// Kollisionsbox des Charakters
		Rectangle characterCollision = character.getCollision();

		// Kollision mit Boden
		if (characterCollision.getY() >= WINDOW_HEIGHT) {
			return true;
		}

		// Kollision mit Hindernissen
		for (Obstacle obstacle : obstacles) {
			Rectangle[] obstacleCollisions = obstacle.getCollision();

			if (intersects(characterCollision, obstacleCollisions[0])
					|| intersects(characterCollision, obstacleCollisions[1])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Methode welche überprüft ob der Charakter mit einer Scorebox kollidiert und
	 * den Score counter in diesem Fall um 1 erhöht
	 */
	public void checkScoreCollision() {
		Rectangle characterCollision = character.getCollision();

		for (Obstacle obstacle : obstacles) {
			Rectangle scoreBox = obstacle.getScoreBox();

			// falls Kollision -> Hindernis wird als scored markiert & Score wird um 1 erhöht
			if (intersects(characterCollision, scoreBox) && !obstacle.isScored()) {
				obstacle.setScored(true);
				score.incrementScore();
				break;
			}
		}
	}
	
	public Score getScore() {
		return score;
	}
	
	public Score getHighScore() {
		return highscore;
	}

	/**
	 * Methode um zu überprüfen ob 2 Rechtecke sich schneiden
	 * 
	 * @param r1 - Rechteck 1
	 * @param r2 - Rechteck 2
	 * @return true falls Rechtecke sich schneiden, false falls nicht
	 */
	public boolean intersects(Rectangle r1, Rectangle r2) {
		return r1.getX() <= r2.getX() + r2.getWidth() && r1.getX() + r1.getWidth() >= r2.getX()
				&& r1.getY() <= r2.getY() + r2.getHeight() && r1.getY() + r1.getHeight() >= r2.getY();
	}

}

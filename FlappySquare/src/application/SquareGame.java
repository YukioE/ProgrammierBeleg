//package application;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class SquareGame {

	public static final double GRAVITY = 0.2;
	public static final int WINDOW_WIDTH = 700;
	public static final int WINDOW_HEIGHT = 700;
	public static final Image PIPE_HEAD_IMG = new Image(
			SquareGame.class.getResource("pipe_head.png").toExternalForm());
	public static final Image PIPE_BODY_IMG = new Image(SquareGame.class.getResource("pipe_body.png").toExternalForm());

	private double backgroundPos1, backgroundPos2;
	private final double scrollSpeed = 0.3;
	private final Image backgroundImg, sunImg;
	private final Score score, highscore;
	private final GameCharacter character;
	private final ArrayList<Obstacle> obstacles;

	/**
	 * Konstruktor welcher gleichzeitig als Spiel Initialisierung dient
	 */
	public SquareGame() {
		character = new GameCharacter();
		obstacles = new ArrayList<Obstacle>();
		score = new Score(false);
		highscore = new Score(true);
		backgroundImg = new Image(getClass().getResource("background.png").toExternalForm());
		sunImg = new Image(getClass().getResource("sun.png").toExternalForm());
		backgroundPos1 = 0;
		backgroundPos2 = backgroundImg.getWidth();
		obstacles.add(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT));
	}

	/**
	 * update Methode welche Position aller Objekte aktualisiert, Kollisionen prüft
	 * und neue Hindernisse hinzufügt
	 */
	public void update() {
		// GameCharacter Position mit Schwerkraft updaten
		character.updatePos(GRAVITY);

		checkScoreCollision();

		// Positionen des Hintergrundbild updaten
		backgroundPos1 -= scrollSpeed;
		backgroundPos2 -= scrollSpeed;

		// Hintergrundbilder wieder nach Rechts verschieben falls sie nicht mehr zu
		// sehen sind
		if (backgroundPos1 <= -backgroundImg.getWidth()) {
			backgroundPos1 = backgroundImg.getWidth();
		}
		if (backgroundPos2 <= -backgroundImg.getWidth()) {
			backgroundPos2 = backgroundImg.getWidth();
		}

		// Hindernisse updaten + entfernen sobald sie nicht mehr zu sehen sind
		for (Obstacle obstacle : obstacles) {
			obstacle.updatePosition();

			// Hindernis wird entfernt sobald seine letzten Pixel nicht mehr zu sehen sind
			if (obstacle.getPosition() + obstacle.getWidth() <= 0) {
				obstacles.remove(0);
				break;
			}
		}

		// neues Hindernis erzeugen
		// falls weniger als 4 Hindernisse auf dem Bildschirm sind und das neue
		// Hindernis mit genügend Abstand platziert wird
		if (obstacles.size() < 4 && obstacles.get(obstacles.size() - 1).getPosition() + 300 <= WINDOW_WIDTH) {
			obstacles.add(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT, obstacles.get(obstacles.size() - 1)));
		}
	}

	/**
	 * Render-Methode welche die verschiedenen Spiel-Objekte auf den Canvas zeichnet
	 * 
	 * @param gc 2D Grafikkontext des Spiel-Canvas wird übergeben
	 */
	public void render(GraphicsContext gc) {

		// Hintergrund rendern
		gc.drawImage(backgroundImg, backgroundPos1, 0);
		gc.drawImage(backgroundImg, backgroundPos2, 0);
		gc.drawImage(sunImg, 0, 0);

		// Charakter rendern
		character.render(gc);

		// Hindernisse rendern
		for (Obstacle obstacle : obstacles) {
			obstacle.render(gc);
		}

		// Scores rendern
		score.render(10, 45, 45, gc);
		highscore.render(10, 70, 25, gc);
	}

	/**
	 * Sprung-Methode welche den Charakter springen/fliegen lässt,
	 * falls er sich nicht über dem oberen Fensterrand befinden wird.
	 */
	public void jump() {
		if ((character.getPosY() - 10) > 0) {
			character.setVelocity(-GRAVITY * GRAVITY * 125);
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

			// falls Kollision -> Hindernis wird als scored markiert & Score wird um 1
			// erhöht
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

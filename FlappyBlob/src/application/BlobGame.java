package application;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class BlobGame {

	private GameCharacter character;
	private ArrayList<Obstacle> obstacles;
	public static final double GRAVITY = 0.1;
	private Score score, highscore;
	public static final int WINDOW_WIDTH = 700;
	public static final int WINDOW_HEIGHT = 700;

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
		// Blob Position updaten
		character.updatePos(GRAVITY);
		checkScoreCollision();

		// Hindernisse updaten + entfernen sobald sie nicht mehr zu sehen sind
		for (Obstacle obstacle : obstacles) {
			obstacle.updatePosition();

			if (obstacle.getPosition() + obstacle.getWidth() <= 0) {
				obstacles.removeFirst();
				break;
			}
		}

		// neues Hindernis erzeugen
		// falls weniger als 4 Hindernisse auf dem Bildschirm sind und das neue
		// Hindernis mit genügend Abstand platziert wird
		if (obstacles.size() < 4 && obstacles.getLast().getPosition() + 300 <= WINDOW_WIDTH) {
			Obstacle lastObstacle = obstacles.getLast();
			obstacles.addLast(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT, lastObstacle));
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

		// Score rendern
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setStroke(Color.BLACK);
		
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText(String.valueOf(score.getCounter()), 25, 45);
		gc.strokeText(String.valueOf(score.getCounter()), 25, 45);
		
		// Highscore rendern
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 25));
		gc.fillText(String.valueOf(highscore.getCounter()), 25, 70);
		gc.strokeText(String.valueOf(highscore.getCounter()), 25, 70);
	}

	/**
	 * Sprung-Methode welche den Charakter "springen" lässt
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

			if (intersects(characterCollision, scoreBox) && !obstacle.isScored()) {
				obstacle.setScored(true);
				score.incScore();
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

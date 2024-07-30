package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 * Klasse zur Verwaltung der Hindernis Objekte
 * 
 * @author Timo Hoffmann
 * @version 1.0
 */
public class Obstacle {

	private boolean scored;

	/**
	 * linke x Koordinate des Hindernis
	 */
	private double position;
	
	/**
	 * untere y Koordinate der Lücke im Hindernis
	 */
	private final double gapPos;

	/**
	 * Geschwindigkeit der Hindernisse/des Spiels
	 */
	private final double velocity = 2.5;

	/**
	 * Größe des Lücke im Hindernis
	 */
	private final int gapSize = 150;

	/**
	 * Breite des Hindernis
	 */
	private final double width = 100;

	/**
	 * Höhe des Hindernis
	 */
	private final double height;

	/**
	 * Konstruktor für das erste Hindernis
	 * 
	 * @param windowWidth
	 * @param windowHeight
	 */
	public Obstacle(int windowWidth, int windowHeight) {
		position = windowWidth;
		height = windowHeight;
		scored = false;

		// Position der Lücke wird zufällig bestimmt
		gapPos = (int) (Math.random() * (windowHeight - gapSize + 2) + gapSize + 1);
	}

	/**
	 * Konstruktor um ein neues Hindernis zu erstellen
	 * 
	 * @param windowWidth  - die Fensterbreite wird für Berechnungen übergeben
	 * @param windowHeight - die Fensterhöhe wird für Berechnungen übergeben
	 * @param prevObstacle - das vorherige Hindernis wird für die Lücken Berechnung
	 *                     übergeben
	 */
	public Obstacle(int windowWidth, int windowHeight, Obstacle prevObstacle) {
		position = windowWidth;
		height = windowHeight;
		scored = false;

		// Anzahl an Updates welche zwischen 2 Hindernissen durchgeführt werden
		// (position des Hindernis - Position des vorherigen HinderGAP_POSelocity
		int tickAmount = (int) ((position - prevObstacle.getPosition()) / velocity);

		// max Falldistanz berechnen
		// GameCharacter static Velocity * Anzahl Updates + Schwerkraft * Anzahl Updates
		int maxFallDistance = (int) (GameCharacter.INITIAL_VELOCITY * tickAmount + SquareGame.GRAVITY * tickAmount);

		// Position der Lücke wird zufällig bestimmt mit Vorraussetzungen
		// sodass der Charakter diese erreichen kann
		double minGapPos = Math.max(gapSize + 1, prevObstacle.getGapPos() - maxFallDistance / 1.5);
		double maxGapPos = Math.min(windowHeight - 1, prevObstacle.getGapPos() + maxFallDistance);
		gapPos = (int) (Math.random() * (maxGapPos - minGapPos + 1) + minGapPos);
	}

	/**
	 * @return linke x Koordinate des Hindernis
	 */
	public double getPosition() {
		return position;
	}

	/**
	 * @return Breite des Hindernis
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return untere y Koordinate der Lücke im Hindernis
	 */
	public double getGapPos() {
		return gapPos;
	}

	/**
	 * updated die Position des Hindernis basierend auf der velocity
	 */
	public void updatePosition() {
		position -= velocity;
	}

	/**
	 * @return ob das Hindernis schon gescored wurde
	 */
	public boolean isScored() {
		return scored;
	}

	/**
	 * setzen des scored booleans,
	 * damit ein Hindernis nicht mehrmals den Score erhöhen kann
	 * 
	 * @param scored
	 */
	public void setScored(boolean scored) {
		this.scored = scored;
	}

	/**
	 * Render Methode welche alle Hindernisse basierend auf der Position der Lücke
	 * auf Canvas zeichnet, Kopf und Körper des Hindernis werden seperat gezeichnet
	 * 
	 * @param gc 2D Grafikkontext des Spiel-Canvas wird übergeben
	 */
	public void render(GraphicsContext gc) {
		gc.drawImage(SquareGame.PIPE_HEAD_IMG, position, gapPos - gapSize - SquareGame.PIPE_HEAD_IMG.getHeight() / 2,
				width, SquareGame.PIPE_HEAD_IMG.getHeight() / 2);
		gc.drawImage(SquareGame.PIPE_BODY_IMG, position, 0, width,
				gapPos - gapSize - SquareGame.PIPE_HEAD_IMG.getHeight() / 2);
		gc.drawImage(SquareGame.PIPE_HEAD_IMG, position, gapPos, width, SquareGame.PIPE_HEAD_IMG.getHeight() / 2);
		gc.drawImage(SquareGame.PIPE_BODY_IMG, position, gapPos + SquareGame.PIPE_HEAD_IMG.getHeight() / 2, width,
				height);

		// Debug um ScoreBoxen/Trigger zu rendern
		// gc.setFill(Color.BLACK);
		// gc.fillRect(position + WIDTH + 20, gapPos - GAP_SIZE, 10, GAP_SIZE);
	}

	/**
	 * Kollisions Methode welche basierend auf der Position der Lücke zwei
	 * Kollisionsboxen aus Rechtecken erstellt
	 * 
	 * @return Kollisionsbox Array aus 2 Rechtecken
	 */
	public Rectangle[] getCollision() {
		Rectangle[] collisionBoxes = new Rectangle[2];

		collisionBoxes[0] = new Rectangle(position, gapPos, width, height - gapPos);
		collisionBoxes[1] = new Rectangle(position, 0, width, gapPos - gapSize);

		return collisionBoxes;
	}

	/**
	 * @return ScoreBox als Rechteck
	 */
	public Rectangle getScoreBox() {
		return new Rectangle(position + width + 20, gapPos - gapSize, 1, gapSize);
	}
}

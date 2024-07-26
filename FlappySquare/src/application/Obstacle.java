//package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Obstacle {

	// position ist linke xPos des Hindernis
	// velocity die Geschwindigkeit
	// gapPos die untere yPos der Lücke
	private double position;
	private boolean scored;
	private final int gapSize = 150;
	private final double gapPos;
	private final double velocity = 2.5;
	private final double width = 100;
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
		// GameCharacter static Velocity (3) + Schwerkraft * Anzahl Updates * Anzahl
		// Updates
		int maxFallDistance = (int) ((GameCharacter.INITIAL_VELOCITY + SquareGame.GRAVITY * tickAmount) * tickAmount);

		// Position der Lücke wird zufällig bestimmt mit Vorraussetzungen
		// sodass der Charakter diese erreichen kann
		double minGapPos = Math.max(gapSize + 1, prevObstacle.getGapPos() + gapSize - maxFallDistance + 1);
		double maxGapPos = Math.min(windowHeight - gapSize - 1, prevObstacle.getGapPos() + maxFallDistance - 1);
		gapPos = (int) (Math.random() * (maxGapPos - minGapPos + 1) + minGapPos);
	}

	public double getPosition() {
		return position;
	}

	public double getWidth() {
		return width;
	}

	public double getGapPos() {
		return gapPos;
	}

	public void updatePosition() {
		position -= velocity;
	}

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
		// gc.setFill(Color.GOLD);
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

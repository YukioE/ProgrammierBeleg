package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {

	// position ist linke xPos des Hindernis
	// velocity die Geschwindigkeit
	// gapPos die untere yPos der Lücke
	private double position;
	private int gapPos;
	private boolean scored;
	private double velocity = -1.5;
	private final int GAP_SIZE = 150;
	private final int WIDTH = 100;
	private final int HEIGHT;

	public Obstacle(int windowWidth, int windowHeight) {
		position = windowWidth;
		HEIGHT = windowHeight;
		scored = false;
		
		// Position der Lücke wird zufällig bestimmt
		gapPos = (int) (Math.random() * (windowHeight - GAP_SIZE + 2) + GAP_SIZE + 1);
	}
	
	/**
	 * Konstruktor um ein neues Hindernis zu erstellen
	 * 
	 * @param windowWidth - die Fensterbreite wird für Berechnungen übergeben
	 * @param windowHeight - die Fensterhöhe wird für Berechnungen übergeben
	 * @param prevObstacle - das vorherige Hindernis wird für die Lücken Berechnung übergeben
	 */
	public Obstacle(int windowWidth, int windowHeight, Obstacle prevObstacle) {
		position = windowWidth;
		HEIGHT = windowHeight;
		scored = false;
		
		// Anzahl an Updates welche zwischen 2 Hindernissen durchgeführt werden
		// (position des Hindernis - Position des vorherigen Hindernis) / velocity
		int tickAmount = (int) ((position - prevObstacle.getPosition()) / -velocity);
		
		// max Falldistanz berechnen
		// GameCharacter static Velocity (3) + Schwerkraft * Anzahl Updates * Anzahl Updates
        int maxFallDistance = (int) ((3 + BlobGame.GRAVITY * tickAmount) * tickAmount);
               
        // Position der Lücke wird zufällig bestimmt mit Vorraussetzungen
        // sodass der Charakter diese erreichen kann
        int minGapPos = Math.max(GAP_SIZE + 1, prevObstacle.getGapPos() + GAP_SIZE - maxFallDistance + 1);
        int maxGapPos = Math.min(windowHeight - GAP_SIZE - 1, prevObstacle.getGapPos() + maxFallDistance - 1);
        gapPos = (int) (Math.random() * (maxGapPos - minGapPos + 1) + minGapPos);
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public void addVelocity(int velocity) {
		this.velocity += velocity;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getGapPos() {
		return gapPos;
	}

	public void setGapPos(int gapPos) {
		this.gapPos = gapPos;
	}

	public int getGAP_SIZE() {
		return GAP_SIZE;
	}

	public void updatePosition() {
		position += velocity;
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
	 * auf Canvas zeichnet
	 * 
	 * @param gc 2D Grafikkontext des Spiel-Canvas wird übergeben
	 */
	public void render(GraphicsContext gc) {
		gc.setFill(Color.DARKGREEN);

		gc.drawImage(BlobGame.BOTTOM_PIPE_IMG, position, gapPos, WIDTH, HEIGHT - gapPos);
		gc.drawImage(BlobGame.TOP_PIPE_IMG, position, 0, WIDTH, gapPos - GAP_SIZE);

//		 Debug um ScoreBoxen/Trigger zu rendern
//		gc.setFill(Color.GOLD);
//		gc.fillRect(position + WIDTH + 20, gapPos - GAP_SIZE, 1, GAP_SIZE);

	}

	/**
	 * Kollisions Methode welche basierend auf der Position der Lücke zwei
	 * Kollisionsboxen aus Rechtecken erstellt
	 * 
	 * @return Kollisionsbox Array aus 2 Rechtecken
	 */
	public Rectangle[] getCollision() {
		Rectangle[] collisionBoxes = new Rectangle[2];

		collisionBoxes[0] = new Rectangle(position, gapPos, WIDTH, HEIGHT - gapPos);
		collisionBoxes[1] = new Rectangle(position, 0, WIDTH, gapPos - GAP_SIZE);

		return collisionBoxes;
	}

	/**
	 * @return ScoreBox als Rechteck
	 */
	public Rectangle getScoreBox() {
		return new Rectangle(position + WIDTH + 20, gapPos - GAP_SIZE, 1, GAP_SIZE);
	}
}

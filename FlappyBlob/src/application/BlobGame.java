package application;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class BlobGame {

	private GameCharacter character;
	private ArrayList<Obstacle> obstacles;
	private final int WINDOW_WIDTH = 700;
	private final int WINDOW_HEIGHT = 700;

	public int getWINDOW_WIDTH() {
		return WINDOW_WIDTH;
	}

	public int getWINDOW_HEIGHT() {
		return WINDOW_HEIGHT;
	}

	public BlobGame() {
		this.character = new GameCharacter();
		this.obstacles = new ArrayList<Obstacle>();
	}

	public void startGame() {
		obstacles.add(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT));
	}

	public void update() {
		// Blob Position updaten
		character.updatePos();

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
			obstacles.addLast(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT));
		}
	}

	public void render(GraphicsContext gc) {
		gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		character.render(gc);

		for (Obstacle obstacle : obstacles) {
			obstacle.render(gc);
		}
	}

	public boolean checkCollision() {
		Rectangle characterCollision = character.getCollision();

		// Check for collision between character and obstacles
		for (Obstacle obstacle : obstacles) {
			Rectangle[] obstacleCollisions = obstacle.getCollision();

			if (intersects(characterCollision, obstacleCollisions[0])
					|| intersects(characterCollision, obstacleCollisions[1])) {
				return true;
			}
		}
		return false;
	}

	public boolean intersects(Rectangle r1, Rectangle r2) {
		return r1.getX() <= r2.getX() + r2.getWidth() && r1.getX() + r1.getWidth() >= r2.getX()
				&& r1.getY() <= r2.getY() + r2.getHeight() && r1.getY() + r1.getHeight() >= r2.getY();
	}

}

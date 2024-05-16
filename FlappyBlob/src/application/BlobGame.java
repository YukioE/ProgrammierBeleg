package application;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BlobGame {

	private GameCharacter character;
	private ArrayList<Obstacle> obstacles;
	private int jumped, score;
	private final int WINDOW_WIDTH = 700;
	private final int WINDOW_HEIGHT = 700;

	public int getWINDOW_WIDTH() {
		return WINDOW_WIDTH;
	}

	public int getWINDOW_HEIGHT() {
		return WINDOW_HEIGHT;
	}

	public BlobGame() {
		character = new GameCharacter();
		obstacles = new ArrayList<Obstacle>();
		jumped = 0;
		score = 0;
		obstacles.add(new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT));
	}

	public void update() {
		// Blob Position updaten
		character.updatePos();
		checkScoreCollision();

		if (jumped > 0) {
			jumped--;
			character.addVel(1);
		} else {
			character.setVelocity(3);
		}

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
		gc.setFill(Color.LIGHTBLUE);
		gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		character.render(gc);

		for (Obstacle obstacle : obstacles) {
			obstacle.render(gc);
		}

		gc.setFill(Color.WHITESMOKE);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText(String.valueOf(score), 15, 45);
	}

	public void jump() {
		if ((character.getPosY() - 10) > 0) {
			jumped = 10;
			character.setVelocity(-jumped);
		}
	}

	public boolean checkCollision() {
		Rectangle characterCollision = character.getCollision();

		if (characterCollision.getY() >= WINDOW_HEIGHT) {
			return true;
		}

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

	public void checkScoreCollision() {
		Rectangle characterCollision = character.getCollision();

		for (Obstacle obstacle : obstacles) {
			Rectangle scoreBox = obstacle.getScoreBox();

			if (intersects(characterCollision, scoreBox) && !obstacle.isScored()) {
				obstacle.setScored(true);
				score++;
			}
		}

	}

	public boolean intersects(Rectangle r1, Rectangle r2) {
		return r1.getX() <= r2.getX() + r2.getWidth() && r1.getX() + r1.getWidth() >= r2.getX()
				&& r1.getY() <= r2.getY() + r2.getHeight() && r1.getY() + r1.getHeight() >= r2.getY();
	}

}

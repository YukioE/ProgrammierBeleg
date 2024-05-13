package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {

	private int position, velocity, gapPos;
	private final int GAP_SIZE = 100;
	private final int WIDTH = 100;
	private final int windowHeight;

	public Obstacle(int windowHeight, int windowWidth) {
		position = windowWidth;
		velocity = -15;
		gapPos = (int) (Math.random() * (windowHeight + 1));
		this.windowHeight = windowHeight;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getVelocity() {
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

	public void render(GraphicsContext gc) {
		gc.setFill(Color.DARKGREEN);

		if (gapPos == windowHeight) {
			// 1. Fall: Lücke ist am oberen Ende des Fensters -> es wird nur ein Hindernis
			// benötigt
			gc.fillRect(position, windowHeight - GAP_SIZE, WIDTH, windowHeight - GAP_SIZE);
		} else if (gapPos == GAP_SIZE) {
			// 2. Fall: Lücke ist am unteren Ende des Fensters -> es wird nur ein Hindernis
			// benötigt
			gc.fillRect(position, windowHeight, WIDTH, windowHeight - GAP_SIZE);
		} else {
			// 3. Fall: Lücke ist an keinem Ende des Fensters -> es werden zwei Hindernisse
			// benötigt
			gc.fillRect(position, windowHeight, WIDTH, windowHeight - gapPos);
			gc.fillRect(position, gapPos - GAP_SIZE, WIDTH, gapPos - GAP_SIZE);
		}

	}

	public Rectangle[] getCollision() {
		Rectangle[] collisionBoxes = new Rectangle[2];

		if (gapPos == windowHeight) {
			// 1. Fall: Lücke ist am unteren Ende des Fensters -> es wird ein Hindernis
			// benötigt
			collisionBoxes[0] = new Rectangle(position, windowHeight - GAP_SIZE, WIDTH, windowHeight - GAP_SIZE);

		} else if (gapPos == GAP_SIZE) {
			// 2. Fall: Lücke ist am oberen Ende des Fensters -> es wird ein Hindernis
			// benötigt
			collisionBoxes[0] = new Rectangle(position, windowHeight, WIDTH, windowHeight - GAP_SIZE);

		} else {
			// 3. Fall: Lücke ist an keinem Ende des Fensters -> es werden zwei Hindernisse
			// benötigt
			collisionBoxes[0] = new Rectangle(position, windowHeight, WIDTH, windowHeight - gapPos);
			collisionBoxes[1] = new Rectangle(position, gapPos - GAP_SIZE, WIDTH, gapPos - GAP_SIZE);

		}

		return collisionBoxes;
	}
}

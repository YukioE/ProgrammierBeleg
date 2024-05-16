package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {

	private int position, velocity, gapPos;
	private final int GAP_SIZE = 150;
	private final int WIDTH = 100;
	private final int HEIGHT;

	public Obstacle(int windowWidth, int windowHeight) {
		position = windowWidth;
		HEIGHT = windowHeight;
		velocity = -3;
		gapPos = (int) (Math.random() * (windowHeight - GAP_SIZE + 1) + GAP_SIZE);
		System.out.println(gapPos);
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

		if (gapPos == HEIGHT) {
			// 1. Fall: Lücke ist am oberen Ende des Fensters -> es wird nur ein Hindernis
			// benötigt
			gc.fillRect(position, gapPos, WIDTH, HEIGHT - GAP_SIZE);
		} else if (gapPos == GAP_SIZE) {
			// 2. Fall: Lücke ist am unteren Ende des Fensters -> es wird nur ein Hindernis
			// benötigt
			gc.fillRect(position, 0, WIDTH, HEIGHT - GAP_SIZE);
		} else {
			// 3. Fall: Lücke ist an keinem Ende des Fensters -> es werden zwei Hindernisse
			// benötigt
			gc.fillRect(position, gapPos, WIDTH, HEIGHT - gapPos);
			gc.fillRect(position, 0, WIDTH, gapPos - GAP_SIZE);
		}

	}

	public Rectangle[] getCollision() {
		Rectangle[] collisionBoxes = new Rectangle[2];

		if (gapPos == HEIGHT) {
			// 1. Fall: Lücke ist am unteren Ende des Fensters -> es wird ein Hindernis
			// benötigt
			collisionBoxes[0] = new Rectangle(position, 0, WIDTH, HEIGHT - GAP_SIZE);

		} else if (gapPos == GAP_SIZE) {
			// 2. Fall: Lücke ist am oberen Ende des Fensters -> es wird ein Hindernis
			// benötigt
			collisionBoxes[0] = new Rectangle(position, gapPos, WIDTH, HEIGHT - GAP_SIZE);

		} else {
			// 3. Fall: Lücke ist an keinem Ende des Fensters -> es werden zwei Hindernisse
			// benötigt
			collisionBoxes[0] = new Rectangle(position, gapPos, WIDTH, HEIGHT - gapPos);
			collisionBoxes[1] = new Rectangle(position, 0, WIDTH, gapPos - GAP_SIZE);

		}

		return collisionBoxes;
	}
}

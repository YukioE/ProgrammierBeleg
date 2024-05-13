package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameCharacter {

	private int posX, posY, velX, velY, size;
	private boolean alive;
	
	public GameCharacter() {
		setPos(300, 300);
		setVel(0, 0);
		setSize(15);
		setAlive(true);
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}

	public void addPos(int x, int y) {
		posX = x;
		posY = y;
	}
	
	public void updatePos() {
		posX += velX;
		posY += velY;
	}

	public int getVelX() {
		return velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVel(int x, int y) {
		velX = x;
		velY = y;
	}

	public void addVel(int x, int y) {
		velX += x;
		velY += y;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.DARKGREY);
		gc.fillRect(posX, posY, size, size);
	}
	
	/**
	 * @return "Collisionbox" als ein Rectangle
	 */
	public Rectangle getCollision() {
		return new Rectangle(posX, posY, size, size);
	}
}

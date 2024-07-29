package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

	private final int width = SquareGame.WINDOW_WIDTH;
	private final int height = SquareGame.WINDOW_HEIGHT;
	private SquareGame game;
	private GraphicsContext gc;
	private Canvas canvas;
	private AnimationTimer timer;
	private GameState gameState;

	/**
	 * Helfer Methode um Text zu rendern, verringert die benötigten Zeilen an
	 * Quellcode
	 * 
	 * @param gc
	 * @param text
	 * @param posX
	 * @param posY
	 */
	private void drawText(GraphicsContext gc, String text, double posX, double posY) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.WHITE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText(text, posX, posY);
		gc.strokeText(text, posX, posY);
	}

	/**
	 * Helfer Methode um das Bild zu verdunkeln, wird in Titel-, Pause- und Game
	 * Over Screen benutzt
	 * 
	 * @param gc
	 */
	private void coverBackground(GraphicsContext gc) {
		gc.setFill(Color.rgb(0, 0, 0, 0.5));
		gc.fillRect(0, 0, width, height);
	}

	/**
	 * rendert den Titelscreen und erstellt ein neues Spiel
	 * 
	 * @param gc
	 */
	private void renderTitleScreen(GraphicsContext gc) {
		game = new SquareGame();
		game.render(gc);
		coverBackground(gc);
		drawText(gc, "FLAPPYSQUARE", width / 2, height / 2 - 100);
		drawText(gc, "press SPACE to play", width / 2, height / 2 + 70);
	}

	/**
	 * rendert den Game Over Screen
	 * 
	 * @param gc
	 */
	private void renderGameOverScreen(GraphicsContext gc) {
		coverBackground(gc);
		drawText(gc, "GAME OVER", width / 2, height / 2 - 100);
		drawText(gc, "Score: " + game.getScore(), width / 2, height / 2);
		drawText(gc, "press R to\n play again", width / 2, height / 2 + 70);
	}

	/**
	 * rendert den Pause Screen
	 * 
	 * @param gc
	 */
	private void renderPauseScreen(GraphicsContext gc) {
		coverBackground(gc);
		drawText(gc, "PAUSED", width / 2, height / 2 - 100);
		drawText(gc, "press SPACE to\n continue or ESC to quit", width / 2, height / 2 + 70);
	}

	/**
	 * erstellt Fenster, bereitet Spiel vor und enthält wichtige Spielmechaniken,
	 * wie z.B. die Game Loop und die Verwaltung von GameStates und Keypresses
	 */
	public void start(Stage primaryStage) {
		try {
			// initialisierung vom Spiel
			gameState = GameState.TITLE_SCREEN;
			BorderPane root = new BorderPane();
			canvas = new Canvas(width, height);
			gc = canvas.getGraphicsContext2D();
			root.getChildren().add(canvas);
			renderTitleScreen(gc);

			// AnimationsTimer, aktualisiert Positionen und Grafiken von Objekten,
			// prüft Game Over Mechanik und
			timer = new AnimationTimer() {
				@Override
				public void handle(long now) {
					game.update();
					game.render(gc);
					if (game.checkCollision()) {

						// Highscore speichern falls der aktuelle Score höher ist
						if (game.getScore().getCounter() > game.getHighScore().getCounter()) {
							game.getScore().saveScore();
						}

						// Spiel beenden falls Kollision mit Hindernis erkannt wurde
						this.stop();
						gameState = GameState.GAME_OVER;
						renderGameOverScreen(gc);
					}
				}
			};

			Scene scene = new Scene(root, width, height);
			primaryStage.setScene(scene);

			// Keypresses festlegen
			primaryStage.getScene().setOnKeyPressed(e -> {

				// Space startet/führt das Spiel fort bzw lässt den Charakter fliegen
				if (e.getCode() == KeyCode.SPACE) {
					if (gameState == GameState.TITLE_SCREEN || gameState == GameState.PAUSED) {
						timer.start();
						gameState = GameState.RUNNING;
					} else if (gameState == GameState.RUNNING) {
						game.jump();
					}

					// R startet das spiel neu
				} else if (e.getCode() == KeyCode.R && gameState == GameState.GAME_OVER) {
					renderTitleScreen(gc);
					gameState = GameState.TITLE_SCREEN;

					// Escape pausiert das Spiel bzw schließt das Spiel
				} else if (e.getCode() == KeyCode.ESCAPE) {
					// Spiel pausieren während es läuft
					if (gameState == GameState.RUNNING) {
						renderPauseScreen(gc);
						timer.stop();
						gameState = GameState.PAUSED;

						// Spiel schließen
					} else if (gameState == GameState.PAUSED || gameState == GameState.TITLE_SCREEN
							|| gameState == GameState.GAME_OVER) {
						primaryStage.close();
					}
				}
			});

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Klasse zur Verwaltung des Hauptfensters und des Spiels
 * 
 * @author Timo Hoffmann
 * @version 1.0
 */
public class Main extends Application {

	/**
	 * Fensterbreite
	 */
	private final int width = SquareGame.WINDOW_WIDTH;

	/**
	 * Fensterhöhe
	 */
	private final int height = SquareGame.WINDOW_HEIGHT;

	/**
	 * Spiel Objekt
	 */
	private SquareGame game;

	/**
	 * Gameloop
	 */
	private AnimationTimer timer;

	/**
	 * aktueller GameState
	 */
	private GameState gameState;
	private GraphicsContext gc;
	private Canvas canvas;

	/**
	 * Helfer Methode um Text zu rendern, verringert die benötigten
	 * Zeilen an Quellcode
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
	 * Helfer Methode um das Bild zu verdunkeln, wird in Titel-, Pause-
	 * und GameOver Screen benutzt
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
		drawText(gc, "FLAPPY SQUARE", width / 2, height / 2 - 100);
		drawText(gc, "press SPACE to play", width / 2, height / 2 + 70);
		gameState = GameState.TITLE_SCREEN;
	}

	/**
	 * rendert den GameOver Screen
	 * 
	 * @param gc
	 */
	private void renderGameOverScreen(GraphicsContext gc) {
		coverBackground(gc);
		drawText(gc, "GAME OVER", width / 2, height / 2 - 100);
		drawText(gc, "Score: " + game.getScore(), width / 2, height / 2);
		drawText(gc, "press R to\n play again", width / 2, height / 2 + 70);
		gameState = GameState.GAME_OVER;
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
		gameState = GameState.PAUSED;
	}

	/**
	 * erstellt Fenster, bereitet Spiel vor und enthält wichtige Spielmechaniken,
	 * wie z.B. die Gameloop und die Verwaltung von GameStates und Keypresses
	 */
	public void start(Stage primaryStage) {
		try {
			// initialisierung vom Spiel
			BorderPane root = new BorderPane();
			canvas = new Canvas(width, height);
			gc = canvas.getGraphicsContext2D();
			root.getChildren().add(canvas);
			renderTitleScreen(gc);

			// AnimationsTimer, aktualisiert Positionen und Grafiken von
			// Objekten, prüft GameOver Mechanik und lässt den Highscore speichern
			timer = new AnimationTimer() {
				@Override
				public void handle(long now) {
					game.update();
					game.render(gc);
					if (game.checkCollision()) {

						// Highscore speichern falls der aktuelle Score höher ist
						if (game.getScore().getCounter() >= game.getHighScore().getCounter()) {
							game.getScore().saveScore();
						}

						// Spiel beenden falls Kollision mit Hindernis erkannt wurde
						this.stop();
						renderGameOverScreen(gc);
						// Garbage Collector ausführen
						System.gc();
					}
				}
			};

			Scene scene = new Scene(root, width, height);
			primaryStage.getIcons().add(new Image(getClass().getResource("character.png").toString()));
			primaryStage.setTitle("Flappy Square!");
			primaryStage.setScene(scene);

			// Keypresses festlegen
			primaryStage.getScene().setOnKeyPressed(e -> {

				switch (e.getCode()) {
					// Space startet/führt das Spiel fort bzw lässt den Charakter fliegen
					case KeyCode.SPACE:
						if (gameState == GameState.TITLE_SCREEN || gameState == GameState.PAUSED) {
							timer.start();
							gameState = GameState.RUNNING;
						} else if (gameState == GameState.RUNNING) {
							game.jump();
						}
						break;
					
					// R startet das spiel neu
					case KeyCode.R:
						if (gameState == GameState.GAME_OVER) {
							renderTitleScreen(gc);
						}
						break;

					// Escape pausiert das Spiel bzw schließt das Spiel
					case KeyCode.ESCAPE:

						// Spiel pausieren während es läuft
						if (gameState == GameState.RUNNING) {
							renderPauseScreen(gc);
							timer.stop();

						// Spiel schließen
						} else if (gameState == GameState.PAUSED || gameState == GameState.TITLE_SCREEN
								|| gameState == GameState.GAME_OVER) {
							primaryStage.close(); 
						}
						break;

					default:
						break;
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
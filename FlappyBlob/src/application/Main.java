package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

	private BlobGame game;
	private final int WIDTH = BlobGame.WINDOW_WIDTH;
	private final int HEIGHT = BlobGame.WINDOW_HEIGHT;
	private Canvas canvas;
	private GraphicsContext gc;
	private AnimationTimer timer;
	private GameState gameState;

	public void renderTitleScreen(GraphicsContext gc) {
		game = new BlobGame();
		game.render(gc);
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.WHITE);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText("FLAPPYBLOB", WIDTH / 2, HEIGHT / 2 - 100);
		gc.strokeText("FLAPPYBLOB", WIDTH / 2, HEIGHT / 2 - 100);
		gc.fillText("press SPACE to play", WIDTH / 2, HEIGHT / 2 + 70);
		gc.strokeText("press SPACE to play", WIDTH / 2, HEIGHT / 2 + 70);
	}

	public void renderGameOverScreen(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.WHITE);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText("GAME OVER", WIDTH / 2, HEIGHT / 2 - 100);
		gc.strokeText("GAME OVER", WIDTH / 2, HEIGHT / 2 - 100);
		gc.fillText("Score: " + game.getScore(), WIDTH / 2, HEIGHT / 2);
		gc.strokeText("Score: " + game.getScore(), WIDTH / 2, HEIGHT / 2);
		gc.fillText("press R to\n play again", WIDTH / 2, HEIGHT / 2 + 70);
		gc.strokeText("press R to\n play again", WIDTH / 2, HEIGHT / 2 + 70);
		game = new BlobGame();
	}
	
	public void renderPauseScreen(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.WHITE);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText("PAUSED", WIDTH / 2, HEIGHT / 2 - 100);
		gc.strokeText("PAUSED", WIDTH / 2, HEIGHT / 2 - 100);
		gc.fillText("press SPACE to\n continue or ESC to quit", WIDTH / 2, HEIGHT / 2 + 70);
		gc.strokeText("press SPACE to\n continue or ESC to quit", WIDTH / 2, HEIGHT / 2 + 70);
	}
	
	public void start(Stage primaryStage) {
		try {
			gameState = GameState.TITLE_SCREEN;
			BorderPane root = new BorderPane();
			canvas = new Canvas(WIDTH, HEIGHT);
			gc = canvas.getGraphicsContext2D();
			root.getChildren().add(canvas);
			renderTitleScreen(gc);
			
			// AnimationsTimer
			timer = new AnimationTimer() {	
				@Override
				public void handle(long now) {
					game.update();
					game.render(gc);
					if (game.checkCollision()) {
						// Highscore speichern falls der aktuelle Score höher ist
						if (game.getHighScore().getCounter() < game.getScore().getCounter()) {
							game.getScore().saveScore();
						}
						this.stop();
						gameState = GameState.GAME_OVER;
						renderGameOverScreen(gc);
					}
				}
			};

			Scene scene = new Scene(root, WIDTH, HEIGHT);
			primaryStage.setScene(scene);

			primaryStage.getScene().setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.SPACE) {
					if (gameState == GameState.TITLE_SCREEN || gameState == GameState.PAUSED) {
						timer.start();
						gameState = GameState.RUNNING;
					} 
					if (gameState == GameState.RUNNING) {
						game.jump();
					}
				} else if (e.getCode() == KeyCode.R && gameState == GameState.GAME_OVER) {
					renderTitleScreen(gc);
					gameState = GameState.TITLE_SCREEN;
				} else if (e.getCode() == KeyCode.ESCAPE) {
					if (gameState == GameState.RUNNING) {
						renderPauseScreen(gc);
						timer.stop();
						gameState = GameState.PAUSED;
					} else if (gameState == GameState.PAUSED || gameState == GameState.TITLE_SCREEN || gameState == GameState.GAME_OVER) {
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
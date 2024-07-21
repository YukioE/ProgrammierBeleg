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

	private BlobGame game;
	private Canvas canvas;
	private GraphicsContext gc;
	private AnimationTimer timer;
	private GameState gameState;

	public void renderTitleScreen(GraphicsContext gc) {
		game = new BlobGame();
		game.render(gc);
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText("FLAPPYBLOB", game.WINDOW_WIDTH / 2, game.WINDOW_HEIGHT / 2 - 100);
		gc.fillText("press SPACE to play", game.WINDOW_WIDTH / 2, game.WINDOW_HEIGHT / 2 + 70);
	}

	public void renderGameOverScreen(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 45));
		gc.fillText("GAME OVER", game.WINDOW_WIDTH / 2, game.WINDOW_HEIGHT / 2 - 100);
		gc.fillText("Score: " + game.getScore(), game.WINDOW_WIDTH / 2, game.WINDOW_HEIGHT / 2);
		gc.fillText("press R to\n play again", game.WINDOW_WIDTH / 2, game.WINDOW_HEIGHT / 2 + 70);
		game = new BlobGame();
	}

	public void start(Stage primaryStage) {
		try {
			gameState = GameState.TITLE_SCREEN;
			BorderPane root = new BorderPane();
			canvas = new Canvas(game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
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

			Scene scene = new Scene(root, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
			primaryStage.setScene(scene);

			primaryStage.getScene().setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.SPACE) {
					if (gameState == GameState.TITLE_SCREEN) {
						timer.start();
						gameState = GameState.RUNNING;
					}
					if (gameState == GameState.RUNNING) {
						game.jump();
					}
				} else if (e.getCode() == KeyCode.R && gameState == GameState.GAME_OVER) {
					renderTitleScreen(gc);
					gameState = GameState.TITLE_SCREEN;

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
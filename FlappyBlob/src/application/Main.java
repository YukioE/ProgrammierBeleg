package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private BlobGame game;
	private Canvas canvas;
	private GraphicsContext gc;

	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			game = new BlobGame();
			canvas = new Canvas(game.getWINDOW_WIDTH(), game.getWINDOW_HEIGHT());
			gc = canvas.getGraphicsContext2D();
			root.getChildren().add(canvas);

			new AnimationTimer() {
				public void handle(long now) {
					game.update();
					game.render(gc);
					if (game.checkCollision()) {
						this.stop();
					}
				}
			}.start();

			Scene scene = new Scene(root, game.getWINDOW_WIDTH(), game.getWINDOW_HEIGHT());
			primaryStage.setScene(scene);

			primaryStage.getScene().setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.SPACE) {
					game.jump();
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
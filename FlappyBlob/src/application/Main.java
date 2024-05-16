package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	
	private BlobGame game;
    private Canvas canvas;
    private GraphicsContext gc;


    @Override

    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();

            // Create an instance of BlobGame
            game = new BlobGame();
            game.startGame();
            
            // Create a canvas to render the game
            canvas = new Canvas(game.getWINDOW_WIDTH(), game.getWINDOW_HEIGHT());
            gc = canvas.getGraphicsContext2D();
            root.getChildren().add(canvas);
           
            // Set up an animation loop to update and render the game
            new AnimationTimer() {

            	@Override
                public void handle(long now) {
                    game.update();
                    game.render(gc);
                    if (game.checkCollision()) {
                    	 // Stop the animation timer
                        this.stop();
                    }
                }
            }.start();

            Scene scene = new Scene(root, game.getWINDOW_WIDTH(), game.getWINDOW_HEIGHT());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
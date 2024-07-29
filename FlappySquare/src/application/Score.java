package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Score {

	private int counter;
	private final File scoreFile = new File("Highscore.data");
	private BufferedWriter out;
	private BufferedReader in;

	/**
	 * Konstruktor
	 * 
	 * @param highscore true falls der Highscore Konstruktor verwendet werden soll,
	 *                  false falls der normle Konstruktor verwendet werden soll
	 */
	public Score(boolean highscore) {
		if (highscore) {
			try {
				
				// falls kein Highscore existiert wird "0" verwendet
				if (!scoreFile.exists() || scoreFile == null) {
					counter = 0;
				} else {
					
					// Highscore einlesen
					in = new BufferedReader(new FileReader(scoreFile));
					String line = in.readLine();

					if (line != null) {
						line.strip();
						counter = Integer.parseInt(line);
						
						// falls kein Highscore gelesen werden konnte wird "0" verwendet
					} else {
						counter = 0;
					}
					
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				counter = 0;
			}

			// normaler Score Konstruktor
		} else {
			counter = 0;
		}

	}

	/**
	 * Methode welche den Highscore speichert
	 */
	public void saveScore() {
		try {

			// Datei löschen falls ein älterer Highscore existiert
			if (scoreFile.exists()) {
				Files.delete(Paths.get(scoreFile.toString()));
			}

			// Highscore in Datei schreiben
			out = new BufferedWriter(new FileWriter(scoreFile));
			out.write(String.valueOf(counter));

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode welche den Score Counter um 1 erhöht
	 */
	public void incrementScore() {
		counter++;
	}
	
	/**
	 * rendert den Score Counter
	 * 
	 * @param posX
	 * @param posY
	 * @param fontSize
	 * @param gc
	 */
	public void render(int posX, int posY, int fontSize, GraphicsContext gc) {
		
		// Farbe einstellen
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setStroke(Color.BLACK);
		gc.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, fontSize));
		
		// text rendern
		gc.fillText(String.valueOf(counter), posX, posY);
		gc.strokeText(String.valueOf(counter), posX, posY);		
	}

	public int getCounter() {
		return counter;
	}
	
	public String toString() {
		return String.valueOf(counter);
	}

}

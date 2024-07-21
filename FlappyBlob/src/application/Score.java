package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Score {

	private int counter;
	private File file = new File(".//Highscore.data");
	private BufferedWriter out = null;
	private BufferedReader in = null;

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
				if (!file.exists() || file == null) {
					counter = 0;
				} else {
					
					// Highscore einlesen
					in = new BufferedReader(new FileReader(file));
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
			if (file.exists()) {
				Files.delete(Paths.get(file.toString()));
			}

			// Highscore in Datei schreiben
			out = new BufferedWriter(new FileWriter(file));
			out.write(String.valueOf(counter));

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode welche den Score Counter um 1 erhöht
	 */
	public void incScore() {
		counter++;
	}

	/**
	 * 
	 * @return counter
	 */
	public int getCounter() {
		return counter;
	}
	
	public String toString() {
		return String.valueOf(counter);
	}

}

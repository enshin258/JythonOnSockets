package com.diamond.iain.javagame.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIOManager {

	public static final String HighScoresPath = "resources/highScores.txt";

	/**
	 * The very first time the game is played the high score file won't exist so
	 * the exception will be raised. If the error message is displayed after
	 * that there is a 'real' problem.
	 * 
	 * @param path
	 *            Of the high score file
	 * @return the stored high score
	 */
	public static int readHighScoreFromFile() {

		// Uses try-with-resources to close Buffered Reader
		try (BufferedReader br = new BufferedReader(new FileReader(
				HighScoresPath))) {
			return Integer.parseInt(br.readLine());
		} catch (IOException e) {
			System.out.println("Unable to read High Score from file");
			return 0;
		}
	}

	/**
	 * Writes a high score to the high score file
	 * 
	 * @param path
	 *            Of the high score file
	 * @param score
	 *            the new high score
	 */
	public static void writeHighScoreToFile(Integer score) {

		// Uses try-with-resources to close Buffered Writer
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(
				HighScoresPath))) {
			bw.write(score.toString());
		} catch (IOException e) {
			System.out.println("Unable to write High Score to File");
		}
	}

}

package com.amiabledata.golferview;

import java.util.Arrays;
import com.google.gson.GsonBuilder;

/*
 * A class for golf player.
 */
class GolfPlayer {

	/*
	 * The name of the golf player.
	 */
	private String name;
	
	/*
	 * The scores of the golf player.
	 */
	private int scores[][] = new int[3][18];

	/*
	 * Creates a new golf player with name and scores.
	 * 
	 * @param name 		Name of the player.
	 * @param scores 	Scores of the player, expecting int[3][18].
	 */
	public GolfPlayer(String name, int[][] scores) {
		setName(name);
		if (scores != null) {
			setScores(scores);
		}
	}

	/*
	 * Creates a new golf player with name.
	 * 
	 * @param name 		Name of the player.
	 */
	public GolfPlayer(String name) {
		this(name, null);
	}
	
	/*
	 * Copies from a golf player.
	 * 
	 * @param inst 		Golf player instance to be copied.
	 */
	public GolfPlayer(GolfPlayer inst) {
		setName(inst.getName());
		setScores(inst.getScores());
	}

	/*
	 * Sets the player name.
	 * 
	 * @param name 		Name of the player.
	 */
	public void setName(String name) {
		this.name = new String(name);
	}

	/*
	 * Sets the player score.
	 * 
	 * @param scores 	Scores of the player, expecting int[3][18].
	 * 
	 * @throws InvalidArrayLengthException If the array length is not correct.
	 */
	public GolfPlayer setScores(int[][] scores) throws InvalidArrayLengthException {
		if (scores.length != 3) {
			throw new InvalidArrayLengthException();
		}

		this.scores = new int[3][18];
		for (int i=0; i<3; i++) {
			setScore(i, scores[i]);
		}
		
		return this;
	}

	/*
	 * Sets the player score for a given round.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * @param scores 	Scores of the player, expecting int[18].
	 * 
	 * @throws InvalidArrayLengthException If the array length is not correct.
	 * @throws ArrayIndexOutOfBoundsException If the round number is invalid.
	 */
	public GolfPlayer setScore(int round, int[] scores) throws InvalidArrayLengthException {
		if (scores.length != 18) {
			throw new InvalidArrayLengthException();
		}
		this.scores[round] = Arrays.copyOf(scores, scores.length);
		return this;
	}

	/*
	 * Sets the player score for a given round and hole.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * @param hole		Hole number of the game minus one (counting from 0).
	 * @param score 	Score of the player.
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the round and hole number are invalid.
	 */
	public GolfPlayer setScore(int round, int hole, int score) {
		this.scores[round][hole] = new Integer(score);
		return this;
	}

	/*
	 * Gets the name of the player.
	 * 
	 * @return Name of the player.
	 */
	public String getName() {
		return new String(this.name);
	}

	/*
	 * Gets the scores of the player.
	 * 
	 * @return Scores of the player as int[3][18]
	 */
	public int[][] getScores() {
		int[][] ret = new int[3][18];
		for (int i=0; i<3; i++) {
			ret[i] = getScores(i);
		}
		return ret;
	}

	/*
	 * Gets the scores of the player for a given round.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the round number is invalid.
	 * @return Scores of the player as int[3]
	 */
	public int[] getScores(int round) {
		return Arrays.copyOf(this.scores[round], this.scores[round].length);
	}

	/*
	 * Gets the score of the player for a given round and hole.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * @param hole		Hole number of the game minus one (counting from 0).
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the round and hole number are invalid.
	 * @return Score of the player
	 */
	public int getScore(int round, int hole) {
		return new Integer(this.scores[round][hole]);
	}

	/*
	 * Gets the total score of the player.
	 * 
	 * @return The total score of the player.
	 */
	public int getTotalScore() {
		int ret = 0;
		for (int i=0; i<3; i++) {
			ret += getTotalScore(i);
		}
		return ret;
	}

	
	/*
	 * Gets the total score of the player for a given round.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the round is invalid.
	 * @return The total score of the player.
	 */
	public int getTotalScore(int round) {
		int ret = 0;
		for (int i=0; i<18; i++) {
			ret += this.scores[round][i];
		}
		return ret;
	}
	
	/*
	 * Serialize this golf team into JSON.
	 * 
	 * @return JSON string representation.
	 */
	public String toJSON() {
		return new GsonBuilder().setPrettyPrinting().serializeNulls()
								.create()
								.toJson(this);
	}
	
	/*
	 * Deserialize a golf team from JSON.
	 * 
	 * @param json		A string containing JSON.
	 * 
	 * @return A golf team instance.
	 */
	public static GolfPlayer fromJSON(String json) {
		return new GsonBuilder().create()
								.fromJson(json, GolfPlayer.class);
	}

}

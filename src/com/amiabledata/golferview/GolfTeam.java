package com.amiabledata.golferview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/*
 * A class for golf team.
 */
public class GolfTeam {

	/*
	 * The name of the golf team.
	 */
	private String name;

	/*
	 * The players from the golf team.
	 */
	private ArrayList<GolfPlayer> players = new ArrayList<GolfPlayer>();
	
	/*
	 * Creates a new golf team.
	 */
	public GolfTeam() {
		this(new String());
	}
	
	/*
	 * Creates a new golf team with name.
	 * 
	 * @param name 		Name of the team.
	 */
	public GolfTeam(String name) {
		setName(name);
	}
	
	/*
	 * Copies from a golf team.
	 * 
	 * @param inst 		Golf team instance to be copied.
	 */
	public GolfTeam(GolfTeam inst) {
		setName(inst.getName());
		players = inst.getPlayers();
	}
	
	/*
	 * Sets the team name.
	 * 
	 * @param name 		Name of the team.
	 */
	public void setName(String name) {
		this.name = new String(name);
	}
	
	/*
	 * Gets the team name.
	 */
	public String getName() {
		return new String(this.name);
	}
	
	/*
	 * Creates a new golf player with name, and put it into this team.
	 * 
	 * @param name 		Name of the player.
	 * 
	 * @return The created player.
	 */
	public GolfPlayer newPlayer(String name) {
		GolfPlayer p = new GolfPlayer(name);
		this.players.add(p);
		return p;
	}
	
	/*
	 * Creates a new golf player with name and scores, and put it into this team.
	 * 
	 * @param name 		Name of the player.
	 * @param scores 	Scores of the player, expecting int[3][18].
	 * 
	 * @return The created player.
	 */
	public GolfPlayer newPlayer(String name, int[][] scores) {
		GolfPlayer p = new GolfPlayer(name, scores);
		this.players.add(p);
		return p;
	}
	
	/*
	 * Gets the golf player at index.
	 * 
	 * @param index 	Index of the player.
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the index number is invalid.
	 * @return The created player.
	 */
	public GolfPlayer getPlayer(int index) {
		return this.players.get(index);
	}
	
	/*
	 * Gets players from this team.
	 * 
	 * @return An ArrayList containing the golf players.
	 */
	public ArrayList<GolfPlayer> getPlayers() {
		ArrayList<GolfPlayer> r = new ArrayList<GolfPlayer>();
		for (Iterator<GolfPlayer> i = this.players.iterator(); i.hasNext();) {
		    r.add(i.next());
		}
		return r;
	}
	
	/*
	 * Remove the golf player at index.
	 * 
	 * @param index 	Index of the player.
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the index number is invalid.
	 */
	public void removePlayer(int index) {
		this.players.remove(index);
	}
	
	/*
	 * Remove the golf player at index.
	 * 
	 * @param player 	Player object.
	 */
	public void removePlayer(GolfPlayer player) {
		this.players.remove(player);
	}
	
	/*
	 * Gets total score of the team.
	 * 
	 * @return Total score of the team.
	 */
	public int getTotalScore() {
		int ret = 0;
		for (Iterator<GolfPlayer> it = this.players.iterator(); it.hasNext(); ) {
			ret += it.next().getTotalScore();
		}
		return ret;
	}
	
	/*
	 * Gets the total score of the team for a given round.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the round is invalid.
	 * @return The total score of the team.
	 */
	public int getTotalScore(int round) {
		int ret = 0;
		for (Iterator<GolfPlayer> it = this.players.iterator(); it.hasNext(); ) {
			ret += it.next().getTotalScore(round);
		}
		return ret;
	}
	
	/*
	 * Gets board total score of the team.
	 * 
	 * @return Board total score of the team.
	 */
	public int getBoardTotalScore() {
		int ret = 0;
		for (int i=0; i<3; i++) {
			ret += getBoardTotalScore(i);
		}
		return ret;
	}
	
	/*
	 * Gets board total score of the team for a given round.
	 * 
	 * @param round		Round number of the game minus one (counting from 0).
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the round is invalid.
	 * @return Board total score of the team.
	 */
	public int getBoardTotalScore(int round) {
		int ret = 0;
		int[] scores = new int[this.players.size()];
		for (int i=0; i<scores.length; i++) {
			scores[i] = this.players.get(i).getTotalScore(round);
		}
		Arrays.sort(scores);
		scores = Arrays.copyOfRange(scores, 0, 4);
		for (int i=0; i<scores.length; i++) {
			ret += scores[i];
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
	 * Serialize this golf team into JsonObject.
	 * 
	 * @return JsonObject instance.
	 */
	public JsonObject toJSONObject() {
		return new GsonBuilder().setPrettyPrinting().serializeNulls()
								.create()
								.toJsonTree(this).getAsJsonObject();
	}
	
	/*
	 * Deserialize a golf team from JSON.
	 * 
	 * @param json		A string containing JSON.
	 * 
	 * @return A golf team instance.
	 */
	public static GolfTeam fromJSON(String json) {
		return new GsonBuilder().create()
								.fromJson(json, GolfTeam.class);
	}
	
	/*
	 * Deserialize a golf team from JSON.
	 * 
	 * @param json		A JsonObject instance.
	 * 
	 * @return A golf team instance.
	 */
	public static GolfTeam fromJSON(JsonObject json) {
		return new GsonBuilder().create()
								.fromJson(json, GolfTeam.class);
	}

}

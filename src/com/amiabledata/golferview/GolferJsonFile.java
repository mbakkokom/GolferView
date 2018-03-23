package com.amiabledata.golferview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GolferJsonFile {
	private String magic_token = new String("golferview_json");
	private Integer[] version;
	private ArrayList<GolfTeam> teams;
	
	public static final Integer[] CURRENT_JSON_VERSION = { 0, 1 };
	
	GolferJsonFile(ArrayList<GolfTeam> teams, Integer[] version) {
		setTeams(teams);
		if (version == null || version.length != 2) {
			setVersion(CURRENT_JSON_VERSION);
		} else {
			setVersion(version);
		}
	}
	
	GolferJsonFile(ArrayList<GolfTeam> teams) {
		this(teams, null);
	}
	
	void toFile(File f) throws IOException {
		
		class GolfTeamSerializer implements JsonSerializer<GolfTeam> {
			public JsonElement serialize(GolfTeam src, Type typeOfSrc, JsonSerializationContext context) {
				return src.toJSONObject();
			}
		}
		
		JsonWriter writer = new JsonWriter(new FileWriter(f));
		GsonBuilder gs = new GsonBuilder();
		gs.setPrettyPrinting().serializeNulls();
		gs.registerTypeAdapter(GolfTeam.class, new GolfTeamSerializer());
		gs.create().toJson(this, GolferJsonFile.class, writer);
		writer.flush();
		writer.close();
	}
	
	static GolferJsonFile fromFile(File f) throws FileNotFoundException {
		
		class GolfTeamDeserializer implements JsonDeserializer<GolfTeam> {
			public GolfTeam deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return GolfTeam.fromJSON(json.getAsJsonObject());
			}
		}
		
		JsonReader reader = new JsonReader(new FileReader(f));
		GsonBuilder gs = new GsonBuilder();
		gs.registerTypeAdapter(GolfTeam.class, new GolfTeamDeserializer());
		return gs.create().fromJson(reader, GolferJsonFile.class);
	}
	
	void setVersion(Integer[] version) {
		this.version = new Integer[2];
		for (int i=0; i<2; i++) {
			this.version[i] = new Integer(version[i]);
		}
	}
	
	Integer[] getVersion() {
		Integer[] ret = new Integer[2];
		for (int i=0; i<2; i++) {
			ret[i] = new Integer(this.version[i]);
		}
		return ret;
	}
	
	void setTeams(ArrayList<GolfTeam> teams) {
		this.teams = new ArrayList<GolfTeam>();
		for (Iterator<GolfTeam> it=teams.iterator(); it.hasNext();) {
			this.teams.add(new GolfTeam(it.next()));
		}
	}
	
	ArrayList<GolfTeam> getTeams() {
		ArrayList<GolfTeam> ret = new ArrayList<GolfTeam>();
		for (Iterator<GolfTeam> it=this.teams.iterator(); it.hasNext();) {
			ret.add(new GolfTeam(it.next()));
		}
		return ret;
	}
	
	boolean isValid() {
		return magic_token.equals("golferview_json") && version.length == 2;
	}
	
}

package com.truetera.model;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ServerConfig {
	ServerConfig(){
		worldServerID = UUID.randomUUID();
		serverKey = "to-core.com";
		gameVersion = 1609;
	}
	
	public UUID getWorldServerID() {
		return worldServerID;
	}

	public String getServerKey() {
		return serverKey;
	}

	public int getGameVersion() {
		return gameVersion;
	}	

	private UUID worldServerID;	
	private String serverKey;
	private int gameVersion;
}

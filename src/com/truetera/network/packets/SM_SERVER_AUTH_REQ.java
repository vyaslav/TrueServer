/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network.packets;

import com.truetera.model.ServerConfig;
import com.truetera.network.ServerPacket;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class SM_SERVER_AUTH_REQ extends ServerPacket{
	@Override
	protected void make() {
		serverBuffer.writeUUID(serverConfig.getWorldServerID());
		serverBuffer.writeBytes(sha1Key);
	}
	

	public byte[] getSha1Key() {
		return sha1Key;
	}

	public void setSha1Key(byte[] sha1Key) {
		this.sha1Key = sha1Key;
	} 
	
	byte[] sha1Key;
	
	@Autowired
	ServerConfig serverConfig;	
}
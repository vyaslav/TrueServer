/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network.packets;

import com.truetera.model.ServerConfig;
import com.truetera.network.ServerPacket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class SM_VERSION extends ServerPacket{

	@Override
	protected void make() {
		serverBuffer.writeInt((version==serverConfig.getGameVersion())?1:0);
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	int version;
	
	@Autowired
	ServerConfig serverConfig;		
}

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
public class SM_NEW_SERVER extends ServerPacket{

	@Override
	protected void make() {
		serverBuffer.writeUUID(serverConfig.getWorldServerID());
		System.out.println("SERVRID: " + serverConfig.getWorldServerID().toString());
	}
	
	@Autowired
	ServerConfig serverConfig;	
}

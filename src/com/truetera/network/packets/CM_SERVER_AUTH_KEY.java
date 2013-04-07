/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network.packets;

import com.truetera.model.ServerConfig;
import com.truetera.network.ClientPacket;
import com.truetera.services.packets.ServerPacketFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.jboss.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope("prototype")
@Component
public class CM_SERVER_AUTH_KEY extends ClientPacket{

	@Override
	protected void make() {
		loginServerID = clientBuffer.readUUID();
		System.out.println("LOGIN SERVRID: " + loginServerID.toString());
		authKey = clientBuffer.readInt();
	}
		
	
	@Override
	public void run(){
		try {

			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] skBytes = serverConfig.getServerKey().getBytes("US-ASCII");
			
			md.update(skBytes, 0, skBytes.length);			
			
			byte[] authKeyBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(authKey).array();

			md.update(authKeyBytes, 0, 4);
			
			SM_SERVER_AUTH_REQ pkt = serverPacketFactory.getPacket(SM_SERVER_AUTH_REQ.class);
			pkt.setSha1Key(md.digest());
			
			ChannelFuture fut = pkt.writeToChannel(channel);
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
			
	private UUID loginServerID;	
	private int authKey;
	
	@Autowired
	ServerConfig serverConfig;	
	
	@Autowired
	ServerPacketFactory serverPacketFactory;
}

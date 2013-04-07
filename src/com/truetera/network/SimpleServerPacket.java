/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network;

import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class SimpleServerPacket extends ServerPacket{

	@Override
	protected void make() {
		serverBuffer.writeBytes(internalBuffer.array());
	}
	
	public SimpleServerPacket writeShort(short val){
		internalBuffer.writeShort(val);
		return this;
	}

	public SimpleServerPacket writeInt(int val){
		internalBuffer.writeInt(val);
		return this;
	}	

	public SimpleServerPacket writeBytes(byte[] val){
		internalBuffer.writeBytes(val);
		return this;
	}	
	
	private ChannelBuffer internalBuffer = ChannelBuffers.dynamicBuffer(ByteOrder.LITTLE_ENDIAN, 0);;
	
	
}

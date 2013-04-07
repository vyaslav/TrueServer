package com.truetera.network;
/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public abstract class ClientPacket extends Packet implements Runnable{
	public ClientPacket(){
		super();
		buffer = null;
	}
	
	protected abstract void make();
	public abstract void run();
	
	public void readFromChannelBuffer(Channel channel, ChannelBuffer channelBuffer){
		this.channel = channel;
		buffer = channelBuffer;
		make();	
		buffer = null;
	}
	
	private ChannelBuffer buffer;
	
	protected class ClientBuffer{
		public short readShort(){
			return buffer.readShort();
		}

		public int readInt(){
			return buffer.readInt();
		}
				
		
		public UUID readUUID(){
			
			Long ms = ByteBuffer.wrap(readBytes(8)).order(ByteOrder.BIG_ENDIAN).getLong();
			Long ls = ByteBuffer.wrap(readBytes(8)).order(ByteOrder.BIG_ENDIAN).getLong();	
			
			//long ls = buffer.readLong();
			//long ms = buffer.readLong();
			return new UUID(ms, ls);
		}
		
		public byte[] readBytes(int size){
			byte[] buf = new byte[size];
			buffer.readBytes(buf);
			return buf;

		}
		
	}
	
	protected ClientBuffer clientBuffer = new ClientBuffer();
	
}

/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

public abstract class ServerPacket extends Packet {
	public ServerPacket(){
		super();
		pktSize = 4;
		buffer = ChannelBuffers.dynamicBuffer(ByteOrder.LITTLE_ENDIAN, pktSize);
		buffer.writeShort(pktSize);
		buffer.writeShort(opcode);
	}
	
	protected abstract void make();
	
	public ChannelFuture writeToChannel(Channel channel){
		this.channel = channel;
		pktSize = 4;
		buffer.writerIndex(pktSize);
		buffer.markWriterIndex();
		make();	
		buffer.setShort(0, pktSize);	
		buffer.setShort(2, opcode);
		buffer.resetReaderIndex();
		return channel.write(buffer);
	}
	
	private ChannelBuffer buffer;
	private short pktSize;
	
	protected class ServerBuffer{
		public void writeShort(short val){
			buffer.writeShort(val);
			pktSize+=2;
		}
		
		public void writeInt(int val){
			buffer.writeInt(val);
			pktSize+=4;
		}		
		
		public void writeUUID(UUID uuid){
			Long ms = new Long(uuid.getMostSignificantBits());
			writeBytes(ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(ms).array());
			Long ls = new Long(uuid.getLeastSignificantBits());
			writeBytes(ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(ls).array());	
			//buffer.writeLong(uuid.getLeastSignificantBits());			
			//buffer.writeLong(uuid.getMostSignificantBits());			
			//pktSize+=16;
		}
		
		public void writeBytes(byte[] bytes){
			buffer.writeBytes(bytes);
			pktSize+=bytes.length;
		}
		
		public void writeLEBytes(byte[] bytes){
			byte[] lebb = new byte[bytes.length];
			for(int i=bytes.length-1;i>=0;i--){
				lebb[bytes.length-1-i]=bytes[i];
			}
			writeBytes(lebb);
		}		
		
	}
	
	protected ServerBuffer serverBuffer = new ServerBuffer();
	
}

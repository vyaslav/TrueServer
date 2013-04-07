/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public abstract class Packet {
	public Packet(){
		this.opcode = -1;
		this.pktSize = 0;
	}

	public Packet setOpcode(short opcode) {
		this.opcode = opcode;
		return this;
	}	
	
	public short getOpcode() {
		return opcode;
	}

	public short getPktSize() {
		return pktSize;
	}
	
	
	protected short opcode;
	protected short pktSize;
	protected ChannelBuffer buffer;
	protected Channel channel;
}

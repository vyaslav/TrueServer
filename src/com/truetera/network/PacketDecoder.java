/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.truetera.services.packets.ClientPacketFactory;

@Scope("prototype")
@Component
public class PacketDecoder extends FrameDecoder {	
    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        // Wait until the length prefix is available.
        if (buffer==null || buffer.readableBytes() < 2) {
            return null;
        }

        buffer.markReaderIndex();

        // Wait until the whole data is available.
        short dataLength = (short)(buffer.readShort()-2);
        
        int bytesToLeft = buffer.readableBytes() - dataLength;
        
        if (bytesToLeft < 0) {
            buffer.resetReaderIndex();
            return null;
        }
        
        short opcode = buffer.readShort();
        
        ClientPacket pkt = clientPacketFactory.getPacketByOpcode(opcode);
        buffer.markWriterIndex();
        
        if(bytesToLeft > 0)
        	buffer.writerIndex(buffer.writerIndex() - bytesToLeft);
        
        if (pkt != null){
        	pkt.readFromChannelBuffer(channel, buffer);
        	//TODO: run in threadpool
        	pkt.run();
        }
        //skip if not all buffer for this packet had been read
        buffer.skipBytes(buffer.readableBytes());
        
        buffer.resetWriterIndex();
        
        return pkt;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }    
	
	@Autowired
	ClientPacketFactory clientPacketFactory;	
}
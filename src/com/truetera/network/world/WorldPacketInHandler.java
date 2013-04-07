package com.truetera.network.world;

import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.truetera.services.packets.ServerPacketFactory;

@Scope("prototype")
@Component
public class WorldPacketInHandler extends SimpleChannelUpstreamHandler{

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
	        throws Exception {
		ChannelBuffer buf = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, initMsg.length);
		buf.writeBytes(initMsg);
		e.getChannel().write(buf);
	    
	    
	}	
	
	private final  static byte[] initMsg = {0x01, 0x00, 0x00, 0x00};
	
	@Autowired
	ServerPacketFactory serverPacketFactory;	
	
}

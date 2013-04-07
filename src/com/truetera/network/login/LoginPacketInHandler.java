package com.truetera.network.login;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.truetera.network.packets.SM_NEW_SERVER;
import com.truetera.services.packets.ServerPacketFactory;

@Component
public class LoginPacketInHandler extends SimpleChannelUpstreamHandler{

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
	        throws Exception {
        SM_NEW_SERVER pkt = serverPacketFactory.getPacket(SM_NEW_SERVER.class);
        
        //ChannelFuture fut = 
        pkt.writeToChannel(e.getChannel());
	    
	    
	}	
	
	@Autowired
	ServerPacketFactory serverPacketFactory;	
	
}

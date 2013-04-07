package com.truetera.network.packets;

import org.jboss.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.truetera.model.ServerConfig;
import com.truetera.network.ClientPacket;
import com.truetera.services.packets.ServerPacketFactory;

@Scope("prototype")
@Component
public class CM_AUTHENTICATION extends ClientPacket{

	@Override
	protected void make() {
		version = clientBuffer.readInt();
		System.out.println("Client Version is: " + version);
		
	}
		
	
	@Override
	public void run(){
		SM_VERSION pkt = serverPacketFactory.getPacket(SM_VERSION.class);
		pkt.setVersion(version);
		
		ChannelFuture fut = pkt.writeToChannel(channel);
		
	}
				
	private int version;
	
	@Autowired
	ServerConfig serverConfig;	
	
	@Autowired
	ServerPacketFactory serverPacketFactory;
}
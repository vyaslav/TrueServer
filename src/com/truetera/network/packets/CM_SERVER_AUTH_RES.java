package com.truetera.network.packets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.truetera.model.ServerConfig;
import com.truetera.network.ClientPacket;

@Scope("prototype")
@Component
public class CM_SERVER_AUTH_RES extends ClientPacket{

	@Override
	protected void make() {
	}
		
	
	@Override
	public void run(){
		System.out.println("CM_SERVER_AUTH_RES");
		
	}
	
	@Autowired
	ServerConfig serverConfig;		
}

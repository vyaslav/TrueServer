package com.truetera;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.truetera.network.login.LoginClientPipelineFactory;
import com.truetera.network.world.WorldServerPipelineFactory;

@Component
public class TTGameServer {
	
	public void start() throws IOException{		
		//idiot check
		if (started) return;
			
		ClientBootstrap loginBootstrap = setUpLoginBootsrap();
		ServerBootstrap serverBootstrap = setUpWorldBootsrap();
        
        //World sends SERVER_LOGIN_REQ to login server when client connects?
        //then login server send SERVER_LOGIN_RES with all data
        
		started = true;
		
        while(System.in.available()==0);
        
		loginChannel.close().awaitUninterruptibly();
        loginBootstrap.releaseExternalResources();
        serverBootstrap.releaseExternalResources();

	}
	
	private ClientBootstrap setUpLoginBootsrap(){
		ClientBootstrap loginBootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        loginBootstrap.setPipelineFactory(loginClientPipelineFactory);

        loginBootstrap.setOption( "bufferFactory", new HeapChannelBufferFactory(
        		ByteOrder.LITTLE_ENDIAN ) );

        // Make a new connection.
        ChannelFuture connectFuture =
        	loginBootstrap.connect(new InetSocketAddress("127.0.0.1", 6666));
		
        // Wait until the connection is made successfully.
        loginChannel = connectFuture.awaitUninterruptibly().getChannel();

        return loginBootstrap;
	}

	private ServerBootstrap setUpWorldBootsrap(){
        ServerBootstrap serverBootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        
        serverBootstrap.setPipelineFactory(worldServerPipelineFactory);   
        
        
        
        serverBootstrap.setOption( "bufferFactory", new HeapChannelBufferFactory(
        		ByteOrder.LITTLE_ENDIAN ) );        

        serverBootstrap.setOption( "child.bufferFactory", new HeapChannelBufferFactory(
        		ByteOrder.LITTLE_ENDIAN ) );           
        
        serverBootstrap.bind(new InetSocketAddress(11101));

        return serverBootstrap;
	}	
	
	public Channel getLoginChannel(){
		return (started)?loginChannel:null;
	}
	
	public boolean isStarted(){
		return started;
	}
	
	private Channel loginChannel;
	boolean started = false;
	
	@Autowired
	LoginClientPipelineFactory loginClientPipelineFactory;
	
	@Autowired
	WorldServerPipelineFactory worldServerPipelineFactory;
	
}

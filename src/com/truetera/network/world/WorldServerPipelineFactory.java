package com.truetera.network.world;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.truetera.network.PacketDecoder;
import com.truetera.network.PacketEncoder;
import com.truetera.network.login.LoginPacketInHandler;

@Component
public class WorldServerPipelineFactory implements ChannelPipelineFactory{
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline(worldChannelHandler);
        
        //pipeline.addLast("decryptor", worldChannelHandler);
        pipeline.addLast("packetDecoder", packetDecoder);
        //pipeline.addLast("packetDecoder", new PacketDecoder());
        
        return pipeline;
    }
    
	@Autowired
	PacketDecoder packetDecoder;	
	@Autowired
	WorldChannelHandler worldChannelHandler;
	//WorldPacketInHandler worldPacketInHandler;    
}

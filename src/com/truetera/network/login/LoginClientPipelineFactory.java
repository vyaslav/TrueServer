/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */
package com.truetera.network.login;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.truetera.network.PacketDecoder;
import com.truetera.network.PacketEncoder;

@Component
public class LoginClientPipelineFactory implements ChannelPipelineFactory{	
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
        
        pipeline.addLast("handler", loginPacketInHandler);
        pipeline.addLast("packetDecoder", packetDecoder);
        pipeline.addLast("packetEncoder", new PacketEncoder());
        
        return pipeline;
    }

	@Autowired
	PacketDecoder packetDecoder;	
	@Autowired
	LoginPacketInHandler loginPacketInHandler;
}

package com.truetera.network.world;

import java.nio.ByteOrder;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.truetera.network.world.crypt.TERACryptor;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

@Scope("prototype")
@Component
public class WorldChannelHandler extends SimpleChannelHandler {
	private static final Logger log = Logger.getLogger(WorldChannelHandler.class);
    private final static short KEY_SIZE = 128;    
	private final  static byte[] initMsg = {0x01, 0x00, 0x00, 0x00};    
	
	private byte[][] clientCryptPkt;
	private byte[][] serverCryptPkt;
    
    int istate;
    
    private TERACryptor decCrypt, encCrypt; 

    
    WorldChannelHandler(){
    	init();
    }
    
    private void init(){
    	clientCryptPkt = new byte[2][KEY_SIZE];
    	serverCryptPkt = new byte[2][KEY_SIZE];
    	istate = 0;
    	decCrypt = null;
    	encCrypt = null;
    }
    
    
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
	        throws Exception {
		ChannelBuffer buf = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, initMsg.length);
		buf.writeBytes(initMsg);
		e.getChannel().write(buf);
		
		super.channelConnected(ctx, e);
	}	  
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		init();
		super.channelDisconnected(ctx, e);
	}
	
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
        Object m = e.getMessage();
        if (!(m instanceof ChannelBuffer)) {
            ctx.sendDownstream(e);
            return;
        }

        ChannelBuffer output = (ChannelBuffer) m;
        
        //Ready to encrypt
    	if (istate == 2){
    		encCrypt.applyCryptor(output.array());
    	}
    	//send anyway
    	ctx.sendDownstream(e);
        
	}
    

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Object m = e.getMessage();
        if (!(m instanceof ChannelBuffer)) {
            ctx.sendUpstream(e);
            return;
        }

        ChannelBuffer input = (ChannelBuffer) m;
        if (!input.readable()) {
            return;
        }
        
        log.info("WCH MsgRcv :"+input.array().length);
        
        //Ready to decrypt
    	if (istate == 2){
    		
    		decCrypt.applyCryptor(input.array());
    		
    		ctx.sendUpstream(e);
    	//making enccryptor and decryptor	
    	}else{
    		if(input.readableBytes() >= KEY_SIZE){
				//read client keys
    			input.readBytes(clientCryptPkt[istate], 0, KEY_SIZE);
    		   
    			//write server keys
				Random rndm = new Random();
				rndm.nextBytes(serverCryptPkt[istate]);
    		   
				ChannelBuffer cBuf = ChannelBuffers.buffer(KEY_SIZE);
				cBuf.writeBytes(serverCryptPkt[istate]);
    		   
				e.getChannel().write(cBuf);    			
    			
    			
    			//creating cryptors
    			if (istate + 1 == 2){
    				byte[][] tmp = new byte[2][KEY_SIZE];
    				byte[][] cryptKey = new byte[2][KEY_SIZE];
    				
    				TERACryptor.shiftKey(serverCryptPkt[0], tmp[0], 31, true);
    				TERACryptor.xorKey(tmp[0], clientCryptPkt[0], tmp[1]);
					TERACryptor.shiftKey(clientCryptPkt[1], tmp[0], 17, false);
					TERACryptor.xorKey(tmp[0], tmp[1], cryptKey[0]);
					
					decCrypt = new TERACryptor();
					
					decCrypt.generateKey(cryptKey[0]);
					
					TERACryptor.shiftKey(serverCryptPkt[1], tmp[0], 79, true);
					decCrypt.applyCryptor(tmp[0]);
					cryptKey[1] = tmp[0];	
					
					encCrypt = new TERACryptor();
					
					encCrypt.generateKey(cryptKey[1]);					
    			}
    			
    			istate++;
    		}  
	       
    	}
    	
    }   
    
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }    

}
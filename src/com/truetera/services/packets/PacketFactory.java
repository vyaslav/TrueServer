package com.truetera.services.packets;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;

import com.truetera.network.Packet;

public class PacketFactory<T extends Packet> implements InitializingBean, ApplicationContextAware{
	
	public String getOpcodesPropsFile() {
		return opcodesPropsFile;
	}

	public void setOpcodesPropsFile(String opcodesProps) {
		this.opcodesPropsFile = opcodesProps;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		pktByOpcodes = new HashMap<>();
		opcodesByPkt = new HashMap<>();
		PktByPktCLass = new HashMap<>();
		
		Properties props = new Properties();
		InputStream psStream = new ClassPathResource(opcodesPropsFile).getInputStream();
		props.load(psStream);
		psStream.close();
		
		for(Object key : props.keySet()){
			String keyStr = (String) key;
			Integer val = Integer.valueOf(props.getProperty(keyStr));
			
			pktByOpcodes.put(val, keyStr);
			opcodesByPkt.put(keyStr, val);
			PktByPktCLass.put(Class.forName(packetsPackage+"."+keyStr.toUpperCase()), keyStr);
		}
			

		
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;
	}
	
	@SuppressWarnings("unchecked")
	public T getPacketById(String id){	
		
		T pkt = (T) ((!opcodesByPkt.containsKey(id))?null:context.getBean(id));
		if(pkt!=null) pkt.setOpcode(opcodesByPkt.get(id).shortValue());
		return pkt;
	}
	
	@SuppressWarnings("unchecked")
	public T getPacketByOpcode(int opcode){	
		T pkt = (T) ((!pktByOpcodes.containsKey(opcode))?null:context.getBean(pktByOpcodes.get(opcode)));
		if(pkt!=null) pkt.setOpcode((short)opcode);
		return pkt;
	}	
	
	public <K> K getPacket(Class<K> type){	
		return type.cast(((!PktByPktCLass.containsKey(type))?null:getPacketById(PktByPktCLass.get(type))));
	}	
	
	public String getPacketsPackage() {
		return packetsPackage;
	}

	public void setPacketsPackage(String packetsPackage) {
		this.packetsPackage = packetsPackage;
	}

	String packetsPackage;
	String opcodesPropsFile;
	Map<Integer, String> pktByOpcodes;
	Map<String, Integer> opcodesByPkt;
	Map<Class<?>, String> PktByPktCLass;
	
	ApplicationContext context;
		
}

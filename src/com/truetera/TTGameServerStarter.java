/* Copyright (C) TrueTera project - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TrueTera team, 01.01.2012
 * File Author: Vyaslav
 */

package com.truetera;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TTGameServerStarter {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("beans.xml");
		
		TTGameServer gameServer = (TTGameServer)context.getBean("TTGameServer");
		gameServer.start();
		
		
	}
}

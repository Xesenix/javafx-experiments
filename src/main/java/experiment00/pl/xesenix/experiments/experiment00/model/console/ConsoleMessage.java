/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/
package pl.xesenix.experiments.experiment00.model.console;


public class ConsoleMessage implements IMessage {

	String message;
	
	
	private IMessageType type;
	
	
	public static ConsoleMessage info(String message)
	{
		return new ConsoleMessage(message, MessageType.INFO);
	}
	
	
	public static ConsoleMessage error(String message)
	{
		return new ConsoleMessage(message, MessageType.ERROR);
	}
	
	
	public static ConsoleMessage message(String message, MessageType type)
	{
		return new ConsoleMessage(message, type);
	}
	
	
	public ConsoleMessage(String message, MessageType type)
	{
		this.message = message;
		this.type = type;
	}
	
	
	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setMessageType(IMessageType type)
	{
		this.type = type;
	}

	public String getMessage()
	{
		return message;
	}

	public IMessageType getType()
	{
		return type;
	}
	
}

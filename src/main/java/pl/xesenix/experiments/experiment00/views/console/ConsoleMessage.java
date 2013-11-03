package pl.xesenix.experiments.experiment00.views.console;

import pl.xesenix.experiments.experiment00.Controller;

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
	
	
	@Override
	public void setMessage(String message)
	{
		this.message = message;
	}

	@Override
	public void setMessageType(IMessageType type)
	{
		this.type = type;
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public IMessageType getType()
	{
		return type;
	}
	
}
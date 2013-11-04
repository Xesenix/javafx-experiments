package pl.xesenix.experiments.experiment01.model.console;



public class ConsoleMessage implements IMessage {

	private String message;
	
	
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
		setMessage(message);
		setMessageType(type);
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
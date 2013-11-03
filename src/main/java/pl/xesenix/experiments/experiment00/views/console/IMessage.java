package pl.xesenix.experiments.experiment00.views.console;

public interface IMessage
{
	void setMessage(String message);
	
	
	void setMessageType(IMessageType type);


	String getMessage();


	IMessageType getType();
}

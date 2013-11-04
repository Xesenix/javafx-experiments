package pl.xesenix.experiments.experiment01.model.console;


public interface IMessage
{
	void setMessage(String message);
	
	
	void setMessageType(IMessageType type);


	String getMessage();


	IMessageType getType();
}

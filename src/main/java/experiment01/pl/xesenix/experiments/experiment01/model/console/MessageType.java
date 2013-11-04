package pl.xesenix.experiments.experiment01.model.console;


public enum MessageType implements IMessageType {
	INFO ("info"),
	ERROR ("error");
	
	private String cssClassName;
	
	MessageType(String cssClass)
	{
		cssClassName = cssClass;
	}

	public String getCssClassName()
	{
		return cssClassName;
	}
}
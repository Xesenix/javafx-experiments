package pl.xesenix.experiments.experiment00.views.console;

import javafx.scene.paint.Color;

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
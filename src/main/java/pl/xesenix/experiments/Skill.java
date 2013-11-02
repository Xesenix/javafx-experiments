package pl.xesenix.experiments;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Skill
{
	private StringProperty name = new SimpleStringProperty(this, "name", "");

	public Skill(String name)
	{
		setName(name);
	}

	public StringProperty getNameProperty()
	{
		return name;
	}
	
	public String getName()
	{
		return name.get();
	}

	public void setName(String name)
	{
		this.name.set(name);
	}
	
	@Override
	public String toString()
	{
		return "Skill: " + getName();
	}
}

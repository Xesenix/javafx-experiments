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
package pl.xesenix.experiments.experiment01.vo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SkillVO
{
	private StringProperty name = new SimpleStringProperty(this, "name", "");

	public SkillVO(String name)
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
		return "SkillVO: " + getName();
	}
}

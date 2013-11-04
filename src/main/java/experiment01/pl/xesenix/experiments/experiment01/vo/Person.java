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

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Person
{
	private static Logger log = LoggerFactory.getLogger(Person.class);
	
	
	private StringProperty name = new SimpleStringProperty(this, "name", "");
	
	
	private IntegerProperty age = new SimpleIntegerProperty(this, "age", 0);


	private ListProperty<Skill> skills = new SimpleListProperty<Skill>(this, "skills", FXCollections.observableList(new ArrayList<Skill>()));


	public Person(String name)
	{
		setName(name);
		getNameProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				log.debug("name changed: {} -> {}", new String[] {oldValue, newValue});
			}
		});
		
		getAgeProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				log.debug("age changed: {} -> {}", new Number[] {oldValue, newValue});
			}
		});
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


	public IntegerProperty getAgeProperty()
	{
		return age;
	}


	public Integer getAge()
	{
		return age.get();
	}


	public void setAge(Integer age)
	{
		this.age.set(age);
	}

	public ListProperty<Skill> getSkillsProperty()
	{
		return skills;
	}
	
	public ObservableList<Skill> getSkills()
	{
		return skills.get();
	}

	public void setSkills(ObservableList<Skill> skills)
	{
		this.skills.set(skills);
	}
	
	@Override
	public String toString()
	{
		return "{Person: " + getName() + ", age: " + getAge() + "}(" + super.toString() + ")";
	}
}

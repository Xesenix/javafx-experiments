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
package pl.xesenix.experiments.experiment00.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonsListModel implements IPersonsListModel
{
	private ListProperty<Person> personsList = new SimpleListProperty<Person>(this, "personsList", FXCollections.<Person>observableArrayList());

	public PersonsListModel()
	{
		Skill karate = new Skill("karate");
		Skill programming = new Skill("programming");
		Skill walking = new Skill("walking");

		Person adam = new Person("Adam");
		adam.setAge(12);
		adam.getSkills().add(walking);
		adam.getSkills().add(programming);

		Person ewa = new Person("Ewa");
		ewa.getSkills().add(karate);

		Person pawel = new Person("Paweł");
		pawel.getSkills().add(karate);
		pawel.getSkills().add(programming);
		pawel.getSkills().add(walking);
		
		ObservableList<Person> list = getPersonsList();
		list.add(adam);
		list.add(ewa);
		list.add(pawel);
	}
	
	
	public ObservableList<Person> getPersonsList()
	{
		return personsList.get();
	}

	public void setPersonsList(ObservableList<Person> persons)
	{
		personsList.set(persons);
	}

	public ListProperty<Person> getPersonsListProperty()
	{
		return personsList;
	}

}

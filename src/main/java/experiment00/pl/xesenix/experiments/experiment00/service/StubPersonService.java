/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors: Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/

package pl.xesenix.experiments.experiment00.service;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment00.model.persons.PersonsListModel;
import pl.xesenix.experiments.experiment00.vo.Person;
import pl.xesenix.experiments.experiment00.vo.Skill;

import com.google.inject.Inject;


public class StubPersonService implements IPersonService
{
	@Inject
	PersonsListModel persons;


	public StubPersonService()
	{
	}


	public ObservableList<Person> getPersons()
	{
		return persons.getPersonsList();
	}


	public ListProperty<Person> getPersonsListProperty()
	{
		return persons.getPersonsListProperty();
	}


	public Person getPersonDetails(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}


	public void acceptPerson(Person person)
	{
		// TODO Auto-generated method stub

	}


	public void declinePerson(Person person, String Reason)
	{
		// TODO Auto-generated method stub

	}


	public void loadPersons()
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

		ObservableList<Person> list = getPersons();
		list.add(adam);
		list.add(ewa);
		list.add(pawel);
	}

}

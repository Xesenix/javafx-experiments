/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors: Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/

package pl.xesenix.experiments.experiment01.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment01.vo.Person;
import pl.xesenix.experiments.experiment01.vo.Skill;

import com.google.inject.Singleton;


@Singleton
public class StubPersonService implements IPersonService
{
	private static Logger log = LoggerFactory.getLogger(StubPersonService.class);


	private ListProperty<Person> personsList = new SimpleListProperty<Person>(this,
		"personsList",
		FXCollections.<Person> observableArrayList());


	public StubPersonService()
	{
		personsList.addListener(new ChangeListener<ObservableList<Person>>() {

			public void changed(ObservableValue<? extends ObservableList<Person>> observable,
				ObservableList<Person> oldValue, ObservableList<Person> newValue)
			{
				log.debug("old:{}\nnew: {}", oldValue, newValue);
			}
		});
	}


	public ObservableList<Person> getPersons()
	{
		return personsList.get();
	}


	public ListProperty<Person> getPersonsListProperty()
	{
		return personsList;
	}


	public Person getPersonDetails(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}


	public void commitPerson(Person person)
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

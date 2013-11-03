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
package pl.xesenix.experiments.experiment00.service;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment00.model.Person;
import pl.xesenix.experiments.experiment00.model.PersonsListModel;

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

}

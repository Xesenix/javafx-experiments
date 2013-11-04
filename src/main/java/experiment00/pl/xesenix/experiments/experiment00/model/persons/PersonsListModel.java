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
package pl.xesenix.experiments.experiment00.model.persons;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment00.vo.Person;

public class PersonsListModel implements IPersonsListModel
{
	private ListProperty<Person> personsList = new SimpleListProperty<Person>(this, "personsList", FXCollections.<Person>observableArrayList());


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

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
package pl.xesenix.experiments.experiment01.model.persons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.xesenix.experiments.experiment01.vo.Person;


public class PersonSelectionModel implements IPersonSelectionModel
{
	private ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<Person>(this, "selectedPerson");


	public Person getSelectedPerson()
	{
		return selectedPerson.get();
	}


	public void setSelectedPerson(Person person)
	{
		selectedPerson.set(person);
	}


	public ObjectProperty<Person> getSelectedPersonProperty()
	{
		return selectedPerson;
	}

}

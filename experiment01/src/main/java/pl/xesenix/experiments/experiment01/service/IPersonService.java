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
package pl.xesenix.experiments.experiment01.service;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment01.vo.Person;

public interface IPersonService
{
	ObservableList<Person> getPersons();
	
	
	Person getPersonDetails(String name);
	
	
	void commitPerson(Person person);
	
	
	void declinePerson(Person person, String Reason);


	ListProperty<Person> getPersonsListProperty();


	void loadPersons();
}

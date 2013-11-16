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
import pl.xesenix.experiments.experiment01.vo.PersonVO;


public class PersonSelectionModel implements IPersonSelectionModel
{
	private ObjectProperty<PersonVO> selectedPerson = new SimpleObjectProperty<PersonVO>(this, "selectedPerson");


	public PersonVO getSelectedPerson()
	{
		return selectedPerson.get();
	}


	public void setSelectedPerson(PersonVO person)
	{
		selectedPerson.set(person);
	}


	public ObjectProperty<PersonVO> getSelectedPersonProperty()
	{
		return selectedPerson;
	}

}

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

import pl.xesenix.experiments.experiment01.vo.PersonVO;
import pl.xesenix.experiments.experiment01.vo.SkillVO;

import com.google.inject.Singleton;


@Singleton
public class StubPersonService implements IPersonService
{
	private static Logger log = LoggerFactory.getLogger(StubPersonService.class);


	private ListProperty<PersonVO> personsList = new SimpleListProperty<PersonVO>(this,
		"personsList",
		FXCollections.<PersonVO> observableArrayList());


	public StubPersonService()
	{
		personsList.addListener(new ChangeListener<ObservableList<PersonVO>>() {

			public void changed(ObservableValue<? extends ObservableList<PersonVO>> observable,
				ObservableList<PersonVO> oldValue, ObservableList<PersonVO> newValue)
			{
				log.debug("old:{}\nnew: {}", oldValue, newValue);
			}
		});
	}


	public ObservableList<PersonVO> getPersons()
	{
		return personsList.get();
	}


	public ListProperty<PersonVO> getPersonsListProperty()
	{
		return personsList;
	}


	public PersonVO getPersonDetails(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}


	public void commitPerson(PersonVO person)
	{
		// TODO Auto-generated method stub

	}


	public void declinePerson(PersonVO person, String Reason)
	{
		// TODO Auto-generated method stub

	}


	public void loadPersons()
	{
		SkillVO karate = new SkillVO("karate");
		SkillVO programming = new SkillVO("programming");
		SkillVO walking = new SkillVO("walking");

		PersonVO adam = new PersonVO("Adam");
		adam.setAge(12);
		adam.getSkills().add(walking);
		adam.getSkills().add(programming);

		PersonVO ewa = new PersonVO("Ewa");
		ewa.getSkills().add(karate);

		PersonVO pawel = new PersonVO("Paweł");
		pawel.getSkills().add(karate);
		pawel.getSkills().add(programming);
		pawel.getSkills().add(walking);

		ObservableList<PersonVO> list = getPersons();
		list.add(adam);
		list.add(ewa);
		list.add(pawel);
	}

}

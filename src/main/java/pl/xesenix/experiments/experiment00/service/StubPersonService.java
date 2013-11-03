package pl.xesenix.experiments.experiment00.service;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment00.model.Person;
import pl.xesenix.experiments.experiment00.model.PersonsListModel;
import pl.xesenix.experiments.experiment00.model.Skill;

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

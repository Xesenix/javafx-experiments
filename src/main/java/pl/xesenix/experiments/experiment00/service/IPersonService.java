package pl.xesenix.experiments.experiment00.service;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment00.model.Person;

public interface IPersonService
{
	ObservableList<Person> getPersons();
	
	
	Person getPersonDetails(String name);
	
	
	void acceptPerson(Person person);
	
	
	void declinePerson(Person person, String Reason);


	ListProperty<Person> getPersonsListProperty();
}

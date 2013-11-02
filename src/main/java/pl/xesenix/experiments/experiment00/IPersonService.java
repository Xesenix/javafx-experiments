package pl.xesenix.experiments.experiment00;

import java.util.List;

import pl.xesenix.experiments.Person;

public interface IPersonService
{
	List<Person> getPersons();
	
	
	Person getPersonDetails(String name);
	
	
	void acceptPerson(Person person);
	
	
	void declinePerson(Person person, String Reason);
}

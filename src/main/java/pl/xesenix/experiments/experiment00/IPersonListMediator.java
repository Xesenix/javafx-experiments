package pl.xesenix.experiments.experiment00;

import pl.xesenix.experiments.Person;

public interface IPersonListMediator
{
	void loadPersons();
	
	
	void updateSelectedPerson(Person person);
}

package pl.xesenix.experiments.experiment00.views;

import pl.xesenix.experiments.experiment00.model.Person;

public interface IPersonListMediator
{
	void loadPersons();
	
	
	void updateSelectedPerson(Person person);
}


package pl.xesenix.experiments.experiment00.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


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

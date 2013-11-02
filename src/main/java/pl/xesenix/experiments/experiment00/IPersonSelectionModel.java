
package pl.xesenix.experiments.experiment00;

import javafx.beans.property.ObjectProperty;
import pl.xesenix.experiments.Person;


public interface IPersonSelectionModel
{
	Person getSelectedPerson();


	void setSelectedPerson(Person person);


	ObjectProperty<Person> getSelectedPersonProperty();
}

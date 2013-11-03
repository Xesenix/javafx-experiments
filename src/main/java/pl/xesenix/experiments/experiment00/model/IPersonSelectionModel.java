
package pl.xesenix.experiments.experiment00.model;

import javafx.beans.property.ObjectProperty;


public interface IPersonSelectionModel
{
	Person getSelectedPerson();


	void setSelectedPerson(Person person);


	ObjectProperty<Person> getSelectedPersonProperty();
}

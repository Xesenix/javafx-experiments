package pl.xesenix.experiments.experiment00.views;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import pl.xesenix.experiments.experiment00.model.Person;

public interface IPersonListView
{
	void updatePersonList(ObservableList<Person> persons);
	
	
	void bindPersonList(ListProperty<Person> property);
}

package pl.xesenix.experiments.experiment00.model;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

public interface IPersonsListModel
{
	ObservableList<Person> getPersonsList();
	
	
	void setPersonsList(ObservableList<Person> persons);
	
	
	ListProperty<Person> getPersonsListProperty();
}

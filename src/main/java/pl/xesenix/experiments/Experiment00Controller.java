
package pl.xesenix.experiments;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pl.xesenix.experiments.experiment00.IPersonDetailView;
import pl.xesenix.experiments.experiment00.IPersonListMediator;
import pl.xesenix.experiments.experiment00.IPersonListView;
import pl.xesenix.experiments.experiment00.IPersonOverviewMediator;



@Singleton
public class Experiment00Controller implements IPersonListView, IPersonDetailView
{
	private static Logger log = LoggerFactory.getLogger(Experiment00Controller.class);
	
	
	@Inject
	private IPersonListMediator listMediator;
	
	
	@Inject
	private IPersonOverviewMediator mediator;


	@FXML
	private ResourceBundle resources;


	@FXML
	private ComboBox<Person> combobox;


	@FXML
	private ListView<Skill> list;


	@FXML
	private TextField age;


	@FXML
	private TextField name;


	@FXML
	private AnchorPane view;


	public Parent getView()
	{
		
		
		return view;
	}


	public Experiment00Controller()
	{
		log.debug("construct");
	}


	/**
	 * 
	 */
	@FXML
	public void initialize()
	{
		assert age != null : "fx:id=\"age\" was not injected: check your FXML file 'experiment00.fxml'.";
		assert combobox != null : "fx:id=\"combobox\" was not injected: check your FXML file 'experiment00.fxml'.";
		assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'experiment00.fxml'.";
		assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'experiment00.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'experiment00.fxml'.";

		/*log.debug("init");

		Skill karate = new Skill("karate");
		Skill programming = new Skill("programming");
		Skill walking = new Skill("walking");

		Person adam = new Person("Adam");
		adam.setAge(12);
		adam.getSkills().add(walking);
		adam.getSkills().add(programming);

		Person ewa = new Person("Ewa");
		ewa.getSkills().add(karate);

		persons.addAll(adam, ewa);

		//final ObjectProperty<Person> selected = new SimpleObjectProperty<Person>(this, "selected");

		combobox.itemsProperty().bindBidirectional(persons);
		//selected.bind(combobox.getSelectionModel().selectedItemProperty());
		
		//Bindings.<Person>select(selected)
		combobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
			public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue)
			{
				log.debug("changed: {}", newValue);
				
				if (name.textProperty().isBound())
				{
					log.debug("unbinding: {}", observable);
					name.textProperty().unbindBidirectional(observable.getValue().getNameProperty());
				}
				
				name.textProperty().bind(newValue.getNameProperty());
				age.textProperty().bindBidirectional((Property<Number>) newValue.getAgeProperty(), new StringConverter<Number>() {

					@Override
					public Integer fromString(String arg0)
					{
						return Integer.parseInt(arg0);
					}

					@Override
					public String toString(Number arg0)
					{
						return String.valueOf((Integer) arg0);
					}
					
				});
				list.itemsProperty().bind(newValue.getSkillsProperty());
			}
		});
		
		/*Bindings.selectString(selected, "name").addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				log.debug("changed name: {}", newValue);
			}
		});*/

		// list.itemsProperty().bind(selected.get().getSkillsProperty());
		// name.textProperty().bind(Bindings.convert(combobox.getSelectionModel().selectedItemProperty()));
		// name.textProperty().bind(selected.get().getNameProperty());
		// list.itemsProperty().bind(selected.get().getSkillsProperty());
		
		combobox.getSelectionModel().selectedItemProperty().addListener(new PersonsListSelectionChangedHandler());
		
		mediator.init();
		listMediator.loadPersons();
	}


	/**
	 * @interface IPersonListView
	 */
	public void updatePersonList(List<Person> persons)
	{
		log.debug("updatePersonList: {}", persons);
		combobox.setItems(FXCollections.observableList(persons));
	}


	/**
	 * @interface IPersonDetailView
	 */
	public void updatePersonDetailsView(Person person)
	{
		log.debug("updatePersonDetails: {}", person);
		name.textProperty().bindBidirectional(person.getNameProperty());
		age.textProperty().bind(person.getAgeProperty().asString());
		list.itemsProperty().bind(person.getSkillsProperty());
	}
	
	
	private class PersonsListSelectionChangedHandler implements ChangeListener<Person> {

		public void changed(ObservableValue<? extends Person> observed, Person oldPerson, Person newPerson)
		{
			log.debug("combobox selection changed to: {}", newPerson);
			listMediator.updateSelectedPerson(newPerson);
		}
		
	}
}

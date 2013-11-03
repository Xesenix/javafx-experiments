
package pl.xesenix.experiments.experiment00;

import java.lang.ref.WeakReference;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment00.model.Person;
import pl.xesenix.experiments.experiment00.model.Skill;
import pl.xesenix.experiments.experiment00.views.IPersonDetailView;
import pl.xesenix.experiments.experiment00.views.IPersonListMediator;
import pl.xesenix.experiments.experiment00.views.IPersonListView;
import pl.xesenix.experiments.experiment00.views.IPersonOverviewMediator;

import com.google.inject.Inject;
import com.google.inject.Singleton;



@Singleton
public class Controller implements IPersonListView, IPersonDetailView
{
	private static Logger log = LoggerFactory.getLogger(Controller.class);
	
	
	@Inject
	private IPersonListMediator listMediator;
	
	
	@Inject
	private IPersonOverviewMediator mediator;


	@FXML
	private ResourceBundle resources;


	@FXML
	private ComboBox<Person> combobox;


	@FXML
	private ListView<Person> peopleList;


	@FXML
	private ListView<Skill> skillList;


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


	public Controller()
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
		assert skillList != null : "fx:id=\"skillList\" was not injected: check your FXML file 'experiment00.fxml'.";
		assert peopleList != null : "fx:id=\"personList\" was not injected: check your FXML file 'experiment00.fxml'.";
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
	public void updatePersonList(ObservableList<Person> persons)
	{
		log.debug("updatePersonList: {}", persons);
		combobox.setItems(persons);
		
	}


	/**
	 * @interface IPersonListView
	 */
	public void bindPersonList(ListProperty<Person> property)
	{
		log.debug("bindPersonList: {}", property);
		combobox.itemsProperty().bind(property);
		peopleList.itemsProperty().bind(property);
	}
	
	
	Person viewedPerson;


	/**
	 * @interface IPersonDetailView
	 */
	public void updatePersonDetailsView(Person person)
	{
		log.debug("updatePersonDetails: {}", person);
		
		if (viewedPerson != null)
		{
			name.textProperty().unbindBidirectional(viewedPerson.getNameProperty());
			age.textProperty().unbindBidirectional(viewedPerson.getAgeProperty());
		}
		
		viewedPerson = person;
		
		name.textProperty().bindBidirectional(viewedPerson.getNameProperty());
		age.textProperty().bindBidirectional((Property<Number>) viewedPerson.getAgeProperty(), new IntegerStringConverter());
		skillList.itemsProperty().bind(viewedPerson.getSkillsProperty());
	}
	
	
	private final class IntegerStringConverter extends StringConverter<Number>
	{
		@Override
		public Integer fromString(String value)
		{
			return Integer.parseInt(value);
		}


		@Override
		public String toString(Number value)
		{
			return String.valueOf((Integer) value);
		}
	}


	private class PersonsListSelectionChangedHandler implements ChangeListener<Person> {

		public void changed(ObservableValue<? extends Person> observed, Person oldPerson, Person newPerson)
		{
			log.debug("combobox selection changed to: {}", newPerson);
			listMediator.updateSelectedPerson(newPerson);
		}
		
	}
}

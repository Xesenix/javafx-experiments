/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/
package pl.xesenix.experiments.experiment00;

import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment00.model.console.ConsoleMessage;
import pl.xesenix.experiments.experiment00.model.console.IMessage;
import pl.xesenix.experiments.experiment00.model.console.IMessageType;
import pl.xesenix.experiments.experiment00.utils.IntegerStringConverter;
import pl.xesenix.experiments.experiment00.views.console.IConsoleMediator;
import pl.xesenix.experiments.experiment00.views.console.IConsoleView;
import pl.xesenix.experiments.experiment00.views.persons.IPersonDetailView;
import pl.xesenix.experiments.experiment00.views.persons.IPersonListMediator;
import pl.xesenix.experiments.experiment00.views.persons.IPersonListView;
import pl.xesenix.experiments.experiment00.views.persons.IPersonOverviewMediator;
import pl.xesenix.experiments.experiment00.vo.Person;
import pl.xesenix.experiments.experiment00.vo.Skill;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class Controller implements IPersonListView, IPersonDetailView, IConsoleView
{
	private static Logger log = LoggerFactory.getLogger(Controller.class);


	@Inject
	private IConsoleMediator consoleMediator;


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
	private Text console;


	@FXML
	private AnchorPane view;


	private Person selectedPerson;


	private Timeline switchConsoleMessageTransition;


	private IMessage consoleUpdateMessage;


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
		assert console != null : "fx:id=\"message\" was not injected: check your FXML file 'experiment00.fxml'.";

		// people list aspect view setup
		
		combobox.getSelectionModel().selectedItemProperty().addListener(new PersonsListSelectionChangedHandler());
		peopleList.getSelectionModel().selectedItemProperty().addListener(new PersonsListSelectionChangedHandler());
		
		mediator.init();
		listMediator.loadPersons();
		
		// persons details aspect view
		
		// console aspect view setup
		
		consoleUpdateMessage = ConsoleMessage.info("");
		
		switchConsoleMessageTransition = TimelineBuilder.create()
			.keyFrames(
				new KeyFrame(new Duration(800), new SwitchMessageAction(console, consoleUpdateMessage)),
				new KeyFrame(new Duration(800), new KeyValue(console.opacityProperty(), 0)),
				new KeyFrame(new Duration(800), new KeyValue(console.translateXProperty(), 100, Interpolator.SPLINE(0.6, 0, 1, 1))),
				new KeyFrame(new Duration(1600), new KeyValue(console.opacityProperty(), 1)),
				new KeyFrame(new Duration(1600), new KeyValue(console.translateXProperty(), 0, Interpolator.SPLINE(0, 0, 0.4, 1)))
			)
			.autoReverse(true)
			.build();
		
		console.opacityProperty().set(0);
		
		consoleMediator.showMessage(ConsoleMessage.info("message.welcome"));
	}
	
	
	// View aspects


	/**
	 * @interface IController
	 * @return Parent root node
	 */
	public Parent getView()
	{
		return view;
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


	/**
	 * @interface IPersonDetailView
	 */
	public void updatePersonDetailsView(Person person)
	{
		log.debug("updatePersonDetails: {}", person);

		if (selectedPerson != null)
		{
			name.textProperty().unbindBidirectional(selectedPerson.getNameProperty());
			age.textProperty().unbindBidirectional(selectedPerson.getAgeProperty());
		}

		selectedPerson = person;

		combobox.getSelectionModel().select(selectedPerson);
		peopleList.getSelectionModel().select(selectedPerson);
		
		name.textProperty().bindBidirectional(selectedPerson.getNameProperty());
		age.textProperty().bindBidirectional((Property<Number>) selectedPerson.getAgeProperty(), new IntegerStringConverter());
		skillList.itemsProperty().bind(selectedPerson.getSkillsProperty());
	}


	/**
	 * @interface IConsoleView
	 */
	public void showMessage(String message, IMessageType type)
	{
		log.debug("showMessage: '{}' type: {}", message, type);
		
		switchConsoleMessageTransition.stop();
		
		consoleUpdateMessage.setMessage(message);
		consoleUpdateMessage.setMessageType(type);
		
		switchConsoleMessageTransition.play();
	}
	
	
	/**
	 * @interface IConsoleView
	 */
	public void showMessage(IMessage message)
	{
		showMessage(message.getMessage(), message.getType());
	}
	
	
	// view helpers


	private class SwitchMessageAction implements EventHandler<ActionEvent>
	{
		private Text console;


		private IMessage message;


		public SwitchMessageAction(Text console, IMessage message)
		{
			this.console = console;
			this.message = message;
		}
		
		
		public void handle(ActionEvent event)
		{
			if (message != null)
			{
				this.console.textProperty().set(message.getMessage());
				this.console.getStyleClass().add(message.getType().getCssClassName());
			}
			else
			{
				this.console.textProperty().set("");
			}
		}
	}


	private class PersonsListSelectionChangedHandler implements ChangeListener<Person>
	{
		public void changed(ObservableValue<? extends Person> observed, Person oldPerson, Person newPerson)
		{
			log.debug("combobox selection changed to: {}", newPerson);
			consoleMediator.showMessage(ConsoleMessage.info(String.format("Editing person: %s", newPerson.getName())));
			listMediator.updateSelectedPerson(newPerson);
		}
	}
}

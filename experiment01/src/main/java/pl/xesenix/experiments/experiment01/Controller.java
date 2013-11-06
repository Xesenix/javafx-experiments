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
package pl.xesenix.experiments.experiment01;

import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment01.model.console.ConsoleMessage;
import pl.xesenix.experiments.experiment01.model.console.IMessage;
import pl.xesenix.experiments.experiment01.model.console.IMessageType;
import pl.xesenix.experiments.experiment01.views.console.IConsoleMediator;
import pl.xesenix.experiments.experiment01.views.console.IConsoleView;
import pl.xesenix.experiments.experiment01.views.persons.IPersonDetailView;
import pl.xesenix.experiments.experiment01.views.persons.IPersonDetailsMediator;
import pl.xesenix.experiments.experiment01.views.persons.IPersonListMediator;
import pl.xesenix.experiments.experiment01.views.persons.IPersonListView;
import pl.xesenix.experiments.experiment01.views.persons.IPersonOverviewMediator;
import pl.xesenix.experiments.experiment01.vo.Person;
import pl.xesenix.experiments.experiment01.vo.Skill;

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
	private IPersonDetailsMediator personDetailsMediator;


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
	private StackPane titlebar;


	@FXML
	private AnchorPane view;


	private Timeline switchConsoleMessageTransition;


	private IMessage consoleUpdateMessage;
	
	
	@FXML
	public void closeApplication()
	{
		System.exit(0);
	}
	
	
	@FXML
	public void minimizeApplication()
	{
		Stage stage = (Stage) getView().getScene().getWindow();
		stage.setIconified(true);
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
		assert age != null : "fx:id=\"age\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert combobox != null : "fx:id=\"combobox\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert skillList != null : "fx:id=\"skillList\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert peopleList != null : "fx:id=\"personList\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert console != null : "fx:id=\"message\" was not injected: check your FXML file 'experiment01.fxml'.";
		assert titlebar != null : "fx:id=\"titlebar\" was not injected: check your FXML file 'experiment01.fxml'.";
		
		final double[] offset = new double[2];
		
		titlebar.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event)
			{
				offset[0] = event.getSceneX();
				offset[1] = event.getSceneY();
			}
		});
		
		titlebar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent event)
			{
				Stage stage = (Stage) getView().getScene().getWindow();
				stage.setX(event.getScreenX() - offset[0]);
				stage.setY(event.getScreenY() - offset[1]);
			}
		});

		// people list aspect view setup
		
		Callback<ListView<Person>, ListCell<Person>> peopleCellFactory = new Callback<ListView<Person>, ListCell<Person>>() {

			public ListCell<Person> call(ListView<Person> param)
			{
				return new PersonListCell();
			}
		};
		
		combobox.getSelectionModel().selectedItemProperty().addListener(new PersonsListSelectionChangedHandler());
		combobox.setButtonCell(new PersonListCell());
		combobox.setCellFactory(peopleCellFactory);
		
		peopleList.getSelectionModel().selectedItemProperty().addListener(new PersonsListSelectionChangedHandler());
		peopleList.setCellFactory(peopleCellFactory);
		
		mediator.init();
		listMediator.loadPersons();
		
		// persons details aspect view
		
		initDetails();
		
		// console aspect view setup
		
		initConsole();
	}


	private void initDetails()
	{
		name.focusedProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (!newValue)
				{
					// focus lost
					log.debug("commit name: {}", name.textProperty().get());
					personDetailsMediator.setName(name.textProperty().get());
				}
			}
		});
		
		age.focusedProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (!newValue)
				{
					// focus lost
					try {
						log.debug("commit age: {}", age.textProperty().get());
						personDetailsMediator.setAge(Integer.parseInt(age.textProperty().get()));
					}
					catch (NumberFormatException ex)
					{
						log.debug("age is not a number");
						personDetailsMediator.invalidAge();
					}
				}
			}
		});
	}


	private void initConsole()
	{
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
	public void updatePersonList(ObservableList<Person> persons)
	{
		log.debug("updatePersonList: {}", persons);
		
		combobox.setItems(persons);
		peopleList.setItems(persons);
	}


	/**
	 * @interface IPersonListView
	 */
	public void updateSelectedPerson(Person person)
	{
		combobox.getSelectionModel().select(person);
		peopleList.getSelectionModel().select(person);
	}


	/**
	 * @interface IPersonDetailView
	 */
	public void updatePersonDetailsView(Person person)
	{
		log.debug("updatePersonDetails: {}", person);
		
		//combobox.getSelectionModel().select(person);
		//peopleList.getSelectionModel().select(person);
		if (person != null)
		{
			name.textProperty().set(person.getName());
			age.textProperty().set(person.getAge().toString());
			skillList.setItems(person.getSkills());
		}
		else
		{
			name.textProperty().set("");
			age.textProperty().set("");
			skillList.setItems(null);
		}
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


	private class PersonListCell extends ListCell<Person>
	{
		private ChangeListener<String> personChangeParameterListener;

		@Override
		protected void updateItem(Person item, boolean empty)
		{
			if (!empty)
			{
				log.debug("list cell value {}", item);
				
				if (personChangeParameterListener == null)
				{
					personChangeParameterListener = new ChangeListener<String>() {
	
						@Override
						public void changed(ObservableValue<? extends String> observed, String oldValue, String newValue)
						{
							log.debug("list cell value change from '{}' to '{}'", oldValue, newValue);
							
							if (!oldValue.equals(newValue))
							{
								setText(newValue);
								//updateItem(item, empty);
							}
						}
					};
				}
				
				setText(item.getName());
				item.getNameProperty().addListener(personChangeParameterListener);
				
				// fancy icon with animation
				
				Group icon = new Group();
				Rectangle rect = new Rectangle(20, 20);
				rect.setArcHeight(10);
				rect.setArcWidth(10);
				Circle circle = new Circle(10, 10, 5, Color.web("#0f4"));
				
				icon.getChildren().add(rect);
				icon.getChildren().add(circle);
				
				Timeline animation = TimelineBuilder.create()
					.keyFrames(new KeyFrame(new Duration(800), new KeyValue(circle.opacityProperty(), 0.5, Interpolator.SPLINE(0.3, 0, 0.7, 1))))
					.cycleCount(Animation.INDEFINITE)
					.autoReverse(true)
					.build();
				
				setGraphic(icon);
				animation.play();
			}
			else
			{
				setText(null);
			}
			
			super.updateItem(item, empty);
		}
	}


	private class SwitchMessageAction implements EventHandler<ActionEvent>
	{
		private Text console;


		private IMessage message;


		private String messageCssClass;


		public SwitchMessageAction(Text console, IMessage message)
		{
			this.console = console;
			this.message = message;
		}
		
		
		public void handle(ActionEvent event)
		{
			String newCssClass = message.getType().getCssClassName();
			
			if (messageCssClass != newCssClass)
			{
				this.console.getStyleClass().remove(messageCssClass);
				messageCssClass = newCssClass;
				this.console.getStyleClass().add(messageCssClass);
			}
			
			if (message != null)
			{
				this.console.textProperty().set(message.getMessage());
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
			listMediator.updateSelectedPerson(newPerson);
		}
	}
}

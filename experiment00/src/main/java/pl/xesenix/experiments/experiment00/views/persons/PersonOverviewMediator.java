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
package pl.xesenix.experiments.experiment00.views.persons;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment00.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.requests.IRequestProvider;
import pl.xesenix.experiments.experiment00.requests.LoadPersonDetailsRequest;
import pl.xesenix.experiments.experiment00.vo.Person;

import com.google.inject.Inject;


public class PersonOverviewMediator implements IPersonOverviewMediator
{
	@Inject
	private IRequestProvider requestProvider;


	@Inject
	public IPersonSelectionModel personSelectionModel;


	@Inject
	public IPersonDetailView view;


	public void init()
	{
		personSelectionModel.getSelectedPersonProperty().addListener(new SelectedPersonChangeHandler());
	}


	private class SelectedPersonChangeHandler implements ChangeListener<Person>
	{

		public void changed(ObservableValue<? extends Person> observed, Person oldPerson, Person newPerson)
		{
			LoadPersonDetailsRequest request = requestProvider.get(LoadPersonDetailsRequest.class);
			request.person = newPerson;
			request.setOnSucceeded(new LoadPersonDetailsSucceedHandler());
			request.start();
		}

	}
	
	
	private class LoadPersonDetailsSucceedHandler implements EventHandler<WorkerStateEvent> {

		public void handle(WorkerStateEvent workerStateEvent)
		{
			view.updatePersonDetailsView((Person) workerStateEvent.getSource().getValue());
		}
		
	}
}

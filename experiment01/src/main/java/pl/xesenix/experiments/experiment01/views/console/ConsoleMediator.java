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
package pl.xesenix.experiments.experiment01.views.console;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment01.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment01.model.console.IMessage;
import pl.xesenix.experiments.experiment01.requests.IRequestProvider;
import pl.xesenix.experiments.experiment01.requests.ShowMessageRequest;

import com.google.inject.Inject;


public class ConsoleMediator implements IConsoleMediator
{
	@Inject
	public IRequestProvider commandProvider;


	@Inject
	public ITranslationProvider translationProvider;


	@Inject
	public IConsoleView view;


	public void showMessage(IMessage message)
	{
		ShowMessageRequest command = commandProvider.get(ShowMessageRequest.class);
		command.message = message;
		command.setOnSucceeded(new LoadMessageHandler());
		command.start();
	}
	
	
	private class LoadMessageHandler implements EventHandler<WorkerStateEvent> {

		public void handle(WorkerStateEvent workerStateEvent)
		{
			view.showMessage((IMessage) workerStateEvent.getSource().getValue());
		}
		
	}
}

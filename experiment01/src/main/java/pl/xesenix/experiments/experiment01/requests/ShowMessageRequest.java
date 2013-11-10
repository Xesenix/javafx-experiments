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
package pl.xesenix.experiments.experiment01.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment01.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment01.model.console.IMessage;

import com.google.inject.Inject;

public class ShowMessageRequest extends  Service<IMessage>
{
	@Inject
	public ITranslationProvider translationProvider;
	
	
	public IMessage message;
	
	
	@Override
	protected Task<IMessage> createTask()
	{
		return new Task<IMessage>() {

			@Override
			protected IMessage call() throws Exception
			{
				message.setMessage(translationProvider.getString(message.getMessage()));
				return message;
			}
		};
	}

}

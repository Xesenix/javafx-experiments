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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class RequestProvider implements IRequestProvider
{
	@Inject
	public Injector injector;


	@SuppressWarnings("rawtypes")
	public <T extends Service> T get(Class<T> type)
	{
		 return injector.getInstance(type);
	}

}

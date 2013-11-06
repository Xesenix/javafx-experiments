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

import pl.xesenix.experiments.experiment01.model.console.IMessage;
import pl.xesenix.experiments.experiment01.model.console.IMessageType;

public interface IConsoleView
{
	void showMessage(String message, IMessageType type);
	
	
	void showMessage(IMessage message);
}

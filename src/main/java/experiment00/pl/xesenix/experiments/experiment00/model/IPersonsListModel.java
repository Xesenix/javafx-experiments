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
package pl.xesenix.experiments.experiment00.model;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

public interface IPersonsListModel
{
	ObservableList<Person> getPersonsList();
	
	
	void setPersonsList(ObservableList<Person> persons);
	
	
	ListProperty<Person> getPersonsListProperty();
}

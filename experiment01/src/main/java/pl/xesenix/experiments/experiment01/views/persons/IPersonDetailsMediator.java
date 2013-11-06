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
package pl.xesenix.experiments.experiment01.views.persons;

public interface IPersonDetailsMediator
{
	/**
	 * Sets current edited person name.
	 * 
	 * @param name
	 */
	void setName(String name);

	
	/**
	 * Sets current edited person age.
	 * 
	 * @param age
	 */
	void setAge(Integer age);


	/**
	 * @param message
	 */
	void infoMessage(String message);


	/**
	 * @param message
	 */
	void errorMessage(String message);
	
	
	/**
	 * Fix incorrect user age.
	 */
	void invalidAge();
	
	
	/**
	 * Update persons lists
	 */
	void updatePersons();
}

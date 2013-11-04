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
package pl.xesenix.experiments.experiment01.utils;

import javafx.util.StringConverter;

public class IntegerStringConverter extends StringConverter<Number>
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

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
package pl.xesenix.experiments.experiment01.application.translations;

import java.util.Locale;
import java.util.ResourceBundle;


public class ResourceBundleTranslationProvider implements ITranslationProvider
{

	private final ResourceBundle bundle;


	public ResourceBundleTranslationProvider()
	{
		Locale locale = Locale.getDefault();
		bundle = ResourceBundle.getBundle("translations.experiment01", locale);
	}


	public String getString(String key)
	{
		if (bundle.containsKey(key))
		{
			return bundle.getString(key);
		}
		
		return key;
	}


	public ResourceBundle getResourceBundle()
	{
		return bundle;
	}
}

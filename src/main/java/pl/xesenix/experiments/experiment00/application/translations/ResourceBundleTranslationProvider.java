
package pl.xesenix.experiments.experiment00.application.translations;

import java.util.Locale;
import java.util.ResourceBundle;


public class ResourceBundleTranslationProvider implements ITranslationProvider
{

	private final ResourceBundle bundle;


	public ResourceBundleTranslationProvider()
	{
		Locale locale = Locale.getDefault();
		bundle = ResourceBundle.getBundle("translations.experiment00", locale);
	}


	public String getString(String key)
	{
		return bundle.getString(key);
	}


	public ResourceBundle getResourceBundle()
	{
		return bundle;
	}
}
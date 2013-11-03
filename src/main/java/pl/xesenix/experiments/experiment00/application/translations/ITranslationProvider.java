
package pl.xesenix.experiments.experiment00.application.translations;

import java.util.ResourceBundle;

public interface ITranslationProvider
{
	String getString(String key);
	
	
	ResourceBundle getResourceBundle();
}
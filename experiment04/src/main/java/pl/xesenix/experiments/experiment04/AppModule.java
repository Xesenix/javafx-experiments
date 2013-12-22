package pl.xesenix.experiments.experiment04;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule
{
	protected void configure()
	{
		bind(AppModule.class).toInstance(this);
		
		Locale locale = new Locale("pl", "PL");
		bind(ResourceBundle.class).toInstance(ResourceBundle.getBundle("bundles.app", locale));
	}
}

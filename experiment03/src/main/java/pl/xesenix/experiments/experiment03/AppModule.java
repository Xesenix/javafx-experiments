package pl.xesenix.experiments.experiment03;

import java.util.Locale;
import java.util.ResourceBundle;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule
{
	@Override
	public void configure()
	{
		bind(AppModule.class).toInstance(this);
		
		Locale locale = new Locale("pl", "PL");
		bind(ResourceBundle.class).toInstance(ResourceBundle.getBundle("bundles.app", locale));
	}
}

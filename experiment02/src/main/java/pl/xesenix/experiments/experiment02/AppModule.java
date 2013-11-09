package pl.xesenix.experiments.experiment02;

import java.util.Locale;
import java.util.ResourceBundle;

import pl.xesenix.experiments.experiment02.requests.IRequestProvider;
import pl.xesenix.experiments.experiment02.requests.RequestProvider;
import pl.xesenix.experiments.experiment02.views.path.IPathMediator;
import pl.xesenix.experiments.experiment02.views.path.IPathView;
import pl.xesenix.experiments.experiment02.views.path.PathMediator;
import pl.xesenix.experiments.experiment02.views.path.PathsEditor;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule
{
	@Override
	public void configure()
	{
		bind(AppModule.class).toInstance(this);
		
		Locale locale = new Locale("pl", "PL");
		bind(ResourceBundle.class).toInstance(ResourceBundle.getBundle("bundles.app", locale));
		
		bind(IPathMediator.class).to(PathMediator.class);
		bind(IRequestProvider.class).to(RequestProvider.class);
		bind(IPathView.class).to(PathsEditor.class);
		bind(PathsEditor.class);
	}
}

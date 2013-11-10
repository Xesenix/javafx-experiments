package pl.xesenix.experiments.experiment02;

import java.util.Locale;
import java.util.ResourceBundle;

import pl.xesenix.experiments.experiment02.components.patheditor.IPathEditorMediator;
import pl.xesenix.experiments.experiment02.components.patheditor.IPathEditorView;
import pl.xesenix.experiments.experiment02.components.patheditor.PathEditorMediator;
import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;
import pl.xesenix.experiments.experiment02.requests.IRequestProvider;
import pl.xesenix.experiments.experiment02.requests.RequestProvider;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule
{
	@Override
	public void configure()
	{
		bind(AppModule.class).toInstance(this);
		
		Locale locale = new Locale("pl", "PL");
		bind(ResourceBundle.class).toInstance(ResourceBundle.getBundle("bundles.app", locale));
		
		bind(IPathEditorMediator.class).to(PathEditorMediator.class);
		bind(IRequestProvider.class).to(RequestProvider.class);
		bind(IPathEditorView.class).to(PathsEditor.class);
		bind(PathsEditor.class);
	}
}

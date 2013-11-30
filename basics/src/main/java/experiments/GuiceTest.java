
package experiments;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;


public class GuiceTest
{
	private static final Logger log = LoggerFactory.getLogger(GuiceTest.class);


	public static void main(String args[]) throws Exception
	{
		log.debug("started");

		Injector injector = Guice.createInjector(new Module());

		ContextManager manager = injector.getInstance(ContextManager.class);
		log.debug("manager: {}", manager);

		Context cntx1 = injector.getInstance(Context.class);
		cntx1.setMessage("Context 1");
		log.debug("cntx1: {}", cntx1);

		Context cntx2 = injector.getInstance(Context.class);
		cntx2.setMessage("Context 2");
		log.debug("cntx2: {}", cntx2);

		NeedContext test = injector.getInstance(NeedContext.class);
		test.execute();
		
		// changing context
		manager.setCurrent(cntx1);
		manager.manage(test);
		
		test.execute();
		
		// changing context
		manager.setCurrent(cntx2);
		manager.manage(test);
		
		test.execute();

		ContextManager manager2 = injector.getInstance(ContextManager.class);
		log.debug("manager: {}", manager2);
		
		test.execute();
	}


	public static class Module extends AbstractModule
	{
		protected void configure()
		{
			bind(NeedContext.class);
			bind(ContextManager.class).toInstance(new ContextManager());
			bind(Context.class).annotatedWith(Names.named("managed")).toProvider(ContextManager.class);
		}
	}

	public static class Context
	{
		private String message;


		public String getMessage()
		{
			return message;
		}


		public void setMessage(String message)
		{
			this.message = message;
		}


		public String toString()
		{
			return String.format("[Context: {message: %s}]", message);
		}
	}

	
	public static class ContextManager implements Provider<Context>
	{
		@Inject
		private Context current;
		
		
		@Inject
		private Injector injector;


		public Context getCurrent()
		{
			return current;
		}


		public void manage(NeedContext test)
		{
			injector.injectMembers(test);
		}


		public void setCurrent(Context context)
		{
			log.debug("changing context to: {}", context);
			this.current = context;
		}


		public Context get()
		{
			return getCurrent();
		}


		public String toString()
		{
			return String.format("[ContextManager: {context: %s}]", current);
		}
	}

	public static class NeedContext
	{
		@Inject
		@Named("managed")
		private Context context;


		public void execute()
		{
			if (context != null)
			{
				log.debug(context.getMessage());
			}
			else
			{
				log.debug("NULL Context");
			}
		}
	}
}

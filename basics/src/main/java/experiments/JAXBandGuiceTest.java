
package experiments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;


public class JAXBandGuiceTest
{
	private static final Logger log = LoggerFactory.getLogger(JAXBandGuiceTest.class);


	@Inject
	private static JAXBContext jaxbContext;


	public static void main(String args[]) throws Exception
	{
		Injector injector = Guice.createInjector(new Module());

		IActor adam = injector.getInstance(IActor.class);
		adam.setName("Adam");

		IAction action = injector.getInstance(IAction.class);
		action.setActor(adam);
		action.execute();
		
		IActor robert = injector.getInstance(IActor.class);
		robert.setName("Robert");

		Lists<Object> list = new Lists<Object>();

		list.getValues().add(adam);
		list.getValues().add(robert);
		list.getValues().add(action);

		String xml = serializeObjects(list);

		log.debug("serialized: {}", xml);

		deserializeXml(injector, jaxbContext, new StringReader(xml));

		log.debug("{}", JAXBandGuiceTest.class.getResource("/test.xml").toExternalForm());
		
		InputStreamReader isr = new InputStreamReader(JAXBandGuiceTest.class.getResourceAsStream("/test.xml"));
		BufferedReader br = new BufferedReader(isr);

		deserializeXml(injector, jaxbContext, br);
	}


	private static String serializeObjects(Lists<Object> list)
	{
		StringWriter xmlWriter = null;

		try
		{
			jaxbContext = JAXBContext.newInstance(Say.class, Star.class, Lists.class);

			xmlWriter = new StringWriter();

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.marshal(list, xmlWriter);

			return xmlWriter.toString();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}

		return null;
	}


	private static void deserializeXml(Injector injector, JAXBContext jaxbContext, Reader reader)
	{
		Lists<Object> list;

		try
		{
			list = (Lists<Object>) jaxbContext.createUnmarshaller().unmarshal(reader);

			for (Object element : list.getValues())
			{
				injector.injectMembers(element);

				if (element instanceof IAction)
				{
					((IAction) element).execute();
				}
			}
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}


	public static class Module extends AbstractModule
	{
		private int counter;


		protected void configure()
		{
			bind(IAction.class).to(Say.class);
			bind(IActor.class).to(Star.class);
			bind(IContext.class).to(Scene.class);

			try
			{
				bind(JAXBContext.class).toInstance(JAXBContext.newInstance(Say.class, Star.class, Lists.class));
			}
			catch (JAXBException e)
			{
				e.printStackTrace();
			}
		}


		@Provides
		@Named("instancesCounter")
		public String getInstanceId()
		{
			return String.valueOf(counter++);
		}
	}

	public static class AnyTypeAdapter extends XmlAdapter<Object, Object>
	{
		public Object unmarshal(Object v)
		{
			return v;
		}


		public Object marshal(Object v)
		{
			return v;
		}
	}

	@XmlRootElement
	public static class Lists<T>
	{
		private List<T> values = new ArrayList<T>();


		@XmlAnyElement(lax = true)
		public List<T> getValues()
		{
			return values;
		}
	}

	@Singleton
	public static class Scene implements IContext
	{
		public String getText()
		{
			return "Be or not to be?";
		}
	}

	@XmlRootElement
	public static class Star implements IActor, IReferable
	{
		private String name;


		@Inject
		@Named("instancesCounter")
		private String xmlRefId;


		@XmlID
		@XmlAttribute
		public String getRefId()
		{
			return xmlRefId;
		}


		public void setRefId(String id)
		{
			xmlRefId = id;
		}


		@XmlAttribute
		public String getName()
		{
			if (name != null)
			{
				return name.toUpperCase();
			}

			return "UNKNOWN";
		}


		public void setName(String name)
		{
			this.name = name;
		}
	}

	@XmlRootElement
	public static class Say implements IAction
	{

		private IActor actor;


		@Inject
		private IContext context;


		@XmlTransient
		public IContext getContext()
		{
			return context;
		}


		public void setContext(IContext context)
		{
			this.context = context;
		}


		@XmlIDREF
		public void setActor(IActor actor)
		{
			this.actor = actor;
		}


		public IActor getActor()
		{
			return actor;
		}


		public void execute()
		{
			if (actor != null)
			{
				log.debug("{} say`s '{}'", actor.getName(), context.getText());
			}
			else
			{
				log.debug("narrator say`s '{}'", context.getText());
			}
		}

	}

	@XmlJavaTypeAdapter(AnyTypeAdapter.class)
	public static interface IAction
	{
		void setActor(IActor actor);


		IActor getActor();


		void execute();
	}

	@XmlJavaTypeAdapter(AnyTypeAdapter.class)
	public static interface IActor
	{
		void setName(String name);


		String getName();
	}

	public static interface IContext
	{
		String getText();
	}

	public static interface IReferable
	{
		String getRefId();


		void setRefId(String id);
	}
}

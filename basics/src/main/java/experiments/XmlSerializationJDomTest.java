
package experiments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.ObjectContext;
import org.apache.commons.jexl2.Script;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;


public class XmlSerializationJDomTest
{
	private static final Logger log = LoggerFactory.getLogger(XmlSerializationJDomTest.class);


	public static void main(String args[]) throws Exception
	{
		long startTime;

		log.debug("started");

		Context context = new Context();

		Any<Context> alternative = new Any<Context>();
		All<Context> coniunction = new All<Context>();
		alternative
			.add(new ScriptCondition<Context>("var p = new (\"java.awt.geom.Point2D$Double\", x, y); return p.distance(0, 0) < 10"));
		coniunction.add(new True());
		coniunction.add(new True());
		coniunction.add(new Equal<Context>(new PropertyGetter<Context>("x"), new PropertyGetter<Context>("y")));
		coniunction.add(new True());
		coniunction.add(new False());
		alternative.add(coniunction);
		alternative.add(new False());
		alternative.add(new False());

		alternative.setContext(context);

		log.debug("condition: {}", alternative.toString());

		startTime = System.currentTimeMillis();
		log.debug("condition evaluation: {}", alternative.check());
		log.debug("evaluation time: {}ms", System.currentTimeMillis() - startTime);

		startTime = System.currentTimeMillis();
		String xmlCondition = serializeObjectToXmlString(alternative);
		log.debug("serialization time: {}ms", System.currentTimeMillis() - startTime);

		log.debug("condition as xml:\n{}", xmlCondition);

		startTime = System.currentTimeMillis();
		IContextDependant<Context> condition = (IContextDependant<Context>) deserializeXmlStringObject(xmlCondition);
		log.debug("deserialization time: {}ms", System.currentTimeMillis() - startTime);

		condition.setContext(context);

		log.debug("condition: {}", condition);

		startTime = System.currentTimeMillis();
		log.debug("condition evaluation: {}", ((ICondition) condition).check());
		log.debug("evaluation time: {}ms", System.currentTimeMillis() - startTime);
	}


	public static String serializeObjectToXmlString(Object object)
	{
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		Element root;

		if (object instanceof IXmlSerializable)
		{
			root = ((IXmlSerializable) object).toXml();
		}
		else
		{
			root = new Element("unknown:" + object.getClass().getCanonicalName());
		}

		return xmlOutput.outputString(root);
	}


	public static Object deserializeXmlStringObject(String xmlCondition)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(xmlCondition.getBytes());

		InputSource in = new InputSource(bais);

		SAXBuilder builder = new SAXBuilder();

		try
		{
			Document document = builder.build(in);

			Element root = document.getRootElement();

			return transformXmlToObject(root);
		}
		catch (JDOMException | IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}


	public static Object transformXmlToObject(Element root)
	{
		switch (root.getName())
		{
			case "any":
				return Any.buildFromXml(root);
			case "all":
				return All.buildFromXml(root);
			case "true":
				return new True();
			case "false":
				return new False();
			case "equal":
				return Equal.buildFromXml(root);
			case "condition":
				return ScriptCondition.buildFromXml(root);
			case "property":
				return PropertyGetter.buildFromXml(root);
		}

		return null;
	}


	public static class Context
	{
		private Double x = 30 * Math.random() - 15;


		private Double y = 30 * Math.random() - 15;


		public Double getX()
		{
			return x;
		}


		public Double getY()
		{
			return y;
		}


		public void setX(Double x)
		{
			this.x = x;
		}
	}

	public static class PropertyGetter<T> implements IGetter<T>
	{
		public static <T> PropertyGetter<T> buildFromXml(Element root)
		{
			String selector = root.getAttributeValue("selector");

			PropertyGetter<T> result = new PropertyGetter<T>(selector);

			return result;
		}


		private T bean;


		private String selector;


		public PropertyGetter(String selector)
		{
			this.selector = selector;
		}


		public void setContext(T context)
		{
			this.bean = context;
		}


		public Object get()
		{
			JexlEngine jexl = new JexlEngine();

			try
			{
				if (bean != null)
				{
					return jexl.getProperty(bean, selector);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return null;
		}


		public Element toXml()
		{
			Element root = new Element("property");

			root.setAttribute("selector", selector);

			return root;
		}


		public String toString()
		{
			return String.format("%s", get());
		}
	}

	public static class ScriptCondition<T> implements ICondition, IContextDependant<T>
	{
		public static <T> ScriptCondition<T> buildFromXml(Element root)
		{
			String script = root.getText();

			ScriptCondition<T> result = new ScriptCondition<T>(script);

			return result;
		}


		private Script script;


		private JexlContext scriptContext;


		public ScriptCondition(String script)
		{
			JexlEngine jexl = new JexlEngine();

			this.script = jexl.createScript(script);
		}


		public void setContext(T context)
		{
			JexlEngine jexl = new JexlEngine();

			if (context != null)
			{
				scriptContext = new ObjectContext<T>(jexl, context);
			}
		}


		public Boolean check()
		{
			try
			{
				if (scriptContext != null)
				{
					Object result = script.execute(scriptContext);

					log.debug("script: [{}] result: {}", script.getText(), result);

					if (result != null)
					{
						return (Boolean) result;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return false;
		}


		public Element toXml()
		{
			Element root = new Element("condition");

			root.addContent(new CDATA(script.getText()));

			return root;
		}


		public String toString()
		{
			return String.format("function() {%s}", script.getText());
		}
	}

	public static class True implements ICondition
	{
		public Boolean check()
		{
			return true;
		}


		public Element toXml()
		{
			Element root = new Element("true");

			return root;
		}


		public String toString()
		{
			return "true";
		}
	}

	public static class False implements ICondition
	{
		public Boolean check()
		{
			return false;
		}


		public Element toXml()
		{
			Element root = new Element("false");

			return root;
		}


		public String toString()
		{
			return "false";
		}
	}

	public static class Equal<T> implements ICondition, IContextDependant<T>
	{
		public static <T> Equal<T> buildFromXml(Element root)
		{
			IGetter<T> subject = null;
			IGetter<T> target = null;

			Element subjectsXml = root.getChild("subject");

			if (subjectsXml != null)
			{
				Object result = null;

				for (Element subjectXml : subjectsXml.getChildren())
				{
					result = transformXmlToObject(subjectXml);

					if (result instanceof IGetter)
					{
						subject = (IGetter<T>) result;
					}
				}
			}

			Element targetsXml = root.getChild("target");

			if (targetsXml != null)
			{
				Object result = null;

				for (Element targetXml : targetsXml.getChildren())
				{
					result = transformXmlToObject(targetXml);

					if (result instanceof IGetter)
					{
						target = (IGetter<T>) result;
					}
				}
			}

			Equal<T> result = new Equal<T>(subject, target);

			return result;
		}


		private IGetter<T> subject;


		private IGetter<T> target;


		private T context;


		public Equal(IGetter<T> subject, IGetter<T> target)
		{
			this.subject = subject;
			this.target = target;
		}


		public void setContext(T context)
		{
			this.context = context;

			if (subject != null)
			{
				subject.setContext(context);
			}

			if (target != null)
			{
				target.setContext(context);
			}
		}


		public Boolean check()
		{
			subject.setContext(context);

			if (target != null)
			{
				return target.equals(subject.get());
			}

			return subject.get() == null;
		}


		public Element toXml()
		{
			Element root = new Element("equal");

			if (this.subject != null)
			{
				root.addContent(new Element("subject").setContent(this.subject.toXml()));
			}

			if (this.target != null)
			{
				root.addContent(new Element("target").setContent(this.target.toXml()));
			}

			return root;
		}


		public String toString()
		{
			return String.format("%s == %s", this.subject, this.target);
		}
	}

	public static class Any<T> implements ICompositeCondition, IContextDependant<T>
	{
		public static <T> Any<T> buildFromXml(Element root)
		{
			Any<T> result = new Any<T>();

			for (Element conditionXml : root.getChildren())
			{
				Object part = transformXmlToObject(conditionXml);

				if (part instanceof ICondition)
				{
					result.add((ICondition) part);
				}
			}

			return result;
		}


		ArrayList<ICondition> conditions = new ArrayList<ICondition>();


		private T context;


		public void add(ICondition condition)
		{
			conditions.add(condition);
		}


		public void setContext(T context)
		{
			this.context = context;

			for (ICondition condition : conditions)
			{
				if (condition instanceof IContextDependant)
				{
					((IContextDependant<T>) condition).setContext(context);
				}
			}
		}


		public Boolean check()
		{
			for (ICondition condition : conditions)
			{
				if (condition instanceof IContextDependant)
				{
					((IContextDependant<T>) condition).setContext(context);
				}

				if (condition.check())
				{
					return true;
				}
			}

			return false;
		}


		public Element toXml()
		{
			Element root = new Element("any");

			for (ICondition condition : conditions)
			{
				root.addContent(condition.toXml());
			}

			return root;
		}


		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			String delim = "";

			for (ICondition condition : conditions)
			{
				builder.append(delim + condition.toString());
				delim = " or ";
			}

			return "( " + builder.toString() + " )";
		}
	}

	public static class All<T> implements ICompositeCondition, IContextDependant<T>
	{
		public static <T> All<T> buildFromXml(Element root)
		{
			All<T> result = new All<T>();

			for (Element conditionXml : root.getChildren())
			{
				Object part = transformXmlToObject(conditionXml);

				if (part instanceof ICondition)
				{
					result.add((ICondition) part);
				}
			}

			return result;
		}


		ArrayList<ICondition> conditions = new ArrayList<ICondition>();


		private T context;


		public void add(ICondition condition)
		{
			conditions.add(condition);
		}


		public void setContext(T context)
		{
			this.context = context;

			for (ICondition condition : conditions)
			{
				if (condition instanceof IContextDependant)
				{
					((IContextDependant<T>) condition).setContext(context);
				}
			}
		}


		public Boolean check()
		{
			for (ICondition condition : conditions)
			{
				if (condition instanceof IContextDependant)
				{
					((IContextDependant<T>) condition).setContext(context);
				}

				if (!condition.check())
				{
					return false;
				}
			}

			return true;
		}


		public Element toXml()
		{
			Element root = new Element("all");

			for (ICondition condition : conditions)
			{
				root.addContent(condition.toXml());
			}

			return root;
		}


		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			String delim = "";

			for (ICondition condition : conditions)
			{
				builder.append(delim + condition.toString());
				delim = " and ";
			}

			return "( " + builder.toString() + " )";
		}
	}

	public interface IXmlSerializable
	{
		Element toXml();
	}

	public interface ICondition extends IXmlSerializable
	{
		Boolean check();
	}

	public interface ICompositeCondition extends ICondition
	{
		void add(ICondition condition);
	}

	public interface IContextDependant<T> extends IXmlSerializable
	{
		void setContext(T context);
	}

	public interface IGetter<T> extends IContextDependant<T>
	{
		Object get();
	}
}

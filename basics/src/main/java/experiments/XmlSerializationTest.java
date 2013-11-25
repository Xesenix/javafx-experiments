
package experiments;

import java.util.ArrayList;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.ObjectContext;
import org.apache.commons.jexl2.Script;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jdom2.CDATA;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlSerializationTest
{
	private static final Logger log = LoggerFactory.getLogger(XmlSerializationTest.class);
	
	
	public static void main(String args[]) throws Exception
	{
		log.debug("started");
		
		Context context = new Context();
		
		Any<Context> alternative = new Any<Context>();
		All<Context> coniunction = new All<Context>();
		alternative.add(new ScriptCondition<Context>("var p = new (\"java.awt.geom.Point2D$Double\", x, y); return p.distance(0, 0) < 10"));
		coniunction.add(new True());
		coniunction.add(new True());
		coniunction.add(new Equal<Context>(new PropertyGetter<Context>("x"), new PropertyGetter<Context>("y")));
		coniunction.add(new True());
		coniunction.add(new False());
		alternative.add(coniunction);
		alternative.add(new False());
		alternative.add(new False());
		
		alternative.setContext(context);
		
		log.debug("condition: {}", StringEscapeUtils.unescapeHtml4(alternative.toString()));
		log.debug("condition evaluation: {}", alternative.check());		
		log.debug("condition as xml:\n{}", serializeObjectToXmlString(alternative));
		
		
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
	
	
	public static class Context
	{
		private Double x = 30 * Math.random();
		
		
		private Double y = 30 * Math.random();
		

		public Double getX()
		{
			return x;
		}


		public Double getY() {
			return y;
		}


		public void setX(Double x)
		{
			this.x = x;
		}
	}


	public static class PropertyGetter<T> implements IGetter<T>
	{
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
			
			try {
				if (bean != null)
				{
					return jexl.getProperty(bean, selector);
				}
			}
			catch (Exception e) {
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
		private T context;
		
		
		private String script;
		
		
		public ScriptCondition(String script)
		{
			this.script = script;
		}


		public void setContext(T context)
		{
			this.context = context;
		}
		
		
		public Boolean check()
		{
			JexlEngine jexl = new JexlEngine();
			
			try {
				if (context != null)
				{
					Script s = jexl.createScript(script);
					
					JexlContext jexlContext = new ObjectContext<T>(jexl, context);
						
					Object result = s.execute(jexlContext);
					
					log.debug("script: [{}] result: {}", script, result);
					
					if (result != null)
					{
						return (Boolean) result;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		
		public Element toXml()
		{
			Element root = new Element("condition");
			
			root.addContent(new CDATA(script));
			
			return root;
		}
		
		
		public String toString()
		{
			return String.format("function() {%s}", script);
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
			
			subject.setContext(context);
			target.setContext(context);
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
			return this.subject.toString() + " == " + this.target.toString();
		}
	}


	public static class Any<T> implements ICompositeCondition, IContextDependant<T>
	{
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

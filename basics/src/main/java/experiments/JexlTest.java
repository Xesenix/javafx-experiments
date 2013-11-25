
package experiments;

import org.apache.commons.jexl2.JexlEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JexlTest
{
	private static final Logger log = LoggerFactory.getLogger(JexlTest.class);


	public static void main(String args[]) throws Exception
	{
		Context context = new Context();
		context.setText("Hello world of scripting!!!");
		context.setNumber(42);
		
		Context childContext = new Context();
		childContext.setText("I can divide my parent number!");
		childContext.setNumber(7);
		
		context.setContext(childContext);

		try
		{
			queryProperty(context, "text");
			queryProperty(context, "number");
			queryProperty(context, "context");
			queryProperty(context, "context.text");
			queryProperty(context, "context.number");
			queryProperty(context, "context.context");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	private static void queryProperty(Object context, String query)
	{
		JexlEngine jexl = new JexlEngine();
		
		log.debug("{}: {}", query, jexl.getProperty(context, query));
	}


	public static class Context
	{
		private Context context;


		private String text;


		private Integer number;


		public Context getContext()
		{
			return context;
		}


		public void setContext(Context context)
		{
			this.context = context;
		}


		public String getText()
		{
			return text;
		}


		public void setText(String text)
		{
			this.text = text;
		}


		public Integer getNumber()
		{
			return number;
		}


		public void setNumber(Integer number)
		{
			this.number = number;
		}

		
		public String toString()
		{
			return String.format("{text: %s; number: %d; child: %s}", text, number, context);
		}
	}
}

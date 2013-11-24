
package experiments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import org.eclipse.core.internal.preferences.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ObjectSerializationTest
{
	private static final Logger log = LoggerFactory.getLogger(ObjectSerializationTest.class);


	public static void main(String args[]) throws Exception
	{
		log.debug("started");

		AdvancedPropertiesClass test = new AdvancedPropertiesClass();
		test.text = "Hello serialized world";
		test.number = 2 + 2;
		test.object = new SimplePropertiesClass();
		test.object.setProperty("object property test value");

		log.debug("object: {}", test);

		String stringTest = serializeObjectToString(test);

		log.debug("serialized object: {}", stringTest);

		AdvancedPropertiesClass deserialized = (AdvancedPropertiesClass) deserializeStringObject(stringTest);

		log.debug("deserialized object: {}", deserialized);
	}


	public static String serializeObjectToString(Object object) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ObjectOutputStream os = new ObjectOutputStream(baos);

		os.writeObject(object);
		os.close();

		baos.close();

		return new String(Base64.encode(baos.toByteArray()));
	}


	public static Object deserializeStringObject(String data)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(data.getBytes()));

		try
		{
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}


	public static class SimplePropertiesClass implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		
		private String property = "object property";


		public void setProperty(String value)
		{
			property = value;
		}


		public String toString()
		{
			return String.format("{property: %s}", property);
		}
	}

	public static class AdvancedPropertiesClass implements Serializable
	{
		private static final long serialVersionUID = 1L;


		public String text;


		public Integer number;


		/**
		 * Non primitive property requires us to serialize this class by hand.
		 */
		public SimplePropertiesClass object;


		public String toString()
		{
			return String.format("{text: %s; number: %d; object: %s}", text, number, object);
		}


		private void writeObject(ObjectOutputStream out) throws IOException
		{
			ObjectOutputStream.PutField fields = out.putFields();

			fields.put("text", text);
			fields.put("number", number);
			fields.put("object", serializeObjectToString(object));

			out.writeFields();
		}


		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
		{
			ObjectInputStream.GetField fields = in.readFields();
			text = (String) fields.get("text", new String());
			number = (Integer) fields.get("number", new Integer(0));
			object = (SimplePropertiesClass) deserializeStringObject((String) fields.get("object", null));
		}


		private void readObjectNoData() throws ObjectStreamException
		{

		}
	}
}

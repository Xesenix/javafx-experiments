
package pl.xesenix.experiments.experiment04;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.AnchorPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;


public class Controller
{
	private static final Logger log = LoggerFactory.getLogger(Controller.class);
	
	
	@FXML
	private ResourceBundle resources;


	@FXML
	private URL location;


	@FXML
	private TreeView<Object> treeView;


	@FXML
	private AnchorPane view;


	private Project project;


	@Inject
	private JAXBContext jaxbContext;


	@FXML
	void initialize()
	{
		assert treeView != null : "fx:id=\"treeView\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";

		project = new Project();
		List<Entity> entties = project.getEntities();
		String[] types = new String[] {
			"Health",
			"Mana",
			"Speed",
			"Damage",
			"Size",
		};

		for (int i = 0; i < 10; i++)
		{
			Entity e = new Entity();
			entties.add(e);

			for (int j = 0; j < 5; j++)
			{
				Component c = new Component(types[j]);
				e.getComponents().add(c);

				Property str = new Property("text");
				str.setType(PropertyType.STRING);

				c.getProperties().add(str);

				Property min = new Property("min");
				min.setType(PropertyType.INTEGER);

				c.getProperties().add(min);

				Property max = new Property("max");
				max.setType(PropertyType.INTEGER);

				c.getProperties().add(max);
			}
		}
		
		getProjectAsXmlString();

		TreeItem<Object> rootNode = new TreeItem<Object>(entties);

		for (Entity entity : entties)
		{
			TreeItem<Object> entityNode = new TreeItem<Object>(entity);

			for (Component component : entity.getComponents())
			{
				TreeItem<Object> componentNode = new TreeItem<Object>(component);

				for (Property property : component.getProperties())
				{
					TreeItem<Object> propertyNode = new TreeItem<Object>(property);

					componentNode.getChildren().add(propertyNode);
				}

				entityNode.getChildren().add(componentNode);
			}

			rootNode.getChildren().add(entityNode);
		}

		treeView.setCellFactory(CheckBoxTreeCell.<Object> forTreeView());

		treeView.setRoot(rootNode);
	}
	
	
	public String getProjectAsXmlString()
	{
		StringWriter xmlWriter = new StringWriter();

		Marshaller jaxbMarshaller;
		try
		{
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, xmlWriter);
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}

		String result = xmlWriter.toString();

		log.debug("serialized - XML Project: {}", result);

		return result;
	}
	
	
	@XmlRootElement
	public static class Project
	{
		private List<Entity> entities = new ArrayList<Entity>();

		public List<Entity> getEntities()
		{
			return entities;
		}

		public void setEntities(List<Entity> entities)
		{
			this.entities = entities;
		}
	}


	@XmlRootElement
	public static class Entity
	{
		private static int counter = 0;


		private List<Component> components = new ArrayList<Component>();


		private int id;


		@XmlAttribute
		public int getId()
		{
			return id;
		}


		public void setId(int id)
		{
			this.id = id;
		}


		public Entity()
		{
			setId(counter++);
		}


		public List<Component> getComponents()
		{
			return components;
		}


		public void setComponents(List<Component> components)
		{
			this.components = components;
		}


		public String toString()
		{
			return String.format("Entity%04d", getId());
		}
	}

	
	@XmlRootElement
	public static class Component
	{
		private List<Property> properties = new ArrayList<Property>();


		private String type;
		
		
		public Component()
		{
		}


		public Component(String type)
		{
			this.type = type;
		}


		public List<Property> getProperties()
		{
			return properties;
		}


		public void setProperties(List<Property> properties)
		{
			this.properties = properties;
		}


		public String toString()
		{
			return String.format("Component%s", type);
		}
	}

	
	@XmlRootElement
	public static class Property
	{
		private PropertyType type;


		private Object value;


		private String name;
		
		
		public Property()
		{
			// TODO Auto-generated constructor stub
		}


		public Property(String name)
		{
			this.setName(name);
		}


		@XmlAttribute
		public String getName()
		{
			return name;
		}


		public void setName(String name)
		{
			this.name = name;
		}


		@XmlAttribute
		public PropertyType getType()
		{
			return type;
		}


		public void setType(PropertyType type)
		{
			this.type = type;
		}


		public Object getValue()
		{
			return value;
		}


		public void setValue(Object value)
		{
			this.value = value;
		}


		public String toString()
		{
			return String.format("%s (%s): %s", name, type, value);
		}
	}

	public static enum PropertyType
	{
		STRING, INTEGER;
	}
}

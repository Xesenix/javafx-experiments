package experiments;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSerializationJAXBTest
{
	private static final Logger log = LoggerFactory.getLogger(XmlSerializationJAXBTest.class);


	public static void main(String args[]) throws Exception
	{
		log.debug("started");
		
		Node root = new Node();
		root.setName("root");
		root.setData("sequence");
		
		root.addChild(new Node()
			.setName("filter")
			.setData("race in ['elf', 'drow']")
			.addChild(new Node()
				.setName("action")
				.setData("say('Hello world')")
				.addChild(root)
				.addChild(new Leaf()
					.setName("script")
					.setData("take('coins', 550)")
				)
			)
		);
		
		root.addChild(new Node()
			.setName("filter")
			.setData("age lt 120")
			.addChild(root)
		);
		
		log.debug("graph: {}", root);
		
		JAXBContext jaxbContext = null;
		StringWriter xmlWriter = null;
		
		try {
			jaxbContext = JAXBContext.newInstance(Graph.class);
			xmlWriter = new StringWriter();
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(new Graph(), xmlWriter);
			
			log.debug("{}", xmlWriter);
			
			Graph copy = (Graph) jaxbContext.createUnmarshaller().unmarshal(new StringReader(xmlWriter.toString()));
			
			log.debug("graph: {}", copy.getNodes().get(0));
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
	}
	
	@XmlRootElement(name="graph")
	public static class Graph
	{
		private static Integer counter = 0;
		
		
		@XmlElements({ 
			@XmlElement(name="leaf", type=Leaf.class),
			@XmlElement(name="node", type=Node.class)
		})
		private static List<? extends Leaf> nodes = new ArrayList<Leaf>();
		
		
		public static List<Leaf> getNodes()
		{
			return (List<Leaf>) nodes;
		}
		
		public static void setNodes(List<? extends Leaf> nodes)
		{
			Graph.nodes = nodes;
		}
	}
	
	
	@XmlRootElement(name="leaf")
	public static class Leaf
	{
		private Integer id;
		
		
		private String name;
		
		
		private String data;
		
		
		private Node parent = null;
		
		
		public Leaf()
		{
			id = Graph.counter++;
			Graph.getNodes().add(this);
		}


		@XmlID
		@XmlAttribute(name="node-id")
		public String getId()
		{
			return id.toString();
		}
		
		
		public Leaf setId(String id)
		{
			this.id = Integer.valueOf(id);
			
			return this;
		}


		@XmlAttribute
		public String getName()
		{
			return name;
		}


		public Leaf setName(String name)
		{
			this.name = name;
			
			return this;
		}


		public String getData()
		{
			return data;
		}


		public Leaf setData(String data)
		{
			this.data = data;
			
			return this;
		}


		@XmlTransient
		//@XmlAttribute
		//@XmlIDREF
		public Node getParent()
		{
			return parent;
		}


		public Leaf setParent(Node parent)
		{
			this.parent = parent;
			
			if (parent != null)
			{
				parent.addChild(this);
			}
			
			return this;
		}
		
		
		public String toString()
		{
			return String.format("{id:%s; name: %s; data:%s;}", getId(), getName(), getData());
		}
	}
	
	
	@XmlRootElement(name="node")
	public static class Node extends Leaf
	{
		private boolean rendering = false;
		
		
		private ArrayList<Leaf> children = new ArrayList<Leaf>();
		
		
		public Node addChild(Leaf node)
		{
			if (!children.contains(node))
			{
				children.add(node);
				
				if (node.getParent() == null || !node.getParent().equals(this))
				{
					node.setParent(this);
				}
			}
			
			return this;
		}
		
		
		public Node removeChild(Leaf node)
		{
			children.remove(node);
			node.setParent(null);
			
			return this;
		}
		
		
		@XmlElementWrapper(name="children")
		@XmlElement(name="child")
		@XmlIDREF
		public ArrayList<Leaf> getChildren()
		{
			return children;
		}
		
		
		public Node setChildren(ArrayList<Leaf> children)
		{
			this.children = children;
			
			return this;
		}
		
		
		public String toString()
		{
			String result = null;
			
			if (rendering)
			{
				result = String.format("{id:%s; name: %s;}", getId(), getName());
			}
			else
			{
				rendering  = true;
				result = String.format("{id:%s; name: %s; data:%s; children:%s}", getId(), getName(), getData(), getChildren());
				rendering  = false;
			}
			
			return result;
		}
		
		
		public Node setId(String id)
		{
			return (Node) super.setId(id);
		}
		
		
		public Node setName(String name)
		{
			return (Node) super.setName(name);
		}
		
		
		public Node setData(String data)
		{
			return (Node) super.setData(data);
		}
		
		
		public Node setParent(Node parent)
		{
			return (Node) super.setParent(parent);
		}
	}
}

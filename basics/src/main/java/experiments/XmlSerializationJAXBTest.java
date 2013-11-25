package experiments;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
			jaxbContext = JAXBContext.newInstance(Nodes.class);
			xmlWriter = new StringWriter();
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(new Nodes(), xmlWriter);
			
			log.debug("{}", xmlWriter);
			
			Nodes copy = (Nodes) jaxbContext.createUnmarshaller().unmarshal(new StringReader(xmlWriter.toString()));
			
			log.debug("graph: {}", copy.getNodes().get(0));
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
	}
	
	@XmlRootElement(name="graph")
	public static class Nodes
	{
		private static Integer counter = 0;
		
		
		@XmlElement(name = "vertex")
		private static List<Node> nodes = new ArrayList<Node>();
		
		
		public static List<Node> getNodes()
		{
			return (List<Node>) nodes;
		}
		
		public static void setNodes(List<Node> nodes)
		{
			Nodes.nodes = nodes;
		}
	}
	
	
	@XmlRootElement(name="node")
	public static class Node
	{
		private Integer id;
		
		
		private String name;
		
		
		private String data;
		
		
		private Node parent = null;
		
		
		private ArrayList<Node> children = new ArrayList<Node>();


		private boolean rendering = false;
		
		
		public Node()
		{
			id = Nodes.counter++;
			Nodes.nodes.add(this);
		}


		@XmlID
		@XmlAttribute
		public String getId()
		{
			return id.toString();
		}
		
		
		public Node setId(String id)
		{
			this.id = Integer.valueOf(id);
			
			return this;
		}


		@XmlAttribute
		public String getName()
		{
			return name;
		}


		public Node setName(String name)
		{
			this.name = name;
			
			return this;
		}


		public String getData()
		{
			return data;
		}


		public Node setData(String data)
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


		public Node setParent(Node parent)
		{
			this.parent = parent;
			
			if (parent != null)
			{
				parent.addChild(this);
			}
			
			return this;
		}
		
		
		public Node addChild(Node node)
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
		
		
		public Node removeChild(Node node)
		{
			children.remove(node);
			node.setParent(null);
			
			return this;
		}
		
		
		@XmlElement(name="child")
		@XmlIDREF
		@XmlElementWrapper(name="children")
		public ArrayList<Node> getChildren()
		{
			return children;
		}
		
		
		public Node setChildren(ArrayList<Node> children)
		{
			this.children = children;
			
			return this;
		}
		
		
		public String toString()
		{
			String result = null;
			
			if (rendering)
			{
				result = String.format("{id:%d; name: %s;}", id, name);
			}
			else
			{
				rendering  = true;
				result = String.format("{id:%d; name: %s; data:%s; children:%s}", id, name, data, children);
				rendering  = false;
			}
			
			return result;
		}
	}
}

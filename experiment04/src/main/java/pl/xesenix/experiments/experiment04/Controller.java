
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtil;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
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
	private TextArea console;


	@FXML
	private AnchorPane view;


	@FXML
	void loadXml(ActionEvent event)
	{
		treeView.setRoot(getXmlAsTree(console.getText()));
	}


	@FXML
	void initialize() throws IOException
	{
		assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'app.fxml'.";
		assert treeView != null : "fx:id=\"treeView\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";

		treeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {

			public TreeCell<Object> call(TreeView<Object> param)
			{
				return new XmlTreeCell();
			}
		});

		InputStream in = getClass().getResourceAsStream("/test.xml");
		console.setText(IOUtil.toString(in));
		in.close();
	}


	public TreeItem<Object> getXmlAsTree(String source)
	{

		StringReader xmlReader = new StringReader(source);

		SAXBuilder builder = new SAXBuilder();

		try
		{
			Document document = (Document) builder.build(xmlReader);

			Element xmlNode = document.getRootElement();

			return converToTreeItem(xmlNode);
		}
		catch (JDOMException | IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * @param xmlNode
	 */
	public TreeItem<Object> converToTreeItem(Element xmlNode)
	{
		TreeItem<Object> rootNode = new TreeItem<Object>(xmlNode);

		for (Object obj : xmlNode.getAttributes())
		{
			rootNode.getChildren().add(new TreeItem<Object>(obj));
		}

		for (Object obj : xmlNode.getContent())
		{
			if (obj instanceof Text)
			{
				if (!((Text) obj).getTextTrim().isEmpty())
				{
					rootNode.getChildren().add(new TreeItem<Object>(obj));
				}
			}
			else if (obj instanceof Element)
			{
				rootNode.getChildren().add(converToTreeItem((Element) obj));
			}
		}

		return rootNode;
	}


	public static class XmlTreeCell extends TreeCell<Object>
	{
		public XmlTreeCell()
		{
			super();
		}


		protected void updateItem(Object item, boolean empty)
		{
			getStyleClass().removeAll("xml-element", "xml-attribute", "xml-text");
			
			if (empty)
			{
				setText(null);
				setGraphic(null);
			}
			else
			{
				if (item instanceof Element)
				{
					Element element = (Element) item;
					
					StringBuilder attributes = new StringBuilder();
					
					for (Object obj : element.getAttributes())
					{
						Attribute attribute = (Attribute) obj;
						
						attributes.append(" ");
						attributes.append(attribute.getName());
						attributes.append(" = \"");
						attributes.append(attribute.getValue());
						attributes.append("\"");
					}
					
					if (element.getContent().isEmpty())
					{
						setText(String.format("<%s%s/>", element.getName(), attributes));
					}
					else
					{
						setText(String.format("<%s%s>...</%s>", element.getName(), attributes, element.getName()));
					}
					
					getStyleClass().add("xml-element");
				}
				else if (item instanceof Attribute)
				{
					Attribute attribute = (Attribute) item;
					
					setText(String.format("%s = %s", attribute.getName(), attribute.getValue()));
					
					getStyleClass().add("xml-attribute");
				}
				else
				{
					Text text = (Text) item;
					
					setText(text.getTextNormalize());
					
					getStyleClass().add("xml-text");
				}
			}
			
			super.updateItem(item, empty);
		}
		
		
		public void resize(double width, double height)
		{
			super.resize(width, height);
			
			
			
		}
	}
}

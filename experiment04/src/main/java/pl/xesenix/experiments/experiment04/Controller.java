
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.apache.commons.io.IOUtil;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		
		treeView.requestFocus();
		treeView.getSelectionModel().select(0);
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
		
		treeView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event)
			{
				KeyCodeCombination expandAllKeyCombination = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHIFT_DOWN);
				KeyCodeCombination collapseAllKeyCombination = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHIFT_DOWN);
				KeyCodeCombination copyKeyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
				KeyCodeCombination pasteKeyCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
				
				int index = treeView.getSelectionModel().getSelectedIndex();
				log.debug("selected: {}", index);
				
				if (expandAllKeyCombination.match(event))
				{
					expand(treeView.getFocusModel().getFocusedItem());
				}
				else if (collapseAllKeyCombination.match(event))
				{
					collapse(treeView.getFocusModel().getFocusedItem());
				}
				else if (copyKeyCombination.match(event))
				{
					TreeItem<Object> item = treeView.getFocusModel().getFocusedItem();
					
					if (item != null)
					{
						Map<DataFormat, Object> map = new HashMap<DataFormat, Object>();
						
						Object data = item.getValue();
						
						if (data instanceof Element)
						{
							XMLOutputter xmlOut = new XMLOutputter();
							String xml = xmlOut.outputString((Element) data);
							
							log.debug("xml: {}", xml);
							
							map.put(DataFormat.HTML, xml);
							map.put(DataFormat.PLAIN_TEXT, xml);
						}
						else if (data instanceof Text)
						{
							String txt = ((Text) data).getTextTrim();
							map.put(DataFormat.PLAIN_TEXT, txt);
						}
						
						Clipboard clipboard = Clipboard.getSystemClipboard();
						clipboard.setContent(map);
					}
				}
				else if (pasteKeyCombination.match(event))
				{
					TreeItem<Object> item = treeView.getFocusModel().getFocusedItem();
					
					if (item != null)
					{
						Clipboard clipboard = Clipboard.getSystemClipboard();
						
						if (clipboard.hasContent(DataFormat.lookupMimeType("text/xml")))
						{
							String xml = (String) clipboard.getContent(DataFormat.lookupMimeType("text/xml"));
							
							TreeItem<Object> dataItem = getXmlAsTree(xml);
							
							item.getChildren().add(dataItem);
						}
						else if (clipboard.hasContent(DataFormat.HTML))
						{
							String xml = (String) clipboard.getContent(DataFormat.HTML);
							
							TreeItem<Object> dataItem = getXmlAsTree(xml);
							
							item.getChildren().add(dataItem);
						}
						else if (clipboard.hasContent(DataFormat.PLAIN_TEXT))
						{
							String xml = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
							
							TreeItem<Object> dataItem = getXmlAsTree(xml);
							
							item.getChildren().add(dataItem);
						}
					}
				}
				
				treeView.getSelectionModel().select(index);
			}
		});

		InputStream in = getClass().getResourceAsStream("/test.xml");
		console.setText(IOUtil.toString(in));
		in.close();
	}
	
	
	public static void expand(TreeItem<Object> root)
	{
		if (root != null)
		{
			for (TreeItem<Object> child : root.getChildren())
			{
				expand(child);
			}
			
			root.setExpanded(true);
		}
	}
	
	
	public static void collapse(TreeItem<Object> root)
	{
		if (root != null)
		{
			root.setExpanded(false);
			
			for (TreeItem<Object> child : root.getChildren())
			{
				collapse(child);
			}
		}
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
		
		return new TreeItem<Object>(new Text(source));
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
		private HBox editLine = new HBox();
		
		
		private Label prefixLabel = new Label();
		
		
		private Label postfixLabel = new Label();
		
		
		private TextField editField = new TextField();


		private ContextMenu textMenu;


		private ContextMenu attributeMenu;


		private ContextMenu elementMenu;


		public XmlTreeCell()
		{
			VBox.setVgrow(editField, Priority.ALWAYS);
			
			prefixLabel.getStyleClass().add("prefix-text");
			
			postfixLabel.getStyleClass().add("postfix-text");
			
			editField.getStyleClass().add("edited-text");
			
			editLine.getChildren().add(prefixLabel);
			editLine.getChildren().add(editField);
			editLine.getChildren().add(postfixLabel);
			
			editField.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event)
				{
					if (event.getCode() == KeyCode.ENTER)
					{
						commitEdit(editField.getText());
					}
					else if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN)
					{
						cancelEdit();
					}
				}
			});
		}
		
		
		public void startEdit()
		{
			super.startEdit();
			
			editField.getStyleClass().removeAll("xml-element", "xml-attribute", "xml-text");
			
			Object data = getItem();
			
			if (data instanceof Element)
			{
				prefixLabel.setText("<");
				editField.setText(((Element) data).getName());
				postfixLabel.setText("/>");
				
				editField.getStyleClass().add("xml-element");
			}
			else if (data instanceof Attribute)
			{
				prefixLabel.setText(((Attribute) data).getName() + " = ");
				editField.setText(((Attribute) data).getValue());
				postfixLabel.setText("");
				
				editField.getStyleClass().add("xml-attribute");
			}
			else
			{
				prefixLabel.setText(null);
				editField.setText(((Text) data).getText());
				postfixLabel.setText(null);
				
				editField.getStyleClass().add("xml-text");
			}
			
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			
			setGraphic(editLine);
			setText(null);
			
			Platform.runLater(new Runnable() {
				public void run()
				{
					editField.requestFocus();
					editField.selectAll();
				}
			});
		}


		public void commitEdit(Object newValue)
		{
			Object data = getItem();
			
			if (data instanceof Element)
			{
				((Element) data).setName((String) newValue);
			}
			else if (data instanceof Attribute)
			{
				((Attribute) data).setValue((String) newValue);
			}
			else
			{
				((Text) data).setText((String) newValue);
			}
			
			super.commitEdit(data);
		}


		public void cancelEdit()
		{
			prepareStaticView(getItem());
			
			super.cancelEdit();
		}


		protected void updateItem(Object data, boolean empty)
		{
			getStyleClass().removeAll("xml-element", "xml-attribute", "xml-text");
			
			if (empty)
			{
				setText(null);
				setGraphic(null);
			}
			else
			{
				prepareStaticView(data);
			}
			
			super.updateItem(data, empty);
		}
		
		
		public void prepareStaticView(Object data)
		{
			setContextMenu(null);
			setContentDisplay(ContentDisplay.LEFT);
			setGraphic(getTreeItem().getGraphic());
			
			if (data instanceof Element)
			{
				Element element = (Element) data;
				
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
				
				setContextMenu(getElementMenu());
				
				getStyleClass().add("xml-element");
			}
			else if (data instanceof Attribute)
			{
				Attribute attribute = (Attribute) data;
				
				setText(String.format("%s = %s", attribute.getName(), attribute.getValue()));
				
				setContextMenu(getAttributeMenu());
				
				getStyleClass().add("xml-attribute");
			}
			else
			{
				Text text = (Text) data;
				
				setText(text.getTextNormalize());
				
				setContextMenu(getTextMenu());
				
				getStyleClass().add("xml-text");
			}
		}
		
		
		private ContextMenu getTextMenu()
		{
			if (textMenu == null)
			{
				textMenu = new ContextMenu();
				textMenu.getItems().add(new MenuItem("delete"));
			}
			
			return textMenu;
		}


		private ContextMenu getAttributeMenu()
		{
			if (attributeMenu == null)
			{
				attributeMenu = new ContextMenu();
				attributeMenu.getItems().add(new MenuItem("delete"));
			}
			
			return attributeMenu;
		}


		private ContextMenu getElementMenu()
		{
			if (elementMenu == null)
			{
				elementMenu = new ContextMenu();
				
				MenuItem expandAll = new MenuItem("expand all");
				expandAll.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event)
					{
						expand(getTreeItem());
					}
				});
				
				MenuItem collapseAll = new MenuItem("callapse all");
				collapseAll.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event)
					{
						collapse(getTreeItem());
					}
				});
				
				elementMenu.getItems().add(expandAll);
				elementMenu.getItems().add(collapseAll);
				elementMenu.getItems().add(new MenuItem("delete"));
			}
			
			return elementMenu;
		}


		public void resize(double width, double height)
		{
			super.resize(width, height);
		}
	}
}

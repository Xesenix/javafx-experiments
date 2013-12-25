package pl.xesenix.experiments.experiment04;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Text;

public class XmlTreeCell extends TreeCell<Object>
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
		
		editLine.getStyleClass().removeAll("xml-element", "xml-attribute", "xml-text");
		
		Object data = getItem();
		
		if (data instanceof Element)
		{
			editField.setText(((Element) data).getName());
			editLine.getStyleClass().add("xml-element");
		}
		else if (data instanceof Attribute)
		{
			editField.setText(((Attribute) data).getValue());
			postfixLabel.setText("");
			
			editLine.getStyleClass().add("xml-attribute");
		}
		else
		{
			prefixLabel.setText(null);
			editField.setText(((Text) data).getText());
			postfixLabel.setText(null);
			
			editLine.getStyleClass().add("xml-text");
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
				
				prefixLabel.setText("<");
				editField.setText(element.getName());
				postfixLabel.setText(String.format("%s", attributes));
			}
			else
			{
				setText(String.format("<%s%s>...</%s>", element.getName(), attributes, element.getName()));

				prefixLabel.setText("<");
				editField.setText(element.getName());
				postfixLabel.setText(String.format("%s>...</%s>", attributes, element.getName()));
			}
			
			setContextMenu(getElementMenu());
			
			getStyleClass().add("xml-element");
		}
		else if (data instanceof Attribute)
		{
			Attribute attribute = (Attribute) data;
			
			setText(String.format("%s = %s", attribute.getName(), attribute.getValue()));
			
			prefixLabel.setText(attribute.getName() + " = ");
			editField.setText(attribute.getValue());
			postfixLabel.setText("// attribute");
			
			setContextMenu(getAttributeMenu());
			
			getStyleClass().add("xml-attribute");
		}
		else
		{
			Text text = (Text) data;
			
			setText(text.getTextNormalize());
			
			prefixLabel.setText(null);
			editField.setText(text.getTextNormalize());
			postfixLabel.setText("// text");
			
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
			elementMenu = getTreeView().getContextMenu();
			
			if (elementMenu == null) 
			{
				elementMenu = new ContextMenu();
			}
			
			elementMenu.getItems().add(new MenuItem("delete"));
		}
		
		return elementMenu;
	}


	public void resize(double width, double height)
	{
		super.resize(width, height);
	}
}
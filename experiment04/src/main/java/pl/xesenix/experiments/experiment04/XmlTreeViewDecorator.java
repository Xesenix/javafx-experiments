
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class XmlTreeViewDecorator
{
	private static final Logger log = LoggerFactory.getLogger(XmlTreeViewDecorator.class);
	
	
	public static class KeyCombinationEventHandler implements EventHandler<KeyEvent>
	{
		private final TreeView<Object> treeView;
		
		
		private final XmlTreeViewDecorator helper;


		private KeyCodeCombination expandAllKeyCombination = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHIFT_DOWN);


		private KeyCodeCombination collapseAllKeyCombination = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHIFT_DOWN);


		private KeyCodeCombination prevNodeKeyCombination = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);


		private KeyCodeCombination nextNodeKeyCombination = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);


		private KeyCodeCombination copyKeyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);


		private KeyCodeCombination pasteKeyCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);


		private KeyCombinationEventHandler(XmlTreeViewDecorator helper)
		{
			this.treeView = helper.getTreeView();
			this.helper = helper;
		}


		public void handle(KeyEvent event)
		{
			if (event.getEventType().equals(KeyEvent.KEY_PRESSED))
			{
				if (prevNodeKeyCombination.match(event))
				{
					log.debug("prev");
					helper.selectPrevSibling();
					
					event.consume();
				}
				else if (nextNodeKeyCombination.match(event))
				{
					log.debug("next");
					helper.selectNextSibling();
					
					event.consume();
				}	
			}
			else if (event.getEventType().equals(KeyEvent.KEY_RELEASED))
			{
				if (expandAllKeyCombination.match(event))
				{
					log.debug("expand");
					helper.expandAll(treeView.getSelectionModel().getSelectedItem());
					
					// treeView.getSelectionModel().select(index);
					// treeView.scrollTo(index);
				}
				else if (collapseAllKeyCombination.match(event))
				{
					log.debug("collapse");
					helper.collapseAll(treeView.getSelectionModel().getSelectedItem());
					
					// treeView.getSelectionModel().select(index);
					// treeView.scrollTo(index);
				}
				else if (copyKeyCombination.match(event))
				{
					log.debug("copy");
					helper.copySelectedItemToClipboard();
					
					// treeView.getSelectionModel().select(index);
				}
				else if (pasteKeyCombination.match(event))
				{
					log.debug("paste");
					helper.pasteItemFromClipboard();
					
					// treeView.getSelectionModel().select(index);
				}
			}
		}
	}
	
	
	private TreeView<Object> treeView;


	public TreeView<Object> getTreeView()
	{
		return treeView;
	}


	public void setTreeView(TreeView<Object> treeView)
	{
		this.treeView = treeView;
	}


	public XmlTreeViewDecorator(final TreeView<Object> treeView)
	{
		setTreeView(treeView);
		treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		treeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {

			public TreeCell<Object> call(TreeView<Object> param)
			{
				return new XmlTreeCell();
			}
		});
		
		treeView.addEventFilter(KeyEvent.ANY, new KeyCombinationEventHandler(this));
		treeView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event)
			{
				log.debug("filter {}", event);
				log.debug("{}", treeView.getSelectionModel().getSelectedIndex());
				log.debug("{}", treeView.getFocusModel().getFocusedIndex());
			}
			
		});
		
		treeView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event)
			{
				log.debug("handler {}", event);
				log.debug("{}", treeView.getSelectionModel().getSelectedIndex());
				log.debug("{}", treeView.getFocusModel().getFocusedIndex());
				
				// fix
				treeView.getFocusModel().focus(treeView.getSelectionModel().getSelectedIndex());
			}
			
		});
	}
	
	
	protected void selectNextSibling()
	{
		TreeItem<Object> root = getTreeView().getSelectionModel().getSelectedItem();
		
		if (root != null)
		{
			TreeItem<Object> parent = (TreeItem<Object>) root.getParent();
			
			if (parent != null)
			{
				int index = parent.getChildren().indexOf(root);
				
				if (index < parent.getChildren().size() - 1)
				{
					TreeItem<Object> next = parent.getChildren().get(index + 1);
					int nextIndex = getTreeView().getRow(next);
					
					getTreeView().getSelectionModel().select(nextIndex);
					getTreeView().scrollTo(nextIndex);
				}
			}
		}
	}


	protected void selectPrevSibling()
	{
		TreeItem<Object> root = getTreeView().getSelectionModel().getSelectedItem();
		
		if (root != null)
		{
			TreeItem<Object> parent = (TreeItem<Object>) root.getParent();
			
			if (parent != null)
			{
				int index = parent.getChildren().indexOf(root);
				
				if (index > 0)
				{
					TreeItem<Object> prev = parent.getChildren().get(index - 1);
					int prevIndex = getTreeView().getRow(prev);
					
					getTreeView().getSelectionModel().select(prevIndex);
					getTreeView().scrollTo(prevIndex);
				}
			}
		}
	}


	public void expandAll(TreeItem<Object> root)
	{
		if (root != null)
		{
			for (TreeItem<Object> child : root.getChildren())
			{
				expandAll(child);
			}
			
			root.setExpanded(true);
		}
	}
	
	
	public void collapseAll(TreeItem<Object> root)
	{
		if (root != null)
		{
			root.setExpanded(false);
			
			for (TreeItem<Object> child : root.getChildren())
			{
				collapseAll(child);
			}
		}
	}


	public void copySelectedItemToClipboard()
	{
		TreeItem<Object> selectedItem = treeView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null)
		{
			Map<DataFormat, Object> map = new HashMap<DataFormat, Object>();
			
			Object data = selectedItem.getValue();
			
			if (data instanceof Element)
			{
				XMLOutputter xmlOut = new XMLOutputter();
				String xml = xmlOut.outputString((Element) data);
				
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


	public void pasteItemFromClipboard()
	{
		TreeItem<Object> selectedItem = treeView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null)
		{
			Clipboard clipboard = Clipboard.getSystemClipboard();
			
			if (clipboard.hasContent(DataFormat.lookupMimeType("text/xml")))
			{
				String xml = (String) clipboard.getContent(DataFormat.lookupMimeType("text/xml"));
				
				TreeItem<Object> dataItem = getXmlAsTree(xml);
				
				selectedItem.getChildren().add(dataItem);
			}
			else if (clipboard.hasContent(DataFormat.HTML))
			{
				String xml = (String) clipboard.getContent(DataFormat.HTML);
				
				TreeItem<Object> dataItem = getXmlAsTree(xml);
				
				selectedItem.getChildren().add(dataItem);
			}
			else if (clipboard.hasContent(DataFormat.PLAIN_TEXT))
			{
				String xml = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
				
				TreeItem<Object> dataItem = getXmlAsTree(xml);
				
				selectedItem.getChildren().add(dataItem);
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
}

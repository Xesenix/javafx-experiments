
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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


		private KeyCodeCombination editKeyCombination = new KeyCodeCombination(KeyCode.ENTER);


		private KeyCodeCombination clearSelectionKeyCombination = new KeyCodeCombination(KeyCode.ESCAPE);


		private KeyCodeCombination expandAllKeyCombination = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN);


		private KeyCodeCombination collapseAllKeyCombination = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN);


		private KeyCodeCombination expandKeyCombination = new KeyCodeCombination(KeyCode.RIGHT);


		private KeyCodeCombination collapseKeyCombination = new KeyCodeCombination(KeyCode.LEFT);


		private KeyCodeCombination prevSiblingKeyCombination = new KeyCodeCombination(KeyCode.UP, KeyCombination.ALT_DOWN);


		private KeyCodeCombination nextSiblingKeyCombination = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.ALT_DOWN);


		private KeyCodeCombination prevRowKeyCombination = new KeyCodeCombination(KeyCode.UP);


		private KeyCodeCombination nextRowKeyCombination = new KeyCodeCombination(KeyCode.DOWN);


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
				if (prevSiblingKeyCombination.match(event))
				{
					log.debug("prev-sibling");
					helper.focusPrevSibling();
					
					event.consume();
				}
				else if (nextSiblingKeyCombination.match(event))
				{
					log.debug("next-sibling");
					helper.focusNextSibling();
					
					event.consume();
				}
				else if (prevRowKeyCombination.match(event))
				{
					log.debug("prev");
					helper.focusPrevRow();
					
					event.consume();
				}
				else if (nextRowKeyCombination.match(event))
				{
					log.debug("next");
					helper.focusNextRow();
					
					event.consume();
				}
				else if (expandKeyCombination.match(event))
				{
					log.debug("expand");
					helper.expandFocused();
					
					event.consume();
				}
				else if (collapseKeyCombination.match(event))
				{
					log.debug("collapse");
					helper.collapseFocused();
					
					event.consume();
				}
				else if (editKeyCombination.match(event))
				{
					log.debug("edit");
					
					if (helper.editFocused())
					{
						event.consume();
					}
				}
				else if (clearSelectionKeyCombination.match(event))
				{
					log.debug("clear-selection");
					
					helper.clearSelection();
				}
			}
			else if (event.getEventType().equals(KeyEvent.KEY_RELEASED))
			{
				if (expandAllKeyCombination.match(event))
				{
					log.debug("expand-all");
					helper.expandAllSelected();
					
					// treeView.getSelectionModel().select(index);
					// treeView.scrollTo(index);
				}
				else if (collapseAllKeyCombination.match(event))
				{
					log.debug("collapse-all");
					helper.collapseAllSelected();
					
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


	public void clearSelection()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		treeView.getSelectionModel().clearSelection();
		treeView.getFocusModel().focus(focusedItemIndex);
	}


	public boolean editFocused()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		
		if (focusedItem != null && !focusedItem.equals(treeView.getEditingItem()))
		{
			treeView.edit(focusedItem);
			
			return true;
		}
		
		return false;
	}


	public void focusNextRow()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		treeView.getFocusModel().focus(focusedItemIndex + 1);
		
		if (treeView.getFocusModel().getFocusedIndex() == -1)
		{
			treeView.getFocusModel().focus(focusedItemIndex);
		}
		
		treeView.scrollTo(treeView.getFocusModel().getFocusedIndex());
	}


	public void focusPrevRow()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		treeView.getFocusModel().focus(focusedItemIndex - 1);
		
		if (treeView.getFocusModel().getFocusedIndex() == -1)
		{
			treeView.getFocusModel().focus(focusedItemIndex);
		}
		
		treeView.scrollTo(treeView.getFocusModel().getFocusedIndex());
	}
	
	
	protected void focusNextSibling()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		
		if (focusedItem != null)
		{
			TreeItem<Object> next = focusedItem.nextSibling();
			
			if (next != null)
			{
				int nextIndex = treeView.getRow(next);
				
				treeView.getFocusModel().focus(nextIndex);
				treeView.scrollTo(nextIndex);
			}
		}
	}


	protected void focusPrevSibling()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		
		if (focusedItem != null)
		{
			TreeItem<Object> prev = focusedItem.previousSibling();
			
			if (prev != null)
			{
				int prevIndex = treeView.getRow(prev);
				
				treeView.getFocusModel().focus(prevIndex);
				treeView.scrollTo(prevIndex);
			}
		}
	}


	public void expandFocused()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		if (focusedItem != null)
		{
			expandLevel(focusedItem);
			treeView.getFocusModel().focus(focusedItemIndex);
		}
	}
	
	
	public void expandLevel(TreeItem<Object> root)
	{
		if (root != null)
		{
			if (!root.isExpanded())
			{
				root.setExpanded(true);
			}
			else
			{
				for (TreeItem<Object> child : root.getChildren())
				{
					expandLevel(child);
				}
			}
		}
	}


	public void collapseFocused()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		if (focusedItem != null)
		{
			collapseLevel(focusedItem);
			treeView.getFocusModel().focus(focusedItemIndex);
		}
	}
	
	
	public void collapseLevel(TreeItem<Object> root)
	{
		if (root != null)
		{
			boolean anyExpanded = false;
			
			for (TreeItem<Object> child : root.getChildren())
			{
				anyExpanded |= child.isExpanded();
			}
			
			if (anyExpanded)
			{
				for (TreeItem<Object> child : root.getChildren())
				{
					collapseLevel(child);
				}
			}
			else
			{
				root.setExpanded(false);
			}
		}
	}


	public void expandAllSelected()
	{
		List<TreeItem<Object>> selected = new ArrayList<TreeItem<Object>>(treeView.getSelectionModel().getSelectedItems());
		
		log.debug("selected {}", selected);
		log.debug("selected {}", treeView.getSelectionModel().getSelectedIndex());
		
		for (TreeItem<Object> selectedItem : selected)
		{
			expandAll(selectedItem);
		}
		
		for (TreeItem<Object> selectedItem : selected)
		{
			treeView.getSelectionModel().select(selectedItem);
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


	public void collapseAllSelected()
	{
		List<TreeItem<Object>> selected = new ArrayList<TreeItem<Object>>(treeView.getSelectionModel().getSelectedItems());
		
		log.debug("selected {}", selected);
		
		for (TreeItem<Object> selectedItem : selected)
		{
			collapseAll(selectedItem);
		}
		
		log.debug("selected {}", selected);
		
		for (TreeItem<Object> selectedItem : selected)
		{
			treeView.getSelectionModel().select(selectedItem);
			//selectedItem.setExpanded(false);
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

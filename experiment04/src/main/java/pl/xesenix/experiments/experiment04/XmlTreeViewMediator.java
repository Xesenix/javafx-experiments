
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlTreeViewMediator
{
	static final Logger log = LoggerFactory.getLogger(XmlTreeViewMediator.class);
	
	
	private TreeView<Object> treeView;
	
	
	private Map<TreeItem<Object>, Integer> nodeDepthMap = new HashMap<TreeItem<Object>, Integer>();


	private Map<TreeItem<Object>, Integer> nodeLevelMap = new HashMap<TreeItem<Object>, Integer>();


	public TreeView<Object> getTreeView()
	{
		return treeView;
	}


	public void setTreeView(TreeView<Object> treeView)
	{
		this.treeView = treeView;
	}

	public XmlTreeViewMediator(final TreeView<Object> treeView)
	{
		setTreeView(treeView);
		treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		treeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {

			public TreeCell<Object> call(TreeView<Object> param)
			{
				return new XmlTreeCell();
			}
		});
		
		treeView.addEventFilter(KeyEvent.ANY, new XmlTreeViewKeyboardController(this));
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
				//treeView.getFocusModel().focus(treeView.getSelectionModel().getSelectedIndex());
			}
			
		});
	}


	public void clearSelection()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		treeView.getSelectionModel().clearSelection();
		treeView.getFocusModel().focus(focusedItemIndex);
	}


	public void setFocussedAsSelection()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		if (focusedItemIndex >= 0)
		{
			treeView.getSelectionModel().clearAndSelect(focusedItemIndex);
		}
	}


	public void selectFocussed()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		
		if (focusedItem != null)
		{
			treeView.getSelectionModel().select(focusedItem);
		}
	}


	public void deselectFocussed()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		
		if (focusedItem != null)
		{
			Object[] selected = treeView.getSelectionModel().getSelectedItems().toArray();
			
			treeView.getSelectionModel().clearSelection();
			
			for (int i = 0; i < selected.length; i++)
			{
				if (!focusedItem.equals(selected[i]))
				{
					treeView.getSelectionModel().select((TreeItem<Object>) selected[i]);
				}
			}
			
			treeView.getFocusModel().focus(focusedItemIndex);
		}
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
	
	
	public int getExpandedDepth(TreeItem<Object> root)
	{
		Integer cached = nodeDepthMap.get(root);
		
		if (cached == null)
		{
			int depth = 0;
			
			if (root.isExpanded())
			{
				for (TreeItem<Object> child : root.getChildren())
				{
					int nodeDepth = getExpandedDepth(child) + 1;
					
					if (depth < nodeDepth)
					{
						depth = nodeDepth;
					}
				}
			}
			
			cached = depth;
			
			nodeDepthMap.put(root, cached);
		}
		
		return cached;
	}
	
	
	public int getNodeLevel(TreeItem<Object> node)
	{
		Integer cached = nodeLevelMap.get(node);
		
		if (cached == null)
		{
			int level = 0;
			
			TreeItem<Object> parent = node.getParent();
			
			if (parent != null)
			{
				level = getNodeLevel(parent) + 1;
			}
			
			cached = level;
			
			nodeDepthMap.put(node, cached);
		}
		
		return cached;
	}
	
	
	public void expandLevel(TreeItem<Object> root, int level)
	{
		if (root != null)
		{
			boolean expand = level == -1 || getNodeLevel(root) < level;
			
			root.setExpanded(expand);
			
			if (expand)
			{
				for (TreeItem<Object> child : root.getChildren())
				{
					expandLevel(child, level);
				}
			}
		}
	}


	public void expandFocussed()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		// clear depth cache
		nodeDepthMap.clear();
		
		if (focusedItem != null)
		{
			int currentDepth = getExpandedDepth(focusedItem) + getNodeLevel(focusedItem) + 1;
			
			expandLevel(focusedItem, currentDepth);
			treeView.getFocusModel().focus(focusedItemIndex);
		}
	}


	public void collapseFocussed()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		// clear depth cache
		nodeDepthMap.clear();
		
		if (focusedItem != null)
		{
			int currentDepth = getExpandedDepth(focusedItem) + getNodeLevel(focusedItem) - 1;
			
			if (currentDepth >= 0)
			{
				expandLevel(focusedItem, currentDepth);
				treeView.getFocusModel().focus(focusedItemIndex);
			}
		}
	}


	public void expandAllFocussed()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		// clear depth cache
		nodeDepthMap.clear();
		
		if (focusedItem != null)
		{
			expandLevel(focusedItem, -1);
			treeView.getFocusModel().focus(focusedItemIndex);
		}
	}


	public void collapseAllFocussed()
	{
		TreeItem<Object> focusedItem = treeView.getFocusModel().getFocusedItem();
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		// clear depth cache
		nodeDepthMap.clear();
		
		if (focusedItem != null)
		{
			expandLevel(focusedItem, 0);
			treeView.getFocusModel().focus(focusedItemIndex);
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

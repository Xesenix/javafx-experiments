
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeView.EditEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.XPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.collections.NonIterableChange.SimplePermutationChange;


public class XmlTreeViewMediator
{
	static final Logger log = LoggerFactory.getLogger(XmlTreeViewMediator.class);


	private TreeView<Object> treeView;


	private Map<TreeItem<Object>, Integer> nodeDepthMap = new HashMap<TreeItem<Object>, Integer>();


	private Map<TreeItem<Object>, Integer> nodeLevelMap = new HashMap<TreeItem<Object>, Integer>();
	
	
	private WeakHashMap<Object, XmlItem> elementToItemMap = new WeakHashMap<Object, XmlItem>();


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
		treeView.setShowRoot(false);
		treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		treeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {

			public TreeCell<Object> call(TreeView<Object> param)
			{
				return new XmlTreeCell();
			}
		});

		treeView.addEventFilter(KeyEvent.ANY, new XmlTreeViewKeyboardController(this));
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
	
	public void toggleSelectionOnFocused()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();
		
		if (treeView.getSelectionModel().isSelected(focusedItemIndex))
		{
			treeView.getSelectionModel().clearSelection(focusedItemIndex);
		}
		else
		{
			treeView.getSelectionModel().select(focusedItemIndex);
		}
	}


	public void selectFocussed()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();

		treeView.getSelectionModel().select(focusedItemIndex);
	}


	public void deselectFocussed()
	{
		int focusedItemIndex = treeView.getFocusModel().getFocusedIndex();

		treeView.getSelectionModel().clearSelection(focusedItemIndex);
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


	public void deleteSelected()
	{
		List<TreeItem<Object>> itemsToRemove = new ArrayList<TreeItem<Object>>();

		for (TreeItem<Object> selectedItem : treeView.getSelectionModel().getSelectedItems())
		{
			itemsToRemove.add(selectedItem);
		}

		for (TreeItem<Object> item : itemsToRemove)
		{
			TreeItem<Object> parent = item.getParent();

			if (parent != null)
			{
				//parent.getChildren().remove(item);
				
				Element parentElement = (Element) parent.getValue();
				
				if (item.getValue() instanceof Attribute)
				{
					parentElement.removeAttribute((Attribute) item.getValue());
				}
				else
				{
					parentElement.removeContent((Content) item.getValue());
				}
			}
		}
		
		commitXmlChange();
	}


	public void commitXmlChange()
	{
		TreeItem<Object> rootItem = treeView.getRoot();
		Element root = null;
		
		if (rootItem != null)
		{
			root = (Element) rootItem.getValue();
		}
		
		List<String> expanded = collectExpanded(rootItem);
		
		rebuildSubtree(rootItem, root);
		
		expandFromList(rootItem, expanded);
	}


	public void expandFromList(TreeItem<Object> rootItem, List<String> expanded)
	{
		for (String path : expanded)
		{
			XPathExpression<Element> xpath = XPathFactory.instance().compile(path, Filters.element());
			
			for (Element expandedElement : xpath.evaluate(treeView.getRoot().getValue()))
			{
				elementToItemMap.get(expandedElement).setExpanded(true);
			}
		}
	}


	public List<String> collectExpanded(TreeItem<Object> collectedItem)
	{
		List<String> expanded = new ArrayList<String>();
		
		if (collectedItem.getValue() instanceof Element)
		{
			for (TreeItem<Object> item : collectedItem.getChildren())
			{
				expanded.addAll(collectExpanded(item));
			}
			
			if (collectedItem.isExpanded())
			{
				expanded.add(XPathHelper.getAbsolutePath((Content) collectedItem.getValue()));
			}
		}
		
		return expanded;
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
			else if (data instanceof Attribute)
			{
				DataFormat df = DataFormat.lookupMimeType("text/attribute");
				
				if (df == null)
				{
					df = new DataFormat("text/attribute");
				}
				
				String txt = ((Attribute) data).getName() + "=\"" + ((Attribute) data).getValue() + "\"";
				map.put(df, txt);
				map.put(DataFormat.PLAIN_TEXT, txt);
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


	public String copyElementToString()
	{
		String result = "";
		
		/*if (data instanceof Element)
		{
			XMLOutputter xmlOut = new XMLOutputter();
			String xml = xmlOut.outputString((Element) data);
		}*/

		return result;
	}


	public void pasteItemFromClipboard()
	{
		TreeItem<Object> selectedItem = treeView.getSelectionModel().getSelectedItem();

		if (selectedItem != null)
		{
			Element parent = (Element) selectedItem.getValue();
			Clipboard clipboard = Clipboard.getSystemClipboard();

			if (clipboard.hasContent(DataFormat.PLAIN_TEXT))
			{
				String xml = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);

				updateTree(xml, selectedItem);
			}
		}
	}


	public void updateTree(String source, TreeItem<Object> updatedItem)
	{
		Pattern xmlPattern = Pattern.compile("(<[a-zA-Z]+)");
		Matcher matcher = xmlPattern.matcher(source);
		
		if (matcher.find())
		{
			// detected xml string
			// add root element
			source = source.replaceFirst("(<[a-zA-Z]+)", "<root>$1") + "</root>";
			
			StringReader xmlReader = new StringReader(source);
			SAXBuilder builder = new SAXBuilder();
			
			try
			{
				// parse source as xml with additional root element
				Document document = (Document) builder.build(xmlReader);
				
				if (updatedItem == null)
				{
					// convert to tree and put it all in tree view
					updatedItem = convertElementToTreeItem(document.getRootElement());
					
					treeView.setRoot(updatedItem);
				}
				else
				{
					// appending existing item
					if (updatedItem.getValue() instanceof Element)
					{
						Element element = (Element) updatedItem.getValue();
						
						// rewrite encapsulated content to target item element
						for (Object obj : document.getRootElement().getContent())
						{
							element.addContent((Content) ((Content) obj).clone());
						}
						
						rebuildSubtree(updatedItem, element);
					}
				}
			}
			catch (JDOMException | IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			// handling source that isn`t xml
			
			if (updatedItem == null)
			{
				// tree is empty create root and add source as it content
				
				Element root = new Element("root");
				
				root.addContent(source);
				
				treeView.setRoot(convertElementToTreeItem(root));
			}
			else if (updatedItem.getValue() instanceof Element)
			{
				// adding to existing node
				
				Element newElementValue = (Element) updatedItem.getValue();
				
				Pattern attributePattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9\\-_]*)=\"([^\"]*)\"");
				matcher = attributePattern.matcher(source);
				
				// check if passed in data isn`t list of attributes
				if (matcher.matches())
				{
					// rewrite attributes to element
					while (matcher.find())
					{
						if (updatedItem != null && updatedItem.getValue() instanceof Element)
						{
							newElementValue.setAttribute(matcher.group(1), matcher.group(2));
						}
					}
				}
				else
				{
					// add source as text content
					newElementValue.addContent(source);
				}
				
				rebuildSubtree(updatedItem, newElementValue);
			}
		}
	}


	/**
	 * @param updatedItem
	 * @param element
	 */
	public void rebuildSubtree(TreeItem<Object> updatedItem, Element element)
	{
		// recreate subtree 
		TreeItem<Object> parentItem = updatedItem.getParent();
		
		if (parentItem == null)
		{
			treeView.setRoot(convertElementToTreeItem(element));
		}
		else
		{
			int index = parentItem.getChildren().indexOf(updatedItem);
			
			parentItem.getChildren().set(index, convertElementToTreeItem(element));
		}
	}


	/**
	 * @param xmlElement
	 */
	public XmlItem convertElementToTreeItem(Object xmlElement)
	{
		XmlItem treeViewElementItem = new XmlItem(xmlElement);
		
		elementToItemMap.put(xmlElement, treeViewElementItem);
		
		if (xmlElement instanceof Element)
		{
			for (Object attribute : ((Element) xmlElement).getAttributes())
			{
				XmlItem treeViewAttributeItem = new XmlItem(attribute);
				
				elementToItemMap.put(attribute, treeViewAttributeItem);
				
				treeViewElementItem.getChildren().add(treeViewAttributeItem);
			}
			
			for (Object obj : ((Element) xmlElement).getContent())
			{
				if (obj instanceof Text)
				{
					if (!((Text) obj).getTextTrim().isEmpty())
					{
						XmlItem treeViewTextItem = new XmlItem(obj);
						
						elementToItemMap.put(obj, treeViewTextItem);
						
						treeViewElementItem.getChildren().add(treeViewTextItem);
					}
				}
				else if (obj instanceof Element)
				{
					treeViewElementItem.getChildren().add(convertElementToTreeItem(obj));
				}
			}
		}

		return treeViewElementItem;
	}
}

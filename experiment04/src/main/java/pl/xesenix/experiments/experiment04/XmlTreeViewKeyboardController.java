package pl.xesenix.experiments.experiment04;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class XmlTreeViewKeyboardController implements EventHandler<KeyEvent>
{
	private final XmlTreeViewMediator mediator;


	private KeyCodeCombination editKeyCombination = new KeyCodeCombination(KeyCode.ENTER);


	private KeyCodeCombination deleteKeyCombination = new KeyCodeCombination(KeyCode.DELETE);


	private KeyCodeCombination clearSelectionKeyCombination = new KeyCodeCombination(KeyCode.ESCAPE);


	private KeyCodeCombination selectCurrentKeyCombination = new KeyCodeCombination(KeyCode.SPACE);


	// private KeyCodeCombination toggleSelectionOnFocussedKeyCombination = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);


	// private KeyCodeCombination addToSelectionCurrentFocussedKeyCombination = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);


	private KeyCodeCombination deselectCurrentKeyCombination = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN);


	private KeyCodeCombination expandAllKeyCombination = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.ALT_DOWN);


	private KeyCodeCombination collapseAllKeyCombination = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.ALT_DOWN);


	private KeyCodeCombination expandKeyCombination = new KeyCodeCombination(KeyCode.RIGHT);


	private KeyCodeCombination collapseKeyCombination = new KeyCodeCombination(KeyCode.LEFT);


	private KeyCodeCombination prevSiblingKeyCombination = new KeyCodeCombination(KeyCode.UP, KeyCombination.ALT_DOWN);


	private KeyCodeCombination nextSiblingKeyCombination = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.ALT_DOWN);


	private KeyCodeCombination prevRowKeyCombination = new KeyCodeCombination(KeyCode.UP);


	private KeyCodeCombination nextRowKeyCombination = new KeyCodeCombination(KeyCode.DOWN);


	private KeyCodeCombination copyKeyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);


	private KeyCodeCombination pasteKeyCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);


	XmlTreeViewKeyboardController(XmlTreeViewMediator mediator)
	{
		this.mediator = mediator;
	}


	public void handle(KeyEvent event)
	{
		// if something is editing leave keyboard handling to tree cell
		if (mediator.getTreeView().getEditingItem() != null)
		{
			return;
		}
		
		// to override any native control uncomment
		// event.consume();
		
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED))
		{
			if (prevSiblingKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("prev-sibling");
				mediator.focusPrevSibling();
				
				event.consume();
			}
			else if (nextSiblingKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("next-sibling");
				mediator.focusNextSibling();
				
				event.consume();
			}
			else if (prevRowKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("prev");
				mediator.focusPrevRow();
				
				event.consume();
			}
			else if (nextRowKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("next");
				mediator.focusNextRow();
				
				event.consume();
			}
			else if (expandKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("expand");
				mediator.expandFocussed();
				
				event.consume();
			}
			else if (collapseKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("collapse");
				mediator.collapseFocussed();
				
				event.consume();
			}
			else if (clearSelectionKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("clear-selection");
				mediator.clearSelection();
			}
			else if (editKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("edit");
				
				if (mediator.editFocused())
				{
					event.consume();
				}
			}
		}
		else if (event.getEventType().equals(KeyEvent.KEY_RELEASED))
		{
			if (expandAllKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("expand-all");
				mediator.expandAllFocussed();
			}
			else if (collapseAllKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("collapse-all");
				mediator.collapseAllFocussed();
			}
			else if (selectCurrentKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("select");
				mediator.setFocussedAsSelection();
				
				event.consume();
			}
			/* 
			else if (addToSelectionCurrentFocussedKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("add-to-selection");
				mediator.selectFocussed();
				
				event.consume();
			}
			else if (deselectCurrentKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("deselect");
				mediator.deselectFocussed();
				
				event.consume();
			}*/
			else if (deleteKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("delete");
				mediator.deleteSelected();
			}
			else if (copyKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("copy");
				mediator.copySelectedItemToClipboard();
			}
			else if (pasteKeyCombination.match(event))
			{
				XmlTreeViewMediator.log.debug("paste");
				mediator.pasteItemFromClipboard();
			}
		}
	}
}
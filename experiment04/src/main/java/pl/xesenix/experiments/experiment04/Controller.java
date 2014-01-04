
package pl.xesenix.experiments.experiment04;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import org.apache.commons.io.IOUtil;
import org.jdom.Document;
import org.jdom.JDOMException;
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


	private XmlTreeViewMediator xmlTreeViewDecorator;


	@FXML
	void loadXml(ActionEvent event)
	{
		xmlTreeViewDecorator.loadXml(console.getText());
		
		treeView.requestFocus();
		treeView.getSelectionModel().select(0);
	}


	@FXML
	void initialize() throws IOException
	{
		assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'app.fxml'.";
		assert treeView != null : "fx:id=\"treeView\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";
		
		xmlTreeViewDecorator = new XmlTreeViewMediator(treeView);
		
		InputStream in = getClass().getResourceAsStream("/test.xml");
		console.setText(IOUtil.toString(in));
		in.close();
	}
}

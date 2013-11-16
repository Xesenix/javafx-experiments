
package pl.xesenix.experiments.experiment03;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment03.components.viewport.Viewport;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Controller
{
	private static final Logger log = LoggerFactory.getLogger(Controller.class);


	@FXML
	private ResourceBundle resources;


	@FXML
	private URL location;


	@FXML
	private AnchorPane view;


	@FXML
	private Viewport viewport;


	@FXML
	private TextArea console;


	private Graph<String, String> graph;


	@FXML
	void initialize()
	{
		log.debug("controller initializing");

		assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";
		assert viewport != null : "fx:id=\"viewport\" was not injected: check your FXML file 'app.fxml'.";
		
		viewport.addNodeToCanvas(new Circle(30, Color.web("#def")));

		String[] npcActions = new String[] {
			"Welcome what do yo want to do?",
			"I have nothing to sell.",
			"I know someone who may help you."
		};
		
		String[] playerActions = new String[] {
			"I want to buy some things",
			"I want to go for an adventure"
		};
		
		graph = new SparseMultigraph<String, String>();

		graph.addVertex(npcActions[0]);
		graph.addVertex(npcActions[1]);
		graph.addVertex(npcActions[2]);

		graph.addEdge(playerActions[0], npcActions[0], npcActions[1]);
		graph.addEdge(playerActions[1], npcActions[0], npcActions[2]);

		console.setText(graph.toString());
	}

}

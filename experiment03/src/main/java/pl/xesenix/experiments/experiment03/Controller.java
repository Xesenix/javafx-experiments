
package pl.xesenix.experiments.experiment03;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment03.components.viewport.Viewport;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


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


	private HashMap<String, NodeView> map = new HashMap<String, NodeView>();


	private Graph<String, String> graph;


	@FXML
	void initialize()
	{
		log.debug("controller initializing");

		assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";
		assert viewport != null : "fx:id=\"viewport\" was not injected: check your FXML file 'app.fxml'.";

		String[] npcActions = new String[] { "Welcome what do yo want to do?", "I have nothing to sell.",
			"I know someone who may help you." };

		String[] playerActions = new String[] { "I want to buy some things", "I want to go for an adventure" };

		graph = new SparseMultigraph<String, String>();

		graph.addVertex(npcActions[0]);
		graph.addVertex(npcActions[1]);
		graph.addVertex(npcActions[2]);

		graph.addEdge(playerActions[0], npcActions[0], npcActions[1]);
		graph.addEdge(playerActions[1], npcActions[0], npcActions[2]);

		console.setText(graph.toString());

		Layout<String, String> layout = new CircleLayout<String, String>(graph);

		layout.setSize(new Dimension(800, 600));
		layout.initialize();

		log.debug("{}", layout.transform(npcActions[0]));
		log.debug("{}", layout.transform(npcActions[1]));
		log.debug("{}", layout.transform(npcActions[2]));

		for (String string : npcActions)
		{
			viewport.addNodeToCanvas(getNodeFor(string, layout));
		}

		for (String string : playerActions)
		{
			viewport.addNodeToCanvas(getEdgeFor(string, layout));
		}
	}


	private Node getNodeFor(String vertice, Layout<String, String> layout)
	{
		Point2D point = layout.transform(vertice);
		NodeView pane;
		
		if (!map.containsKey(vertice))
		{
			pane = new NodeView(vertice);

			map.put(vertice, pane);
		}
		else
		{
			pane = map.get(vertice);
		}
		
		pane.setLayoutX(point.getX());
		pane.setLayoutY(point.getY());

		return pane;
	}


	private Node getEdgeFor(String vertice, Layout<String, String> layout)
	{
		Point2D point = layout.transform(vertice);

		Node node = new Circle(point.getX(), point.getY(), 20);

		return node;
	}


	public class NodeView extends Pane
	{
		public NodeView(String label)
		{
			TextArea text = new TextArea();

			text.setPrefColumnCount(20);
			text.setPrefRowCount(3);
			text.setText(label);

			getChildren().add(text);
		}
	}
}

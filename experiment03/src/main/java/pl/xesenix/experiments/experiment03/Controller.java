
package pl.xesenix.experiments.experiment03;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment03.components.viewport.Viewport;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.util.Pair;


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


	private HashMap<Say, NodeView> map = new HashMap<Say, NodeView>();


	private DelegateTree<Say, ICondition> graph;


	@FXML
	void initialize()
	{
		log.debug("controller initializing");

		assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";
		assert viewport != null : "fx:id=\"viewport\" was not injected: check your FXML file 'app.fxml'.";

		Actor[] npcs = new Actor[] { new Actor("Sam", Color.web("#0f0")), new Actor("Tom", Color.web("#080")) };
		Actor[] team = new Actor[] { new Actor("Xesenix", Color.web("#f00")), new Actor("Hordrak", Color.web("#00f")) };

		Say[] npcActions = new Say[] {
			new Say(npcs[0], "Welcome what do yo want to do?"),
			new Say(npcs[0], "I have nothing to sell."),
			new Say(npcs[1], "I know someone who may help you. What do you want to do exacly?"),
			new Say(npcs[0], "I know elven hunter should I arrange meeting?"),
			new Say(npcs[0], "I heard rummors about ancient tomb deep in forest, maybe you want to check it?") };

		Say[] playerActions = new Say[] {
			new Say(team[0], "I want to buy some things"),
			new Say(team[1], "I want to go for an adventure"),
			new Say(team[1], "I need some wolf leather"),
			new Say(team[0], "Do you know where can i get some better equipment?"),
			new Say(team[1], "Good idea i need some armor.")
		};

		graph = new DelegateTree<Say, ICondition>();

		graph.addVertex(npcActions[0]);
		
		addRelation(npcActions[0], playerActions[0]);
		addRelation(npcActions[0], playerActions[1]);
		addRelation(playerActions[0], npcActions[1]);
		addRelation(playerActions[1], npcActions[2]);
		addRelation(npcActions[2], playerActions[2]);
		addRelation(npcActions[2], playerActions[3]);
		addRelation(playerActions[2], npcActions[3]);
		addRelation(playerActions[3], playerActions[4]);
		addRelation(playerActions[4], npcActions[4]);

		console.setText(graph.toString());

		Layout<Say, ICondition> layout = new RadialTreeLayout<Say, ICondition>((Forest<Say, ICondition>) graph);
		layout.setSize(new Dimension(800, 600));

		for (Say action : graph.getVertices())
		{
			viewport.addNodeToCanvas(getNodeFor(action, layout));
		}
		
		for (ICondition condition : graph.getEdges())
		{
			Node node = getEdgeFor(condition, layout);
			viewport.addNodeToCanvas(node);
		}
	}


	private Node getEdgeFor(ICondition condition, Layout<Say, ICondition> layout)
	{
		Pair<Say> pair = graph.getEndpoints(condition);
		
		final NodeView a = getNodeFor(pair.getFirst(), layout);
		final NodeView b = getNodeFor(pair.getSecond(), layout);
		
		Group group = new Group();
		
		Group arrow = new Group();
		
		Rectangle arrowBounds = new Rectangle(80, 10);
		arrowBounds.setLayoutX(-40);
		arrowBounds.setLayoutY(-5);
		arrowBounds.setOpacity(0);
		
		Polygon arrowGlyph = new Polygon(0, 0, -20, 5, -15, 0, -20, -5);
		arrowGlyph.setLayoutX(-15);
		
		arrow.getChildren().add(arrowBounds);
		arrow.getChildren().add(arrowGlyph);
		
		Path path = new Path();
		MoveTo move = new MoveTo();
		LineTo line = new LineTo();
		
		arrow.setLayoutX(10);
		arrow.layoutXProperty().bind(b.layoutXProperty());
		arrow.layoutYProperty().bind(b.layoutYProperty());
		arrow.rotateProperty().bind(new DoubleBinding() {

			protected double computeValue()
			{
				return 180f * Math.atan2(b.layoutYProperty().get() - a.layoutYProperty().get(), b.layoutXProperty().get() - a.layoutXProperty().get()) / Math.PI;
			}
		});
		
		move.xProperty().bind(a.layoutXProperty());
		move.yProperty().bind(a.layoutYProperty());
		
		line.xProperty().bind(b.layoutXProperty());
		line.yProperty().bind(b.layoutYProperty());
		
		path.getElements().add(move);
		path.getElements().add(line);
		
		group.getChildren().add(path);
		group.getChildren().add(arrow);
		
		return group;
	}


	private void addRelation(Say question, Say anwser)
	{
		graph.addChild(new ActorAvailable(anwser.getPerson()), question, anwser);
	}


	private NodeView getNodeFor(Say vertice, Layout<Say, ICondition> layout)
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


	public class NodeView extends Pane
	{
		private EventHandler<? super MouseEvent> mouseEventHandler;


		private Tooltip tip;


		public NodeView(Say action)
		{
			final Circle circle = new Circle(0, 0, 15, action.getPerson().getColor());
			circle.setStroke(Color.web("#000"));
			circle.setStrokeWidth(2f);
			
			getChildren().add(circle);
			
			tip = new Tooltip(action.say());

			mouseEventHandler = new EventHandler<MouseEvent>() {

				public void handle(MouseEvent event)
				{
					if (event.getEventType().equals(MouseEvent.MOUSE_MOVED))
					{
						tip.show(circle, event.getScreenX(), event.getScreenY());
					}
					else if (event.getEventType().equals(MouseEvent.MOUSE_EXITED))
					{
						tip.hide();
					}
				}
			};

			addEventHandler(MouseEvent.ANY, mouseEventHandler);
		}
	}

	class Actor
	{
		private String name;


		private Color color;


		public Actor(String name, Color color)
		{
			this.color = color;
			this.name = name;
		}


		public String getName()
		{
			return name;
		}


		public void setName(String name)
		{
			this.name = name;
		}


		public Color getColor()
		{
			return color;
		}


		public void setColor(Color color)
		{
			this.color = color;
		}
		
		
		public String toString()
		{
			return name;
		}
	}

	// action
	class Say
	{
		private String text;


		private Actor person;


		public Say(Actor person, String text)
		{
			this.person = person;
			this.text = text;
		}


		public Actor getPerson()
		{
			return person;
		}


		public String say()
		{
			return person.getName() + "\n" + text;
		}


		public String toString()
		{
			return text;
		}
	}
	
	
	public interface ICondition
	{
		boolean check();
	}
	
	
	class ActorAvailable implements ICondition
	{
		private Actor actor;


		public ActorAvailable(Actor actor)
		{
			this.actor = actor;
		}


		public boolean check()
		{
			return true;
		}
		
		
		public String toString()
		{
			return "check actor available: " + actor.getName();
		}
	}
}

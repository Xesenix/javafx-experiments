
package pl.xesenix.experiments.experiment03;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
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

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.eclipse.core.internal.preferences.Base64;
import org.jdom2.CDATA;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment03.components.animation.FPSRendererAnimator;
import pl.xesenix.experiments.experiment03.components.viewport.Viewport;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.shortestpath.PrimMinimumSpanningTree;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLWriter;


public class Controller
{
	private static final Logger log = LoggerFactory.getLogger(Controller.class);


	@FXML
	private ResourceBundle resources;


	@FXML
	private URL location;


	@FXML
	private ChoiceBox<Layout<IAction, ICondition>> layout;


	@FXML
	private AnchorPane view;


	@FXML
	private Viewport viewport;


	@FXML
	private TextArea console;


	private HashMap<IAction, Node> map = new HashMap<IAction, Node>();


	private DirectedSparseGraph<IAction, ICondition> graph;


	private SAXBuilder builder;


	private XMLOutputter xmlOutput;


	@FXML
	void initialize()
	{
		log.debug("controller initializing");

		assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'app.fxml'.";
		assert layout != null : "fx:id=\"layout\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";
		assert viewport != null : "fx:id=\"viewport\" was not injected: check your FXML file 'app.fxml'.";
		
		Group lineLayer = new Group();
		Group pointLayer = new Group();
		
		viewport.addNodeToCanvas(lineLayer);
		viewport.addNodeToCanvas(pointLayer);

		Actor[] npcs = new Actor[] { new Actor("Sam", Color.web("#0f0")), new Actor("Tom", Color.web("#080")) };
		Actor[] team = new Actor[] { new Actor("Xesenix", Color.web("#f00")), new Actor("Hordrak", Color.web("#00f")) };
		
		EventAction enter = new EventAction("start");

		IAction[] npcActions = new IAction[] {
			new Say(npcs[0], "Welcome what do you want to do?"),
			new Say(npcs[0], "I have nothing to sell."),
			new Say(npcs[1], "I know someone who may help you. What do you want to do exactly?"),
			new Say(npcs[0], "I know elven hunter should I arrange meeting?"),
			new Say(npcs[0], "I heard rumors about ancient tomb deep in forest, maybe you want to check it?") };

		IAction[] playerActions = new IAction[] {
			new Say(team[0], "I want to buy some things"),
			new Say(team[1], "I want to go for an adventure"),
			new Say(team[1], "I need some wolf leather"),
			new Say(team[0], "Do you know where can i get some better equipment?"),
			new Say(team[1], "Good idea i need some armor."),
			new EventAction("back")
		};

		graph = new DirectedSparseGraph<IAction, ICondition>();
		
		addRelation(enter, npcActions[0]);
		
		addRelation(npcActions[0], playerActions[0]);
		addRelation(npcActions[0], playerActions[1]);
		addRelation(playerActions[0], npcActions[1]);
		addRelation(playerActions[1], npcActions[2]);
		addRelation(npcActions[2], playerActions[2]);
		addRelation(npcActions[2], playerActions[3]);
		addRelation(playerActions[2], npcActions[3]);
		addRelation(playerActions[3], playerActions[4]);
		addRelation(playerActions[4], npcActions[4]);

		addRelation(npcActions[1], playerActions[5]);
		addRelation(npcActions[2], playerActions[5]);
		addRelation(npcActions[3], playerActions[5]);
		addRelation(npcActions[4], playerActions[5]);
		
		addRelation(playerActions[5], npcActions[0]);
		
		//addRelation(playerActions[5], npcActions[0]);
		
		builder = new SAXBuilder();
		
		xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		
		try
		{
			String filePath = "out.xml";
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			GraphMLWriter<IAction, ICondition> graphWriter = new GraphMLWriter<IAction, ICondition>();
			HashMap<String, GraphMLMetadata<IAction>> map = new HashMap<String, GraphMLMetadata<IAction>>();
			
			map.put("action", new GraphMLMetadata<IAction>("some desc", "def", new Transformer<IAction, String>() {

				public String transform(IAction input)
				{
					Element actionNode = new Element("action");
					
					actionNode.addContent(new Element("text").setContent(new CDATA(input.execute())));
					
					return xmlOutput.outputString(actionNode);
				}
			}));
			
			graphWriter.setVertexData(map);
			graphWriter.save(graph, out);
			
			//InputStream is = getClass().getResource("/out.xml").openStream();
			
			File file = new File(filePath);
			
			if (file.canRead())
			{
				FileInputStream fis = new FileInputStream(file);
				
				StringBuilder text = new StringBuilder();
				
				Scanner scanner = new Scanner(fis, "utf-8");
				
				while (scanner.hasNextLine())
				{
					text.append(scanner.nextLine() + "\n");
				}
				scanner.close();
				
				console.setText(text.toString());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		prepareLayouts(graph);

		//KKLayout good for not crossing edges
		
		//DAGLayout 

		for (IAction action : graph.getVertices())
		{
			pointLayer.getChildren().add(getNodeFor(action));
		}
		
		for (ICondition condition : graph.getEdges())
		{
			lineLayer.getChildren().add(getEdgeFor(condition));
		}
		
		Canvas canvas = new Canvas(200, 200);
		canvas.setMouseTransparent(true);

		viewport.getChildren().add(canvas);

		final GraphicsContext gc = canvas.getGraphicsContext2D();
		
		(new FPSRendererAnimator(30, gc) {
			HashMap<Node, Point2D> velocity = new HashMap<Node, Point2D>();
			
			public void start() {
				super.start();
				
				for (IAction action : graph.getVertices())
				{
					Node node = getNodeFor(action);
					
					velocity.put(node, new Point2D.Double());
				}
			};

			public void dologic(long stepMiliseconds)
			{
				Layout<IAction, ICondition> selection = layout.getSelectionModel().getSelectedItem();
				
				if (selection != null)
				{
					if (selection instanceof SpringLayout)
					{
						((SpringLayout<IAction, ICondition>) selection).step();
					}
					else if (selection instanceof SpringLayout2)
					{
						((SpringLayout2<IAction, ICondition>) selection).step();
					}
					else if (selection instanceof KKLayout)
					{
						((KKLayout<IAction, ICondition>) selection).step();
					}
					else if (selection instanceof FRLayout)
					{
						((FRLayout<IAction, ICondition>) selection).step();
					}
					else if (selection instanceof FRLayout2)
					{
						((FRLayout2<IAction, ICondition>) selection).step();
					}
				}
				
				super.dologic(stepMiliseconds);
			}

			public void render(long stepMiliseconds)
			{
				Layout<IAction, ICondition> selection = layout.getSelectionModel().getSelectedItem();
				
				if (selection != null)
				{
					for (IAction action : graph.getVertices())
					{
						Node node = getNodeFor(action);
						Point2D target = selection.transform(action);
						
						Point2D v = velocity.get(node);
						
						double d = target.distance(node.getLayoutX(), node.getLayoutY());
						double dx = target.getX() - node.getLayoutX();
						double dy = target.getY() - node.getLayoutY();
						double maxSpeed = Math.min(150, v.distance(0, 0) + 0.1);
						double scale = 1;
						
						if (d != 0)
						{
							scale = Math.min(maxSpeed / d, 1);
						}
						
						v.setLocation(dx * scale,dy * scale);
						
						node.setLayoutX(node.getLayoutX() + v.getX());
						node.setLayoutY(node.getLayoutY() + v.getY());
						
						selection.setLocation(action, new Point2D.Double(node.getLayoutX(), node.getLayoutY()));
					}
				}
				
				super.render(stepMiliseconds);
			}
		}).start();
	}
	
	
	private void prepareLayouts(Graph<IAction, ICondition> graph)
	{
		ObservableList<Layout<IAction, ICondition>> layouts = FXCollections.observableArrayList();
		
		layouts.add(prepareDAGLayout(graph));
		layouts.add(prepareKKLayout(graph));
		layouts.add(prepareFRLayout(graph));
		layouts.add(prepareFRLayout2(graph));
		layouts.add(prepareSpringLayout(graph));
		layouts.add(prepareSpringLayout2(graph));
		
		layout.setItems(layouts);
	}


	private Layout<IAction, ICondition> prepareSpringLayout2(Graph<IAction, ICondition> graph)
	{
		SpringLayout2<IAction, ICondition> result = new SpringLayout2<IAction, ICondition>(graph);
		result.setSize(new Dimension(800, 600));
		result.initialize();
		
		return result;
	}


	private Layout<IAction, ICondition> prepareSpringLayout(Graph<IAction, ICondition> graph)
	{
		SpringLayout<IAction, ICondition> result = new SpringLayout<IAction, ICondition>(graph);
		result.setSize(new Dimension(800, 600));
		result.initialize();
		
		return result;
	}


	private Layout<IAction, ICondition> prepareFRLayout2(Graph<IAction, ICondition> graph)
	{
		FRLayout2<IAction, ICondition> result = new FRLayout2<IAction, ICondition>(graph);
		result.setSize(new Dimension(800, 600));
		result.initialize();
		
		return result;
	}


	private Layout<IAction, ICondition> prepareFRLayout(Graph<IAction, ICondition> graph)
	{
		FRLayout<IAction, ICondition> result = new FRLayout<IAction, ICondition>(graph);
		result.setSize(new Dimension(800, 600));
		result.initialize();
		
		return result;
	}


	private Layout<IAction, ICondition> prepareDAGLayout(Graph<IAction, ICondition> graph)
	{
		PrimMinimumSpanningTree<IAction, ICondition> algorithm = new PrimMinimumSpanningTree<IAction, ICondition>(new Factory<Graph<IAction, ICondition>>() {

			@Override
			public Graph<IAction, ICondition> create()
			{
				return new DelegateTree<IAction, ICondition>();
			}
		});
		
		Graph<IAction, ICondition> tree = algorithm.transform(graph);
		
		DAGLayout<IAction, ICondition> result = new DAGLayout<IAction, ICondition>(tree);
		result.setSize(new Dimension(800, 600));
		result.initialize();
		
		return result;
	}


	private Layout<IAction, ICondition> prepareKKLayout(Graph<IAction, ICondition> graph)
	{
		KKLayout<IAction, ICondition> result = new KKLayout<IAction, ICondition>(graph);
		result.setSize(new Dimension(800, 600));
		result.initialize();
		
		return result;
	}


	private Node getEdgeFor(ICondition condition)
	{
		Pair<IAction> pair = graph.getEndpoints(condition);
		
		final Node a = getNodeFor(pair.getFirst());
		final Node b = getNodeFor(pair.getSecond());
		
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
		arrow.rotateProperty().bind(new RotationBinding(b, a));
		
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


	private void addRelation(IAction question, IAction anwser)
	{
		if (!graph.containsVertex(question))
		{
			graph.addVertex(question);
		}
		
		if (!graph.containsVertex(anwser))
		{
			graph.addVertex(anwser);
		}
		
		if (anwser instanceof Say)
		{
			graph.addEdge(new ActorAvailable(((Say) anwser).getPerson()), question, anwser, EdgeType.DIRECTED);
		}
		else
		{
			graph.addEdge(new Tautology(), question, anwser, EdgeType.DIRECTED);
		}
	}


	private Node getNodeFor(IAction vertice)
	{
		if (!map.containsKey(vertice))
		{
			if (vertice instanceof Say)
			{
				map.put(vertice, new SayView((Say) vertice));
			}
			else if (vertice instanceof EventAction)
			{
				map.put(vertice, new EventView((EventAction) vertice));
			}
		}
		
		return map.get(vertice);
	}


	public class RotationBinding extends DoubleBinding
	{
		private final Node b;


		private final Node a;


		public RotationBinding(Node b, Node a)
		{
			this.b = b;
			this.a = a;
			
			this.bind(a.layoutXProperty());
			this.bind(a.layoutYProperty());
			this.bind(b.layoutXProperty());
			this.bind(b.layoutYProperty());
		}


		protected double computeValue()
		{
			return 180f * Math.atan2(b.layoutYProperty().get() - a.layoutYProperty().get(), b.layoutXProperty().get() - a.layoutXProperty().get()) / Math.PI;
		}
	}


	public class SayView extends Pane
	{
		private EventHandler<? super MouseEvent> mouseEventHandler;


		private Tooltip tip;


		public SayView(Say action)
		{
			final Circle circle = new Circle(0, 0, 15, action.getPerson().getColor());
			circle.setStroke(Color.web("#000"));
			circle.setStrokeWidth(2f);
			
			getChildren().add(circle);
			
			tip = new Tooltip(action.execute());

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


	public class EventView extends Pane
	{
		private EventHandler<? super MouseEvent> mouseEventHandler;


		private Tooltip tip;


		public EventView(EventAction action)
		{
			final Circle circle = new Circle(0, 0, 15, Color.WHITE);
			circle.setStroke(Color.web("#000"));
			circle.setStrokeWidth(2f);
			
			getChildren().add(circle);
			
			double[] vertices = new double[20];
			
			for (int i = 0; i < 5; i++)
			{
				double a = 2 * Math.PI * ((double) i / 5);
				double b = 2 * Math.PI * (((double) i + 0.5) / 5);
				
				vertices[i * 4] = 14 * Math.cos(a);
				vertices[i * 4 + 1] = 14 * Math.sin(a);
				vertices[i * 4 + 2] = 5 * Math.cos(b);
				vertices[i * 4 + 3] = 5 * Math.sin(b);
			}
			
			Polygon star = new Polygon(vertices);
			star.setFill(Color.BLACK);
			
			getChildren().add(star);
			
			tip = new Tooltip(action.execute());

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

	
	public static class Actor implements Serializable
	{
		private static final long serialVersionUID = 1L;


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
	
	
	public interface IAction extends Serializable
	{
		String execute();
	}
	
	
	// action
	class Say implements IAction
	{
		private static final long serialVersionUID = 1L;


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


		public String execute()
		{
			return person.getName() + "\n" + text;
		}


		public String toString()
		{
			return text;
		}
	}
	
	
	public class EventAction implements IAction
	{
		private static final long serialVersionUID = 1L;
		
		
		private String name;


		public EventAction(String name)
		{
			this.name = name;
		}
		
		
		public String execute()
		{
			return "event: " + this.name;
		}
		
		
		public String toString()
		{
			return this.name;
		}
	}
	
	
	public interface ICondition
	{
		boolean check();
	}
	
	
	class Tautology implements ICondition
	{
		public boolean check()
		{
			return true;
		}
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

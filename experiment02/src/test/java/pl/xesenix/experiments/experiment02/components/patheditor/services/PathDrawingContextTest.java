package pl.xesenix.experiments.experiment02.components.patheditor.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

public class PathDrawingContextTest
{

	private PathDrawingContext context;


	@Before
	public void setUp() throws Exception
	{
		context = new PathDrawingContext();
	}


	@After
	public void tearDown() throws Exception
	{
	}


	@Test
	public void testCreatingPath()
	{
		IPath pathA = context.createPath();
		IPath pathB = context.createPath();
		
		assertSame("Created path should be empty.", 0, pathA.getSize());
		assertNotSame("Each created path should be new object.", pathB, pathA);
	}


	@Test
	public void testSelectingPath()
	{
		IPath path = context.createPath();
		
		context.setEditedPath(path);
		assertSame("After setting edited path to some instance you should be able to get it back by calling getEditedPath.", context.getEditedPath(), path);
	}


	@Test
	public void testCreatingPoint()
	{
		int precision = 2;
		double x = 1f, y = 2f, inX = 3f, inY = 4f, outX = 5f, outY = 6f;
		IPathPoint pathPointA = context.createPoint(x, y, inX, inY, outX, outY);
		
		assertEquals("Created point x property should be same as passed value.", x, pathPointA.getX(), precision);
		assertEquals("Created point y property should be same as passed value.", y, pathPointA.getY(), precision);
		assertEquals("Created point inX property should be same as passed value.", inX, pathPointA.getInX(), precision);
		assertEquals("Created point inY property should be same as passed value.", inY, pathPointA.getInY(), precision);
		assertEquals("Created point outX property should be same as passed value.", outX, pathPointA.getOutX(), precision);
		assertEquals("Created point outY property should be same as passed value.", outY, pathPointA.getOutY(), precision);
	}


	@Test
	public void testSelectingPoint()
	{
		int precision = 2;
		double x = 1f, y = 2f, inX = 3f, inY = 4f, outX = 5f, outY = 6f;
		IPathPoint pathPointA = context.createPoint(0, 0, 0, 0, 0, 0);
		
		assertSame("If no point was selected retriving edited point should return null", null, context.getEditedPoint());
		
		context.selectPoint(pathPointA);
		assertSame("After setting edited point you should be able to retrive it by calling getEditedPoint", pathPointA, context.getEditedPoint());
		
		context.updateSelectedPoint(x, y, inX, inY, outX, outY);
		assertEquals("Edited point x property should be same as passed value.", x, context.getEditedPoint().getX(), precision);
		assertEquals("Edited point y property should be same as passed value.", y, context.getEditedPoint().getY(), precision);
		assertEquals("Edited point inX property should be same as passed value.", inX, context.getEditedPoint().getInX(), precision);
		assertEquals("Edited point inY property should be same as passed value.", inY, context.getEditedPoint().getInY(), precision);
		assertEquals("Edited point outX property should be same as passed value.", outX, context.getEditedPoint().getOutX(), precision);
		assertEquals("Edited point outY property should be same as passed value.", outY, context.getEditedPoint().getOutY(), precision);
	}


	@Test
	public void testPointToPathRelation()
	{
		double x = 1f, y = 2f, inX = 3f, inY = 4f, outX = 5f, outY = 6f;
		IPathPoint pathPointA = context.createPoint(x, y, inX, inY, outX, outY);
		IPath path = context.createPath();
		
		context.setEditedPath(path);
		context.addPointToCurrentPath(pathPointA);
		assertTrue("Point should be contained in edited path affter adding it to it.", context.getEditedPath().getPathPoints().contains(pathPointA));
		context.removePoint(pathPointA);
		assertFalse("Point shouldn`t be contained in edited path affter removing it from it.", context.getEditedPath().getPathPoints().contains(pathPointA));
	}
}

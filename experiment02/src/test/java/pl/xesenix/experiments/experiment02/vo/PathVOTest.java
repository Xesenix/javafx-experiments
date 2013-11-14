/**
 * 
 */
package pl.xesenix.experiments.experiment02.vo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Xesenix
 *
 */
public class PathVOTest
{

	private PathVO path;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		path = new PathVO();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		path = null;
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathVO#addPathPoint(pl.xesenix.experiments.experiment02.vo.IPathPoint)}.
	 */
	@Test
	public void testAddPathPoint()
	{
		PathPointVO pathPointA = new PathPointVO();
		PathPointVO pathPointB = new PathPointVO();
		
		path.addPathPoint(pathPointA);
		assertTrue("Added point should exist on path points list", path.getPathPoints().contains(pathPointA));
		assertSame("Added point path property should be set to path that it was added to", pathPointA.getPath(), path);
		
		path.addPathPoint(pathPointB);
		assertTrue("Added point should exist on path points list", path.getPathPoints().contains(pathPointB));
		assertSame("Added point path property should be set to path that it was added to", pathPointB.getPath(), path);
		assertTrue("Adding another point shouldn`t remove already exisitng ones", path.getPathPoints().contains(pathPointA));
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathVO#addPathPoints(pl.xesenix.experiments.experiment02.vo.IPathPoint[])}.
	 */
	@Test
	public void testAddPathPointsIPathPointArray()
	{
		PathPointVO pathPointA = new PathPointVO();
		PathPointVO pathPointB = new PathPointVO();
		
		path.addPathPoints(pathPointA, pathPointB);
		assertTrue("Points added as multiple arguments should exist on path points list", path.getPathPoints().contains(pathPointA));
		assertSame("Points added as multiple arguments path property should be set to path that they were added to", pathPointA.getPath(), path);
		assertTrue("Points added as multiple arguments should exist on path points list", path.getPathPoints().contains(pathPointB));
		assertSame("Points added as multiple arguments path property should be set to path that they were added to", pathPointB.getPath(), path);
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathVO#addPathPoints(java.util.Collection)}.
	 */
	@Test
	public void testAddPathPointsCollectionOfQextendsIPathPoint()
	{
		PathPointVO pathPointA = new PathPointVO();
		PathPointVO pathPointB = new PathPointVO();
		Collection<IPathPoint> collection = new ArrayList<IPathPoint>();
		
		collection.add(pathPointA);
		collection.add(pathPointB);
		
		path.addPathPoints(collection);
		assertTrue("Points added as collection should exist on path points list", path.getPathPoints().contains(pathPointA));
		assertSame("Points added as collection path property should be set to path that they were added to", pathPointA.getPath(), path);
		assertTrue("Points added as collection should exist on path points list", path.getPathPoints().contains(pathPointB));
		assertSame("Points added as collection path property should be set to path that they were added to", pathPointB.getPath(), path);
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathVO#removePoint(pl.xesenix.experiments.experiment02.vo.IPathPoint)}.
	 */
	@Test
	public void testRemovePoint()
	{
		PathPointVO pathPointA = new PathPointVO();
		PathPointVO pathPointB = new PathPointVO();
		
		path.addPathPoints(pathPointA, pathPointB);
		path.removePoint(null);
		assertTrue("Removing null shouldn`t affect any of path points", path.getPathPoints().contains(pathPointA));
		assertSame("Removing null shouldn`t affect any of path points path property", pathPointA.getPath(), path);
		assertTrue("Removing null shouldn`t affect any of path points", path.getPathPoints().contains(pathPointB));
		assertSame("Removing null shouldn`t affect any of path points path property", pathPointB.getPath(), path);
		
		path.removePoint(pathPointA);
		assertFalse("Removed point shouldn`t exist on path points list", path.getPathPoints().contains(pathPointA));
		assertSame("Removed point path property should be set to null", pathPointA.getPath(), null);
		assertTrue("Removing other point shouldn`t affect any other point", path.getPathPoints().contains(pathPointB));
		assertSame("Removing other point shouldn`t affect any other point path property", pathPointB.getPath(), path);
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathVO#getPathPoints()}.
	 */
	@Test
	public void testGetPathPoints()
	{
		PathPointVO pathPointA = new PathPointVO();
		PathPointVO pathPointB = new PathPointVO();
		
		assertTrue("Empty path path points list should be empty", path.getPathPoints().isEmpty());
		
		path.addPathPoint(pathPointA);
		assertTrue("Added point should exist on path points list", path.getPathPoints().contains(pathPointA));
		
		// TODO how to manage adding/removing points to path points list (readonly or clone?)
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathVO#getSize()}.
	 */
	@Test
	public void testGetSize()
	{
		PathPointVO pathPointA = new PathPointVO();
		PathPointVO pathPointB = new PathPointVO();
		
		assertSame("Empty path size should be 0", path.getSize(), 0);
		
		path.addPathPoint(pathPointA);
		assertSame("Path size should be 1", path.getSize(), 1);
		
		path.addPathPoint(pathPointA);
		assertSame("Adding multiple times the same point should exist only once", path.getSize(), 1);
		
		path.addPathPoint(pathPointB);
		assertSame("Path size should be 2", path.getSize(), 2);
		
		path.removePoint(pathPointA);
		assertSame("Path size should be 1", path.getSize(), 1);
		
		path.removePoint(pathPointB);
		assertSame("Path size after removing all points should be 0", path.getSize(), 0);
	}

}

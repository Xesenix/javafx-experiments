/**
 * 
 */

package pl.xesenix.experiments.experiment02.vo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Xesenix
 * 
 */
public class PathPointVOTest
{

	private PathPointVO pathPoint;


	private int precision = 2;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		pathPoint = new PathPointVO();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		pathPoint = null;
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathPointVO#setPath(pl.xesenix.experiments.experiment02.vo.IPath)} and
	 * {@link pl.xesenix.experiments.experiment02.vo.PathPointVO#getPath()} and
	 * {@link pl.xesenix.experiments.experiment02.vo.PathPointVO#removeFromPath(pl.xesenix.experiments.experiment02.vo.IPath)}.
	 */
	@Test
	public void testPath()
	{
		IPath pointPath = new PathVO();
		IPath otherPath = new PathVO();

		pathPoint.setPath(pointPath);
		assertSame("Get path expected to return what was set in setPath", pointPath, pathPoint.getPath());

		pathPoint.removeFromPath(otherPath);
		assertSame("Removing from other path shouldn`t affect pathPoint path", pointPath, pathPoint.getPath());

		pathPoint.removeFromPath(null);
		assertSame("Removing from null shouldn`t affect pathPoint path", pointPath, pathPoint.getPath());

		pathPoint.removeFromPath(pointPath);
		assertSame("Removing from point path should point pathPoint path to null", null, pathPoint.getPath());

		pathPoint.setPath(pointPath);
		pathPoint.setPath(otherPath);
		assertSame("Changing path by setPath should result in getting last set path", otherPath, pathPoint.getPath());
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathPointVO#setInDirection(double)}.
	 */
	@Test
	public void testSetInDirection()
	{
		pathPoint.setInX(3);
		assertEquals("Setting in x should affect get in x", 3, pathPoint.getInX(), precision);

		pathPoint.setInY(4);
		assertEquals("Setting in y should affect get in y", 4, pathPoint.getInY(), precision);

		pathPoint.setInDirection(0);

		assertEquals("Changing angle should affect x coordinate", 5, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", 0, pathPoint.getInY(), precision);

		pathPoint.setInDirection(Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", 0, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", 5, pathPoint.getInY(), precision);

		pathPoint.setInDirection(Math.PI);

		assertEquals("Changing angle should affect x coordinate", -5, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", 0, pathPoint.getInY(), precision);

		pathPoint.setInDirection(3 * Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", 0, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", -5, pathPoint.getInY(), precision);

		pathPoint.setInDirection(Math.atan2(4, 3));

		assertEquals("Not changing the angle shouldn`t affect x coordinate", 3, pathPoint.getInX(), precision);
		assertEquals("Not changing the angle shouldn`t affect y coordinate", 4, pathPoint.getInY(), precision);

		pathPoint.setInDirection(Math.atan2(4, 3) + Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", -4, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", 3, pathPoint.getInY(), precision);

		pathPoint.setInDirection(Math.atan2(4, 3) + Math.PI);

		assertEquals("Changing angle should affect x coordinate", -3, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", -4, pathPoint.getInY(), precision);

		pathPoint.setInDirection(Math.atan2(4, 3) + 3 * Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", 4, pathPoint.getInX(), precision);
		assertEquals("Changing angle should affect y coordinate", -3, pathPoint.getInY(), precision);
	}


	/**
	 * Test method for {@link pl.xesenix.experiments.experiment02.vo.PathPointVO#setOutDirection(double)}.
	 */
	@Test
	public void testSetOutDirection()
	{
		pathPoint.setOutX(3);
		assertEquals("Setting out x should affect get out x", 3, pathPoint.getOutX(), precision);

		pathPoint.setOutY(4);
		assertEquals("Setting out y should affect get out y", 4, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(0);

		assertEquals("Changing angle should affect x coordinate", 5, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", 0, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", 0, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", 5, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(Math.PI);

		assertEquals("Changing angle should affect x coordinate", -5, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", 0, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(3 * Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", 0, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", -5, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(Math.atan2(4, 3));

		assertEquals("Not changing the angle shouldn`t affect x coordinate", 3, pathPoint.getOutX(), precision);
		assertEquals("Not changing the angle shouldn`t affect y coordinate", 4, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(Math.atan2(4, 3) + Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", -4, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", 3, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(Math.atan2(4, 3) + Math.PI);

		assertEquals("Changing angle should affect x coordinate", -3, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", -4, pathPoint.getOutY(), precision);

		pathPoint.setOutDirection(Math.atan2(4, 3) + 3 * Math.PI / 2);

		assertEquals("Changing angle should affect x coordinate", 4, pathPoint.getOutX(), precision);
		assertEquals("Changing angle should affect y coordinate", -3, pathPoint.getOutY(), precision);
	}
}

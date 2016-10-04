package computergraphics.framework.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrianglesTest {
	@Test
	public void testCalculateNormal_X() {
		Vector v0 = new Vector(0, 1, 1);
		Vector v1 = new Vector(0, 2, 1);
		Vector v2 = new Vector(0, 1, 2);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals(1, normal.x(), 0);
		assertEquals(0, normal.y(), 0);
		assertEquals(0, normal.z(), 0);
	}
	
	@Test
	public void testCalculateNormal_Y() {
		Vector v0 = new Vector(1, 0, 1);
		Vector v1 = new Vector(1, 0, 2);
		Vector v2 = new Vector(2, 0, 1);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals(0, normal.x(), 0);
		assertEquals(1, normal.y(), 0);
		assertEquals(0, normal.z(), 0);
	}
	
	@Test
	public void testCalculateNormal_Z() {
		Vector v0 = new Vector(1, 1, 0);
		Vector v1 = new Vector(2, 1, 0);
		Vector v2 = new Vector(1, 2, 0);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals(0, normal.x(), 0);
		assertEquals(0, normal.y(), 0);
		assertEquals(1, normal.z(), 0);
	}
	
	@Test
	public void testCalculateNormal_XNegativeY() {
		Vector v0 = new Vector(0, 1, 1);
		Vector v1 = new Vector(1, 2, 1);
		Vector v2 = new Vector(0, 1, 2);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals( 0.707, normal.x(), 0.001);
		assertEquals(-0.707, normal.y(), 0.001);
		assertEquals( 0,     normal.z(), 0);
	}
	
	@Test
	public void testCalculateNormal_YNegativeZ() {
		Vector v0 = new Vector(1, 0, 1);
		Vector v1 = new Vector(1, 1, 2);
		Vector v2 = new Vector(2, 0, 1);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals( 0,     normal.x(), 0);
		assertEquals( 0.707, normal.y(), 0.001);
		assertEquals(-0.707, normal.z(), 0.001);
	}
	
	@Test
	public void testCalculateNormal_ZNegativeX() {
		Vector v0 = new Vector(1, 1, 0);
		Vector v1 = new Vector(2, 1, 1);
		Vector v2 = new Vector(1, 2, 0);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals(-0.707, normal.x(), 0.001);
		assertEquals( 0,     normal.y(), 0);
		assertEquals( 0.707, normal.z(), 0.001);
	}
	
	@Test
	public void testCalculateNormal_XYZ() {
		Vector v0 = new Vector(1, 0, 0);
		Vector v1 = new Vector(0, 1, 0);
		Vector v2 = new Vector(0, 0, 1);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals(0.577, normal.x(), 0.001);
		assertEquals(0.577, normal.y(), 0.001);
		assertEquals(0.577, normal.z(), 0.001);
	}
	
	@Test
	public void testCalculateNormal_NegativeXYZ() {
		Vector v0 = new Vector(2, 2, 5);
		Vector v1 = new Vector(2, 5, 2);
		Vector v2 = new Vector(5, 2, 2);
		Vector normal = Triangles.calculateNormal(v0, v1, v2);
		assertEquals(-0.577, normal.x(), 0.001);
		assertEquals(-0.577, normal.y(), 0.001);
		assertEquals(-0.577, normal.z(), 0.001);
	}
}

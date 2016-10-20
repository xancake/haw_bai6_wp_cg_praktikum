package computergraphics.framework.mesh;

import computergraphics.framework.math.Vector;

/**
 * Represents a vertex in 3-space with position and normal.
 * 
 * @author Philipp Jenke
 */
public class Vertex {
	
	/**
	 * 3D position of the vertex.
	 */
	protected final Vector position = new Vector(0, 0, 0);
	
	/**
	 * (Normalized) normal direction of the vertex.
	 */
	protected Vector normal = new Vector(1, 0, 0);

	public Vertex(Vector position) {
		this(position, new Vector(0, 1, 0));
	}

	public Vertex(Vector position, Vector normal) {
		this.position.copy(position);
		this.normal.copy(normal);
	}
	
	public void setNormal(Vector normal) {
		this.normal.copy(normal);
	}

	public Vector getPosition() {
		return position;
	}

	public Vector getNormal() {
		return normal;
	}
	
	@Override
	public String toString() {
		return "Vertex(" + position.toString() + ")";
	}
}

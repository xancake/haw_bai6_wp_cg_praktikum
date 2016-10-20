/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.framework.datastructures.halfedge;

import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.Vertex;

/**
 * Representation of a vertex.
 * 
 * @author Philipp Jenke
 */
public class HalfEdgeVertex extends Vertex{
	
	/**
	 * Color value at the vertex
	 */
	private Vector color = new Vector(0, 0, 0);
	
	/**
	 * Reference to one of the outgoing half edges.
	 */
	private HalfEdge halfEgde = null;
	
	/**
	 * Constructor.
	 * @param position Initial value for position.
	 */
	public HalfEdgeVertex(Vector position) {
		super(position);
	}
	
	/**
	 * Constructor.
	 * @param position Initial value for position.
	 * @param normal Initial value for normal.
	 */
	public HalfEdgeVertex(Vector position, Vector normal) {
		super(position, normal);
	}
	
	/**
	 * Constructor.
	 * @param position Initial value for position.
	 * @param normal Initial value for normal.
	 */
	public HalfEdgeVertex(Vector position, Vector normal, Vector color) {
		super(position, normal);
		this.color.copy(color);
	}
	
	public void setColor(Vector color) {
		this.color.copy(color);
	}
	
	public Vector getColor() {
		return color;
	}
	
	public void setHalfEgde(HalfEdge halfEgde) {
		this.halfEgde = halfEgde;
	}
	
	public HalfEdge getHalfEdge() {
		return halfEgde;
	}
}
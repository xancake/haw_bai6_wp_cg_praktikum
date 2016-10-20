package computergraphics.framework.datastructures.halfedge;

import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.Triangle;

/**
 * A facet has a reference to one of its half edges. This datastructure
 * represents a general mesh (triangle, quad, ...). However, we only use
 * triangle meshes here.
 * 
 * @author Philipp Jenke
 */
public class HalfEdgeTriangle extends Triangle{
	
	/**
	 * One of the half edges around the facet.
	 */
	private HalfEdge halfEdge;
	

	public HalfEdgeTriangle(int a, int b, int c) {
		super(a,b,c);
	}

	public HalfEdgeTriangle(int a, int b, int c, int tA, int tB, int tC) {
		super(a, b, c, tA, tB, tC);
	}

	public HalfEdgeTriangle(int a, int b, int c, int tA, int tB, int tC, Vector normal) {
		super(a, b, c ,tA, tB, tC, normal);
	}
	
	public HalfEdge getHalfEdge() {
		return halfEdge;
	}
	
	public void setHalfEdge(HalfEdge halfEdge) {
		this.halfEdge = halfEdge;
	}
	
	public Vector getNormal() {
		return normal;
	}
	
	/**
	 * Compute the area of the facet. Area of the facet.
	 * @return Area of the triangle.
	 */
	public double getArea() {
		Vector v0 = halfEdge.getStartVertex().getPosition();
		Vector v1 = halfEdge.getNext().getStartVertex().getPosition();
		Vector v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
		return v1.subtract(v0).cross(v2.subtract(v0)).getNorm() / 2.0;
	}
	
	/**
	 * Compute the centroid (center of mass) of the triangle.
	 * @return Centroid of the triangle.
	 */
	public Vector getCentroid() {
		Vector v0 = halfEdge.getStartVertex().getPosition();
		Vector v1 = halfEdge.getNext().getStartVertex().getPosition();
		Vector v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
		return (v0.add(v1).add(v2)).multiply(1.0 / 3.0);
	}
	
	public int getVertexIndex(int i) {
		return vertexIndices[i];
	}
	
	public HalfEdgeVertex getVertex(int vertexIndex) {
		if(vertexIndex < 0 || vertexIndex > 2) {
			throw new IndexOutOfBoundsException("Index can only be within 0-2");
		}
		HalfEdge aktuelleKante = halfEdge;
		for(int i = 0; i < vertexIndex; i++) {
			aktuelleKante = aktuelleKante.getNext();
		}
		return aktuelleKante.getStartVertex();
	}
	
	public int getTexCoordIndex(int i) {
		return texCoordIndices[i];
	}
	
	@Override
	public String toString() {
		return "Triangular Facet";
	}
}

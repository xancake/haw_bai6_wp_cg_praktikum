/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.framework.mesh;

import java.util.List;

import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Texture;

/**
 * Shared interface for all triangle mesh implementations.
 * 
 * @author Philipp Jenke
 */
public interface ITriangleMesh {
	/**
	 * Add a new vertex (given by position) to the vertex list. The new vertex is appended to the end of the list.
	 */
	public int addVertex(Vector position);

	/**
	 * Add a new triangle to the mesh with the vertex indices a, b, c. The index of the first vertex is 0.
	 */
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3);

	/**
	 * Add triangle by vertex indices and corresponding texture coordinate indices.
	 */
	public void addTriangle(
			int vertexIndex1,
			int vertexIndex2,
			int vertexIndex3,
			int texCoordIndex1,
			int texCoordIndex2,
			int texCoordIndex3);

	/**
	 * Add texture coordinate to mesh.
	 */
	public void addTextureCoordinate(Vector t);

	/**
	 * Set a texture object for the mesh.
	 */
	public void setTexture(Texture texture);

	/**
	 * Clear mesh - remove all triangles and vertices.
	 */
	public void clear();

	public int getNumberOfTriangles();

	public int getNumberOfVertices();

	public Vertex getVertex(int index);

	public Triangle getTriangle(int triangleIndex);

	public Vector getTextureCoordinate(int index);

	public Texture getTexture();
	
	/**
	 * Gibt eine paarweise Liste aller Vertices zurück, die alle Kanten des Meshes repräsentieren.
	 * @return Eine Liste der paarweisen Vertices
	 */
	List<Pair<Vertex, Vertex>> getWireframeVertices();
	
	/**
	 * Gibt an, ob das Mesh einen Rand (oder mehrere Ränder) hat.
	 * @return {@code true} wenn das Mesh einen Rand hat, ansonsten {@code false}
	 */
	boolean hasBorder();
	
	/**
	 * Gibt eine paarweise Liste aller Vertices zurück, die sich an einem Rand befinden.
	 * Jedes Paar beschreibt somit eine Kante.
	 * @return Eine Liste der paarweisen Vertices
	 */
	List<Pair<Vertex, Vertex>> getBorderVertices();

	/**
	 * Compute the triangles normals.
	 */
	public void computeNormals();

	/**
	 * Create a mesh of the shadow polygons.
	 * @param lightPosition Position of the light source
	 * @param extend Length of the polygons
	 * @param shadowPolygonMesh Result is put in there
	 */
	public void createShadowPolygons(Vector lightPosition, float extend, ITriangleMesh shadowPolygonMesh);
}

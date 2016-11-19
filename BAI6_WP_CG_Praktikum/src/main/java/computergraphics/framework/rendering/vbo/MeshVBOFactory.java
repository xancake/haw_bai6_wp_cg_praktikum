package computergraphics.framework.rendering.vbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.mesh.Vertex;
import computergraphics.framework.rendering.RenderVertex;
import computergraphics.framework.rendering.VertexBufferObject;

/**
 * Klasse zum Erzeugen von {@link VertexBufferObject}s für {@link ITriangleMesh Meshes}
 */
public class MeshVBOFactory extends VertexBufferObjectFactory {
	private ITriangleMesh _mesh;
	
	/**
	 * Instanziiert eine neue Fabrik für das übergebene {@link ITriangleMesh Mesh}.
	 * @param mesh Das {@link ITriangleMesh Mesh} für das diese Fabrik {@link VertexBufferObject}s erzeugen soll, nicht {@code null}
	 * @throws NullPointerException, wenn das Mesh {@code null} ist
	 */
	public MeshVBOFactory(ITriangleMesh mesh) {
		_mesh = Objects.requireNonNull(mesh);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} für das Mesh wobei für die Beleuchtung die Facettennormalen verwendet
	 * werden.
	 * @param color Die Farbe in der das Mesh dargestellt werden soll
	 * @return Ein {@link VertexBufferObject} das das Mesh visualisiert
	 */
	public VertexBufferObject createMeshVBOWithTriangleNormals(Vector color) {
		checkColor(color);
		
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for(int t=0; t<_mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = _mesh.getTriangle(t);
			addTriangle(
					renderVertices,
					_mesh.getVertex(triangle.getVertexIndex(2)).getPosition(),
					_mesh.getVertex(triangle.getVertexIndex(1)).getPosition(),
					_mesh.getVertex(triangle.getVertexIndex(0)).getPosition(),
					triangle.getNormal(),
					triangle.getNormal(),
					triangle.getNormal(),
					color,
					color,
					color
			);
		}
		return new VertexBufferObject(GL2.GL_TRIANGLES, renderVertices);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} für das Mesh wobei für die Beleuchtung die Vertexnormalen verwendet
	 * werden.
	 * @param color Die Farbe in der das Mesh dargestellt werden soll
	 * @return Ein {@link VertexBufferObject} das das Mesh visualisiert
	 */
	public VertexBufferObject createMeshVBOWithVertexNormals(Vector color) {
		checkColor(color);
		
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for(int t=0; t<_mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = _mesh.getTriangle(t);
			addTriangle(
					renderVertices,
					_mesh.getVertex(triangle.getVertexIndex(2)).getPosition(),
					_mesh.getVertex(triangle.getVertexIndex(1)).getPosition(),
					_mesh.getVertex(triangle.getVertexIndex(0)).getPosition(),
					_mesh.getVertex(triangle.getVertexIndex(2)).getNormal(),
					_mesh.getVertex(triangle.getVertexIndex(1)).getNormal(),
					_mesh.getVertex(triangle.getVertexIndex(0)).getNormal(),
					color,
					color,
					color
			);
		}
		return new VertexBufferObject(GL2.GL_TRIANGLES, renderVertices);
	}
	
	/**
	 * Hilfsmethode die den {@code renderVertices} ein Dreieck hinzufügt.
	 */
	private void addTriangle(
			List<RenderVertex> renderVertices,
			Vector p0, Vector p1, Vector p2,
			Vector n0, Vector n1, Vector n2,
			Vector c0, Vector c1, Vector c2
	) {
		renderVertices.add(new RenderVertex(p2, n2, c2));
		renderVertices.add(new RenderVertex(p1, n1, c1));
		renderVertices.add(new RenderVertex(p0, n0, c0));
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} das die Facettennormalen des Meshes darstellt.
	 * @param drawLength Die Länge mit der die Normalen dargestellt werden sollen
	 * @param color Die Farbe in der die Normalen dargestellt werden sollen
	 * @return Ein {@link VertexBufferObject} das die Facettennormalen visualisiert
	 */
	public VertexBufferObject createFacettNormalsVBO(double drawLength, Vector color) {
		checkColor(color);
		
		List<RenderVertex> normalVertices = new ArrayList<>();
		for(int t=0; t<_mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = _mesh.getTriangle(t);
			Vector v0 = _mesh.getVertex(triangle.getVertexIndex(0)).getPosition();
			Vector v1 = _mesh.getVertex(triangle.getVertexIndex(1)).getPosition();
			Vector v2 = _mesh.getVertex(triangle.getVertexIndex(2)).getPosition();
			Vector schwerpunkt = Vector.calculateSchwerpunkt(v0, v1, v2);
			Vector normal = triangle.getNormal();
			
			normalVertices.add(new RenderVertex(schwerpunkt, normal, color));
			normalVertices.add(new RenderVertex(schwerpunkt.add(normal.multiply(drawLength)), normal, color));
		}
		return new VertexBufferObject(GL2.GL_LINES, normalVertices);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} das die Vertexnormalen des Meshes darstellt.
	 * @param drawLength Die Länge mit der die Normalen dargestellt werden sollen
	 * @param color Die Farbe in der die Normalen dargestellt werden sollen
	 * @return Ein {@link VertexBufferObject} das die Vertexnormalen visualisiert
	 */
	public VertexBufferObject createVertexNormalsVBO(double drawLength, Vector color) {
		checkColor(color);
		
		List<RenderVertex> normalVertices = new ArrayList<>();
		for (int i = 0; i < _mesh.getNumberOfVertices(); i++) {
			Vertex currentVertex = _mesh.getVertex(i);
			Vector position = currentVertex.getPosition();
			Vector normal = currentVertex.getNormal();
			
			normalVertices.add(new RenderVertex(position, normal, color));
			normalVertices.add(new RenderVertex(position.add(normal.multiply(drawLength)), normal, color));
		}
		return new VertexBufferObject(GL2.GL_LINES, normalVertices);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} das das Wireframe des Meshes darstellt.
	 * @param color Die Farbe in der das Wireframe dargestellt werden soll
	 * @return Ein {@link VertexBufferObject} das das Wireframe visualisiert
	 */
	public VertexBufferObject createWireframeVBO(Vector color) {
		checkColor(color);
		
		List<Pair<Vertex, Vertex>> wireframeVertexPairs = _mesh.getWireframeVertices();
		if(wireframeVertexPairs == null) {
			return new VertexBufferObject();
		}
		
		List<RenderVertex> wireframeVertices = new ArrayList<>();
		for (Pair<Vertex, Vertex> vertexPair : wireframeVertexPairs) {
			Vertex startVertex = vertexPair.getKey();
			Vertex endVertex = vertexPair.getValue();
			
			wireframeVertices.add(new RenderVertex(startVertex.getPosition(), startVertex.getNormal(), color));
			wireframeVertices.add(new RenderVertex(endVertex.getPosition(), endVertex.getNormal(), color));
		}
		return new VertexBufferObject(GL2.GL_LINES, wireframeVertices);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} das den Rand des Meshes darstellt.
	 * Wenn das Mesh keinen Rand hat (also geschlossen ist) wird ein leeres {@link VertexBufferObject} zurückgegeben.
	 * @param color Die Farbe in der der Rand dargestellt werden soll
	 * @return Ein {@link VertexBufferObject} das den Rand visualisiert
	 */
	public VertexBufferObject createBorderVBO(Vector color) {
		checkColor(color);
		if(!_mesh.hasBorder()) {
			return new VertexBufferObject();
		}
		
		List<RenderVertex> borderVertices = new ArrayList<>();
		for (Pair<Vertex, Vertex> vertexPair : _mesh.getBorderVertices()) {
			Vertex startVertex = vertexPair.getKey();
			Vertex endVertex = vertexPair.getValue();
			
			borderVertices.add(new RenderVertex(startVertex.getPosition(), startVertex.getNormal(), color));
			borderVertices.add(new RenderVertex(endVertex.getPosition(), endVertex.getNormal(), color));
		}
		return new VertexBufferObject(GL2.GL_LINES, borderVertices);
	}
}

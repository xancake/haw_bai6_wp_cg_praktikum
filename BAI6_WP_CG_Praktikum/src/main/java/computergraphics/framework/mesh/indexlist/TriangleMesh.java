package computergraphics.framework.mesh.indexlist;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Triangles;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.ObjReader;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.mesh.Vertex;
import computergraphics.framework.rendering.Texture;

public class TriangleMesh implements ITriangleMesh {
	private List<Vertex> _vertices;
	private List<Triangle> _triangles;
	private List<Vector> _textureCoordinates;
	private Texture _texture;
	
	public TriangleMesh() {
		_vertices = new LinkedList<>();
		_triangles = new LinkedList<>();
		_textureCoordinates = new LinkedList<>();
	}
	
	public TriangleMesh(String filename) throws IOException {
		this();
		ObjReader reader = new ObjReader();
		reader.read(filename, this);
	}
	
	@Override
	public int getNumberOfTriangles() {
		return _triangles.size();
	}
	
	@Override
	public int getNumberOfVertices() {
		return _vertices.size();
	}
	
	@Override
	public Vertex getVertex(int index) {
		return _vertices.get(index);
	}
	
	@Override
	public Triangle getTriangle(int index) {
		return _triangles.get(index);
	}
	
	@Override
	public Vector getTextureCoordinate(int index) {
		return _textureCoordinates.get(index);
	}
	
	@Override
	public Texture getTexture() {
		return _texture;
	}
	
	@Override
	public List<Pair<Vertex, Vertex>> getWireframeVertices() {
		return null;
	}
	
	@Override
	public boolean hasBorder() {
		return false;
	}
	
	@Override
	public List<Pair<Vertex, Vertex>> getBorderVertices() {
		// TODO: Falls man irgendwie mal lernt, wie man hier Kanten erkennen kann, dann machen wir das
		throw new UnsupportedOperationException("Not yet implemented!");
	}
	
	@Override
	public int addVertex(Vector position) {
		int index = _vertices.size();
		_vertices.add(new Vertex(position));
		return index;
	}
	
	@Override
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
		_triangles.add(new Triangle(vertexIndex1, vertexIndex2, vertexIndex3));
		
	}
	
	@Override
	public void addTriangle(
		int vertexIndex1,
		int vertexIndex2,
		int vertexIndex3,
		int texCoordIndex1,
		int texCoordIndex2,
		int texCoordIndex3
	)
	{
		_triangles
			.add(new Triangle(vertexIndex1, vertexIndex2, vertexIndex3, texCoordIndex1, texCoordIndex2, texCoordIndex3));
	}
	
	@Override
	public void addTextureCoordinate(Vector t) {
		_textureCoordinates.add(t);
	}
	
	@Override
	public void setTexture(Texture texture) {
		_texture = Objects.requireNonNull(texture);
	}
	
	@Override
	public void clear() {
		_vertices.clear();
		_triangles.clear();
	}
	
	@Override
	public void computeNormals() {
		for (Triangle t : _triangles) {
			Vertex v0 = getVertex(t.getVertexIndex(0));
			Vertex v1 = getVertex(t.getVertexIndex(1));
			Vertex v2 = getVertex(t.getVertexIndex(2));
			t.setNormal(Triangles.calculateNormal(v0.getPosition(), v1.getPosition(), v2.getPosition()));
		}
	}
	
	@Override
	public List<Pair<Vertex, Vertex>> getSilhouetteVertices(Vector viewpoint) {
		// TODO implement when necessary
		throw new IllegalArgumentException("Not yet implemented");
	}
	
	@Override
	public void createShadowPolygons(Vector lightPosition, float extend, ITriangleMesh shadowPolygonMesh) {
		// TODO implement when necessary
		throw new IllegalArgumentException("Not yet implemented");
	}
}

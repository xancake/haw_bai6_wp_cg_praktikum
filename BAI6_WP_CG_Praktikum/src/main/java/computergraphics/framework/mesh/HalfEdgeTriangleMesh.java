package computergraphics.framework.mesh;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import computergraphics.framework.datastructures.halfedge.HalfEdge;
import computergraphics.framework.datastructures.halfedge.HalfEdgeTriangle;
import computergraphics.framework.datastructures.halfedge.HalfEdgeVertex;
import computergraphics.framework.math.Triangles;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Texture;
import computergraphics.util.MultiKey;

public class HalfEdgeTriangleMesh implements ITriangleMesh {

	private Map<MultiKey, HalfEdge> _oppositeHalfEdges;
	private List<HalfEdge> _halfEdges;
	private List<HalfEdgeVertex> _halfEdgeVertices;
	private List<HalfEdgeTriangle> _halfEdgeTriangles;
	private List<Vector> _textureCoordinates;
	private Texture _texture;
	
	public HalfEdgeTriangleMesh() {
		_halfEdges = new LinkedList<>();
		_halfEdgeVertices = new LinkedList<>();
		_halfEdgeTriangles = new LinkedList<>();
		_textureCoordinates = new LinkedList<>();
		_oppositeHalfEdges = new HashMap<>();
		}
	
	public HalfEdgeTriangleMesh(String filename) {
		this();
		ObjReader reader = new ObjReader();
		reader.read(filename, this);
	}
	
	@Override
	public int getNumberOfTriangles() {
		return _halfEdgeTriangles.size();
	}
	
	@Override
	public int getNumberOfVertices() {
		return _halfEdgeVertices.size();
	}
	
	@Override
	public Vertex getVertex(int index) {
		return _halfEdgeVertices.get(index);
	}
	
	@Override
	public Triangle getTriangle(int index) {
		return _halfEdgeTriangles.get(index);
	}
	
	public HalfEdgeVertex getHalfEdgeVertex(int index) {
		return _halfEdgeVertices.get(index);
	}
	
	public HalfEdgeTriangle getHalfEdgeTriangle(int index) {
		return _halfEdgeTriangles.get(index);
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
	public int addVertex(Vector position) {
		int index = _halfEdgeVertices.size();
		_halfEdgeVertices.add(new HalfEdgeVertex(position));
		return index;
	}
	
	@Override
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
		HalfEdgeTriangle halfEdgeTriangle = new HalfEdgeTriangle(vertexIndex1, vertexIndex2, vertexIndex3);
		generateHalfEdges(vertexIndex1, vertexIndex2, vertexIndex3, halfEdgeTriangle);
	}
	
	@Override
	public void addTriangle( 
			int vertexIndex1,
			int vertexIndex2,
			int vertexIndex3,
			int texCoordIndex1,
			int texCoordIndex2,
			int texCoordIndex3) {
		
		HalfEdgeTriangle halfEdgeTriangle = new HalfEdgeTriangle(vertexIndex1, vertexIndex2, vertexIndex3, 
			texCoordIndex1, texCoordIndex2, texCoordIndex3);
		generateHalfEdges(vertexIndex1, vertexIndex2, vertexIndex3, halfEdgeTriangle);
	}

	private void generateHalfEdges(int vertexIndex1, int vertexIndex2, int vertexIndex3,
			HalfEdgeTriangle halfEdgeTriangle) {
		
		HalfEdge edge1 = initHalfEdge(halfEdgeTriangle, vertexIndex1, vertexIndex2);
		HalfEdge edge2 = initHalfEdge(halfEdgeTriangle, vertexIndex2, vertexIndex3);
		HalfEdge edge3 = initHalfEdge(halfEdgeTriangle, vertexIndex3, vertexIndex1);
		
		edge1.setNext(edge2);
		edge2.setNext(edge3);
		edge3.setNext(edge1);
		
		halfEdgeTriangle.setHalfEdge(edge1);
		_halfEdgeTriangles.add(halfEdgeTriangle);
	}
	
	private HalfEdge initHalfEdge(HalfEdgeTriangle facet, int startVertexIndex, int endVertexIndex) {
		
		HalfEdge newHalfEdge = new HalfEdge();
		HalfEdgeVertex startVertex = getHalfEdgeVertex(startVertexIndex);
		
		newHalfEdge.setFacet(facet);
		newHalfEdge.setStartVertex(startVertex);
		
		MultiKey key = new MultiKey(startVertexIndex, endVertexIndex);
		if(_oppositeHalfEdges.containsKey(key)) {
			HalfEdge oppositeHalfEdge = _oppositeHalfEdges.get(key);
			oppositeHalfEdge.setOpposite(newHalfEdge);
			newHalfEdge.setOpposite(oppositeHalfEdge);
		} else {
			_oppositeHalfEdges.put(key, newHalfEdge);
		}
		
		if(startVertex.getHalfEdge() == null)
			startVertex.setHalfEgde(newHalfEdge);
		
		_halfEdges.add(newHalfEdge);
		return newHalfEdge;
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
		_halfEdgeVertices.clear();
		_halfEdgeTriangles.clear();
	}
	
	@Override
	public void computeNormals() {
		for(HalfEdgeTriangle t : _halfEdgeTriangles) {
			HalfEdgeVertex v0 = getHalfEdgeVertex(t.getVertexIndex(0));
			HalfEdgeVertex v1 = getHalfEdgeVertex(t.getVertexIndex(1));
			HalfEdgeVertex v2 = getHalfEdgeVertex(t.getVertexIndex(2));
			t.setNormal(Triangles.calculateNormal(v0.getPosition(), v1.getPosition(), v2.getPosition()));
		}
		
		for (HalfEdgeVertex halfEdgeVertex : _halfEdgeVertices) {
			Vector vertexNormal = new Vector(0, 0, 0);
			HalfEdge currentEdge = halfEdgeVertex.getHalfEdge();
			
			do {
				Triangle currentFacette = currentEdge.getFacet();
				vertexNormal = vertexNormal.add(currentFacette.getNormal());
				currentEdge = currentEdge.getOpposite().getNext();
			} while(!currentEdge.equals(halfEdgeVertex.getHalfEdge()));
			
			vertexNormal = vertexNormal.getNormalized();
			halfEdgeVertex.setNormal(vertexNormal);
		}
	}
	
	@Override
	public void createShadowPolygons(Vector lightPosition, float extend, ITriangleMesh shadowPolygonMesh) {
		// TODO implement when necessary
		throw new IllegalArgumentException("Not yet implemented");
	}
}

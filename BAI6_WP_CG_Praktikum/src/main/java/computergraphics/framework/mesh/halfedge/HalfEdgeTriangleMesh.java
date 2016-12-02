package computergraphics.framework.mesh.halfedge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.HessescheEbene;
import computergraphics.framework.math.Triangles;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.ObjReader;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.mesh.Vertex;
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
	
	public HalfEdgeTriangleMesh(String filename) throws IOException {
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
	public List<Pair<Vertex, Vertex>> getWireframeVertices() {
		List<Pair<Vertex, Vertex>> wireframe = new ArrayList<>();
		for(HalfEdge edge : _halfEdges) {
			wireframe.add(new Pair<Vertex, Vertex>(edge.getStartVertex(), edge.getNext().getStartVertex()));
		}
		return wireframe;
	}
	
	@Override
	public boolean hasBorder() {
		return !_oppositeHalfEdges.isEmpty();
	}
	
	@Override
	public List<Pair<Vertex, Vertex>> getBorderVertices() {
		List<Pair<Vertex, Vertex>> border = new ArrayList<>();
		for(HalfEdge edge : _oppositeHalfEdges.values()) {
			border.add(new Pair<Vertex, Vertex>(edge.getStartVertex(), edge.getNext().getStartVertex()));
		}
		return border;
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
			_oppositeHalfEdges.remove(key);
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
				if(currentEdge.getOpposite() == null) {
					currentEdge = null;
				} else {
					currentEdge = currentEdge.getOpposite().getNext();
				}
			} while(currentEdge != null && !currentEdge.equals(halfEdgeVertex.getHalfEdge()));
			
			//Bei Rand werden die Kanten auch in die andere Richtung durchlaufen
			if(currentEdge == null) {
				currentEdge = halfEdgeVertex.getHalfEdge().getNext().getNext().getOpposite();
				
				while(currentEdge != null) {
					Triangle currentFacette = currentEdge.getFacet();
					vertexNormal = vertexNormal.add(currentFacette.getNormal());
					currentEdge = currentEdge.getNext().getNext().getOpposite();
				}
			}
			
			vertexNormal = vertexNormal.getNormalized();
			halfEdgeVertex.setNormal(vertexNormal);
		}
	}
	
	@Override
	public List<Pair<Vertex, Vertex>> getSilhouetteVertices(Vector viewpoint) {
		List<Pair<Vertex, Vertex>> silhouette = new ArrayList<>();
		
		Set<HalfEdge> closedSet = new HashSet<>();
		for(HalfEdge edge : _halfEdges) {
			if(!closedSet.contains(edge)) {
				double lambda = lambda(edge, viewpoint);
				
				HalfEdge opposite = edge.getOpposite();
				if(opposite != null) {
					// Wenn es eine Opposite-Kante gibt, heißt das, dass wir für zwei Dreiecke
					// prüfen müssen, ob nur eins von beiden der Lichtquelle zugewandt ist und
					// das andere eben nicht.
					double oppositeLambda = lambda(opposite, viewpoint);
					if(lambda > 0 && oppositeLambda <= 0) {
						silhouette.add(new Pair<>(edge.getStartVertex(), opposite.getStartVertex()));
					}
					if(lambda <= 0 && oppositeLambda > 0) {
						silhouette.add(new Pair<>(opposite.getStartVertex(), edge.getStartVertex()));
					}
					closedSet.add(opposite);
				} else {
					// Wenn es keine Opposite-Kante gibt, bedeutet das, dass wir uns am Rand des
					// Meshes befinden und die Kante somit automatisch zu Silhouette gehört, wenn
					// das Dreieck der Lichtquelle zugewandt ist.
					if(lambda > 0) {
						silhouette.add(new Pair<>(edge.getStartVertex(), edge.getNext().getStartVertex()));
					}
				}
				closedSet.add(edge);
			}
		}
		
		return silhouette;
	}
	
	/**
	 * Berechnet das Lambda für den Schnitt eines Strahls ausgehend vom übergebenen Viewpoint
	 * durch das zur übergebenen Kante gehörenden Dreieck.
	 * @param edge Die Kante
	 * @param viewpoint Der Viewpoint
	 * @return Das Lambda oder {@link Double#NaN}, wenn der Strahl parallel zum Dreieck verläuft, es also nicht schneidet
	 */
	private double lambda(HalfEdge edge, Vector viewpoint) {
		HalfEdgeTriangle facette = edge.getFacet();
		HessescheEbene facetteEbene = new HessescheEbene(edge.getStartVertex().getPosition(), facette.getNormal());
		return -facetteEbene.calculateLambda(viewpoint, facette.getNormal());
	}
	
	@Override
	public void createShadowPolygons(Vector lightPosition, float extend, ITriangleMesh shadowPolygonMesh) {
		shadowPolygonMesh.clear();
		
		List<Pair<Vertex, Vertex>> silhouette = getSilhouetteVertices(lightPosition);
		for(Pair<Vertex, Vertex> edge : silhouette) {
			Vector v1 = edge.getKey().getPosition();
			Vector v2 = edge.getValue().getPosition();
			Vector ray1 = v1.subtract(lightPosition).getNormalized();
			Vector ray2 = v2.subtract(lightPosition).getNormalized();
			Vector v3 = v1.add(ray1.multiply(extend));
			Vector v4 = v2.add(ray2.multiply(extend));
			
			int i1 = shadowPolygonMesh.addVertex(v1);
			int i2 = shadowPolygonMesh.addVertex(v2);
			int i3 = shadowPolygonMesh.addVertex(v3);
			int i4 = shadowPolygonMesh.addVertex(v4);
			shadowPolygonMesh.addTriangle(i1, i3, i2);
			shadowPolygonMesh.addTriangle(i2, i3, i4);
		}
		
		shadowPolygonMesh.computeNormals();
	}
}

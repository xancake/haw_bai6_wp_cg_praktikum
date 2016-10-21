package computergraphics.framework.rendering;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jogamp.opengl.GL2;

import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.mesh.Vertex;

public class VertexBufferObjectFactory {
	
	
	public VertexBufferObject createMeshVBOWithTriangleNormals(ITriangleMesh mesh, Vector color) {
		Objects.requireNonNull(mesh);
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
		
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for(int t=0; t<mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = mesh.getTriangle(t);
			addTriangle(
					renderVertices,
					mesh.getVertex(triangle.getVertexIndex(2)).getPosition(),
					mesh.getVertex(triangle.getVertexIndex(1)).getPosition(),
					mesh.getVertex(triangle.getVertexIndex(0)).getPosition(),
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
	
	public VertexBufferObject createMeshVBOWithVertexNormals(ITriangleMesh mesh, Vector color) {
		Objects.requireNonNull(mesh);
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
		
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for(int t=0; t<mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = mesh.getTriangle(t);
			addTriangle(
					renderVertices,
					mesh.getVertex(triangle.getVertexIndex(2)).getPosition(),
					mesh.getVertex(triangle.getVertexIndex(1)).getPosition(),
					mesh.getVertex(triangle.getVertexIndex(0)).getPosition(),
					mesh.getVertex(triangle.getVertexIndex(2)).getNormal(),
					mesh.getVertex(triangle.getVertexIndex(1)).getNormal(),
					mesh.getVertex(triangle.getVertexIndex(0)).getNormal(),
					color,
					color,
					color
			);
		}
		return new VertexBufferObject(GL2.GL_TRIANGLES, renderVertices);
	}
	
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
	
	public VertexBufferObject createFacettNormalsVBO(ITriangleMesh mesh, double facetteNormalDrawLength, Vector facetteNormalColor) {
		List<RenderVertex> normalVertices = new ArrayList<>();
		for(int t=0; t<mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = mesh.getTriangle(t);
			Vector v0 = mesh.getVertex(triangle.getVertexIndex(0)).getPosition();
			Vector v1 = mesh.getVertex(triangle.getVertexIndex(1)).getPosition();
			Vector v2 = mesh.getVertex(triangle.getVertexIndex(2)).getPosition();
			Vector schwerpunkt = Vector.calculateSchwerpunkt(v0, v1, v2);
			Vector normal = triangle.getNormal();
			
			normalVertices.add(new RenderVertex(schwerpunkt, normal, facetteNormalColor));
			normalVertices.add(new RenderVertex(schwerpunkt.add(normal.multiply(facetteNormalDrawLength)), normal, facetteNormalColor));
		}
		return new VertexBufferObject(GL2.GL_LINES, normalVertices);
	}
	
	public VertexBufferObject createVertexNormalsVBO(ITriangleMesh mesh, double vertexNormalDrawLength, Vector vertexNormalColor) {
		List<RenderVertex> normalVertices = new ArrayList<>();
		for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
			Vertex currentVertex = mesh.getVertex(i);
			Vector position = currentVertex.getPosition();
			Vector normal = currentVertex.getNormal();
			
			normalVertices.add(new RenderVertex(position, normal, vertexNormalColor));
			normalVertices.add(new RenderVertex(position.add(normal.multiply(vertexNormalDrawLength)), normal, vertexNormalColor));
		}
		return new VertexBufferObject(GL2.GL_LINES, normalVertices);
	}
	
	public VertexBufferObject createBorderVBO(ITriangleMesh mesh, Vector borderColor) {
		if(!mesh.hasBorder()) {
			return null;
		}
		
		List<RenderVertex> borderVertices = new ArrayList<>();
		for (Pair<Vertex, Vertex> vertexPair : mesh.getBorderVertices()) {
			Vertex startVertex = vertexPair.getKey();
			Vertex endVertex = vertexPair.getValue();
			
			borderVertices.add(new RenderVertex(startVertex.getPosition(), startVertex.getNormal(), borderColor));
			borderVertices.add(new RenderVertex(endVertex.getPosition(), endVertex.getNormal(), borderColor));
		}
		return new VertexBufferObject(GL2.GL_LINES, borderVertices);
	}
}

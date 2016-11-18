package computergraphics.framework.rendering;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jogamp.opengl.GL2;

import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
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
	
	public VertexBufferObject createWireframeVBO(ITriangleMesh mesh, Vector color) {
		List<Pair<Vertex, Vertex>> wireframeVertexPairs = mesh.getWireframeVertices();
		if(wireframeVertexPairs == null) {
			return null;
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

	public VertexBufferObject createCurveVBO(Curve curve, int curveResolution, Vector curveColor) {
		List<RenderVertex> curveVertices = new ArrayList<>();
		Vector lastPoint = curve.calculatePoint(0);
		double tMax = curve.getMaxT();
		double step = tMax/curveResolution;
		boolean end = true;
		for(double t=step; end; t+=step) {
			// [DoubleTrouble] Aufgrund von der Ungenauigkeit von Double beim wiederholten Aufaddieren
			// läuft man möglicherweise in die Problematik, dass der gewünschte Endpunkt 1 nicht genau
			// erreicht wird und als Folge dessen die Kurve ggf. früher aufhört.
			// Um das zu umgehen wird mit dieser Bedingung t auf 1 gesetzt, damit immer der letzte
			// Punkt auch erreicht wird.
			if(t>=tMax) {
				t = tMax;
				end = false;
			}
			Vector currentPoint = curve.calculatePoint(t);
			curveVertices.add(new RenderVertex(lastPoint, new Vector(3), curveColor));
			curveVertices.add(new RenderVertex(currentPoint, new Vector(3), curveColor));
			lastPoint = currentPoint;
		}
		return new VertexBufferObject(GL2.GL_LINES, curveVertices);
	}

	public VertexBufferObject createControlPointsVBO(Curve curve, Vector controlPointsColor) {
		List<RenderVertex> controlPointsVertices = new ArrayList<>();
		List<Vector> controlPoints = curve.getControlPoints();
		Vector lastPoint = controlPoints.get(0);
		for(int i=1; i<controlPoints.size(); i++) {
			Vector currentPoint = controlPoints.get(i);
			controlPointsVertices.add(new RenderVertex(lastPoint, new Vector(3), controlPointsColor));
			controlPointsVertices.add(new RenderVertex(currentPoint, new Vector(3), controlPointsColor));
			lastPoint = currentPoint;
		}
		return new VertexBufferObject(GL2.GL_LINES, controlPointsVertices);
	}

	public VertexBufferObject createTangentVBO(Curve curve, double tangentT, double drawLength, Vector tangentColor) {
		List<RenderVertex> controlPointsVertices = new ArrayList<>();
		Vector point = curve.calculatePoint(tangentT);
		Vector tangentVector = curve.calculateTangent(tangentT);
		controlPointsVertices.add(new RenderVertex(point, new Vector(3), tangentColor));
		controlPointsVertices.add(new RenderVertex(point.add(tangentVector.multiply(drawLength)), new Vector(3), tangentColor));
		return new VertexBufferObject(GL2.GL_LINES, controlPointsVertices);
	}
}

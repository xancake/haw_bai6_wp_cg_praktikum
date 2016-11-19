package computergraphics.framework.rendering.vbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.rendering.RenderVertex;
import computergraphics.framework.rendering.VertexBufferObject;

/**
 * Eine Klasse zum Erzeugen von {@link VertexBufferObject}s für {@link Curve Kurven}.
 */
public class CurveVBOFactory extends VertexBufferObjectFactory {
	private Curve _curve;
	
	/**
	 * Instanziiert eine neue Fabrik für die übergebene Kurve.
	 * @param curve Die {@link Curve Kurve} für die diese Fabrik {@link VertexBufferObject}s erzeugen soll, nicht {@code null}
	 * @throws NullPointerException, wenn die Kurve {@code null} ist
	 */
	public CurveVBOFactory(Curve curve) {
		_curve = Objects.requireNonNull(curve);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} aus der Kurve mit der übergebenen Auflösung und Farbe.
	 * @param resolution Die Auflösung in der die Kurve dargestellt werden soll
	 * @param color Die Farbe in der die Kurve dargestellt werden soll
	 * @return Ein {@link VertexBufferObject} das die Kurve visualisiert
	 */
	public VertexBufferObject createCurveVBO(int resolution, Vector color) {
		checkColor(color);
		
		List<RenderVertex> curveVertices = new ArrayList<>();
		Vector lastPoint = _curve.calculatePoint(0);
		double tMax = _curve.getMaxT();
		double step = tMax/resolution;
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
			Vector currentPoint = _curve.calculatePoint(t);
			curveVertices.add(new RenderVertex(lastPoint, new Vector(3), color));
			curveVertices.add(new RenderVertex(currentPoint, new Vector(3), color));
			lastPoint = currentPoint;
		}
		return new VertexBufferObject(GL2.GL_LINES, curveVertices);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} das die Kontrollpunkte der Kurve als Polygon darstellt.
	 * @param color Die Farbe des Kontrollpolygons
	 * @return Ein {@link VertexBufferObject} das das Kontrollpolygon visualisiert
	 */
	public VertexBufferObject createControlPolygonVBO(Vector color) {
		checkColor(color);
		
		List<RenderVertex> controlPointsVertices = new ArrayList<>();
		List<Vector> controlPoints = _curve.getControlPoints();
		Vector lastPoint = controlPoints.get(0);
		for(int i=1; i<controlPoints.size(); i++) {
			Vector currentPoint = controlPoints.get(i);
			controlPointsVertices.add(new RenderVertex(lastPoint, new Vector(3), color));
			controlPointsVertices.add(new RenderVertex(currentPoint, new Vector(3), color));
			lastPoint = currentPoint;
		}
		return new VertexBufferObject(GL2.GL_LINES, controlPointsVertices);
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} das die Tangente der Kurve an dem übergebenen Punkt {@code t} mit der
	 * übergebenen Länge und Farbe darstellt.
	 * @param t Der Parameterwert an dem die Tangente dargestellt werden soll
	 * @param drawLength Die Länge in der die Tangente dargestellt werden soll
	 * @param color Die Farbe der Tangente
	 * @return Ein {@link VertexBufferObject} das die Tangente visualisiert
	 */
	public VertexBufferObject createTangentVBO(double t, double drawLength, Vector color) {
		checkColor(color);
		
		List<RenderVertex> controlPointsVertices = new ArrayList<>();
		Vector point = _curve.calculatePoint(t);
		Vector tangentVector = _curve.calculateTangent(t);
		controlPointsVertices.add(new RenderVertex(point, new Vector(3), color));
		controlPointsVertices.add(new RenderVertex(point.add(tangentVector.multiply(drawLength)), new Vector(3), color));
		return new VertexBufferObject(GL2.GL_LINES, controlPointsVertices);
	}
}

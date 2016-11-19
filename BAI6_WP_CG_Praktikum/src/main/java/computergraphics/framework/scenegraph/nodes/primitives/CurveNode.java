package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.Objects;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.rendering.VertexBufferObject;
import computergraphics.framework.rendering.VertexBufferObjectFactory;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class CurveNode extends LeafNode {
	private VertexBufferObjectFactory _factory;
	
	private Curve _curve;
	private VertexBufferObject _curveVBO;
	private Vector _curveColor = new Vector(1, 0, 0, 1);
	private int _curveResolution = 100;
	private boolean _drawCurve;
	
	private VertexBufferObject _controlPointsVBO;
	private Vector _controlPointsColor = new Vector(0, 1, 0, 1);
	private boolean _drawControlPoints;
	
	private VertexBufferObject _tangentVBO;
	private Vector _tangentColor = new Vector(0, 0, 1, 1);
	private double _tangentT;
	private double _tangentDrawLength = 0.25;
	private boolean _drawTangent;
	
	public CurveNode(Curve curve) {
		_factory = new VertexBufferObjectFactory();
		setCurve(curve);
		setDrawCurve(true); // erzeugt implizit eins der benötigten VBOs
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			
			// Curve
			if(isDrawCurve()) {
				_curveVBO.draw(gl);
			}
			
			// ControlPoints
			if(isDrawControlPoints()) {
				_controlPointsVBO.draw(gl);
			}
			
			// Tangent
			if(isDrawTangent()) {
				_tangentVBO.draw(gl);
			}
		}
	}
	
	/**
	 * Gibt die Kurve zurück, die durch diesen Knoten dargestellt wird.
	 * @return Die Kurve
	 */
	public Curve getCurve() {
		return _curve;
	}
	
	/**
	 * Legt die Kurve fest, die durch diesen Knoten dargestellt werden soll.
	 * @param curve Die Kurve, nicht {@code null}
	 * @throws NullPointerException, wenn die übergebene Kurve {@code null} ist
	 */
	public void setCurve(Curve curve) {
		_curve = Objects.requireNonNull(curve);
		
		invalidateVBOs();
		
		if(isDrawCurve()) {
			initCurve(true);
		}
		if(isDrawControlPoints()) {
			initControlPoints(true);
		}
		if(isDrawTangent()) {
			initTangent(true);
		}
	}
	
	/**
	 * Gibt zurück, ob die Kurve gezeichnet wird.
	 * @return {@code true} wenn die Kurve gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawCurve() {
		return _drawCurve;
	}
	
	/**
	 * Legt fest, ob die Kurve gezeichnet werden soll oder nicht.
	 * @param draw {@code true} wenn die Kurve gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawCurve(boolean draw) {
		if(_drawCurve != draw) {
			initCurve(false);
			_drawCurve = draw;
		}
	}
	
	/**
	 * Gibt die Farbe der Kurve zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getCurveColor() {
		return _curveColor;
	}
	
	/**
	 * Legt die Farbe der Kurve fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setCurveColor(Vector color) {
		if(_curveColor == null || !_curveColor.equals(color)) {
			_curveColor = checkColorVektor(color);
			initCurve(true);
		}
	}
	
	/**
	 * Gibt die Auflösung der Kurve zurück.
	 * @return Die Auflösung
	 */
	public int getCurveResolution() {
		return _curveResolution;
	}
	
	/**
	 * Legt die Auflösung der Kurve fest.
	 * @param curveResolution Die Auflösung
	 */
	public void setCurveResolution(int curveResolution) {
		if(curveResolution < 1) {
			throw new IllegalArgumentException("Die abgegebene Auflösung (" + curveResolution + ") muss größer als 0 sein!");
		}
		if(_curveResolution != curveResolution) {
			_curveResolution = curveResolution;
			initCurve(true);
		}
	}
	
	/**
	 * Gibt zurück, ob die Kontrollpunkte gezeichnet werden.
	 * @return {@code true} wenn die Kontrollpunkte gezeichnet werden, ansonsten {@code false}.
	 */
	public boolean isDrawControlPoints() {
		return _drawControlPoints;
	}
	
	/**
	 * Legt fest, ob die Kontrollpunkte gezeichnet werden sollen.
	 * @param draw {@code true} wenn die Kontrollpunkte gezeichnet werden, ansonsten {@code false}.
	 */
	public void setDrawControlPoints(boolean draw) {
		if(_drawControlPoints != draw) {
			initControlPoints(false);
			_drawControlPoints = draw;
		}
	}
	
	/**
	 * Gibt die Farbe der Kontrollpunkte zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getControlPointsColor() {
		return _controlPointsColor;
	}
	
	/**
	 * Legt die Farbe der Kontrollpunkte fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setControlPointsColor(Vector color) {
		if(!_controlPointsColor.equals(color)) {
			_controlPointsColor = checkColorVektor(color);
			initControlPoints(true);
		}
	}
	
	/**
	 * Gibt zurück, ob die Tangente gezeichnet wird.
	 * @return {@code true}, wenn die Tangente gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawTangent() {
		return _drawTangent;
	}
	
	/**
	 * Legt fest, ob die Tangente gezeichnet werden soll oder nicht.
	 * @param draw {@code true}, wenn die Tangente gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawTangent(boolean draw) {
		if(_drawTangent != draw) {
			initTangent(false);
			_drawTangent = draw;
		}
	}
	
	/**
	 * Gibt zurück an welcher Stelle die Tangente auf der Kurve gezeichnet wird.
	 * <p>Die Tangente wird nur gezeichnet, wenn {@link #isDrawTangent()} {@code true} zurück liefert.
	 * @return Die Stelle der Tangente auf der Kurve
	 * @see #isDrawTangent()
	 */
	public double getTangentT() {
		return _tangentT;
	}
	
	/**
	 * Legt die Stelle fest, an der die Tangente auf der Kurve gezeichnet werden soll.
	 * <p>Das hat zur Folge, dass die Tangente neu berechnet werden muss.
	 * <p>Mittels {@link #setDrawTangent(boolean)} wird festgelegt, ob die Tangente gezeichnet werden soll.
	 * @param t Die Stelle der Tangente auf der Kurve. Muss zwischen 0 und 1 liegen
	 * @see #setDrawTangent(boolean)
	 */
	public void setTangentT(double t) {
		if(t < 0 || t > _curve.getMaxT()) {
			throw new IllegalArgumentException("Die angegebene Stelle (" + t + ") muss zwischen 0 und " + _curve.getMaxT() + " liegen!");
		}
		if(t != _tangentT) {
			_tangentT = t;
			initTangent(true);
		}
	}
	
	/**
	 * Gibt zurück mit welcher Länge die Tangente gezeichnet wird.
	 * <p>Die Tangente wird nur gezeichnet, wenn {@link #isDrawTangent()} {@code true} zurück liefert.
	 * @return Die Darstellungslänge der Tangente
	 * @see #isDrawTangent()
	 */
	public double getTangentDrawLength() {
		return _tangentDrawLength;
	}
	
	/**
	 * Legt die Länge fest, mit der die Tangente gezeichnet werden soll.
	 * <p>Das hat zur Folge, dass die Tangente neu berechnet werden muss.
	 * <p>Mittels {@link #setDrawTangent(boolean)} wird festgelegt, ob die Tangente gezeichnet werden soll.
	 * @param length Die Darstellungslänge der Tangente
	 * @see #setDrawTangent(boolean)
	 */
	public void setTangentDrawLength(double length) {
		if(length < 0 || !Double.isFinite(length)) {
			throw new IllegalArgumentException("Die angegebene Länge (" + length + ") muss positiv und endlich sein!");
		}
		if(length != _tangentDrawLength) {
			_tangentDrawLength = length;
			initTangent(true);
		}
	}
	
	/**
	 * Gibt die Farbe der Tangente zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getTangentColor() {
		return _tangentColor;
	}
	
	/**
	 * Legt die Farbe der Tangente fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setTangentColor(Vector color) {
		if(!_tangentColor.equals(color)) {
			_tangentColor = checkColorVektor(color);
			initTangent(true);
		}
	}
	
	/**
	 * Prüft ob der übergebene Vektor einen gültigen Farbvektor beschreibt und gibt eine Kopie des Vektors zurück,
	 * wenn das der Fall ist. Ansonsten wird eine {@link IllegalArgumentException} geworfen.
	 * <p>Ein gültiger Farbvektor hat entweder drei (R,G,B) oder vier (R,G,B,A) Dimensionen.
	 * @param color Der zu prüfende Farbvektor
	 * @return Eine Kopie des erfolgreich geprüften Vektors
	 * @throws IllegalArgumentException Wenn der Vektor nicht drei- oder vierdimensional ist
	 */
	private Vector checkColorVektor(Vector color) {
		switch(Objects.requireNonNull(color).getDimension()) {
			case 3: return new Vector(color.x(), color.y(), color.z(), 1);
			case 4: return new Vector(color);
			default:
				throw new IllegalArgumentException("Die Farbe muss als drei- oder vierdimensionaler Vektor übergeben werden!");
		}
	}
	
	private void initCurve(boolean recalculate) {
		if(recalculate || _curveVBO == null) {
			_curveVBO = _factory.createCurveVBO(_curve, _curveResolution, _curveColor);
		}
	}
	
	private void initControlPoints(boolean recalculate) {
		if(recalculate || _controlPointsVBO == null) {
			_controlPointsVBO = _factory.createControlPointsVBO(_curve, _controlPointsColor);
		}
	}
	
	private void initTangent(boolean recalculate) {
		if(recalculate || _tangentVBO == null) {
			_tangentVBO = _factory.createTangentVBO(_curve, _tangentT, _tangentDrawLength, _tangentColor);
		}
	}
	
	private void invalidateVBOs() {
		_curveVBO = null;
		_controlPointsVBO = null;
		_tangentVBO = null;
	}
}

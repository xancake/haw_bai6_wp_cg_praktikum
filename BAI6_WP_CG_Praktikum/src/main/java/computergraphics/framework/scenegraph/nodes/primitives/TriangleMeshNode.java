package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.rendering.VertexBufferObject;
import computergraphics.framework.rendering.VertexBufferObjectFactory;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class TriangleMeshNode extends LeafNode {
	private VertexBufferObjectFactory _factory;
	
	private ITriangleMesh _mesh;
	private VertexBufferObject _meshWithFacetteNormals;
	private VertexBufferObject _meshWithVertexNormals;
	private Vector _meshColor;
	private boolean _drawMesh = true;
	private boolean _toggleMeshVertexNormals;
	
	private VertexBufferObject _facetteNormals;
	private double _facetteNormalDrawLength = 0.02;
	private Vector _facetteNormalColor = new Vector(0, 0, 1, 1);
	private boolean _drawFacetteNormals;
	
	private VertexBufferObject _vertexNormals;
	private double _vertexNormalDrawLength = 0.02;
	private Vector _vertexNormalColor = new Vector(0, 1, 0, 1);
	private boolean _drawVertexNormals;
	
	private VertexBufferObject _wireframe;
	private Vector _wireframeColor = new Vector(0, 0, 0, 1);
	private boolean _drawWireframe;
	
	private VertexBufferObject _border;
	private Vector _borderColor = new Vector(0, 0, 0, 1);
	private boolean _drawBorder;
	
	public TriangleMeshNode(ITriangleMesh mesh, Vector color) {
		_mesh = Objects.requireNonNull(mesh);
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
		_meshColor = color;
		_factory = new VertexBufferObjectFactory();
		_meshWithFacetteNormals = _factory.createMeshVBOWithTriangleNormals(_mesh, _meshColor);
		_meshWithVertexNormals = _factory.createMeshVBOWithVertexNormals(_mesh, _meshColor);
		_facetteNormals = _factory.createFacettNormalsVBO(_mesh, _facetteNormalDrawLength, _facetteNormalColor);
		_vertexNormals = _factory.createVertexNormalsVBO(_mesh, _vertexNormalDrawLength, _vertexNormalColor);
		_wireframe = _factory.createWireframeVBO(_mesh, _wireframeColor);
		_border = _factory.createBorderVBO(_mesh, _borderColor);
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			
			// Mesh
			if(isDrawMesh()) {
				if(isDrawMeshVertexNormals()) {
					_meshWithVertexNormals.draw(gl);
				} else {
					_meshWithFacetteNormals.draw(gl);
				}
			}
			
			// Normalen
			if(isDrawFacetteNormals()) {
				_facetteNormals.draw(gl);
			}
			if(isDrawVertexNormals()) {
				_vertexNormals.draw(gl);
			}
			
			// Wireframe
			if(_wireframe!=null && isDrawWireframe()) {
				_wireframe.draw(gl);
			}
			
			// Rand
			if(_border!=null && isDrawBorder()) {
				_border.draw(gl);
			}
		}
	}
	
	/**
	 * Gibt zurück, ob das Mesh gezeichnet wird.
	 * @return {@code true} wenn das Mesh gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawMesh() {
		return _drawMesh;
	}
	
	/**
	 * Legt fest, ob das Mesh gezeichnet werden soll oder nicht.
	 * @param draw {@code true} wenn das Mesh gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawMesh(boolean draw) {
		_drawMesh = draw;
	}
	
	/**
	 * Gibt die Farbe des Meshes zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getMeshColor() {
		return _meshColor;
	}
	
	/**
	 * Legt die Farbe des Meshes fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setMeshColor(Vector color) {
		if(!_meshColor.equals(color)) {
			_meshColor = checkColorVektor(color);
			_meshWithFacetteNormals = _factory.createMeshVBOWithTriangleNormals(_mesh, _meshColor);
			_meshWithVertexNormals = _factory.createMeshVBOWithVertexNormals(_mesh, _meshColor);
		}
	}
	
	/**
	 * Gibt zurück, ob das Mesh anhand der Vertexnormalen oder Facettennormalen gezeichnet wird.
	 * @return {@code true} wenn es nach den Vertexnormalen gezeichnet wird, ansonsten {@code false} für Facettennormalen
	 */
	public boolean isDrawMeshVertexNormals() {
		return _toggleMeshVertexNormals;
	}
	
	/**
	 * Legt fest, ob das Mesh anhand der Vertexnormalen gezeichnet werden soll oder anhand der Facettennormalen.
	 * @param draw {@code true} wenn es nach den Vertexnormalen gezeichnet werden soll, ansonsten {@code false} für Facettennormalen
	 */
	public void setDrawMeshVertexNormals(boolean draw) {
		_toggleMeshVertexNormals = draw;
	}
	
	/**
	 * Gibt zurück, ob die Facettennormalen gezeichnet werden.
	 * @return {@code true}, wenn die Normalen gezeichnet werden, ansonsten {@code false}
	 */
	public boolean isDrawFacetteNormals() {
		return _drawFacetteNormals;
	}
	
	/**
	 * Legt fest, ob die Facettennormalen gezeichnet werden sollen oder nicht.
	 * @param draw {@code true}, wenn die Normalen gezeichnet werden sollen, ansonsten {@code false}
	 */
	public void setDrawFacetteNormals(boolean draw) {
		_drawFacetteNormals = draw;
	}
	
	/**
	 * Gibt zurück mit welcher Länge die Facettennormalen gezeichnet werden.
	 * <p>Die Normalen werden nur gezeichnet, wenn {@link #isDrawFacetteNormals()} {@code true} zurück liefert.
	 * @return Die Darstellungslänge der Facettennormalen
	 * @see #isDrawFacetteNormals()
	 */
	public double getFacetteNormalDrawLength() {
		return _facetteNormalDrawLength;
	}
	
	/**
	 * Legt die Länge fest, mit der die Facettennormalen gezeichnet werden sollen.
	 * <p>Das hat zur Folge, dass die Facettennormalen neu berechnet werden müssen.
	 * <p>Mittels {@link #setDrawFacetteNormals(boolean)} wird festgelegt, ob die Normalen gezeichnet werden sollen.
	 * @param length Die Darstellungslänge der Facettennormalen
	 * @see #setDrawFacetteNormals(boolean)
	 */
	public void setFacetteNormalDrawLength(double length) {
		if(length < 0) {
			throw new IllegalArgumentException("Die angegebene Länge muss positiv sein!");
		}
		if(length != _facetteNormalDrawLength) {
			_facetteNormalDrawLength = length;
			_facetteNormals = _factory.createFacettNormalsVBO(_mesh, _facetteNormalDrawLength, _facetteNormalColor);
		}
	}
	
	/**
	 * Gibt die Farbe der Facettennormalen zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getFacetteNormalColor() {
		return _facetteNormalColor;
	}
	
	/**
	 * Legt die Farbe der Facettennormalen fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setFacetteNormalColor(Vector color) {
		if(!_facetteNormalColor.equals(color)) {
    		_facetteNormalColor = checkColorVektor(color);
			_facetteNormals = _factory.createFacettNormalsVBO(_mesh, _facetteNormalDrawLength, _facetteNormalColor);
		}
	}
	
	/**
	 * Gibt zurück, ob die Vertexnormalen gezeichnet werden.
	 * @return {@code true}, wenn die Normalen gezeichnet werden, ansonsten {@code false}
	 */
	public boolean isDrawVertexNormals() {
		return _drawVertexNormals;
	}
	
	/**
	 * Legt fest, ob die Vertexnormalen gezeichnet werden sollen oder nicht.
	 * @param draw {@code true}, wenn die Normalen gezeichnet werden sollen, ansonsten {@code false}
	 */
	public void setDrawVertexNormals(boolean draw) {
		_drawVertexNormals = draw;
	}
	
	/**
	 * Gibt zurück mit welcher Länge die Vertexnormalen gezeichnet werden.
	 * <p>Die Normalen werden nur gezeichnet, wenn {@link #isDrawVertexNormals()} {@code true} zurück liefert.
	 * @return Die Darstellungslänge der Vertexnormalen
	 * @see #isDrawVertexNormals()
	 */
	public double getVertexNormalDrawLength() {
		return _vertexNormalDrawLength;
	}
	
	/**
	 * Legt die Länge fest, mit der die Vertexnormalen gezeichnet werden sollen.
	 * <p>Das hat zur Folge, dass die Vertexnormalen neu berechnet werden müssen.
	 * <p>Mittels {@link #setDrawVertexNormals(boolean)} wird festgelegt, ob die Normalen gezeichnet werden sollen.
	 * @param length Die Darstellungslänge der Vertexnormalen
	 * @see #setDrawVertexNormals(boolean)
	 */
	public void setVertexNormalDrawLength(double length) {
		if(length < 0) {
			throw new IllegalArgumentException("Die angegebene Länge muss positiv sein!");
		}
		if(length != _vertexNormalDrawLength) {
			_vertexNormalDrawLength = length;
			_vertexNormals = _factory.createVertexNormalsVBO(_mesh, _vertexNormalDrawLength, _vertexNormalColor);
		}
	}
	
	/**
	 * Gibt die Farbe der Vertexnormalen zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getVertexNormalColor() {
		return _vertexNormalColor;
	}
	
	/**
	 * Legt die Farbe der Vertexnormalen fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setVertexNormalColor(Vector color) {
		if(!_vertexNormalColor.equals(color)) {
			_vertexNormalColor = checkColorVektor(color);
			_vertexNormals = _factory.createVertexNormalsVBO(_mesh, _vertexNormalDrawLength, _vertexNormalColor);
		}
	}
	
	/**
	 * Gibt zurück, ob das Wireframe des Meshes gezeichnet wird.
	 * @return {@code true} wenn das Wireframe gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawWireframe() {
		return _drawWireframe;
	}
	
	/**
	 * Legt fest, ob das Wireframe gezeichnet werden soll oder nicht.
	 * @param draw {@code true} wenn das Wireframe gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawWireframe(boolean draw) {
		_drawWireframe = draw;
	}
	
	/**
	 * Gibt die Farbe des Wireframes zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getWireframeColor() {
		return _wireframeColor;
	}
	
	/**
	 * Legt die Farbe des Wireframes fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setWireframeColor(Vector color) {
		if(!_wireframeColor.equals(color)) {
			_wireframeColor = checkColorVektor(color);
			_wireframe = _factory.createWireframeVBO(_mesh, _wireframeColor);
		}
	}
	
	/**
	 * Gibt zurück, ob der Rand gezeichnet wird.
	 * @return {@code true} wenn der Rand gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawBorder() {
		return _drawBorder;
	}
	
	/**
	 * Legt fest, ob der Rand gezeichnet werden soll oder nicht.
	 * @param draw {@code true} wenn der Rand gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawBorder(boolean draw) {
		_drawBorder = draw;
	}
	
	/**
	 * Gibt die Farbe des Rands zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getBorderColor() {
		return _borderColor;
	}
	
	/**
	 * Legt die Farbe des Rands fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setBorderColor(Vector color) {
		if(!_borderColor.equals(color)) {
			_borderColor = checkColorVektor(color);
			_border = _factory.createBorderVBO(_mesh, _borderColor);
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
}

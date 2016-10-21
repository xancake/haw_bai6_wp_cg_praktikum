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
	private Vector _color;
	private VertexBufferObject _vboWithFacetteNormals;
	private VertexBufferObject _vboWithVertexNormals;
	private boolean _drawMeshVertexNormals;
	
	private VertexBufferObject _facetteNormals;
	private double _facetteNormalDrawLength = 0.02;
	private Vector _facetteNormalColor = new Vector(0, 0, 1, 1);
	private boolean _drawFacetteNormals;
	
	private VertexBufferObject _vertexNormals;
	private double _vertexNormalDrawLength = 0.02;
	private Vector _vertexNormalColor = new Vector(0, 1, 0, 1);
	private boolean _drawVertexNormals;
	
	private VertexBufferObject _border;
	private Vector _borderColor = new Vector(0, 0, 0, 1);
	private boolean _drawBorder;
	
	public TriangleMeshNode(ITriangleMesh mesh, Vector color) {
		_mesh = Objects.requireNonNull(mesh);
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
		_color = color;
		_factory = new VertexBufferObjectFactory();
		_vboWithFacetteNormals = _factory.createMeshVBOWithTriangleNormals(_mesh, _color);
		_vboWithVertexNormals = _factory.createMeshVBOWithVertexNormals(_mesh, _color);
		_facetteNormals = _factory.createFacettNormalsVBO(_mesh, _facetteNormalDrawLength, _facetteNormalColor);
		_vertexNormals = _factory.createVertexNormalsVBO(_mesh, _vertexNormalDrawLength, _vertexNormalColor);
		_border = _factory.createBorderVBO(_mesh, _borderColor);
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			
			if(_drawMeshVertexNormals) {
				_vboWithVertexNormals.draw(gl);
			} else {
				_vboWithFacetteNormals.draw(gl);
			}
			
			// Normalen
			if(isDrawFacetteNormals()) {
				_facetteNormals.draw(gl);
			}
			if(isDrawVertexNormals()) {
				_vertexNormals.draw(gl);
			}

			// Rand
			if(isDrawBorder()) {
				_border.draw(gl);
			}
		}
	}
	
	/**
	 * Gibt zurück, ob das Mesh anhand der Vertexnormalen oder Facettennormalen gezeichnet wird.
	 * @return {@code true} wenn es nach den Vertexnormalen gezeichnet wird, ansonsten {@code false} für Facettennormalen
	 */
	public boolean isDrawMeshVertexNormals() {
		return _drawMeshVertexNormals;
	}
	
	/**
	 * Legt fest, ob das Mesh anhand der Vertexnormalen gezeichnet werden soll oder anhand der Facettennormalen.
	 * @param draw {@code true} wenn es nach den Vertexnormalen gezeichnet werden soll, ansonsten {@code false} für Facettennormalen
	 */
	public void setDrawMeshVertexNormals(boolean draw) {
		_drawMeshVertexNormals = draw;
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
	 * Gibt zurück, ob der Rand gezeichnet werden.
	 * @return {@code true}, wenn der Rand gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawBorder() {
		return _drawBorder;
	}
	
	/**
	 * Legt fest, ob der Rand gezeichnet werden soll oder nicht.
	 * @param draw {@code true}, wenn der Rand gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawBorder(boolean draw) {
		_drawBorder = draw;
	}
}

package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.rendering.CGUtils;
import computergraphics.framework.rendering.vbo.MeshVBOFactory;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.rendering.vbo.VertexBufferObjectFactory;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class TriangleMeshNode extends LeafNode {
	private MeshVBOFactory _factory;
	
	private ITriangleMesh _mesh;
	private VertexBufferObject _meshWithFacetteNormals;
	private VertexBufferObject _meshWithVertexNormals;
	private Vector _meshColor = new Vector(1, 0, 0, 1);
	private boolean _drawMesh;
	private boolean _toggleMeshVertexNormals;
	
	private VertexBufferObject _facetteNormals;
	private double _facetteNormalDrawLength = 0.05;
	private Vector _facetteNormalColor = new Vector(0, 0, 1, 1);
	private boolean _drawFacetteNormals;
	
	private VertexBufferObject _vertexNormals;
	private double _vertexNormalDrawLength = 0.05;
	private Vector _vertexNormalColor = new Vector(0, 1, 0, 1);
	private boolean _drawVertexNormals;
	
	private VertexBufferObject _wireframe;
	private Vector _wireframeColor = new Vector(0, 0, 0, 1);
	private boolean _drawWireframe;
	
	private VertexBufferObject _border;
	private Vector _borderColor = new Vector(0, 0, 0, 1);
	private boolean _drawBorder;
	
	private VertexBufferObject _silhouette;
	private Vector _silhouetteViewpoint;
	private Vector _silhouetteColor = new Vector(0, 0, 0, 1);
	private boolean _drawSilhouette;
	
	public TriangleMeshNode(ITriangleMesh mesh, Vector color) {
		setMesh(mesh);
		setMeshColor(color);
		setDrawMesh(true); // erzeugt auch implizit eins der benötigten VBOs
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
			if(isDrawWireframe()) {
				_wireframe.draw(gl);
			}
			
			// Rand
			if(isDrawBorder()) {
				_border.draw(gl);
			}
			
			// Silhouette
			if(isDrawSilhouette()) {
				_silhouette.draw(gl);
			}
		}
	}
	
	/**
	 * Gibt das Mesh zurück, das durch diesen Knoten dargestellt wird.
	 * @return Das Mesh
	 */
	public ITriangleMesh getMesh() {
		return _mesh;
	}
	
	/**
	 * Legt das Mesh fest, das durch diesen Knoten dargestellt werden soll.
	 * <p>Der Aufgruf hat zur Folge, dass sämtliche {@link VertexBufferObject}s invalidiert und neu berechnet werden.
	 * Welche VBOs neu erzeugt werden hängt davon ab, welche Darstellungen gezeichnet werden sollen.
	 * @param mesh Das darzustellende Mesh
	 * @see #isDrawMesh()
	 * @see #isDrawFacetteNormals()
	 * @see #isDrawVertexNormals()
	 * @see #isDrawWireframe()
	 * @see #isDrawBorder()
	 */
	public void setMesh(ITriangleMesh mesh) {
		_mesh = Objects.requireNonNull(mesh);
		
		_factory = VertexBufferObjectFactory.forMesh(_mesh);
		invalidateVBOs();
		
		if(isDrawMesh()) {
			initMesh(true);
		}
		if(isDrawFacetteNormals()) {
			initFacetteNormals(true);
		}
		if(isDrawVertexNormals()) {
			initVertexNormals(true);
		}
		if(isDrawWireframe()) {
			initWireframe(true);
		}
		if(isDrawBorder()) {
			initBorder(true);
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
		if(_drawMesh != draw) {
			initMesh(false);
			_drawMesh = draw;
		}
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
		if(_meshColor == null || !_meshColor.equals(color)) {
			_meshColor = CGUtils.checkColorVector(color);
			initMesh(true);
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
		if(_toggleMeshVertexNormals != draw) {
			initMeshWithVertexNormals(false);
			initMeshWithFacetteNormals(false);
			_toggleMeshVertexNormals = draw;
		}
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
		if(_drawFacetteNormals != draw) {
			initFacetteNormals(false);
			_drawFacetteNormals = draw;
		}
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
			initFacetteNormals(true);
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
    		_facetteNormalColor = CGUtils.checkColorVector(color);
			initFacetteNormals(true);
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
		if(_drawVertexNormals != draw) {
			initVertexNormals(false);
			_drawVertexNormals = draw;
		}
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
			initVertexNormals(true);
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
			_vertexNormalColor = CGUtils.checkColorVector(color);
			initVertexNormals(true);
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
		if(_drawWireframe != draw) {
			initWireframe(false);
			_drawWireframe = draw;
		}
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
			_wireframeColor = CGUtils.checkColorVector(color);
			initWireframe(true);
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
		if(_drawBorder != draw) {
			initBorder(false);
			_drawBorder = draw;
		}
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
			_borderColor = CGUtils.checkColorVector(color);
			initBorder(true);
		}
	}
	
	/**
	 * Gibt zurück, ob die Silhouette gezeichnet wird.
	 * @return {@code true} wenn die Silhouette gezeichnet wird, ansonsten {@code false}
	 */
	public boolean isDrawSilhouette() {
		return _drawSilhouette;
	}
	
	/**
	 * Legt fest, ob die Silhouette gezeichnet werden soll oder nicht.
	 * @param draw {@code true} wenn die Silhouette gezeichnet werden soll, ansonsten {@code false}
	 */
	public void setDrawSilhouette(boolean draw) {
		if(_drawSilhouette != draw) {
			initSilhouette(false);
			_drawSilhouette = draw;
		}
	}
	
	/**
	 * Gibt den Sichtpunkt zurück, von dem aus die Silhouette berechnet wird.
	 * @return Der Sichtpunkt
	 */
	public Vector getSilhouetteViewpoint() {
		return _silhouetteViewpoint;
	}
	
	/**
	 * Legt den Sichtpunkt fest, von dem aus die Silhouette berechnet wird.
	 * @param viewpoint Der Sichtpunkt, darf nicht {@code null} und muss {@link Vector#getDimension() dreidimensional} sein
	 * @throws NullPointerException Wenn der Sichtpunkt {@code null} ist
	 * @throws IllegalArgumentException Wenn der Sichtpunkt nicht dreidimensional ist
	 */
	public void setSilhouetteViewpoint(Vector viewpoint) {
		if(viewpoint.getDimension() != 3) {
			throw new IllegalArgumentException("Die Dimension des Sichtpunkts muss 3 sein (war " + viewpoint.getDimension() + ").");
		}
		if(_silhouetteViewpoint == null || !_silhouetteViewpoint.equals(viewpoint)) {
			_silhouetteViewpoint = viewpoint;
			if(isDrawSilhouette()) {
				initSilhouette(true);
			}
		}
	}
	
	/**
	 * Gibt die Farbe der Silhouette zurück.
	 * @return Die Farbe als vierdimensionaler Vektor (R,G,B,A)
	 */
	public Vector getSilhouetteColor() {
		return _silhouetteColor;
	}
	
	/**
	 * Legt die Farbe der Silhouette fest.
	 * @param color Ein drei- oder vierdimensionaler Vektor, der die Farbe angibt (R,G,B[,A])
	 */
	public void setSilhouetteColor(Vector color) {
		if(!_silhouetteColor.equals(color)) {
			_silhouetteColor = CGUtils.checkColorVector(color);
			initSilhouette(true);
		}
	}
	
	private void initMesh(boolean recalculate) {
		if(isDrawMeshVertexNormals()) {
			initMeshWithVertexNormals(recalculate);
		} else {
			initMeshWithFacetteNormals(recalculate);
		}
	}
	
	private void initMeshWithFacetteNormals(boolean recalculate) {
		if(recalculate || _meshWithFacetteNormals == null) {
			_meshWithFacetteNormals = _factory.createMeshVBOWithTriangleNormals(_meshColor);
		}
	}
	
	private void initMeshWithVertexNormals(boolean recalculate) {
		if(recalculate || _meshWithVertexNormals == null) {
			_meshWithVertexNormals = _factory.createMeshVBOWithVertexNormals(_meshColor);
		}
	}
	
	private void initFacetteNormals(boolean recalculate) {
		if(recalculate || _facetteNormals == null) {
			_facetteNormals = _factory.createFacettNormalsVBO(_facetteNormalDrawLength, _facetteNormalColor);
		}
	}
	
	private void initVertexNormals(boolean recalculate) {
		if(recalculate || _vertexNormals == null) {
			_vertexNormals = _factory.createVertexNormalsVBO(_vertexNormalDrawLength, _vertexNormalColor);
		}
	}
	
	private void initWireframe(boolean recalculate) {
		if(recalculate || _wireframe == null) {
			_wireframe = _factory.createWireframeVBO(_wireframeColor);
		}
	}
	
	private void initBorder(boolean recalculate) {
		if(recalculate || _border == null) {
			_border = _factory.createBorderVBO(_borderColor);
		}
	}
	
	private void initSilhouette(boolean recalculate) {
		if(recalculate || _silhouette == null) {
			_silhouette = _factory.createSilhouetteVBO(_silhouetteViewpoint, _silhouetteColor);
		}
	}
	
	private void invalidateVBOs() {
		_meshWithVertexNormals = null;
		_meshWithFacetteNormals = null;
		_facetteNormals = null;
		_vertexNormals = null;
		_wireframe = null;
		_border = null;
		_silhouette = null;
	}
}
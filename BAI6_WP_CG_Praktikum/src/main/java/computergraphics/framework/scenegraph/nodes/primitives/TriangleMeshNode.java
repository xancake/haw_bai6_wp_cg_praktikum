package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.mesh.Vertex;
import computergraphics.framework.rendering.RenderVertex;
import computergraphics.framework.rendering.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class TriangleMeshNode extends LeafNode {
	private ITriangleMesh _mesh;
	private Vector _color;
	private VertexBufferObject _vbo;
	
	private VertexBufferObject _facetteNormals;
	private double _facetteNormalDrawLength = 0.02;
	private Vector _facetteNormalColor = new Vector(0, 0, 1, 1);
	private boolean _drawFacetteNormals;
	
	private VertexBufferObject _vertexNormals;
	private double _vertexNormalDrawLength = 0.02;
	private Vector _vertexNormalColor = new Vector(0, 1, 0, 1);
	private boolean _drawVertexNormals;
	
	public TriangleMeshNode(ITriangleMesh mesh, Vector color) {
		_mesh = Objects.requireNonNull(mesh);
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
		_color = color;
		_vbo = new VertexBufferObject(_mesh, _color);
		_facetteNormals = createFacettNormalsVBO();
		_vertexNormals = createVertexNormalsVBO();
	}
	
	private VertexBufferObject createFacettNormalsVBO() {
		List<RenderVertex> normalVertices = new ArrayList<>();
		for(int t=0; t<_mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = _mesh.getTriangle(t);
			Vector v0 = _mesh.getVertex(triangle.getVertexIndex(0)).getPosition();
			Vector v1 = _mesh.getVertex(triangle.getVertexIndex(1)).getPosition();
			Vector v2 = _mesh.getVertex(triangle.getVertexIndex(2)).getPosition();
			Vector schwerpunkt = Vector.calculateSchwerpunkt(v0, v1, v2);
			Vector normal = triangle.getNormal();
			
			normalVertices.add(new RenderVertex(schwerpunkt, normal, _facetteNormalColor));
			normalVertices.add(new RenderVertex(schwerpunkt.add(normal.multiply(_facetteNormalDrawLength)), normal, _facetteNormalColor));
		}
		return new VertexBufferObject(GL2.GL_LINES, normalVertices);
	}
	
	private VertexBufferObject createVertexNormalsVBO() {
		List<RenderVertex> normalVertices = new ArrayList<>();
		for (int i = 0; i < _mesh.getNumberOfVertices(); i++) {
			Vertex currentVertex = _mesh.getVertex(i);
			Vector position = currentVertex.getPosition();
			Vector normal = currentVertex.getNormal();
			normalVertices.add(new RenderVertex(position, normal, _vertexNormalColor));
			normalVertices.add(new RenderVertex(position.add(normal.multiply(_vertexNormalDrawLength)), normal, _vertexNormalColor));
		}
		return new VertexBufferObject(GL2.GL_LINES, normalVertices);
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			_vbo.draw(gl);
			if(isDrawFacetteNormals()) {
				_facetteNormals.draw(gl);
			}
			if(isDrawVertexNormals()) {
				_vertexNormals.draw(gl);
			}
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
			_facetteNormals = createFacettNormalsVBO();
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
			_vertexNormals = createVertexNormalsVBO();
		}
	}
}

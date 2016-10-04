package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.rendering.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class TriangleMeshNode extends LeafNode {
	private ITriangleMesh _mesh;
	private Vector _color;
	private VertexBufferObject _vbo;
	
	private double _triangleNormalDrawLength = 0.02;
	private boolean _drawTriangleNormals;
	
	public TriangleMeshNode(ITriangleMesh mesh, Vector color) {
		_mesh = Objects.requireNonNull(mesh);
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
		_color = color;
		_vbo = new VertexBufferObject(_mesh, _color);
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			_vbo.draw(gl);
			if(isDrawTriangleNormals()) {
				drawTriangleNormals(gl);
			}
		}
	}
	
	private void drawTriangleNormals(GL2 gl) {
		for(int t=0; t<_mesh.getNumberOfTriangles(); t++) {
			Triangle triangle = _mesh.getTriangle(t);
			Vector v0 = _mesh.getVertex(triangle.getVertexIndex(0)).getPosition();
			Vector v1 = _mesh.getVertex(triangle.getVertexIndex(1)).getPosition();
			Vector v2 = _mesh.getVertex(triangle.getVertexIndex(2)).getPosition();
			Vector schwerpunkt = Vector.calculateSchwerpunkt(v0, v1, v2);
			Vector normal = triangle.getNormal();
			double normalDrawLength = _triangleNormalDrawLength;
			
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex3d(schwerpunkt.x(), schwerpunkt.y(), schwerpunkt.z());
			gl.glVertex3d(
					schwerpunkt.x()+normalDrawLength*normal.x(),
					schwerpunkt.y()+normalDrawLength*normal.y(),
					schwerpunkt.z()+normalDrawLength*normal.z()
			);
			gl.glEnd();
		}
	}
	
	/**
	 * Gibt zurück, ob die Dreiecksnormalen gezeichnet werden.
	 * @return {@code true}, wenn die Normalen gezeichnet werden, ansonsten {@code false}
	 */
	public boolean isDrawTriangleNormals() {
		return _drawTriangleNormals;
	}
	
	/**
	 * Legt fest, ob die Dreiecksnormalen gezeichnet werden sollen oder nicht.
	 * @param draw {@code true}, wenn die Normalen gezeichnet werden sollen, ansonsten {@code false}
	 */
	public void setDrawTriangleNormals(boolean draw) {
		_drawTriangleNormals = draw;
	}
	
	/**
	 * Gibt zurück mit welcher Länge die Dreiecksnormalen gezeichnet werden.
	 * <p>Die Normalen werden nur gezeichnet, wenn {@link #isDrawTriangleNormals()} {@code true} zurück liefert.
	 * @return Die Darstellungslänge der Dreiecksnormalen
	 * @see #isDrawTriangleNormals()
	 */
	public double getTriangleNormalDrawLength() {
		return _triangleNormalDrawLength;
	}
	
	/**
	 * Legt die Länge fest, mit der die Dreiecksnormalen gezeichnet werden sollen.
	 * <p>Mittels {@link #setDrawTriangleNormals(boolean)} wird festgelegt, ob die Normalen gezeichnet werden sollen.
	 * @param length Die Darstellungslänge der Dreiecksnormalen
	 * @see #setDrawTriangleNormals(boolean)
	 */
	public void setTriangleNormalDrawLength(double length) {
		if(length < 0) {
			throw new IllegalArgumentException("Die angegebene Länge muss positiv sein!");
		}
		_triangleNormalDrawLength = length;
	}
}

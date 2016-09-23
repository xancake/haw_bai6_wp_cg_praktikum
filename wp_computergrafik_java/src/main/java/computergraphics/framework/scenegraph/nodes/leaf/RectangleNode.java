package computergraphics.framework.scenegraph.nodes.leaf;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.RenderVertex;
import computergraphics.framework.rendering.VertexBufferObject;

public class RectangleNode extends LeafNode {
	
	private double _width;
	private double _length;
	private double _height;
	
	private VertexBufferObject _vbo;
	
	public RectangleNode(double width, double length, double height) {
		_width = width;
		_length = length;
		_height = height;
		_vbo = createVBO();
	}
	
	private VertexBufferObject createVBO() {
		Vector p0 = new Vector(-_width, -_length, -_height);
		Vector p1 = new Vector( _width, -_length, -_height);
		Vector p2 = new Vector( _width,  _length, -_height);
		Vector p3 = new Vector(-_width,  _length, -_height);
		Vector p4 = new Vector(-_width, -_length,  _height);
		Vector p5 = new Vector( _width, -_length,  _height);
		Vector p6 = new Vector( _width,  _length,  _height);
		Vector p7 = new Vector(-_width,  _length,  _height);
		Vector n0 = new Vector(0, 0, -1);
		Vector n1 = new Vector(1, 0, 0);
		Vector n2 = new Vector(0, 0, 1);
		Vector n3 = new Vector(-1, 0, 0);
		Vector n4 = new Vector(0, 1, 0);
		Vector n5 = new Vector(0, -1, 0);
		Vector color = new Vector(1, 0.35, 0.20, 1);

		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		addSideVertices(renderVertices, p0, p1, p2, p3, n0, color);
		addSideVertices(renderVertices, p1, p5, p6, p2, n1, color);
		addSideVertices(renderVertices, p4, p7, p6, p5, n2, color);
		addSideVertices(renderVertices, p0, p3, p7, p4, n3, color);
		addSideVertices(renderVertices, p2, p6, p7, p3, n4, color);
		addSideVertices(renderVertices, p5, p1, p0, p4, n5, color);
		
		VertexBufferObject vbo = new VertexBufferObject();
		vbo.Setup(renderVertices, GL2.GL_QUADS);
		return vbo;
	}
	
	/**
	 * Add 4 vertices to the array
	 */
	private void addSideVertices(
			List<RenderVertex> renderVertices,
			Vector p0, Vector p1, Vector p2, Vector p3,
			Vector normal,
			Vector color
	) {
		renderVertices.add(new RenderVertex(p3, normal, color));
		renderVertices.add(new RenderVertex(p2, normal, color));
		renderVertices.add(new RenderVertex(p1, normal, color));
		renderVertices.add(new RenderVertex(p0, normal, color));
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			_vbo.draw(gl);
		}
	}
}

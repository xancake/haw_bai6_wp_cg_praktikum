package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.RenderVertex;
import computergraphics.framework.rendering.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class FloorNode extends LeafNode {
	private double _width;
	private double _length;
	
	private VertexBufferObject _vbo;
	
	public FloorNode(double width, double length) {
		_width = width;
		_length = length;
		
		Vector p0 = new Vector(-_width, -_length, 0);
		Vector p1 = new Vector( _width, -_length, 0);
		Vector p2 = new Vector( _width,  _length, 0);
		Vector p3 = new Vector(-_width,  _length, 0);
		Vector n0 = new Vector(0, 0, 1);
		
		Vector color = new Vector(0, 0.5, 0, 1);
		
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		addSideVertices(renderVertices, p0, p1, p2, p3, n0, color);
		
		_vbo = new VertexBufferObject();
		_vbo.Setup(renderVertices, GL2.GL_QUADS);
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
		renderVertices.add(new RenderVertex(p0, normal, color));
		renderVertices.add(new RenderVertex(p1, normal, color));
		renderVertices.add(new RenderVertex(p2, normal, color));
		renderVertices.add(new RenderVertex(p3, normal, color));
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			_vbo.draw(gl);
		}
	}
}

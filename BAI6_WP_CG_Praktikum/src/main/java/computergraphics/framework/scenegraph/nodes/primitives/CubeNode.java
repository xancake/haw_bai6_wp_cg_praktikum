/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.ArrayList;
import java.util.List;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.vbo.RenderVertex;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

/**
 * Representation of a cuboid with different dimensions in x-, y- and
 * z-direction.
 *
 * @author Philipp Jenke
 */
public class CubeNode extends LeafNode {
	private double sideLength;
	
	private VertexBufferObject vbo;

	public CubeNode(double sideLength) {
		this.sideLength = sideLength;
		vbo = createVbo();
	}

	private VertexBufferObject createVbo() {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

		Vector p0 = new Vector(-sideLength, -sideLength, -sideLength);
		Vector p1 = new Vector(sideLength, -sideLength, -sideLength);
		Vector p2 = new Vector(sideLength, sideLength, -sideLength);
		Vector p3 = new Vector(-sideLength, sideLength, -sideLength);
		Vector p4 = new Vector(-sideLength, -sideLength, sideLength);
		Vector p5 = new Vector(sideLength, -sideLength, sideLength);
		Vector p6 = new Vector(sideLength, sideLength, sideLength);
		Vector p7 = new Vector(-sideLength, sideLength, sideLength);
		Vector n0 = new Vector(0, 0, -1);
		Vector n1 = new Vector(1, 0, 0);
		Vector n2 = new Vector(0, 0, 1);
		Vector n3 = new Vector(-1, 0, 0);
		Vector n4 = new Vector(0, 1, 0);
		Vector n5 = new Vector(0, -1, 0);
		Vector color = new Vector(0.25, 0.25, 0.75, 1);

		AddSideVertices(renderVertices, p0, p1, p2, p3, n0, color);
		AddSideVertices(renderVertices, p1, p5, p6, p2, n1, color);
		AddSideVertices(renderVertices, p4, p7, p6, p5, n2, color);
		AddSideVertices(renderVertices, p0, p3, p7, p4, n3, color);
		AddSideVertices(renderVertices, p2, p6, p7, p3, n4, color);
		AddSideVertices(renderVertices, p5, p1, p0, p4, n5, color);

		return new VertexBufferObject(GL2.GL_QUADS, renderVertices);
	}

	/**
	 * Add 4 vertices to the array
	 */
	private void AddSideVertices(
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
			vbo.draw(gl);
		}
	}
}

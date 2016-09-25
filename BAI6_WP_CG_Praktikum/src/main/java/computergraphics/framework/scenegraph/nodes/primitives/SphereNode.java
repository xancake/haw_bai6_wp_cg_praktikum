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
import computergraphics.framework.rendering.RenderVertex;
import computergraphics.framework.rendering.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

/**
 * Geometry node for a sphere with arbitary radius, centered at the origin.
 * 
 * @author Philipp Jenke
 */
public class SphereNode extends LeafNode {
	private double radius;
	private int resolution;
	private VertexBufferObject vbo;

	public SphereNode(double radius, int resolution) {
		this.radius = radius;
		this.resolution = resolution;
		vbo = new VertexBufferObject();
		createVbo();
	}

	private void createVbo() {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

		Vector color = new Vector(0.25, 0.75, 0.25, 1);
		float dTheta = (float) (Math.PI / resolution);
		float dPhi = (float) (Math.PI * 2.0 / resolution);
		for (int i = 0; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				Vector p0 = evaluateSpherePoint(i * dTheta, j * dPhi);
				Vector p1 = evaluateSpherePoint(i * dTheta, (j + 1) * dPhi);
				Vector p2 = evaluateSpherePoint((i + 1) * dTheta, (j + 1)
						* dPhi);
				Vector p3 = evaluateSpherePoint((i + 1) * dTheta, j * dPhi);

				Vector u = p3.subtract(p0);
				Vector t1 = p1.subtract(p0);
				Vector t2 = p3.subtract(p2);
				Vector normal;
				if (t1.getNorm() < 1e-5) {
					normal = u.cross(t2).getNormalized();
				} else {
					normal = u.cross(t1).getNormalized();
				}

				AddSideVertices(renderVertices, p0, p1, p2, p3, normal, color);
			}
		}
		vbo.Setup(renderVertices, GL2.GL_QUADS);
	}

	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if (mode == RenderMode.REGULAR) {
			vbo.draw(gl);
		}
	}

	/**
	 * Compute a surface point for given sphere coordinates.
	 */
	private Vector evaluateSpherePoint(float theta, float phi) {
		float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
		float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
		float z = (float) (radius * Math.cos(theta));
		return new Vector(x, y, z);
	}

	/**
	 * Add 4 vertices to the array
	 */
	private void AddSideVertices(List<RenderVertex> renderVertices, Vector p0,
			Vector p1, Vector p2, Vector p3, Vector normal, Vector color) {
		renderVertices.add(new RenderVertex(p3, normal, color));
		renderVertices.add(new RenderVertex(p2, normal, color));
		renderVertices.add(new RenderVertex(p1, normal, color));
		renderVertices.add(new RenderVertex(p0, normal, color));
	}
}

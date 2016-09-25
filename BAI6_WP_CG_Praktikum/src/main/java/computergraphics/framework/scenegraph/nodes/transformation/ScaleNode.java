/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.framework.scenegraph.nodes.transformation;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;

/**
 * Scene graph node which scales all its child nodes.
 * 
 * @author Philipp Jenke
 */
public class ScaleNode extends InnerNode {
	/**
	 * Scaling factors in x-, y- and z-direction.
	 */
	private final Matrix scaleMatrix;

	public ScaleNode(Vector scale) {
		scaleMatrix = Matrix.createScale(scale);
	}

	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		super.traverse(gl, mode, scaleMatrix.multiply(modelMatrix));
	}

	public void timerTick(int counter) {
		super.timerTick(counter);
	}
}

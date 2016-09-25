package computergraphics.framework.scenegraph.nodes.transformation;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.MathHelpers;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;

public class RotationNode extends InnerNode {
	private final Matrix _rotation;
	
	public RotationNode(Vector axis, double angle) {
		_rotation = Matrix.getRotationMatrix4(axis, MathHelpers.degree2radiens(angle));
	}
	
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		super.traverse(gl, mode, _rotation.multiply(modelMatrix));
	}
}

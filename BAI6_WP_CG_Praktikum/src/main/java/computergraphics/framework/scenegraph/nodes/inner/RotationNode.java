package computergraphics.framework.scenegraph.nodes.inner;

import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;

public class RotationNode extends InnerNode {
	private final Matrix _rotation;
	
	public RotationNode(Vector axis, double angle) {
		_rotation = Matrix.getRotationMatrix4(axis, angle);
	}
	
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		super.traverse(gl, mode, _rotation.multiply(modelMatrix));
	}
}

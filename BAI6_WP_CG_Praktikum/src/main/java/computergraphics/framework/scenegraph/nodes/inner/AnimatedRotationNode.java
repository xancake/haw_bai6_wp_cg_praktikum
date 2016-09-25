package computergraphics.framework.scenegraph.nodes.inner;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.MathHelpers;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;

public class AnimatedRotationNode extends InnerNode {
	private final Vector _axis;
	private final double _angle;
	
	private double _currentAngle;
	
	public AnimatedRotationNode(Vector axis, double angle) {
		if(axis == null) {
			throw new NullPointerException();
		}
		_axis = axis;
		_angle = angle;
	}
	
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		Matrix rotation = Matrix.getRotationMatrix4(_axis, MathHelpers.degree2radiens(_currentAngle));
		super.traverse(gl, mode, rotation.multiply(modelMatrix));
	}
	
	@Override
	public void timerTick(int counter) {
		super.timerTick(counter);
		
		_currentAngle += _angle;
	}
}

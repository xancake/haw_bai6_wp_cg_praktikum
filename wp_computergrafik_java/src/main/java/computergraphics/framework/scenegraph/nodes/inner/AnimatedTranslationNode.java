package computergraphics.framework.scenegraph.nodes.inner;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;

public class AnimatedTranslationNode extends InnerNode {
	private final double _area;
	private boolean _direction;
	private Vector _location;
	
	public AnimatedTranslationNode(double area, Vector location) {
		_area = area;
		_direction = true;
		_location = location;
	}
	
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		Matrix translation = Matrix.createTranslationGl(_location);
		super.traverse(gl, mode, translation.multiply(modelMatrix));
	}
	
	@Override
	public void timerTick(int counter) {
		super.timerTick(counter);
		
		if(_direction) {
			_location = _location.subtract(new Vector(0, 0.01, 0));
			if(_location.y() < -_area) {
				_direction = !_direction;
			}
		} else {
			_location = _location.add(new Vector(0, 0.01, 0));
			if(_location.y() > _area) {
				_direction = !_direction;
			}
		}
	}
}

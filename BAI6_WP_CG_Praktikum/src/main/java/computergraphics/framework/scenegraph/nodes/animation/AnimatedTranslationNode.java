package computergraphics.framework.scenegraph.nodes.animation;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;

public class AnimatedTranslationNode extends InnerNode {
	private Vector _location;
	private Vector _direction;
	private Vector _bounds;
	private boolean _changeDirection;
	
	public AnimatedTranslationNode(Vector direction, Vector bounds) {
		_location = new Vector(0, 0, 0);
		_direction = direction;
		_bounds = bounds;
	}
	
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		Matrix translation = Matrix.createTranslationGl(_location);
		super.traverse(gl, mode, translation.multiply(modelMatrix));
	}
	
	@Override
	public void timerTick(int counter) {
		super.timerTick(counter);
		
		if(_changeDirection) {
			_location = _location.subtract(_direction);
			if(_location.x() < -_bounds.x()
					|| _location.y() < -_bounds.y()
					|| _location.z() < -_bounds.z()) {
				_changeDirection = !_changeDirection;
			}
		} else {
			_location = _location.add(_direction);
			if(_location.x() > _bounds.x()
					|| _location.y() > _bounds.y()
					|| _location.z() > _bounds.z()) {
				_changeDirection = !_changeDirection;
			}
		}
	}
}

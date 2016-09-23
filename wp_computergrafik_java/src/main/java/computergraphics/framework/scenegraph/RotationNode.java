package computergraphics.framework.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;

public class RotationNode extends InnerNode {

	  /**
	   * Translation matrix (model matrix)
	   */
	  private final Matrix rotation;

	  public RotationNode(Vector axis, double angle) {
	    this.rotation = Matrix.getRotationMatrix4(axis, angle);
	  }

	  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
	    super.traverse(gl, mode, rotation.multiply(modelMatrix));
	  }

	  public void timerTick(int counter) {
	    super.timerTick(counter);
	  }

}

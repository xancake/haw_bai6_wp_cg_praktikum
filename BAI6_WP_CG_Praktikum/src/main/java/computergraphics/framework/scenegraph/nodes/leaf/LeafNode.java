package computergraphics.framework.scenegraph.nodes.leaf;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.rendering.ShaderAttributes;
import computergraphics.framework.scenegraph.nodes.INode;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;

/**
 * A leaf node allows to draw OpenGl content.
 */
public abstract class LeafNode extends INode {

  @Override
  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    ShaderAttributes.getInstance().setModelMatrixParameter(gl, modelMatrix);
    ShaderAttributes.getInstance().setViewMatrixParameter(gl,
        getRootNode().getCamera().getViewMatrix());
    drawGL(gl, mode, modelMatrix);
  }

  @Override
  public void timerTick(int counter) {
  }

  /**
   * Draw GL content.
   */
  public abstract void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix);

}

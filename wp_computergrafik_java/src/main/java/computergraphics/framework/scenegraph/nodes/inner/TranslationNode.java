/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.framework.scenegraph.nodes.inner;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.INode;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.math.Matrix;

/**
 * Translate all child nodes.
 * 
 * @author Philipp Jenke
 */
public class TranslationNode extends InnerNode {

  /**
   * Translation matrix (model matrix)
   */
  private final Matrix translation;

  public TranslationNode(Vector translation) {
    this.translation = Matrix.createTranslationGl(translation);
  }

  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    super.traverse(gl, mode, translation.multiply(modelMatrix));
  }

  public void timerTick(int counter) {
    super.timerTick(counter);
  }

}

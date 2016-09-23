package computergraphics.framework.scenegraph.nodes;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.inner.InnerNode;

/**
 * The root node exists only once and is used as the scene graph root. It
 * contains scene information.
 * 
 * @author Philipp Jenke
 *
 */
public class RootNode extends InnerNode {
  /**
   * Currently used shader
   */
  private Shader shader;

  /**
   * Scene camera
   */
  private Camera camera;

  /**
   * This flags indicates that the scene should be animated
   */
  private boolean animated;

  /**
   * Position of the light source
   */
  private Vector lightPosition;
  
  /**
   * Background color
   */
  private Vector backGroundColor;

  public RootNode(Shader shader, Camera camera) {
    this.shader = shader;
    this.camera = camera;
    lightPosition = new Vector(1, 1, 0);
    backGroundColor = new Vector(0.25, 0.25, 0.25);
    animated = true;
  }

  @Override
  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    super.traverse(gl, mode, modelMatrix);
  }

//  @Override
//  public void timerTick(int counter) {
//  }

  public RootNode getRootNode() {
    return this;
  }

  public Shader getShader() {
    return shader;
  }

  public Camera getCamera() {
    return camera;
  }

  public boolean isAnimated() {
    return animated;
  }

  public void setAnimated(boolean animated) {
    this.animated = animated;
  }

  public Vector getLightPosition() {
    return lightPosition;
  }
  
  public Vector getBackgroundColor() {
    return backGroundColor;
  }

  public void setLightPosition(Vector lightPosition) {
    this.lightPosition = lightPosition;
  }
}

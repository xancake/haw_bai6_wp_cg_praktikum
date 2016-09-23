/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.exercises;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.CubeNode;
import computergraphics.framework.scenegraph.INode;
import computergraphics.framework.scenegraph.SphereNode;
import computergraphics.framework.scenegraph.TranslationNode;
import computergraphics.framework.scenegraph.INode.RenderMode;

/**
 * Application for the first exercise.
 * 
 * @author Philipp Jenke
 */
public class Exercise1 extends Scene {
  private static final long serialVersionUID = 8141036480333465137L;

  public Exercise1() {
    // Timer timeout and shader mode (PHONG, TEXTURE, NO_LIGHTING)
    super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

    getRoot().setLightPosition(new Vector(1, 1, 1));
    getRoot().setAnimated(true);

    // Sphere geometry
    TranslationNode sphereTranslation =
        new TranslationNode(new Vector(1, -0.5, 0));
    SphereNode sphereNode = new SphereNode(0.5, 20);
    sphereTranslation.addChild(sphereNode);
    getRoot().addChild(sphereTranslation);

    // Cube geometry
    TranslationNode cubeTranslation =
        new TranslationNode(new Vector(-1, 0.5, 0));
    CubeNode cubeNode = new CubeNode(0.5);
    cubeTranslation.addChild(cubeNode);
    getRoot().addChild(cubeTranslation);

    // Light geometry
    TranslationNode lightTranslation =
        new TranslationNode(getRoot().getLightPosition());
    INode lightSphereNode = new SphereNode(0.1f, 10);
    lightTranslation.addChild(lightSphereNode);
    getRoot().addChild(lightTranslation);

  }

  @Override
  public void keyPressed(int keyCode) {
    // Key pressed event
  }

  @Override
  public void timerTick(int counter) {
    // Timer tick event
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    new Exercise1();
  }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.exercises.exercise1;

import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedRotationNode;
import computergraphics.framework.scenegraph.nodes.primitives.CubeNode;
import computergraphics.framework.scenegraph.nodes.primitives.FloorNode;
import computergraphics.framework.scenegraph.nodes.primitives.SphereNode;
import computergraphics.framework.scenegraph.nodes.transformation.RotationNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class RotationTest extends Scene {
	private static final long serialVersionUID = 8141036480333465137L;
	
	public RotationTest() {
		// Timer timeout and shader mode (PHONG, TEXTURE, NO_LIGHTING)
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);
		
		getRoot().setLightPosition(new Vector(-2, -2, 2));
		getRoot().setAnimated(true);
		
		// Cube 1
		CubeNode cube1 = new CubeNode(0.25);
		TranslationNode cube1Translation = new TranslationNode(new Vector(-1, 0, 0));
		RotationNode cube1Rotation = new RotationNode(new Vector(1, 0, 0), 45);
		cube1Rotation.addChild(cube1);
		cube1Translation.addChild(cube1Rotation);
		getRoot().addChild(cube1Translation);
		
		// Cube 2
		CubeNode cube2 = new CubeNode(0.25);
		RotationNode cube2Rotation = new RotationNode(new Vector(0, 1, 0), 45);
		cube2Rotation.addChild(cube2);
		getRoot().addChild(cube2Rotation);
		
		// Cube 3
		CubeNode cube3 = new CubeNode(0.25);
		TranslationNode cube3Translation = new TranslationNode(new Vector(1, 0, 0));
		RotationNode cube3Rotation = new RotationNode(new Vector(0, 0, 1), 45);
		cube3Rotation.addChild(cube3);
		cube3Translation.addChild(cube3Rotation);
		getRoot().addChild(cube3Translation);
		
		// Cube 4
		CubeNode cube4 = new CubeNode(0.25);
		TranslationNode cube4Translation = new TranslationNode(new Vector(-1, 1, 0));
		AnimatedRotationNode cube4Rotation = new AnimatedRotationNode(new Vector(1, 0, 0), 5);
		cube4Rotation.addChild(cube4);
		cube4Translation.addChild(cube4Rotation);
		getRoot().addChild(cube4Translation);
		
		// Cube 5
		CubeNode cube5 = new CubeNode(0.25);
		TranslationNode cube5Translation = new TranslationNode(new Vector(0, 1, 0));
		AnimatedRotationNode cube5Rotation = new AnimatedRotationNode(new Vector(0, 1, 0), 5);
		cube5Rotation.addChild(cube5);
		cube5Translation.addChild(cube5Rotation);
		getRoot().addChild(cube5Translation);
		
		// Cube 6
		CubeNode cube6 = new CubeNode(0.25);
		TranslationNode cube6Translation = new TranslationNode(new Vector(1, 1, 0));
		AnimatedRotationNode cube6Rotation = new AnimatedRotationNode(new Vector(0, 0, 1), 5);
		cube6Rotation.addChild(cube6);
		cube6Translation.addChild(cube6Rotation);
		getRoot().addChild(cube6Translation);
		
		FloorNode floor = new FloorNode(2, 2);
		getRoot().addChild(floor);
		
		// Light geometry
		TranslationNode lightTranslation = new TranslationNode(getRoot().getLightPosition());
		INode lightSphereNode = new SphereNode(0.1, 10);
		lightTranslation.addChild(lightSphereNode);
		getRoot().addChild(lightTranslation);
	}
	
	public static void main(String[] args) {
		new RotationTest();
	}
}

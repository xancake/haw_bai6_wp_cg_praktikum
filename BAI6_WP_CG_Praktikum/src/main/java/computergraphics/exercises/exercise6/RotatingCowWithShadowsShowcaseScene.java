package computergraphics.exercises.exercise6;

import java.io.IOException;

import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedRotationNode;
import computergraphics.framework.scenegraph.nodes.primitives.JenkeTriangleMeshNode;
import computergraphics.framework.scenegraph.nodes.transformation.ScaleNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

@SuppressWarnings("serial")
public class RotatingCowWithShadowsShowcaseScene extends Scene {
	private JenkeTriangleMeshNode _cowNode;
	
	public RotatingCowWithShadowsShowcaseScene() throws IOException {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);
		getRoot().setLightPosition(new Vector(1, 1, 1));
		
		JenkeTriangleMeshNode sphereNode = new JenkeTriangleMeshNode(new HalfEdgeTriangleMesh("meshes/sphere.obj"));
		ScaleNode sphereScale = new ScaleNode(new Vector(0.05, 0.05, 0.05));
		sphereScale.addChild(sphereNode);
		TranslationNode sphereTrans = new TranslationNode(new Vector(0.3, 0.45, 0.3));
		sphereTrans.addChild(sphereScale);
		getRoot().addChild(sphereTrans);
		
		_cowNode = new JenkeTriangleMeshNode(new HalfEdgeTriangleMesh("meshes/cow.obj"));
		AnimatedRotationNode cowRotation = new AnimatedRotationNode(new Vector(0, 1, 0), 5);
		cowRotation.addChild(_cowNode);
		getRoot().addChild(cowRotation);
		
		JenkeTriangleMeshNode squareNode = new JenkeTriangleMeshNode(new HalfEdgeTriangleMesh("meshes/square.obj"));
		ScaleNode squareScale = new ScaleNode(new Vector(2,2,2));
		squareScale.addChild(squareNode);
		TranslationNode squareTrans = new TranslationNode(new Vector(-0.5, -0.5, -0.5));
		squareTrans.addChild(squareScale);
		getRoot().addChild(squareTrans);
	}

	@Override
	public void keyPressed(int keyCode) {
		switch (Character.toUpperCase(keyCode)) {
			case 'N':
				_cowNode.setShowNormals(!_cowNode.isShowNormals());
				break;
		}
	}
	
	public static void main(String[] args) throws IOException {
		new RotatingCowWithShadowsShowcaseScene();
	}
}

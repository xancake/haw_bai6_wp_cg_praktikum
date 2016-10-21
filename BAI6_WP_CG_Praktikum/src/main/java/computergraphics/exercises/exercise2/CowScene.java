package computergraphics.exercises.exercise2;

import java.io.IOException;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.TriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class CowScene extends Scene {
	private static final double NORMAL_DRAW_LENGTH_STEP = 0.01;
	
	private TriangleMeshNode _cowNode;
	
	public CowScene() throws IOException {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);
		
		ITriangleMesh cowMesh = new TriangleMesh("meshes/cow.obj");
		_cowNode = new TriangleMeshNode(cowMesh, new Vector(1, 0, 0, 1));
		getRoot().addChild(_cowNode);
	}
	
	public void keyPressed(int keyCode) {
		int key = Character.toUpperCase(keyCode);
		double normalDrawLength = 0;
		switch(key) {
			case 'N':
    			_cowNode.setDrawFacetteNormals(!_cowNode.isDrawFacetteNormals());
    			break;
			case '+':
				normalDrawLength = _cowNode.getFacetteNormalDrawLength();
				if(normalDrawLength>NORMAL_DRAW_LENGTH_STEP) {
					_cowNode.setFacetteNormalDrawLength(normalDrawLength+NORMAL_DRAW_LENGTH_STEP);
				} else {
					_cowNode.setFacetteNormalDrawLength(normalDrawLength*2);
				}
				break;
			case '-':
				normalDrawLength = _cowNode.getFacetteNormalDrawLength();
				if(normalDrawLength>NORMAL_DRAW_LENGTH_STEP) {
					_cowNode.setFacetteNormalDrawLength(normalDrawLength-NORMAL_DRAW_LENGTH_STEP);
				} else {
					_cowNode.setFacetteNormalDrawLength(normalDrawLength/2);
				}
				break;
		}
	}
	
	public static void main(String... args) throws IOException {
		new CowScene();
	}
}

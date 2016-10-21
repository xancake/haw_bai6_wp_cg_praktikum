package computergraphics.exercises.exercise3;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.HalfEdgeTriangleMesh;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class HalfEdgeCowScene extends Scene {
	private static final double NORMAL_DRAW_LENGTH_STEP = 0.01;

	private TriangleMeshNode _cowNode;

	public HalfEdgeCowScene() {
			super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

			getRoot().setLightPosition(new Vector(1, 1, 1));
			getRoot().setAnimated(true);
			
			ITriangleMesh cowMesh = new HalfEdgeTriangleMesh("meshes/cow.obj");
			_cowNode = new TriangleMeshNode(cowMesh, new Vector(1, 0, 0, 1));
			getRoot().addChild(_cowNode);
		}

	public void keyPressed(int keyCode) {
		int key = Character.toUpperCase(keyCode);
		double normalDrawLength = 0;
		switch (key) {
		case 'L':
			_cowNode.setDrawMeshVertexNormals(!_cowNode.isDrawMeshVertexNormals());
			break;
		case 'N':
			_cowNode.setDrawFacetteNormals(!_cowNode.isDrawFacetteNormals());
			break;
		case 'V':
			_cowNode.setDrawVertexNormals(!_cowNode.isDrawVertexNormals());
		case '+':
			normalDrawLength = _cowNode.getFacetteNormalDrawLength();
			if (normalDrawLength > NORMAL_DRAW_LENGTH_STEP) {
				_cowNode.setFacetteNormalDrawLength(normalDrawLength + NORMAL_DRAW_LENGTH_STEP);
				_cowNode.setVertexNormalDrawLength(normalDrawLength + NORMAL_DRAW_LENGTH_STEP);
			} else {
				_cowNode.setFacetteNormalDrawLength(normalDrawLength * 2);
				_cowNode.setVertexNormalDrawLength(normalDrawLength * 2);
			}
			break;
		case '-':
			normalDrawLength = _cowNode.getFacetteNormalDrawLength();
			if (normalDrawLength > NORMAL_DRAW_LENGTH_STEP) {
				_cowNode.setFacetteNormalDrawLength(normalDrawLength - NORMAL_DRAW_LENGTH_STEP);
				_cowNode.setVertexNormalDrawLength(normalDrawLength - NORMAL_DRAW_LENGTH_STEP);
			} else {
				_cowNode.setFacetteNormalDrawLength(normalDrawLength / 2);
				_cowNode.setVertexNormalDrawLength(normalDrawLength / 2);
			}
			break;
		}
	}

	public static void main(String... args) {
		new HalfEdgeCowScene();
	}
}

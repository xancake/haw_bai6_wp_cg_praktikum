package computergraphics.exercises.exercise3;

import java.io.IOException;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.HalfEdgeTriangleMesh;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class HemisphereScene extends Scene{

	private static final double NORMAL_DRAW_LENGTH_STEP = 0.01;

	private TriangleMeshNode _hemiNode;

	public HemisphereScene() throws IOException {
			super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

			getRoot().setLightPosition(new Vector(1, 1, 1));
			getRoot().setAnimated(true);
			
			ITriangleMesh hemiMesh = new HalfEdgeTriangleMesh("meshes/hemisphere.obj");
			_hemiNode = new TriangleMeshNode(hemiMesh, new Vector(1, 0, 0, 1));
			getRoot().addChild(_hemiNode);
		}

	public void keyPressed(int keyCode) {
		int key = Character.toUpperCase(keyCode);
		double normalDrawLength = 0;
		switch (key) {
		case 'L':
			_hemiNode.setDrawMeshVertexNormals(!_hemiNode.isDrawMeshVertexNormals());
			break;
		case 'N':
			_hemiNode.setDrawFacetteNormals(!_hemiNode.isDrawFacetteNormals());
			break;
		case 'V':
			_hemiNode.setDrawVertexNormals(!_hemiNode.isDrawVertexNormals());
		case '+':
			normalDrawLength = _hemiNode.getFacetteNormalDrawLength();
			if (normalDrawLength > NORMAL_DRAW_LENGTH_STEP) {
				_hemiNode.setFacetteNormalDrawLength(normalDrawLength + NORMAL_DRAW_LENGTH_STEP);
				_hemiNode.setVertexNormalDrawLength(normalDrawLength + NORMAL_DRAW_LENGTH_STEP);
			} else {
				_hemiNode.setFacetteNormalDrawLength(normalDrawLength * 2);
				_hemiNode.setVertexNormalDrawLength(normalDrawLength * 2);
			}
			break;
		case '-':
			normalDrawLength = _hemiNode.getFacetteNormalDrawLength();
			if (normalDrawLength > NORMAL_DRAW_LENGTH_STEP) {
				_hemiNode.setFacetteNormalDrawLength(normalDrawLength - NORMAL_DRAW_LENGTH_STEP);
				_hemiNode.setVertexNormalDrawLength(normalDrawLength - NORMAL_DRAW_LENGTH_STEP);
			} else {
				_hemiNode.setFacetteNormalDrawLength(normalDrawLength / 2);
				_hemiNode.setVertexNormalDrawLength(normalDrawLength / 2);
			}
			break;
		}
	}

	public static void main(String... args) throws IOException {
		new HemisphereScene();
	}

}

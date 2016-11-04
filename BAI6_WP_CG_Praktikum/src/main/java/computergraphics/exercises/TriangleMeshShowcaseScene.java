package computergraphics.exercises;

import java.io.IOException;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class TriangleMeshShowcaseScene extends Scene {
	private static final double NORMAL_DRAW_LENGTH_STEP = 0.01;

	private TriangleMeshNode _meshNode;

	public TriangleMeshShowcaseScene(ITriangleMesh mesh) throws IOException {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);
		
		_meshNode = new TriangleMeshNode(mesh, new Vector(1, 0, 0, 1));
		getRoot().addChild(_meshNode);
	}

	public void keyPressed(int keyCode) {
		int key = Character.toUpperCase(keyCode);
		double normalDrawLength = 0;
		switch (key) {
		case 'M':
			_meshNode.setDrawMesh(!_meshNode.isDrawMesh());
			break;
		case 'W':
			_meshNode.setDrawWireframe(!_meshNode.isDrawWireframe());
			break;
		case 'B':
			_meshNode.setDrawBorder(!_meshNode.isDrawBorder());
			break;
		case 'L':
			_meshNode.setDrawMeshVertexNormals(!_meshNode.isDrawMeshVertexNormals());
			break;
		case 'N':
			_meshNode.setDrawFacetteNormals(!_meshNode.isDrawFacetteNormals());
			break;
		case 'V':
			_meshNode.setDrawVertexNormals(!_meshNode.isDrawVertexNormals());
			break;
		case '+':
			normalDrawLength = _meshNode.getFacetteNormalDrawLength();
			if (normalDrawLength > NORMAL_DRAW_LENGTH_STEP) {
				_meshNode.setFacetteNormalDrawLength(normalDrawLength + NORMAL_DRAW_LENGTH_STEP);
				_meshNode.setVertexNormalDrawLength(normalDrawLength + NORMAL_DRAW_LENGTH_STEP);
			} else {
				_meshNode.setFacetteNormalDrawLength(normalDrawLength * 2);
				_meshNode.setVertexNormalDrawLength(normalDrawLength * 2);
			}
			break;
		case '-':
			normalDrawLength = _meshNode.getFacetteNormalDrawLength();
			if (normalDrawLength > NORMAL_DRAW_LENGTH_STEP) {
				_meshNode.setFacetteNormalDrawLength(normalDrawLength - NORMAL_DRAW_LENGTH_STEP);
				_meshNode.setVertexNormalDrawLength(normalDrawLength - NORMAL_DRAW_LENGTH_STEP);
			} else {
				_meshNode.setFacetteNormalDrawLength(normalDrawLength / 2);
				_meshNode.setVertexNormalDrawLength(normalDrawLength / 2);
			}
			break;
		}
	}
}

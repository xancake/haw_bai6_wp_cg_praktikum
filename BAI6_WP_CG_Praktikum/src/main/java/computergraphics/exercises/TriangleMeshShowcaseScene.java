package computergraphics.exercises;

import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class TriangleMeshShowcaseScene extends Scene {
	private static final double NORMAL_DRAW_LENGTH_STEP = 0.01;

	private TriangleMeshNode _meshNode;

	public TriangleMeshShowcaseScene(ITriangleMesh mesh) {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);

		_meshNode = new TriangleMeshNode(mesh, new Vector(1, 0, 0, 1));
		getRoot().addChild(_meshNode);
	}

	public void keyPressed(int keyCode) {
		switch (Character.toUpperCase(keyCode)) {
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
				if (_meshNode.getFacetteNormalDrawLength() > NORMAL_DRAW_LENGTH_STEP) {
					_meshNode.setFacetteNormalDrawLength(_meshNode.getFacetteNormalDrawLength() + NORMAL_DRAW_LENGTH_STEP);
					_meshNode.setVertexNormalDrawLength(_meshNode.getVertexNormalDrawLength() + NORMAL_DRAW_LENGTH_STEP);
				} else {
					_meshNode.setFacetteNormalDrawLength(_meshNode.getFacetteNormalDrawLength() * 2);
					_meshNode.setVertexNormalDrawLength(_meshNode.getVertexNormalDrawLength() * 2);
				}
				break;
			case '-':
				if (_meshNode.getFacetteNormalDrawLength() > NORMAL_DRAW_LENGTH_STEP) {
					_meshNode.setFacetteNormalDrawLength(_meshNode.getFacetteNormalDrawLength() - NORMAL_DRAW_LENGTH_STEP);
					_meshNode.setVertexNormalDrawLength(_meshNode.getVertexNormalDrawLength() - NORMAL_DRAW_LENGTH_STEP);
				} else {
					_meshNode.setFacetteNormalDrawLength(_meshNode.getFacetteNormalDrawLength() / 2);
					_meshNode.setVertexNormalDrawLength(_meshNode.getVertexNormalDrawLength() / 2);
				}
				break;
		}
	}
}

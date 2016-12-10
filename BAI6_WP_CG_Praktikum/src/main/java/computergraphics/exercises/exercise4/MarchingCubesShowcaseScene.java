package computergraphics.exercises.exercise4;

import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.algorithm.marching.cubes.MarchingCubes;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.TriangleMeshFactory;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;
import computergraphics.framework.scenegraph.nodes.primitives.MarchingCubesVisualizationNode;

@SuppressWarnings("serial")
public class MarchingCubesShowcaseScene extends TriangleMeshShowcaseScene {
	private MarchingCubesVisualizationNode _mcNode;
	
	public MarchingCubesShowcaseScene(MarchingCubes mc, ImplicitFunction function) {
		super(createMesh(mc, function));

		getRoot().setLightPosition(new Vector(2, 2, 2));
		getRoot().setAnimated(true);
		
		_mcNode = new MarchingCubesVisualizationNode(mc);
		_mcNode.setVolumeColor(new Vector(0, 0.25, 1));
		_mcNode.setDrawVolume(false);
		_mcNode.setDrawSubVolumes(true);
		getRoot().addChild(_mcNode);
	}
	
	private static ITriangleMesh createMesh(MarchingCubes ms, ImplicitFunction function) {
		ITriangleMesh soup = new HalfEdgeTriangleMesh();
		ms.createMesh(soup, function, function.getDefaultIso());
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createUnsoupifiedMesh(mesh, soup);
		
		System.out.println(soup.getNumberOfVertices() + " --> " + mesh.getNumberOfVertices());
		return mesh;
	}
	
	public void keyPressed(int keyCode) {
		int key = Character.toUpperCase(keyCode);
		switch (key) {
			case '1':
				_mcNode.setDrawVolume(!_mcNode.isDrawVolume());
				break;
			case '2':
				_mcNode.setDrawSubVolumes(!_mcNode.isDrawSubVolumes());
				break;
			default:
				super.keyPressed(keyCode);
		}
	}
}

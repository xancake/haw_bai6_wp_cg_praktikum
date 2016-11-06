package computergraphics.exercises.exercise4;

import java.io.IOException;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.implicit_functions.BoyscheFlaecheFunction;
import computergraphics.framework.math.implicit_functions.EllipsoidFunction;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.math.implicit_functions.KugelFunction;
import computergraphics.framework.math.implicit_functions.SteinerscheRoemischeFlaecheFunction;
import computergraphics.framework.math.implicit_functions.TorusFunction;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.TriangleMeshFactory;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.MarchingCubesVisualizationNode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class MarchingCubesScene extends Scene {
	public MarchingCubesScene(double size) throws IOException {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(2, 2, 2));
		getRoot().setAnimated(true);
		
		MarchingCubes mc = new MarchingCubes(new Cuboid(-size, size), (int)size*25);
		
		getRoot().addChild(new TriangleMeshNode(createMesh(mc, new KugelFunction(0.5, new Vector(1, 1, -1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(mc, new TorusFunction(0.25, 0.5, new Vector(1, 1, 1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(mc, new EllipsoidFunction(0.25, 0.5, 0.25, new Vector(1, -1, -1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(mc, new SteinerscheRoemischeFlaecheFunction(new Vector(1, -1, 1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(mc, new BoyscheFlaecheFunction(new Vector(-1, 1, -1))), new Vector(0, 0, 1, 1)));
		
		MarchingCubesVisualizationNode mcNode = new MarchingCubesVisualizationNode(mc);
		getRoot().addChild(mcNode);
	}
	
	public static void main(String... args) throws Exception {
		new MarchingCubesScene(2);
	}
	
	private static ITriangleMesh createMesh(MarchingCubes ms, ImplicitFunction function) {
		ITriangleMesh soup = new HalfEdgeTriangleMesh();
		ms.createMesh(soup, function, function.getDefaultIso());
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createUnsoupifiedMesh(mesh, soup);
		return mesh;
	}
}

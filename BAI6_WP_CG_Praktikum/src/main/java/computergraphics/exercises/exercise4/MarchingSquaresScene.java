package computergraphics.exercises.exercise4;

import java.io.IOException;
import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.datastructures.Cube;
import computergraphics.framework.datastructures.implicit_functions.BoyscheFlaecheFunction;
import computergraphics.framework.datastructures.implicit_functions.EllipsoidFunction;
import computergraphics.framework.datastructures.implicit_functions.ImplicitFunction;
import computergraphics.framework.datastructures.implicit_functions.KugelFunction;
import computergraphics.framework.datastructures.implicit_functions.SteinerscheRoemischeFlaecheFunction;
import computergraphics.framework.datastructures.implicit_functions.TorusFunction;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.HalfEdgeTriangleMesh;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.TriangleMeshFactory;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class MarchingSquaresScene extends TriangleMeshShowcaseScene {
	public MarchingSquaresScene(double size) throws IOException {
		super(cube(size));
		
		MarchingSquares ms = new MarchingSquares(new Cube(-size, size), (int)size*25);
		
		getRoot().addChild(new TriangleMeshNode(createMesh(ms, new KugelFunction(0.5, new Vector(1, 1, -1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(ms, new TorusFunction(0.25, 0.5, new Vector(1, 1, 1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(ms, new EllipsoidFunction(0.25, 0.5, 0.25, new Vector(1, -1, -1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(ms, new SteinerscheRoemischeFlaecheFunction(new Vector(1, -1, 1))), new Vector(0, 0, 1, 1)));
		getRoot().addChild(new TriangleMeshNode(createMesh(ms, new BoyscheFlaecheFunction(new Vector(-1, 1, -1))), new Vector(0, 0, 1, 1)));
	}
	
	public static void main(String... args) throws Exception {
		new MarchingSquaresScene(2);
	}
	
	private static ITriangleMesh createMesh(MarchingSquares ms, ImplicitFunction function) {
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		ms.createMesh(mesh, function, function.getDefaultIso());
		return mesh;
	}
	
	private static ITriangleMesh cube(double size) {
		ITriangleMesh cube = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createInvertedCube(cube, size);
		return cube;
	}
}

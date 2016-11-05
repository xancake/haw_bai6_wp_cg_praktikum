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
		
		Vector center = new Vector(0, 0, 0);
//		ImplicitFunction function = new KugelFunction(1, center);
//		ImplicitFunction function = new TorusFunction(0.25, 0.5, center);
		ImplicitFunction function = new BoyscheFlaecheFunction(center);
//		ImplicitFunction function = new EllipsoidFunction(0.25, 0.5, 0.5, center);
//		ImplicitFunction function = new SteinerscheRoemischeFlaecheFunction(center);
		
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		new MarchingSquares(new Cube(-size, size), (int)size*25, function, function.getDefaultIso()).createMesh(mesh);
		getRoot().addChild(new TriangleMeshNode(mesh, new Vector(0, 0, 1, 1)));
	}
	
	public static void main(String... args) throws Exception {
		new MarchingSquaresScene(1);
	}
	
	private static ITriangleMesh cube(double size) {
		ITriangleMesh cube = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createInvertedCube(cube, size);
		return cube;
	}
}

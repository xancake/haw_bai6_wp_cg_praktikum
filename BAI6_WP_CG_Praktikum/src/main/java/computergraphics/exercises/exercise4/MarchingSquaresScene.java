package computergraphics.exercises.exercise4;

import java.io.IOException;

import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.datastructures.Cube;
import computergraphics.framework.datastructures.implicit_functions.BoyscheFlaecheFunction;
import computergraphics.framework.datastructures.implicit_functions.EllipsoidFunction;
import computergraphics.framework.datastructures.implicit_functions.TorusFunction;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.HalfEdgeTriangleMesh;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;

@SuppressWarnings("serial")
public class MarchingSquaresScene extends TriangleMeshShowcaseScene {
	public MarchingSquaresScene(double size) throws IOException {
		super(cube(size));
		
		Vector center = new Vector(0, 0, 0);
		
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		// new Kugel(1, center)
		// new TorusFunction(0.25, 0.5, center)
		// new BoyscheFlaecheFunction(center)
		new MarchingSquares(new Cube(-size, size), 25, new EllipsoidFunction(0.25, 0.5, 0.5, center), 1).createMesh(mesh);
		getRoot().addChild(new TriangleMeshNode(mesh, new Vector(0, 0, 1, 1)));
	}
	
	public static void main(String... args) throws Exception {
		new MarchingSquaresScene(1);
	}
	
	private static ITriangleMesh cube(double size) {
		ITriangleMesh cube = new HalfEdgeTriangleMesh();
		cube.addVertex(new Vector(-size, -size, -size));
		cube.addVertex(new Vector( size, -size, -size));
		cube.addVertex(new Vector( size,  size, -size));
		cube.addVertex(new Vector(-size,  size, -size));
		cube.addVertex(new Vector(-size, -size,  size));
		cube.addVertex(new Vector( size, -size,  size));
		cube.addVertex(new Vector( size,  size,  size));
		cube.addVertex(new Vector(-size,  size,  size));
		cube.addTriangle(0, 1, 2);
		cube.addTriangle(2, 3, 0);
		cube.addTriangle(6, 5, 4);
		cube.addTriangle(4, 7, 6);
		cube.addTriangle(5, 1, 0);
		cube.addTriangle(0, 4, 5);
		cube.addTriangle(1, 5, 6);
		cube.addTriangle(6, 2, 1);
		cube.addTriangle(2, 6, 7);
		cube.addTriangle(7, 3, 2);
		cube.addTriangle(4, 0, 3);
		cube.addTriangle(3, 7, 4);
		return cube;
	}
}

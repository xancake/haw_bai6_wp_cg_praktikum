package computergraphics.exercises.exercise3;

import java.io.IOException;
import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;

public class HalfEdgeCowDemo {
	public static void main(String... args) throws IOException {
		new TriangleMeshShowcaseScene(new HalfEdgeTriangleMesh("meshes/cow.obj"));
	}
}

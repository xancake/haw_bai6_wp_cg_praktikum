package computergraphics.exercises.exercise2;

import java.io.IOException;
import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.mesh.indexlist.TriangleMesh;

public class CowDemo {
	public static void main(String... args) throws IOException {
		new TriangleMeshShowcaseScene(new TriangleMesh("meshes/cow.obj"));
	}
}

package computergraphics.exercises.exercise3;

import java.io.IOException;

import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.mesh.HalfEdgeTriangleMesh;

@SuppressWarnings("serial")
public class HalfEdgeCowScene extends TriangleMeshShowcaseScene {
	public HalfEdgeCowScene() throws IOException {
		super(new HalfEdgeTriangleMesh("meshes/cow.obj"));
	}

	public static void main(String... args) throws IOException {
		new HalfEdgeCowScene();
	}
}

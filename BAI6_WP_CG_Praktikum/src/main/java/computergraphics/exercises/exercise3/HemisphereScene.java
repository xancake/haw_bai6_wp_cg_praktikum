package computergraphics.exercises.exercise3;

import java.io.IOException;

import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.mesh.HalfEdgeTriangleMesh;

@SuppressWarnings("serial")
public class HemisphereScene extends TriangleMeshShowcaseScene {
	public HemisphereScene() throws IOException {
		super(new HalfEdgeTriangleMesh("meshes/hemisphere.obj"));
	}
	
	public static void main(String... args) throws IOException {
		new HemisphereScene();
	}
}

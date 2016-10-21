package computergraphics.exercises.exercise2;

import java.io.IOException;

import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.mesh.TriangleMesh;

@SuppressWarnings("serial")
public class CowScene extends TriangleMeshShowcaseScene {
	public CowScene() throws IOException {
		super(new TriangleMesh("meshes/cow.obj"));
	}
	
	public static void main(String... args) throws IOException {
		new CowScene();
	}
}

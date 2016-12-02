package computergraphics.exercises.exercise6;

import java.io.IOException;

import computergraphics.exercises.TriangleMeshShowcaseScene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;

public class CowShadowPolygonDemo {
	public static void main(String... args) throws IOException {
		ITriangleMesh mesh = new HalfEdgeTriangleMesh("meshes/cow.obj");
		ITriangleMesh shadow = new HalfEdgeTriangleMesh();
		mesh.createShadowPolygons(new Vector(1, 1, 1), 2, shadow);
		
		new TriangleMeshShowcaseScene(shadow);
	}
}

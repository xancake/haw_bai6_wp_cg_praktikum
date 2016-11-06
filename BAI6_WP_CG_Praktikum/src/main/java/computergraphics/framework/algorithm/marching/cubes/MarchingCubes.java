package computergraphics.framework.algorithm.marching.cubes;

import java.util.List;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.mesh.ITriangleMesh;

public interface MarchingCubes {
	/**
	 * Erzeugt das Mesh der übergebenen impliziten Funktion und Isowert.
	 * @param mesh Das Mesh
	 * @param function Die implizite Funktion
	 * @param isowert Der Isowert
	 */
	void createMesh(ITriangleMesh mesh, ImplicitFunction function, double isowert);
	
	
	Cuboid getVolume();
	
	
	List<Cuboid> getSubVolumes();
	
	
	int getResolution();
}

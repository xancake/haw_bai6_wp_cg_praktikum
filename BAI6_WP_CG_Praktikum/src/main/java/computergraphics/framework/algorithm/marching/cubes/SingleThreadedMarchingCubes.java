package computergraphics.framework.algorithm.marching.cubes;

import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.mesh.ITriangleMesh;

public class SingleThreadedMarchingCubes extends AbstractMarchingCubes implements MarchingCubes {
	public SingleThreadedMarchingCubes(Cuboid volume, int resolution) {
		super(volume, resolution);
	}
	
	@Override
	public void createMesh0(ITriangleMesh mesh, ImplicitFunction function, double isowert) {
		for(Cuboid subVolume : getSubVolumes()) {
			doCube(subVolume, mesh, function, isowert);
		}
	}
}

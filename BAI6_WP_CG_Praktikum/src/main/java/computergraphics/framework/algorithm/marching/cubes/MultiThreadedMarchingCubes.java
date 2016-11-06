package computergraphics.framework.algorithm.marching.cubes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.mesh.ITriangleMesh;

public class MultiThreadedMarchingCubes extends AbstractMarchingCubes implements MarchingCubes {
	private int _threadCount;
	
	public MultiThreadedMarchingCubes(Cuboid volume, int resolution) {
		this(volume, resolution, Runtime.getRuntime().availableProcessors());
	}
	
	public MultiThreadedMarchingCubes(Cuboid volume, int resolution, int threadCount) {
		super(volume, resolution);
		_threadCount = threadCount;
	}
	
	@Override
	public void createMesh0(ITriangleMesh mesh, ImplicitFunction function, double isowert) {
		try {
    		ExecutorService pool = Executors.newFixedThreadPool(_threadCount);
    		
    		for(Cuboid subVolume : getSubVolumes()) {
    			pool.submit(() -> doVolume(subVolume, mesh, function, isowert));
    		}
    		
    		pool.shutdown();
			while(!pool.awaitTermination(1, TimeUnit.MINUTES)) {
				System.out.println("Thread-Pool noch nicht beendet, wir warten weiter...");
			}
		} catch(InterruptedException e) {
			throw new RuntimeException("Es ist ein Fehler aufgetreten :'(", e);
		}
	}
	
	@Override
	protected void addTriangle(ITriangleMesh mesh, Vector p1, Vector p2, Vector p3) {
		// Überschreiben, damit das Hinzufügen von Dreiecken auf dem Mesh synchronisiert wird
		// Sonst schreiben sich die Tasks gegenseitig die Liste voll und es kommt Murks raus
		synchronized(mesh) {
			super.addTriangle(mesh, p1, p2, p3);
		}
	}
}

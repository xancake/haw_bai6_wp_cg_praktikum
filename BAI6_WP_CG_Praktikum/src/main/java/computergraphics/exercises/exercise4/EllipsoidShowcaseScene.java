package computergraphics.exercises.exercise4;

import computergraphics.framework.algorithm.marching.cubes.MarchingCubes;
import computergraphics.framework.algorithm.marching.cubes.MultiThreadedMarchingCubes;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.implicit_functions.EllipsoidFunction;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;

public class EllipsoidShowcaseScene {
	public static void main(String[] args) {
		MarchingCubes mc = new MultiThreadedMarchingCubes(new Cuboid(-2, 2), 25);
		ImplicitFunction function = new EllipsoidFunction(0.25, 0.25, 0.5);
		new MarchingCubesShowcaseScene(mc, function);
	}
}

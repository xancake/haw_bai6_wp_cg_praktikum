package computergraphics.exercises.exercise4;

import computergraphics.framework.algorithm.marching.cubes.MarchingCubes;
import computergraphics.framework.algorithm.marching.cubes.MultiThreadedMarchingCubes;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.implicit_functions.BoyscheFlaecheFunction;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;

public class BoyscheFlaecheShowcaseScene {
	public static void main(String[] args) {
		MarchingCubes mc = new MultiThreadedMarchingCubes(new Cuboid(-2, 2), 25);
		ImplicitFunction function = new BoyscheFlaecheFunction();
		new MarchingCubesShowcaseScene(mc, function);
	}
}

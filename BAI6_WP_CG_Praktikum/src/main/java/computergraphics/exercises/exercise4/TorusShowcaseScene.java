package computergraphics.exercises.exercise4;

import computergraphics.framework.algorithm.marching.cubes.MarchingCubes;
import computergraphics.framework.algorithm.marching.cubes.MultiThreadedMarchingCubes;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.math.implicit_functions.TorusFunction;

public class TorusShowcaseScene {
	public static void main(String[] args) {
		MarchingCubes mc = new MultiThreadedMarchingCubes(new Cuboid(-2, 2), 25);
		ImplicitFunction function = new TorusFunction(0.75, 0.25);
		new MarchingCubesShowcaseScene(mc, function);
	}
}

package computergraphics.exercises.exercise7;

import java.util.Arrays;
import computergraphics.framework.math.Vector;

public class BspExample10Demo {
	public static void main(String[] args) {
		new BspScene(Arrays.asList(
				new Vector(-0.75, -0.5,  0),
				new Vector(-0.25, -0.75, 0),
				new Vector(-0.25, -0.25, 0),
				new Vector( 0.25, -0.75, 0),
				new Vector( 0.5,  -0.5,  0),
				new Vector( 0.75, -0.25, 0),
				new Vector(-0.25,  0.25, 0),
				new Vector(-0.75,  0.5,  0),
				new Vector(-0.25,  0.75, 0),
				new Vector( 0.25,  0.25, 0)
		));
	}
}

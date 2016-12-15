package computergraphics.exercises.exercise7;

import java.util.ArrayList;
import java.util.List;
import computergraphics.framework.math.Vector;

public class RandomPointsBspDemo {
	public static void main(String[] args) {
		int numberOfPoints = 20;
		List<Vector> points = new ArrayList<Vector>();
		for(int i=0; i<numberOfPoints; i++) {
			points.add(new Vector(
					(float)(2 * Math.random() - 1),
					(float)(2 * Math.random() - 1),
					0
			));
		}
		new BspScene(points);
	}
}

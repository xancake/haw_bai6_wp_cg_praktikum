package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.math.curve.HermiteCurve;

public class HermiteCurveDemo {
	public static void main(String[] args) {
//		Curve curve = new HermiteCurve(
//				new Vector(0.5, 0, 0),
//				new Vector(0.5, 0, 0),
//				new Vector(0.5, 0, 0),
//				new Vector(0.5, 1, 0)
//		);
		Curve curve = new HermiteCurve(
				new Vector(0.5, 0, 0),
				new Vector(1, 0, 0),
				new Vector(-1, 0, 0),
				new Vector(0.5, 1, 0)
		);
		new CurveShowcaseScene(curve);
	}
}

package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.BezierCurve;
import computergraphics.framework.math.curve.Curve;

public class BezierCurve3DDemo {
	public static void main(String[] args) {
		Curve curve = new BezierCurve(
				new Vector( 0,  1, -1),
				new Vector(-3,  1,  1),
				new Vector( 3, -1,  1),
				new Vector( 0, -1, -1)
		);
		new CurveShowcaseScene(curve);
	}
}

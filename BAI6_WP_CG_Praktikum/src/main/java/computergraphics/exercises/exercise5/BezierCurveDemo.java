package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.BezierCurve;
import computergraphics.framework.math.curve.Curve;

public class BezierCurveDemo {
	public static void main(String[] args) {
		Curve curve = new BezierCurve(
				new Vector(-0.5,  -0.5, 0),
				new Vector(-0.25,  0.5, 0),
				new Vector( 0.25, -0.5, 0),
				new Vector( 0.5,   0.5, 0)
		);
		new CurveShowcaseScene(curve);
	}
}

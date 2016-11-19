package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.math.curve.HermiteCurve;

public class HermiteCurve3DDemo {
	public static void main(String[] args) {
		Curve curve = new HermiteCurve(
				new Vector( 0.5,  0.5, -1),
				new Vector(-2,    2,    0),
				new Vector( 2,    2,    0),
				new Vector(-0.5, -0.5,  1)
		);
		new CurveShowcaseScene(curve);
	}
}

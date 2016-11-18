package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.CurveNode;

@SuppressWarnings("serial")
public class CurveShowcaseScene extends Scene {
	private static final int DEFAULT_RESOLUTION_STEP = 10;
	
	private CurveNode _curveNode;
	
	public CurveShowcaseScene(Curve curve) {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);

		_curveNode = new CurveNode(curve);
		getRoot().addChild(_curveNode);
	}

	public void keyPressed(int keyCode) {
		switch (Character.toUpperCase(keyCode)) {
			case 'C':
				_curveNode.setDrawCurve(!_curveNode.isDrawCurve());
				break;
			case 'P':
				_curveNode.setDrawControlPoints(!_curveNode.isDrawControlPoints());
				break;
			case 'T':
				_curveNode.setDrawTangent(!_curveNode.isDrawTangent());
				break;
			case '+':
				_curveNode.setCurveResolution(_curveNode.getCurveResolution() + DEFAULT_RESOLUTION_STEP);
				break;
			case '-':
				int resolution = _curveNode.getCurveResolution() - DEFAULT_RESOLUTION_STEP;
				if(resolution > 0) {
					_curveNode.setCurveResolution(resolution);
				}
				break;
			case ',': {
				double t = _curveNode.getTangentT() - 1.0/_curveNode.getCurveResolution();
				_curveNode.setTangentT(t > 0 ? t : 0);
				break;
			}
			case '.': {
				double t = _curveNode.getTangentT() + 1.0/_curveNode.getCurveResolution();
				_curveNode.setTangentT(t < 1 ? t : 1);
				break;
			}
		}
	}
}

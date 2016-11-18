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
	private static final double TANGENT_DRAW_LENGTH_STEP = 0.01;
	
	private CurveNode _curveNode;
	
	public CurveShowcaseScene(Curve curve) {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);

		_curveNode = new CurveNode(curve);
		_curveNode.setTangentDrawLength(0.25);
		getRoot().addChild(_curveNode);
	}

	public void keyPressed(int keyCode) {
		double maxT = _curveNode.getCurve().getMaxT();
		int resolution = _curveNode.getCurveResolution();
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
				_curveNode.setCurveResolution(resolution + DEFAULT_RESOLUTION_STEP);
				break;
			case '-':
				int newResolution = resolution - DEFAULT_RESOLUTION_STEP;
				if(newResolution > 0) {
					_curveNode.setCurveResolution(newResolution);
				}
				break;
			case ',': {
				double t = _curveNode.getTangentT() - maxT/resolution;
				_curveNode.setTangentT(t > 0 ? t : 0);
				break;
			}
			case '.': {
				double t = _curveNode.getTangentT() + maxT/resolution;
				_curveNode.setTangentT(t < maxT ? t : maxT);
				break;
			}
			case '*':
				_curveNode.setTangentDrawLength(_curveNode.getTangentDrawLength() + TANGENT_DRAW_LENGTH_STEP);
				break;
			case '/':
				if (_curveNode.getTangentDrawLength()-TANGENT_DRAW_LENGTH_STEP > 0) {
					_curveNode.setTangentDrawLength(_curveNode.getTangentDrawLength() - TANGENT_DRAW_LENGTH_STEP);
				}
				break;
		}
	}
}

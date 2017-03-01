package org.haw.cg.lnielsen.particle.scenegraph;

import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.scenegraph.nodes.primitives.ParticleSystemNode;
import com.jogamp.opengl.GL2;
import computergraphics.framework.algorithm.marching.cubes.SingleThreadedMarchingCubes;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.MarchingCubesVisualizationNode;

@SuppressWarnings("serial")
public class ParticleSystemShowcaseScene extends Scene {
	private static final Vector WIND    = new Vector(0, 0, -1);
	private static final Vector GRAVITY = new Vector(0, -1, 0);
	
	private MarchingCubesVisualizationNode _grid;
	private ParticleSystemNode _particleSystemNode;
	
	private boolean _applyWind;
	private boolean _applyGravity;
	
	public ParticleSystemShowcaseScene(ParticleSystem system, int fps) {
		super((int)(1000./fps), Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);
		
		_particleSystemNode = new ParticleSystemNode(system);
		_particleSystemNode.setDoUpdates(true);
		getRoot().addChild(_particleSystemNode);
		
		// MarchingCubes Zweckentfremden, um das Koordinatenkreuz um den Nullpunkt von -2³ bis 2³ zu zeichnen
		_grid = new MarchingCubesVisualizationNode(new SingleThreadedMarchingCubes(new Cuboid(-2, 2), 2));
		_grid.setDrawVolume(false);
		_grid.setDrawSubVolumes(true);
		getRoot().addChild(_grid);
	}
	
	@Override
	public void init(GL2 gl) {
		super.init(gl);
		gl.glPointSize(5);
		gl.glLineWidth(1);
	}
	
	@Override
	public void keyPressed(int keyCode) {
		switch (Character.toUpperCase(keyCode)) {
			case '1':
				ParticleSystem.DEBUG = !ParticleSystem.DEBUG;
				break;
			case '2':
				_grid.setDrawSubVolumes(!_grid.isDrawSubVolumes());
				break;
			case 'W':
				toggleWind();
				break;
			case 'G':
				toggleGravity();
				break;
			case 'D':
				_particleSystemNode.setDrawParticleSystem(!_particleSystemNode.isDrawParticleSystem());
				break;
			case 'B':
				_particleSystemNode.setBackToFront(!_particleSystemNode.isBackToFront());
				break;
		}
	}
	
	private void toggleWind() {
		_applyWind = !_applyWind;
		if(_applyWind) {
			_particleSystemNode.getParticleSystem().applyForce(WIND);
		} else {
			_particleSystemNode.getParticleSystem().removeForce(WIND);
		}
	}
	
	private void toggleGravity() {
		_applyGravity = !_applyGravity;
		if(_applyGravity) {
			_particleSystemNode.getParticleSystem().applyGravity(GRAVITY);
		} else {
			_particleSystemNode.getParticleSystem().removeGravity(GRAVITY);
		}
	}
	
	@Override
	public void rotateCamera(double angleAroundUp, double angleUpDown) {
		super.rotateCamera(angleAroundUp, angleUpDown);
		// Kameraviewpoint dem Partikelsystem-Node übergeben, damit die Back-to-Front-Sortierung korrekt erfolgen kann
		_particleSystemNode.setViewpoint(getRoot().getCamera().getEye());
	}
}

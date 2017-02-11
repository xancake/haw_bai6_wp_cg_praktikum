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
	private MarchingCubesVisualizationNode _grid;
	private ParticleSystemNode _particleSystemNode;
	
	public ParticleSystemShowcaseScene(ParticleSystem system, int fps) {
		super((int)(1000./fps), Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);
		
		_particleSystemNode = new ParticleSystemNode(system);
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
			case 'G':
				_grid.setDrawSubVolumes(!_grid.isDrawSubVolumes());
				break;
			case 'V':
				_grid.setDrawVolume(!_grid.isDrawVolume());
				break;
			case 'D':
				_particleSystemNode.setDrawParticleSystem(!_particleSystemNode.isDrawParticleSystem());
				break;
			case 'B':
				_particleSystemNode.setBackToFront(!_particleSystemNode.isBackToFront());
				break;
		}
	}
	
	@Override
	public void rotateCamera(double angleAroundUp, double angleUpDown) {
		super.rotateCamera(angleAroundUp, angleUpDown);
		// Kameraviewpoint dem Partikelsystem-Node übergeben, damit die Back-to-Front-Sortierung korrekt erfolgen kann
		_particleSystemNode.setViewpoint(getRoot().getCamera().getEye());
	}
}

package org.haw.cg.lnielsen.particle.scenegraph;

import java.util.Objects;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.physics.Repeller;
import org.haw.cg.lnielsen.particle.scenegraph.nodes.primitives.ParticleSystemNode;
import org.haw.cg.lnielsen.particle.scenegraph.nodes.primitives.RepellerNode;
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
	private static final double REPELLER_POWER_INCREMENT = 1;
	private static final double REPELLER_RANGE_INCREMENT = 0.25;
	private static final Vector WIND    = new Vector(0, 0, -1);
	private static final Vector GRAVITY = new Vector(0, -1, 0);
	
	private MarchingCubesVisualizationNode _grid;
	private ParticleSystemNode _particleSystemNode;
	private RepellerNode _repellerNode;
	
	private boolean _applyWind;
	private boolean _applyGravity;
	private boolean _applyRepeller;
	
	public ParticleSystemShowcaseScene(ParticleSystem system, int fps) {
		super((int)(1000./fps), Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(2, 2, 2));
		getRoot().setAnimated(true);
		
		// MarchingCubes Zweckentfremden, um das Koordinatenkreuz um den Nullpunkt von -2³ bis 2³ zu zeichnen
		_grid = new MarchingCubesVisualizationNode(new SingleThreadedMarchingCubes(new Cuboid(-2, 2), 2));
		_grid.setDrawVolume(false);
		_grid.setDrawSubVolumes(true);
		getRoot().addChild(_grid);
		
		_particleSystemNode = new ParticleSystemNode(system);
		_particleSystemNode.setDoUpdates(true);
		getRoot().addChild(_particleSystemNode);
		
		_repellerNode = new RepellerNode(new Repeller(new Vector(0, 0, 0), 0, 0));
		getRoot().addChild(_repellerNode);
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
			case 'R':
				toggleRepeller();
				break;
			case 'T':
				if(_applyRepeller) {
					_repellerNode.setDrawRepeller(!_repellerNode.isDrawRepeller());
				}
				break;
			case '+':
				_repellerNode.getRepeller().setPower(_repellerNode.getRepeller().getPower() + REPELLER_POWER_INCREMENT);
				break;
			case '-':
				_repellerNode.getRepeller().setPower(_repellerNode.getRepeller().getPower() - REPELLER_POWER_INCREMENT);
				break;
			case '.':
				_repellerNode.getRepeller().setRange(_repellerNode.getRepeller().getRange() + REPELLER_RANGE_INCREMENT);
				break;
			case ',':
				if(_repellerNode.getRepeller().getRange() - REPELLER_RANGE_INCREMENT >= 0) {
					_repellerNode.getRepeller().setRange(_repellerNode.getRepeller().getRange() - REPELLER_RANGE_INCREMENT);
				}
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
	
	private void toggleRepeller() {
		_applyRepeller = !_applyRepeller;
		if(_applyRepeller) {
			_particleSystemNode.getParticleSystem().addRepeller(_repellerNode.getRepeller());
		} else {
			_particleSystemNode.getParticleSystem().removeRepeller(_repellerNode.getRepeller());
		}
		_repellerNode.setDrawRepeller(_applyRepeller);
	}
	
	@Override
	public void rotateCamera(double angleAroundUp, double angleUpDown) {
		super.rotateCamera(angleAroundUp, angleUpDown);
		// Kameraviewpoint dem Partikelsystem-Node übergeben, damit die Back-to-Front-Sortierung korrekt erfolgen kann
		_particleSystemNode.setViewpoint(getRoot().getCamera().getEye());
	}
	
	public void setRepeller(Repeller repeller) {
		Objects.requireNonNull(repeller);
		if(_applyRepeller) {
			// Alten Repeller entfernen und neuen hinzufügen, falls der Repeller gerade angewendet werden soll
			_particleSystemNode.getParticleSystem().removeRepeller(_repellerNode.getRepeller());
			_particleSystemNode.getParticleSystem().addRepeller(repeller);
		}
		_repellerNode.setRepeller(repeller);
	}
}

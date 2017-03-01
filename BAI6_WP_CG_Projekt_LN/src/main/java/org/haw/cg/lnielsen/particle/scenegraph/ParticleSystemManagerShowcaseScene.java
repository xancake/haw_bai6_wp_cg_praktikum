package org.haw.cg.lnielsen.particle.scenegraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.ParticleSystemManager;
import org.haw.cg.lnielsen.particle.ParticleSystemManager.ParticleSystemManagerListener;
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
public class ParticleSystemManagerShowcaseScene extends Scene {
	private MarchingCubesVisualizationNode _grid;
	private ParticleSystemManager _particleSystemManager;
	private Map<ParticleSystem, ParticleSystemNode> _particleSystems;
	
	public ParticleSystemManagerShowcaseScene(int fps) {
		super((int)(1000./fps), Shader.ShaderMode.PHONG, RenderMode.REGULAR);
		
		_particleSystems = new HashMap<>();
		_particleSystemManager = new ParticleSystemManager();
		_particleSystemManager.addListener(new MyManagerListener());

		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);
		
		// MarchingCubes Zweckentfremden, um das Koordinatenkreuz um den Nullpunkt von -2³ bis 2³ zu zeichnen
		_grid = new MarchingCubesVisualizationNode(new SingleThreadedMarchingCubes(new Cuboid(-2, 2), 2));
		_grid.setDrawVolume(false);
		_grid.setDrawSubVolumes(true);
		getRoot().addChild(_grid);
	}
	
	/**
	 * Fügt das übergebene {@link ParticleSystem} dieser Szene hinzu.
	 * <p>Technisch wird das Partikelsystem dem internen {@link ParticleSystemManager} hinzugefügt und auf das
	 * {@link ParticleSystemManagerListener#onParticleSystemAdded(ParticleSystem)}-Event gewartet, wodurch das
	 * Partikelsystem letztendlich wirklich Szenengraphen hinzugefügt wird.
	 * @param system Das hinzuzufügende Partikelsystem
	 */
	public void addParticleSystem(ParticleSystem system) {
		_particleSystemManager.addParticleSystem(system);
	}
	
	/**
	 * Entfernt das übergebene {@link ParticleSystem} von dieser Szene.
	 * <p>Technisch wird das PArtikelsystem von dem internen {@link ParticleSystemManager} entfernt und auf das
	 * {@link ParticleSystemManagerListener#onParticleSystemRemoved(ParticleSystem)}-Event gewartet, wodurch das
	 * Partikelsystem letztendlich wirklich aus dem Szenengraphen entfernt wird.
	 * @param system Das zu entfernende Partikelsystem
	 */
	public void removeParticleSystem(ParticleSystem system) {
		_particleSystemManager.removeParticleSystem(system);
	}
	
	@Override
	public void init(GL2 gl) {
		super.init(gl);
		gl.glPointSize(1);
		gl.glLineWidth(1);
	}
	
	@Override
	public void timerTick(int counter) {
		_particleSystemManager.update();
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
			case '1':
				ParticleSystem.DEBUG = !ParticleSystem.DEBUG;
				break;
			case '+':
				addParticleSystem(ParticlePreset.randomSystem());
				break;
		}
	}
	
	@Override
	public void rotateCamera(double angleAroundUp, double angleUpDown) {
		super.rotateCamera(angleAroundUp, angleUpDown);
		// Kameraviewpoint dem Partikelsystem-Node übergeben, damit die Back-to-Front-Sortierung korrekt erfolgen kann
		for(ParticleSystemNode node : _particleSystems.values()) {
			node.setViewpoint(getRoot().getCamera().getEye());
		}
	}
	
	private class MyManagerListener implements ParticleSystemManagerListener {
		@Override
		public void onParticleSystemAdded(ParticleSystem system) {
			ParticleSystemNode node = new ParticleSystemNode(system);
			_particleSystems.put(system, node);
			getRoot().addChild(node);
			System.out.println("Particle-System added!");
		}
		
		@Override
		public void onParticleSystemDied(ParticleSystem system) {
			System.out.println("Particle-System died!");
		}

		@Override
		public void onParticleSystemRemoved(ParticleSystem system) {
			ParticleSystemNode node = _particleSystems.remove(system);
			getRoot().removeChild(node);
			System.out.println("Particle-System removed!");
		}
	}
	
	private static enum ParticlePreset {
		EXPLOSION(10000, 20000, true, new Particle.Builder()
				.withStartLife(TimeUnit.SECONDS.toMillis(2))
				.withVelocity(new Vector(-1, 0, -1), new Vector(1, 1, 1))
				.withColorStart(new Vector(1, 0.1, 0))
				.withColorEnd(new Vector(1, 0.9, 0))
				.withFadeOut(true)
				.addGravity(new Vector(0, -0.5, 0)));
		
		private Particle.Builder _builder;
		private int _maxParticles;
		private int _spawnPerSecond;
		private boolean _spawnCapped;
		
		private ParticlePreset(int maxParticles, int spawnPerSecond, boolean spawnCapped, Particle.Builder builder) {
			_maxParticles = maxParticles;
			_spawnPerSecond = spawnPerSecond;
			_spawnCapped = spawnCapped;
			_builder = Objects.requireNonNull(builder);
		}
		
		public ParticleSystem create() {
			return new ParticleSystem(_builder, _maxParticles, _spawnPerSecond, _spawnCapped);
		}
		
		public static ParticlePreset random() {
			return values()[(int)(Math.random()*(values().length-1))];
		}
		
		public static ParticleSystem randomSystem() {
			return random().create();
		}
	}
}

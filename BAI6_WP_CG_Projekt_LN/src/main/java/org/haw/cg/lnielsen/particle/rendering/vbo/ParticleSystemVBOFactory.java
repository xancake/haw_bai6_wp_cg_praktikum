package org.haw.cg.lnielsen.particle.rendering.vbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.vbo.RenderVertex;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.rendering.vbo.VertexBufferObjectFactory;
import computergraphics.framework.scenegraph.bsp.BspTreeTools;

public class ParticleSystemVBOFactory extends VertexBufferObjectFactory {
	private ParticleSystem _system;
	private BspTreeTools _bspTools;
	
	public ParticleSystemVBOFactory(ParticleSystem system) {
		_system = Objects.requireNonNull(system);
		_bspTools = new BspTreeTools();
	}
	
	public VertexBufferObject createParticleSystemVBO() {
		List<RenderVertex> renderVertices = new ArrayList<>();
		for(Particle particle : _system.getParticles()) {
			renderVertices.add(new RenderVertex(
					particle.getLocation(),
					new Vector(1, 1, 1),
					particle.getColor()
			));
		}
		return new VertexBufferObject(GL2.GL_POINTS, renderVertices);
	}
	
	public VertexBufferObject createBackToFrontSortedParticleSystemVBO(Vector viewpoint) {
		List<RenderVertex> renderVertices = new ArrayList<>();
		
		List<Particle> particles = new ArrayList<>(_system.getParticles());
		List<Vector> locations = new ArrayList<>(particles.size());
		for(Particle p : particles) {
			locations.add(p.getLocation());
		}
		
		List<Integer> backToFrontOrder = _bspTools.getBackToFront(locations, viewpoint);
		for(int i : backToFrontOrder) {
			Particle particle = particles.get(i);
			renderVertices.add(new RenderVertex(
					particle.getLocation(),
					new Vector(1, 1, 1),
					particle.getColor()
			));
		}
		return new VertexBufferObject(GL2.GL_POINTS, renderVertices);
	}
}

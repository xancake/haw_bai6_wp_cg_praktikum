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

public class ParticleSystemVBOFactory extends VertexBufferObjectFactory {
	private ParticleSystem _system;
	
	public ParticleSystemVBOFactory(ParticleSystem system) {
		_system = Objects.requireNonNull(system);
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
}

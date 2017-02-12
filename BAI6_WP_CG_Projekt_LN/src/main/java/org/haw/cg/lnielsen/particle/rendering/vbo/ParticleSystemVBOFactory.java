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

/**
 * Eine Klasse zum Erzeugen von {@link VertexBufferObject}s für {@link ParticleSystem}e.
 */
public class ParticleSystemVBOFactory extends VertexBufferObjectFactory {
	private ParticleSystem _system;
	private BspTreeTools _bspTools;
	
	/**
	 * Instanziiert eine neue Fabrik für das übergebene {@link ParticleSystem}.
	 * @param system Das {@link ParticleSystem} für das diese Fabrik {@link VertexBufferObject}s erzeugen soll, nicht {@code null}
	 * @throws NullPointerException, wenn das Partikelsystem {@code null} ist
	 */
	public ParticleSystemVBOFactory(ParticleSystem system) {
		_system = Objects.requireNonNull(system);
		_bspTools = new BspTreeTools();
	}
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} für das Partikelsystem, bei dem alle Partikel als Punkte repräsentiert
	 * sind. Die Partikel werden in der Reihenfolge der internen Datenstruktur des PartikelSystems gezeichnet, also
	 * weitestgehend zufällig.
	 * @return Das {@link VertexBufferObject}
	 */
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
	
	/**
	 * Erzeugt ein {@link VertexBufferObject} für das Partikelsystem, bei dem alle Partikel als Punkte repräsentiert
	 * sind. Die Partikel werden abhängig von dem übergebenen Sichtpunkt back-to-front sortiert und in der Reihenfolge
	 * auch gezeichnet.
	 * @param viewpoint Der Sichtpunkt von dem aus die back-to-front-Sortierung berechnet werden soll
	 * @return Das {@link VertexBufferObject}
	 */
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

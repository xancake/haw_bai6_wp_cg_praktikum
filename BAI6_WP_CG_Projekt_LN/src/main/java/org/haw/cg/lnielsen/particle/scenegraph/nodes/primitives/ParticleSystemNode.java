package org.haw.cg.lnielsen.particle.scenegraph.nodes.primitives;

import java.util.Objects;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.rendering.vbo.ParticleSystemVBOFactory;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

/**
 * Ein Szenengraphknoten für die Einbindung eines Partikelsystems in den Szenengraphen.
 */
public class ParticleSystemNode extends LeafNode {
	private ParticleSystem _particleSystem;
	private ParticleSystemVBOFactory _factory;
	
	private VertexBufferObject _particleSystemVBO;
	private boolean _drawBackToFront;
	private Vector _viewpoint;
	private boolean _drawParticleSystem;
	
	/**
	 * Instanziiert einen neuen Szenengraphknoten für das übergebene {@link ParticleSystem}.
	 * @param system Ein Partikelsystem
	 */
	public ParticleSystemNode(ParticleSystem system) {
		setParticleSystem(system);
		setDrawParticleSystem(true);
		setViewpoint(new Vector(0,0,0));
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if(isDrawParticleSystem()) {
			_particleSystemVBO.draw(gl);
		}
	}
	
	@Override
	public void timerTick(int counter) {
		if(isDrawParticleSystem()) {
    		_particleSystem.update();
    		_particleSystemVBO = isBackToFront()
    				? _factory.createBackToFrontSortedParticleSystemVBO(_viewpoint)
    				: _factory.createParticleSystemVBO();
		}
	}
	
	/**
	 * Das {@link ParticleSystem}, das von diesem Knoten dargestellt werd.
	 * @return Das Partikelsystem
	 */
	public ParticleSystem getParticleSystem() {
		return _particleSystem;
	}
	
	/**
	 * Legt das {@link ParticleSystem} fest, das von diesem Knoten dargestellt werden soll.
	 * @param system Das Partikelsystem
	 */
	public void setParticleSystem(ParticleSystem system) {
		_particleSystem = Objects.requireNonNull(system);
		_factory = new ParticleSystemVBOFactory(_particleSystem);
		_particleSystemVBO = _factory.createParticleSystemVBO();
	}
	
	/**
	 * Gibt zurück, ob das Partikelsystem gerade dargestellt wird oder nicht.
	 * @return {@code true} wenn das Partikelsystem dargestellt wird, ansonsten {@code false}
	 */
	public boolean isDrawParticleSystem() {
		return _drawParticleSystem;
	}
	
	/**
	 * Legt fest, ob das {@link ParticleSystem} dargestellt werden soll oder nicht.
	 * @param draw {@code true} wenn das Partikelsystem dargestellt werden soll, ansonsten {@code false}
	 */
	public void setDrawParticleSystem(boolean draw) {
		_drawParticleSystem = draw;
	}
	
	/**
	 * Gibt zurück, ob die {@link Particle Partikel} back-to-front gezeichnet werden oder nicht.
	 * Damit die back-to-front-Sortierung gelingt muss zwingend mit {@link #setViewpoint(Vector)} der Sichtpunkt
	 * übergeben werden, da von diesem ausgehend die Sortierung ermittelt wird.
	 * @return {@code true} wenn die Partikel back-to-front gezeichnet werden, ansonsten {@code false}
	 */
	public boolean isBackToFront() {
		return _drawBackToFront;
	}
	
	/**
	 * Legt fest, ob die {@link Particle Partikel} back-to-front gezeichnet werden sollen oder nicht.
	 * @param backToFront {@code true} wenn die Partikel back-to-front gezeichnet werden sollen, ansonsten {@code false}
	 */
	public void setBackToFront(boolean backToFront) {
		_drawBackToFront = backToFront;
	}
	
	/**
	 * Gibt den aktuellen Sichtpunkt zurück, von dem aus die back-to-front-Sortierung berechnet wird, wenn back-to-front
	 * gezeichnet werden soll.
	 * @return Der Sichtpunkt von dem aus die back-to-front-Sortierung berechnet wird
	 * @see #isBackToFront()
	 */
	public Vector getViewpoint() {
		return _viewpoint;
	}
	
	/**
	 * Legt den Sichtpunkt fest, von dem aus die back-to-front-Sortierung berechnet werden soll, wenn back-to-front
	 * gezeichnet werden soll.
	 * @param viewpoint Der Sichtpunkt von dem aus die back-to-front-Sortierung berechnet werden soll
	 * @see #isBackToFront()
	 */
	public void setViewpoint(Vector viewpoint) {
		_viewpoint = Vector.checkDimension(viewpoint, 3);
	}
}

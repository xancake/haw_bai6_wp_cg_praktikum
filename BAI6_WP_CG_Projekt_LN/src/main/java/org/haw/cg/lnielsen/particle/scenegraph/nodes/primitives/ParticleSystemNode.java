package org.haw.cg.lnielsen.particle.scenegraph.nodes.primitives;

import java.util.Objects;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.rendering.vbo.ParticleSystemVBOFactory;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class ParticleSystemNode extends LeafNode {
	private ParticleSystem _particleSystem;
	private ParticleSystemVBOFactory _factory;
	
	private VertexBufferObject _particleSystemVBO;
	private boolean _drawParticleSystem;
	
	public ParticleSystemNode(ParticleSystem system) {
		setParticleSystem(system);
		setDrawParticleSystem(true);
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
    		_particleSystemVBO = _factory.createParticleSystemVBO();
		}
	}
	
	public ParticleSystem getParticleSystem() {
		return _particleSystem;
	}
	
	public void setParticleSystem(ParticleSystem system) {
		_particleSystem = Objects.requireNonNull(system);
		_factory = new ParticleSystemVBOFactory(_particleSystem);
		_particleSystemVBO = _factory.createParticleSystemVBO();
	}
	
	public boolean isDrawParticleSystem() {
		return _drawParticleSystem;
	}
	
	public void setDrawParticleSystem(boolean draw) {
		_drawParticleSystem = draw;
	}
}

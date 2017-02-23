package org.haw.cg.lnielsen.particle;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.haw.cg.lnielsen.util.event.EventDispatcher;

/**
 * Diese Klasse managed den Lebenszyklus von Partikelsystemen.
 */
public class ParticleSystemManager {
	private EventDispatcher<ParticleSystemManagerListener> _eventDispatcher;
	private Collection<ParticleSystem> _systems;
	
	public ParticleSystemManager() {
		_eventDispatcher = new EventDispatcher<>();
		_systems = new LinkedList<>();
	}
	
	/**
	 * Updated alle registrierten {@link ParticleSystem Partikelsysteme}.
	 * <p>Wenn ein Partikelsystem als {@link ParticleSystem#isDead() tot} eingestuft wurde, wird es von dem Manager
	 * entlassen.
	 */
	public void update() {
		for(Iterator<ParticleSystem> iter = _systems.iterator(); iter.hasNext(); ) {
			ParticleSystem system = iter.next();
			system.update();
			if(system.isDead()) {
				_eventDispatcher.fireEvent(e -> e.onParticleSystemDied(system));
				iter.remove();
				_eventDispatcher.fireEvent(e -> e.onParticleSystemRemoved(system));
			}
		}
	}
	
	/**
	 * Fügt das übergebene {@link ParticleSystem} diesem Manager hinzu.
	 * @param system Das hinzuzufügende Partikelsystem
	 */
	public void addParticleSystem(ParticleSystem system) {
		_systems.add(system);
		_eventDispatcher.fireEvent(e -> e.onParticleSystemAdded(system));
	}
	
	/**
	 * Entfernt das übergebene {@link ParticleSystem} von diesem Manager, wenn es vorhanden war.
	 * @param system Das zu entfernende Partikelsystem
	 * @return {@code true} wenn das Partikelsystem vorhanden war und erfolgreich entfernt wurde, ansonsten {@code false}
	 */
	public boolean removeParticleSystem(ParticleSystem system) {
		boolean removed = _systems.remove(system);
		if(removed) {
			_eventDispatcher.fireEvent(e -> e.onParticleSystemRemoved(system));
		}
		return removed;
	}
	
	/**
	 * Prüft, ob das übergebene {@link ParticleSystem} von diesem Manager gemanaged wird.
	 * @param system Das zu prüfende Partikelsystem
	 * @return {@code true} wenn das Partikelsystem von diesem Manager gemanaged wird, ansonsten {@code false}
	 */
	public boolean hasParticleSystem(ParticleSystem system) {
		return _systems.contains(system);
	}
	
	/**
	 * Gibt zurück, wieviele {@link ParticleSystem Partikelsysteme} an diesem Manager registriert sind.
	 * @return Die Anzahl der aktuell registrierten Partikelsysteme
	 */
	public int getParticleSystemCount() {
		return _systems.size();
	}
	
	/**
	 * Entlässt alle an diesem Manager registrierten {@link ParticleSystem}.
	 */
	public void shutdown() {
		_systems.clear();
	}
	
	/**
	 * Registriert den übergebenen Listener an diesem Manager. Der Listener wird fortan über Ereignisse des Managers
	 * informiert.
	 * @param listener Der zu registrierende Listener
	 */
	public void addListener(ParticleSystemManagerListener listener) {
		_eventDispatcher.addListener(listener);
	}
	
	/**
	 * Entfernt den übergebenen Listener von diesem Manager. Der Listener wird nicht mehr über Ereignisse des Managers
	 * informiert.
	 * @param listener Der zu entfernende Manager
	 */
	public void removeListener(ParticleSystemManagerListener listener) {
		_eventDispatcher.removeListener(listener);
	}
	
	/**
	 * Schnittstelle die es erlaubt an einem {@link ParticleSystemManager} auf Ereignisse zu lauschen und reagieren.
	 */
	public interface ParticleSystemManagerListener {
		/**
		 * Wird immer dann vom {@link ParticleSystemManager} aufgerufen, wenn ein neues {@link ParticleSystem}
		 * hinzugefügt wurde.
		 * @param system Das hinzugefügte Partikelsystem
		 */
		void onParticleSystemAdded(ParticleSystem system);
		
		/**
		 * Wird immer dann vom {@link ParticleSystemManager} aufgerufen, wenn ein {@link ParticleSystem} des Managers
		 * gestorben ist.
		 * @param system Das gestorbene Partikelsystem
		 */
		void onParticleSystemDied(ParticleSystem system);
		
		/**
		 * Wird immer dann vom {@link ParticleSystemManager} aufgerufen, wenn ein {@link ParticleSystem} des Managers
		 * entfernt wurde.
		 * @param system Das entfernte Partikelsystem
		 */
		void onParticleSystemRemoved(ParticleSystem system);
	}
}

package org.haw.cg.lnielsen.util.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Eine Klasse die Listener in einer Liste verwaltet und sie über
 * {@link Event Ereignisse} informiert, die der
 * {@link #fireEvent(Event)}-Methode übergeben werden. 
 * 
 * @param <L> Der Typ des konkreten Listeners der benachrichtigt werden soll
 */
public class EventDispatcher<L> {
	private List<L> _listeners;
	
	/**
	 * Instanziiert einen neuen EventDispatcher, an dem noch keine Listener
	 * registriert sind.
	 */
	public EventDispatcher() {
		this(new ArrayList<L>());
	}
	
	protected EventDispatcher(List<L> listenerList) {
		_listeners = listenerList;
	}
	
	/**
	 * Fügt diesem EventDispatcher einen neuen Listener hinzu.
	 * 
	 * <p>Registrierte Listener werden bei Aufruf von {@link #fireEvent(Event)}
	 * über das übergebene {@link Event Ereignis} benachrichtigt.
	 * 
	 * @param listener Der hinzuzufügende Listener
	 * 
	 * @see #fireEvent(Event)
	 */
	public void addListener(L listener) {
		_listeners.add(Objects.requireNonNull(listener));
	}
	
	/**
	 * Entfernt den übergebenen Listener von diesem EventDispatcher.
	 * 
	 * <p>Registrierte Listener werden bei Aufruf von {@link #fireEvent(Event)}
	 * über das übergebene {@link Event Ereignis} benachrichtigt.
	 * 
	 * @param listener Der zu entfernende Listener
	 * 
	 * @see #fireEvent(Event)
	 */
	public void removeListener(L listener) {
		_listeners.remove(listener);
	}
	
	/**
	 * Gibt eine unmodifizierbare Kopie der Liste aller an diesem
	 * EventDispatcher registrierten Listener zurück.
	 * 
	 * @return Eine unmodifizierbare Kopie der Liste aller registrierten
	 *         Listener
	 */
	public List<L> getListeners() {
		return Collections.unmodifiableList(_listeners);
	}
	
	/**
	 * Meldet das übergebene {@link Event Ereignis} an alle an diesem
	 * EventDispatcher registrierten Listener.
	 * 
	 * @param event Das zu meldende Ereignis
	 * 
	 * @see #getListeners()
	 */
	public void fireEvent(Event<? super L> event) {
		for(L l : _listeners) {
			event.fireEvent(l);
		}
	}
}

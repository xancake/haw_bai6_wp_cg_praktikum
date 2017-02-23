package org.haw.cg.lnielsen.util.event;

/**
 * Eine Schnittstelle für Ereignisse, die mittels einem {@link EventDispatcher}
 * Listener benachrichtigen sollen.
 * 
 * @param <L> Der Typ des konkreten Listeners der benachrichtigt werden soll
 */
@FunctionalInterface
public interface Event<L> {
	/**
	 * Benachrichtigt den übergebenen Listener über dieses Ereignis. Das
	 * konkrete Ereignis kann somit entscheiden, welche Methode am Listener
	 * aufgerufen wird. 
	 * 
	 * <p>Diese Methode wird standardmäßig vom {@link EventDispatcher} für jeden
	 * an ihm registrierten Listener aufgerufen, wenn
	 * {@link EventDispatcher#fireEvent(Event)} aufgerufen wird.
	 * 
	 * @param listener Der zu benachrichtigende Listener
	 */
	void fireEvent(L listener);
}

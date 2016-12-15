package computergraphics.framework.scenegraph.bsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import computergraphics.framework.math.Vector;

/**
 * A node in a BSP tree.
 * 
 * @author Philipp Jenke
 */
public class BspTreeNode {
	/**
	 * Enum for front and back orientation.
	 */
	public enum Orientation {
		POSITIVE,
		NEGATIVE
	}
	
	/**
	 * Plane point.
	 */
	private Vector _base;
	
	/**
	 * Plane normal
	 */
	private Vector _normal;
	
	/**
	 * Child node on positive half space (in normal direction).
	 */
	private BspTreeNode[] children = new BspTreeNode[2];
	
	/**
	 * Element indices in positive and negative subspace.
	 */
	private List<Integer> elementsPos = new ArrayList<Integer>();
	private List<Integer> elementsNeg = new ArrayList<Integer>();
	
	public BspTreeNode(Vector base, Vector normal) {
		_base = Objects.requireNonNull(base);
		_normal = Objects.requireNonNull(normal);
	}
	
	/**
	 * Gibt den Stützvektor zurück, der nach der hesseschen Normalform mit der Normale eine Ebene beschreibt. 
	 * @return Der Stützvektor
	 */
	public Vector getPlaneBase() {
		return _base;
	}
	
	/**
	 * Gibt den Normalenvektor zurück, der nach der hesseschen Normalform mit dem Stützvektor eine Ebene beschreibt.
	 * @return Der Normalenvektor
	 */
	public Vector getPlaneNormal() {
		return _normal;
	}
	
	public BspTreeNode getChild(Orientation orientation) {
		return children[orientation.ordinal()];
	}
	
	public void setChild(Orientation orientation, BspTreeNode childNode) {
		children[orientation.ordinal()] = childNode;
	}
	
	/**
	 * Add an element to a specified subset (front or back).
	 */
	public void addElement(Orientation orientation, int index) {
		if(orientation == Orientation.NEGATIVE) {
			elementsNeg.add(index);
		} else {
			elementsPos.add(index);
		}
	}
	
	/**
	 * Get the number of elements in a specified subset (front or back).
	 */
	public int getNumberOfElements(Orientation orientation) {
		if(orientation == Orientation.NEGATIVE) {
			return elementsNeg.size();
		} else {
			return elementsPos.size();
		}
	}
	
	/**
	 * Get an element in a specified subset (front or back).
	 */
	public int getElement(Orientation orientation, int index) {
		if(orientation == Orientation.NEGATIVE) {
			return elementsNeg.get(index);
		} else {
			return elementsPos.get(index);
		}
	}
	
	/**
	 * Prüft, ob der übergebene Punkt auf der Vorder- oder Rückseite der durch diesen Knoten beschriebenen Ebene liegt.
	 * @param p Der zu prüfende Punkt
	 * @return {@code true} wenn der Punkt auf der Vorderseite liegt, ansonsten {@code false}
	 */
	public boolean isPositive(Vector p) {
		// XXX: Vorzeichen haben wir umgedreht, weil sonst falsche Werte kamen (passt so?)
		return _base.multiply(_normal) - p.multiply(_normal) < 0;
	}
}

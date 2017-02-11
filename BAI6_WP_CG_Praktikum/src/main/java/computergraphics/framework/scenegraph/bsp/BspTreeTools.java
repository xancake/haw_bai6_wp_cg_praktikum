package computergraphics.framework.scenegraph.bsp;

import java.util.ArrayList;
import java.util.List;
import computergraphics.framework.math.PrincipalComponentAnalysis;
import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.bsp.BspTreeNode.Orientation;

public class BspTreeTools {
	public BspTreeNode createBspTree(List<Vector> allPoints) {
		List<Integer> indices = new ArrayList<>();
		for(int i=0; i<allPoints.size(); i++) {
			indices.add(i);
		}
		return createBspTree(allPoints, indices);
	}
	/**
	 * Recursively create a BSP tree for a given set of points.
	 * @param allPoints List with all point positions in the dataset
	 * @param Set if indices used in the current recursive call
	 */
	public BspTreeNode createBspTree(List<Vector> allPoints, List<Integer> pointIndices) {
		if(pointIndices.size() <= 1) {
			return null;
		} else if(pointIndices.size() == 2) {
			Vector v1 = allPoints.get(pointIndices.get(0));
			Vector v2 = allPoints.get(pointIndices.get(1));
			Vector base = Vector.calculateSchwerpunkt(v1, v2);
			Vector normal = new Vector(v1.subtract(v2).getNormalized());
			BspTreeNode node = new BspTreeNode(base, normal);
			node.addElement(Orientation.POSITIVE, pointIndices.get(0));
			node.addElement(Orientation.NEGATIVE, pointIndices.get(1));
			return node;
		}
		
		PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis();
		for(Integer i : pointIndices) {
			pca.add(allPoints.get(i));
		}
		pca.applyPCA();
		
		BspTreeNode node = new BspTreeNode(pca.getCentroid(), pca.getNormal());
		List<Integer> positiveIndices = new ArrayList<>();
		List<Integer> negativeIndices = new ArrayList<>();
		for(Integer i : pointIndices) {
			Vector v = allPoints.get(i);
			if(node.isPositive(v)) {
				node.addElement(Orientation.POSITIVE, i);
				positiveIndices.add(i);
			} else {
				node.addElement(Orientation.NEGATIVE, i);
				negativeIndices.add(i);
			}
		}
		node.setChild(Orientation.POSITIVE, createBspTree(allPoints, positiveIndices));
		node.setChild(Orientation.NEGATIVE, createBspTree(allPoints, negativeIndices));
		
		return node;
	}
	
	/**
	 * Compute the back-to-front ordering for all points in 'points' based on the tree in 'node' and the given eye
	 * position
	 * @param node Root node of the BSP tree
	 * @param points List of points to be considered
	 * @param eye Observer position
	 * @return Sorted (back-to-front) list of points
	 */
	public List<Integer> getBackToFront(BspTreeNode node, Vector eye) {
		if(node == null) {
			return new ArrayList<>();
		}
		
		List<Integer> sortierung = new ArrayList<>();
		
		Orientation orientation = node.isPositive(eye) ? Orientation.NEGATIVE : Orientation.POSITIVE;
		
		sortierung.addAll(getBackToFront(node.getChild(orientation), eye));
		if(node.getChild(orientation) == null) {
			sortierung.add(node.getElement(orientation, 0));
		}
		
		orientation = Orientation.POSITIVE.equals(orientation) ? Orientation.NEGATIVE : Orientation.POSITIVE;
		
		if(node.getChild(orientation) == null) {
			sortierung.add(node.getElement(orientation, 0));
		}
		sortierung.addAll(getBackToFront(node.getChild(orientation), eye));
		
		return sortierung;
	}
	
	public List<Integer> getBackToFront(List<Vector> points, Vector eye) {
		BspTreeNode root = createBspTree(points);
		return getBackToFront(root, eye);
	}
}

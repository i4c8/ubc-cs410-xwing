package calltree;

import java.util.ArrayList;


public class CallTree{
	private ArrayList<CallTreeNodes> nodes;
	private ArrayList<CallTreeLinks> links;
	private ArrayList<CallTreeClasses> classes;
	
	public CallTree(){
		nodes = new ArrayList<CallTreeNodes>();
		links = new ArrayList<CallTreeLinks>();
		classes = new ArrayList<CallTreeClasses>();
	}
	
	/*
	 * @param String name = name of method to add
	 * If method is not already contained in nodes, than adds method to nodes
	 */
	public void addMethod(String name) {
		if (!nodes.contains(new CallTreeNodes(name))){
			nodes.add(new CallTreeNodes(name));
		}	
	}
	
	public ArrayList<CallTreeNodes> getMethods(){
		return nodes;
	}

	/*
	 * @param String name = name of class to add
	 * If class is not already contained in classes, then adds class to classes
	 */
	public void addClass(String name) {
		if (!classes.contains(new CallTreeClasses(name))){
			classes.add(new CallTreeClasses(name));
		}	
	}
	
	public ArrayList<CallTreeClasses> getClasses(){
		return classes;
	}

	/*
	 * @param String className = name of the class to add the method to
	 * @param String methodName = name of the method to add to the class
	 */
	public void addMethodToClass(String className, String methodName) {
		if (classes.contains(new CallTreeClasses(className))){
			if (nodes.contains(new CallTreeNodes(methodName))){
				int methodIndex = this.getMethods().indexOf(new CallTreeNodes(methodName));
				int classIndex = this.getClasses().indexOf(new CallTreeClasses(className));
				if (!this.getClasses().get(classIndex).getMethods().contains(methodIndex)){
					this.getClasses().get(classIndex).addMethod(methodIndex);
				}
			}
		}
	}

	/*
	 * @param String m1Name = name of the first method
	 * @param String m2Name = name of the second method
	 */
	public void addConnection(String m1Name, String m2Name) {
		int m1Index = -1;
		int m2Index = -1;
		if (nodes.contains(new CallTreeNodes(m1Name))){
			m1Index = this.getMethods().indexOf(new CallTreeNodes(m1Name));
		}
		if (nodes.contains(new CallTreeNodes(m2Name))){
			m2Index = this.getMethods().indexOf(new CallTreeNodes(m2Name));
		}
		if (m1Index >= 0 && m2Index >= 0){
			if (!this.getLinks().contains(new CallTreeLinks(m1Index, m2Index))){
				links.add(new CallTreeLinks(m1Index, m2Index));
			}
		}
	}
	
	public ArrayList<CallTreeLinks> getLinks(){
		return links;
	}
	
	
}
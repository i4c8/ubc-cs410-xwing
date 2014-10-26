package xwing;

import java.util.ArrayList;

public class CallTreeClasses {
	String name;
	ArrayList<Integer> contains;
	
	public CallTreeClasses(String name) {
		this.name = name;
		contains = new ArrayList<Integer>();
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof CallTreeClasses){
			CallTreeClasses toCompare = (CallTreeClasses) o;
			return (toCompare.getName().equals(name));
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public void addMethod(int methodIndex){
		contains.add(methodIndex);
	}
	
	public ArrayList<Integer> getMethods(){
		return contains;
	}

}

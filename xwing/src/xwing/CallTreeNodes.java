package xwing;

public class CallTreeNodes {
	String name;
	
	public CallTreeNodes(String name){
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof CallTreeNodes){
			CallTreeNodes toCompare = (CallTreeNodes) o;
			return (toCompare.getName().equals(name));
		}
		return false;
	}

	public String getName() {
		return name;
	}

}

package calltree;

public class CallTreeLinks {
	int source;
	int target;
	
	public CallTreeLinks(int m1Index, int m2Index) {
		source = m1Index;
		target = m2Index;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof CallTreeLinks){
			CallTreeLinks toCompare = (CallTreeLinks) o;
			return (toCompare.getSource() == source && toCompare.getTarget() == target);
		}
		return false;
	}

	private int getTarget() {
		return target;
	}

	private int getSource() {
		return source;
	}

}

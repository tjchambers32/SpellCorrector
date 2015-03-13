package spell;

public class Node implements ITrie.INode {

	int frequency;
	Node[] Nodes;
	
	
	public Node() {
		frequency = 0;
		Nodes = new Node[26];
	}
	
	@Override
	public int getValue() {
		return frequency;
	}
	
	public void setValue(int newValue) {
		frequency = newValue;
	}
}

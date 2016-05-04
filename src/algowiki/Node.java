package algowiki;
public class Node implements Comparable<Node> {
   
   final String name;
   boolean visited = false;   // used for Kosaraju's algorithm and Edmonds's algorithm
   int lowlink = -1;          // used for Tarjan's algorithm
   int index = -1;            // used for Tarjan's algorithm
   
   public Node(final String argName) {
       name = argName;
   }
   
   public String getName(){
	   return name;  
   }
   
   public int compareTo(final Node argNode) {
       return argNode.getName() == this.getName() ? 0 : -1;
   }
   
   @Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Node other = (Node) obj;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	return true;
}
   @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
}
   
   public String toString(){
	   return name;
   }
}
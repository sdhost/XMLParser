package algowiki;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collection;

public class AdjacencyList implements Comparable<AdjacencyList>{

   private Map<Node, List<Edge>> adjacencies = new HashMap<Node, List<Edge>>();

   public void addEdge(Node source, Node target, double weight) {
       List<Edge> list;
       if(!adjacencies.containsKey(source)) {
           list = new ArrayList<Edge>();
           adjacencies.put(source, list);
       } else {
           list = adjacencies.get(source);
       }
       list.add(new Edge(source, target, weight));
   }

   public List<Edge> getAdjacent(Node source) {
       return adjacencies.get(source);
   }

   public void reverseEdge(Edge e) {
       adjacencies.get(e.from).remove(e);
       addEdge(e.to, e.from, e.weight);
   }

   public void reverseGraph() {
       adjacencies = getReversedList().adjacencies;
   }

   public AdjacencyList getReversedList() {
       AdjacencyList newlist = new AdjacencyList();
       for(List<Edge> edges : adjacencies.values()) {
           for(Edge e : edges) {
               newlist.addEdge(e.to, e.from, e.weight);
           }
       }
       return newlist;
   }
   
   public void copy(AdjacencyList al){
	   this.adjacencies.clear();
	   this.adjacencies.putAll(al.adjacencies);
   }

   public Set<Node> getSourceNodeSet() {
       return adjacencies.keySet();
   }

   public Collection<Edge> getAllEdges() {
       List<Edge> edges = new ArrayList<Edge>();
       for(List<Edge> e : adjacencies.values()) {
           edges.addAll(e);
       }
       return edges;
   }
   
   public double getEva(){
	   double result = 0.0;
	   for(Edge e: this.getAllEdges()){
		   result += e.weight;
	   }
	   return result;
   }
   

	@Override
	public int compareTo(final AdjacencyList arg0) {
		
		return -1 * (int)(this.getEva() * 1000 - arg0.getEva() * 1000);
		//Descending Comparator
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adjacencies == null) ? 0 : adjacencies.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdjacencyList other = (AdjacencyList) obj;
		if (adjacencies == null) {
			if (other.adjacencies != null)
				return false;
		} else if (!this.equalTo(other))
			return false;
		return true;
	}

	public boolean equalTo(AdjacencyList g){
		Collection<Edge> own = this.getAllEdges();
		Collection<Edge> other = g.getAllEdges();
		
		if(own.size() != other.size())
			return false;
		for(Edge e:own){
			if(!other.contains(e))
				return false;
		}	
		return true;
	}
   
}

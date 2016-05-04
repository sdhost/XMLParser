import java.util.Collection;
import java.util.Set;

import algowiki.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class GraphTransformer extends AdjacencyList{
	
	GraphTransformer(DirectedWeightedMultigraph<String, DefaultWeightedEdge> g){
		Set<DefaultWeightedEdge> old_edges = g.edgeSet();
		for(DefaultWeightedEdge e: old_edges){
			//String src = (String) g.getEdgeSource(e);
			//String tar = (String) g.getEdgeTarget(e);
			Node src = new Node((String)g.getEdgeSource(e));
			Node tar = new Node((String) g.getEdgeTarget(e));
			double weight = g.getEdgeWeight(e);
			
			this.addEdge(src, tar, weight);
		}
		//System.out.println(this.toString());
	}
	GraphTransformer(AdjacencyList al){
		super();
		super.copy(al);
	}
	public boolean isInGraph(Node n){
		Set<Node> list = this.getSourceNodeSet();
		if(list.contains(n))
			return true;
		else
			return false;
	}
	public String toString(){
		String result = "{[";
		for(Node n:this.getSourceNodeSet()){
			result += n.toString() + ",";
		}
		result += "],[";
		for(Edge e:this.getAllEdges()){
			result += "(" + e.toString() + "),";
		}
		result += "]}";
		return result;
	}
	
	public DirectedWeightedMultigraph<String, DefaultWeightedEdge> GetGraph(){
		DirectedWeightedMultigraph<String, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(Edge e:this.getAllEdges()){
			String src = e.from.getName();
			String tar = e.to.getName();
			double w = e.weight;
			if(!g.containsVertex(src))
				g.addVertex(src);
			if(!g.containsVertex(tar))
				g.addVertex(tar);
			if(!g.containsEdge(src, tar)){
				DefaultWeightedEdge e1 = g.addEdge(src, tar);
				g.setEdgeWeight(e1, w);
			}
		}
		return g;
	}
	
}

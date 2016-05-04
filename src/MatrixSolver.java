import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.*;

import Jama.*;

public class MatrixSolver extends Matrix{
	Map<String,Integer> vertexIdx;
	Map<Integer,String> idxVertex;
	int dim;
	MatrixSolver(double[][] val, int dim){
		super(val);
		this.dim = dim;
	}
	MatrixSolver(int n, DirectedWeightedMultigraph<String, DefaultWeightedEdge> g){
		super(n,n);
		dim = n;
		this.vertexIdx = new HashMap<String,Integer>();
		this.idxVertex = new HashMap<Integer,String>();
		Set<String> vertexS = g.vertexSet();
		int p = 0;
		//Index start from 0
		for(String s: vertexS){
			vertexIdx.put(s, p);
			idxVertex.put(p, s);
			p++;
		}
		Set<DefaultWeightedEdge> edgeS = g.edgeSet();
		for(DefaultWeightedEdge e: edgeS){
			int src = vertexIdx.get(g.getEdgeSource(e));
			int tar = vertexIdx.get(g.getEdgeTarget(e));
			
			this.set(src, tar, -1);
			//this.set(tar, src, -1);
			this.set(src, src, g.inDegreeOf(g.getEdgeSource(e)));
			this.set(tar, tar, g.inDegreeOf(g.getEdgeTarget(e)));
		}
		
	};
	public int SpnningTreeCount(int n){
		int result;
		int[] row = new int[dim-1];
		int j = 0;
		for(int i=0;i<dim-1;i++){
			if(j==n)
				j++;
			row[i]=j;
			j++;
		}
		Matrix sub = this.getMatrix(row, row);
		result = (int) sub.det();
		return result;
	};
	
	public String getByIdx(Integer i){
		return idxVertex.get(i);
	}
}

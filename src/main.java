import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import algowiki.AdjacencyList;
import algowiki.Edge;
import algowiki.Edmonds;
import algowiki.Node;

import java.util.Date;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Date clock = new Date();
		
		List<String> lkeys = new ArrayList<String>();
		Set<String> keys = new HashSet<String>();
		//HashMap<String,Boolean> inEle = new HashMap<String,Boolean>();
		FileReader reader = null;
		try {
			reader = new FileReader("./src/names");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader buf = new BufferedReader(reader);
		String s;
		try {
			while((s=buf.readLine())!=null){
				//inEle.put(key, true);
				lkeys.add(s);
				keys.add(s);
			}
			buf.close();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Set<String> ref_names = new HashSet<String>();
		ref_names.add("item");
		ref_names.add("person");
		ref_names.add("category");
		ref_names.add("open_auction");
		
		
		
		String base = "./src/xml";
		String ext = "0M.xml";
		String output_pre = "/home/qguo/Downloads/EclipseCode/result/Dropbox/result/xml";
		
		
		
		
		
		
		for(int ptr = 1; ptr<6 ; ptr++){
			for(int exp_count = 0; exp_count < 20; exp_count++){
				
				System.out.println("Experiment for "+base+(ptr*2)+ext+" count " + exp_count + " Start");
				
				Set<String> test_key = new HashSet<String>();
				int select_count = 0;
				if(exp_count < 10)
					select_count = 10;
				else
					select_count = 5;
				Random rand = new Random(System.currentTimeMillis());
				test_key.add("site");
				for(int i = 1; i < select_count; i++){
					String k= lkeys.get(rand.nextInt(keys.size()));
					while(test_key.contains(k))
						k= lkeys.get(rand.nextInt(keys.size()));
					//System.out.print(k + " ");
					test_key.add(k);
				}
				
				//test_key.add("buyer");
				//test_key.add("item");
				//test_key.add("name");
				//test_key.add("person");
				//test_key.add("closed_auction");
				//test_key.add("open_auction");
				//test_key.add("category");
				//test_key.add("asia");
				//test_key.add("price");
				//test_key.add("title");
				//test_key.add("book");
				//test_key.add("publisher");
				//test_key.add("name");

				
				
				long start_time = System.currentTimeMillis();
				File input = new File(base+(ptr*2)+ext);
				long after_read = System.currentTimeMillis();
				
				
		
		//		double[][] sample = new double[][]{{1,-1,0,0},
		//											{-1,2,-1,-1},
		//											{0,-1,1,-1},
		//											{0,0,0,2}};
		//		MatrixSolver s0 = new MatrixSolver(sample,4);
		//		for(int i = 0; i < 4; i++){
		//			System.out.println(s0.SpnningTreeCount(i));
		//		}
				List<Double> tot_eva = new ArrayList<Double>();
				
				XMLParser_New test = new XMLParser_New(input, "id", ref_names, test_key, keys);
				System.out.println(test.graph.vertexSet().size());
//				DirectedWeightedMultigraph<String, DefaultWeightedEdge> g = test.GraphCombine();
//				long parser_time = System.currentTimeMillis();
//				ExportGraph(g,output_pre+(ptr*2)+"_c"+exp_count+".result.dot");
//				long before_sptree = System.currentTimeMillis();
//				ArrayList<AdjacencyList> result = findAllSpanningTree(g);
//				long after_sptree = System.currentTimeMillis();
//				int i = 0;
//				int sptree_count = result.size();
//				for(AdjacencyList al:result){
//					if(i >= 5)
//						break;
//					GraphTransformer alg = new GraphTransformer(al);
//					tot_eva.add(alg.getEva());
//					ExportGraph(alg.GetGraph(),output_pre+(ptr*2)+"_c" + exp_count +"sptree" + i + ".dot");
//					//System.out.println(alg.toString());
//					//System.out.println(al.getAllEdges().size());
//					//System.out.println(al.getEva());
//					i++;
//				}
				//ExportGraph(g,"test1.dot");
				
				long initial_time = 0;
				test.init(input,"id");
				while(test.xmlReader(input, "id", ref_names, test_key) == false){
					long before_init = System.currentTimeMillis();
					test.init(input, "id");
					long after_init = System.currentTimeMillis();
					initial_time += after_init - before_init;
				}
				
				
				long end_time = System.currentTimeMillis();
				
				PrintWriter out = null;
				
				try {
					out = new PrintWriter(new FileWriter("/home/qguo/Downloads/EclipseCode/result/Dropbox/result/RunningTime.txt", true));
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String toprint = (end_time - start_time) + "\t"
								+(end_time - after_read - initial_time);
				if(out != null){	
					out.println(base+(ptr*2)+ext +":\t" + test.sptrees.size() + "\t" +toprint);
					out.println(test_key.toString());
					for(Double eva:tot_eva){
						out.print(eva+"\t");
					}
					out.println();
					out.flush();
					out.close();
				}
			}
		}

	}
	
	public static String DisplayTime(long start_time,long after_read,long parser_time,long before_sptree,long after_sptree,long end_time){
		Date t0 = new Date(start_time);
		//Date t1 = new Date(after_read);
		//Date t2 = new Date(parser_time);
		//Date t3 = new Date(before_sptree);
		//Date t4 = new Date(after_sptree);
		Date t5 = new Date(end_time);
		String result = (end_time - start_time) + "\t" +
						  (parser_time - after_read) + "\t" +
						  (after_sptree - before_sptree);
		return result;
		//System.out.println("Start at:" + t0.toString());
		//System.out.println("End at:" + t5.toString());
		//System.out.println("Total running time(ms): " + (end_time - start_time));
		//System.out.println("File Reading Time(ms): " + (after_read - start_time));
		//System.out.println("XMLParser running time(ms):" + (parser_time - after_read));
		//System.out.println("Finding Spanning Tree time(ms):" + (after_sptree - before_sptree));
		
	};
	
	
	
	public static void ExportGraph(final DirectedWeightedMultigraph<String, DefaultWeightedEdge> g, String file_name){
		EdgeNameProvider<DefaultWeightedEdge> EDGE_LABEL_PROVIDER = new EdgeNameProvider<DefaultWeightedEdge>() {
		     @Override
		     public String getEdgeName(DefaultWeightedEdge e) {
		       
		       return String.format("%4.5f",g.getEdgeWeight(e));
		     }
		   };





		ComponentAttributeProvider<String> vertexAttributeProvider = 
		   new ComponentAttributeProvider<String>() { 
		      /* For every vertex v, return a single attribute with name "shape" 
		       * and value "box". (The vertex is drawn as a rectangle) 
		       */ 
		      public Map<String, String> getComponentAttributes(String v) 
		      { 
		         Map<String, String> map = 
		            new LinkedHashMap<String, String>(); 
		         map.put("shape", "box"); 
		         return map; 
		      } 
		   }; 

		ComponentAttributeProvider<DefaultEdge> edgeAttributeProvider = 
		   new ComponentAttributeProvider<DefaultEdge>() { 
		      /* For every vertex e, return two attributes, "dir" with value 
		       * "forward" and "arrowhead" with value "normal". (Edges are drawn 
		       * directed, with an arrowhead.) 
		       */ 
		      public Map<String, String> getComponentAttributes(DefaultEdge e) 
		      { 
		         Map<String, String> map = 
		            new LinkedHashMap<String, String>(); 
		         map.put("dir", "forward"); 
		         map.put("arrowhead", "normal"); 
		         return map; 
		      } 
		   }; 


		DOTExporter dOTExporter = new DOTExporter( 
		   new IntegerNameProvider<String>(), // vertex ids 
		   new StringNameProvider<String>(),  // vertex labels 
		   EDGE_LABEL_PROVIDER, //edge labels 
		   vertexAttributeProvider, // vertex attributes 
		   edgeAttributeProvider); // edge attributes 


		File file = new File(file_name); 
		Writer fileWriter = null;	
		try { 
		         fileWriter = new BufferedWriter(new FileWriter (file)); 
		} 
		catch (IOException e) { 
		         throw new RuntimeException(e); 
		} 

		dOTExporter.export(fileWriter, g); 
	}
	
	public static ArrayList<AdjacencyList> findAllSpanningTree(DirectedWeightedMultigraph<String, DefaultWeightedEdge> g){
		ArrayList<AdjacencyList> result = new ArrayList<AdjacencyList>();
		int dim = g.vertexSet().size();
		MatrixSolver s = new MatrixSolver(dim,g);
		for(int i = 0; i < dim; i++){
			int count = s.SpnningTreeCount(i);
			if(count > 0){
				ArrayList<AdjacencyList> r = findOptSpanningTree(g,i,count);
				for(AdjacencyList al:r)
					if(!result.contains(al))
						result.add(al);
			}
			
			//System.out.println(count);
		}
		
		//ArrayList<AdjacencyList> result = new ArrayList<AdjacencyList>();
		Collections.sort(result,new SpanningTreeComparator());
		
		
		return result;
	}
	public static ArrayList<AdjacencyList> findOptSpanningTree(DirectedWeightedMultigraph<String, DefaultWeightedEdge> g,int node_idx, int count){
		ArrayList<AdjacencyList> sptree = new ArrayList<AdjacencyList>();
		Edmonds cal = new Edmonds();
		GraphTransformer new_g = new GraphTransformer(g);
		int dim = g.vertexSet().size();
		MatrixSolver s = new MatrixSolver(dim,g);
		
		Node i_node = new Node(s.getByIdx(node_idx));
		AdjacencyList tree = cal.getMinBranching(i_node, new_g);
		if(!sptree.contains(tree)){
			sptree.add(tree);
		}
		count -= 1;
		while(count > 0){
			Collection<Edge> last = tree.getAllEdges();
			for(Edge e:last){
				String src = e.from.getName();
				String tar = e.to.getName();
				DefaultWeightedEdge e_g = g.getEdge(src, tar);
				double w = g.getEdgeWeight(e_g);
				g.removeEdge(e_g);
				s = new MatrixSolver(dim, g);
				int count_s = s.SpnningTreeCount(node_idx);
				count -= count_s;
				if(count_s > 0){
					ArrayList<AdjacencyList> r = findOptSpanningTree(g,node_idx,count_s);
					for(AdjacencyList al : r)
						if(!sptree.contains(al))
							sptree.add(al);
				}
				e_g = g.addEdge(src,tar);
				g.setEdgeWeight(e_g, w);
				if(count <= 0)
					break;
			}
		}
		
		return sptree;
	}

}

class SpanningTreeComparator implements Comparator<AdjacencyList>{

	@Override
	public int compare(AdjacencyList o1, AdjacencyList o2) {
		
		return o1.compareTo(o2);
	}
	
}

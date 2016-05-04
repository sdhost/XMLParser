
import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;   
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;  
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.HashMap;
import org.jdom2.Attribute;
import org.jdom2.Document;   
import org.jdom2.Element;   
import org.jdom2.JDOMException;   
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.filter.*;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.*;


public class XMLParser_New{
	
	public DirectedWeightedMultigraph<String, DefaultWeightedEdge> graph;
	public DirectedWeightedMultigraph<String, DefaultWeightedEdge> graphc;
	public Stack<Element> processing, matched, indexed;
	
	public Set<Pair<String, String>> workset, proset;
	public Set<Pair<String, String>> sptset, altset;
	public Set<String> endpoint;
	public int key_count;
	public List<Set<Pair<String,String>>> sptrees;
	
	private boolean initialized;
	
	//TODO
	//Add tag to decide whether an element was matched by name or by text
	Set<String> inElement;
	Document doc0,doc;
	HashMap<String,Element> id2Element;
	HashMap<String,Set<Pair<String,Double>>> id2MatchedChildren;
	//HashMap<String,Set<String>> node_tag;
	//HashMap<String,String> tag_name;
	//TODO
	//Add label to all the nodes indicating the tree structure
	
	public XMLParser_New(File inputXML, String id_name, Set<String> ref_names, Set<String> search_keys, Set<String> inEle){
		this.graph = new DirectedWeightedMultigraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.processing = new Stack<Element>();
		this.matched = new Stack<Element>();
		this.indexed = new Stack<Element>();
		this.id2Element = new HashMap<String,Element>();
		this.id2MatchedChildren = new HashMap<String,Set<Pair<String,Double>>>();
		
		this.inElement = inEle;
		//this.node_tag = new HashMap<String,Set<String>>();
		//this.tag_name = new HashMap<String,String>();
		
		this.workset = new HashSet<Pair<String, String>>();
		this.proset= new HashSet<Pair<String, String>>();
		this.sptset = new HashSet<Pair<String, String>>();
		this.altset = new HashSet<Pair<String, String>>();
		this.endpoint = new HashSet<String>();
		this.sptrees = new ArrayList<Set<Pair<String,String>>>();
		
		this.key_count = search_keys.size();
		this.initialized = false;
		
		//this.xmlReader(inputXML, id_name, ref_names, search_keys);
		//this.GraphCombine();
	}
	
	
	public boolean init(File inputXML, String id_name){
		SAXBuilder parser = new SAXBuilder();
		//DOMBuilder domParser = new DOMBuilder();
		
		this.processing.clear();
		this.matched.clear();
		this.indexed.clear();
		this.id2Element.clear();
		this.id2MatchedChildren.clear();
		
		this.workset.clear();
		this.sptset.clear();
		this.altset.clear();
		
		try{
			this.doc = parser.build(inputXML);
			//DOMOutputter domout = new DOMOutputter();
			//doc = domParser.build(domout.output(this.doc0));
			
			
			ElementFilter filter = new ElementFilter();
			for(Iterator<Element> itr = doc.getDescendants(filter).iterator(); itr.hasNext();){
				Element o = itr.next();
				
				//System.out.println(o.getName());
				if(o.getAttribute("closed_flag") != null)
					o.removeAttribute("closed_flag");
				if(o.getAttribute("matched_flag") != null)
					o.removeAttribute("matched_flag");
				o.setAttribute("closed_flag","f");
				o.setAttribute("matched_flag","f");
				
				//if(node_tag.)

				Attribute id = o.getAttribute(id_name);
				if(id != null){
					if(o.getAttribute("setted_flag") != null)
						o.removeAttribute("setted_flag");
					o.setAttribute("setted_flag", "f");
					this.id2Element.put(id.getValue(), o);
					this.id2MatchedChildren.put(id.getValue(), new HashSet<Pair<String,Double>>());
				}
			}
				
		}catch(JDOMException ex){
			ex.printStackTrace();
			return true;
		}catch(IOException ex){
			ex.printStackTrace();
			return true;
		}
		finally{
	
		}
		
		
		this.endpoint.clear();
		
		
		return false;
	}
	private Double findWeight(Element begin, Element end, Double ex, Set<String> ref_tags){
			
			Double result = -1.0;
			
			
			boolean findit = false;
			for(int i = 0; i < this.processing.size(); i++){
				Element e = this.processing.get(i);
				if(e.getName() == begin.getName()){
					result = 1.0;
					findit = true;
				}
				if(findit && e.getName() == end.getName())
					break;
				if(findit){
					int outdegree = e.getChildren().size();
					for(String s:ref_tags){
						if(e.getAttribute(s) != null){
							outdegree += 1;
							break;
						}
					}
					result *= (double) (1.0/outdegree);
					
				}
				//itr.next();
			}

			if(!findit){
				System.out.println("Error in find the path from " + begin.getName().toString() + " to " + end.getName().toString());
				return -1.0;
			}
				
			//System.out.println(result * ex);
			
			return result * ex;
		}

	private void search(Element node, Set<String> search_keys, String id_name, Set<String> ref_tags){
		
		String name,text,value;
		

		name = node.getName();
		
		text = node.getTextNormalize();
		value = node.getValue().toString();
		//text = "";
		//value = "";
		
		//=====================================
		//Only search for complete equal cases
		//=====================================
		boolean setted = false;
		//int pos = -1;
		if(search_keys.contains(name)){
			//pos = 0;
			setted = true;
		}
//		else if(text != ""){
//			for(Iterator<String> itr_key = search_keys.iterator();itr_key.hasNext();){
//				String key = itr_key.next();
//				if(!this.inElement.contains(key) && value.contains(key)){
//					//pos = 1;
//					setted = true;
//					break;
//				}
//			}
//		}
		
//		if(!setted){
//			//Search Attribute
//			for(Iterator<Attribute> itr = node.getAttributes().iterator(); itr.hasNext(); ){
//				Attribute now = itr.next();
//				name = now.getName();
//				//text = "";
//				text = now.getValue().toString();
//				
//				if(search_keys.contains(name)){
//					//pos = 2;
//					setted = true;
//				}else if(text != ""){
//					for(Iterator<String> itr_key = search_keys.iterator();itr_key.hasNext();){
//						String key = itr_key.next();
//						if(text.contains(key)){
//							//pos = 3;
//							setted = true;
//							break;
//						}
//					}
//				}
//
//				
//			}
//		}
		
		if(setted){
			node.removeAttribute("matched_flag");
			node.setAttribute("matched_flag","t");
			
			if(!this.indexed.isEmpty() && !this.indexed.peek().getName().equals(node.getName()) && this.indexed.peek().getAttribute("setted_flag").getValue() == "f")
			{
				Element idx = this.indexed.peek();
				//System.out.println("in search");
				Double weight = this.findWeight(this.indexed.peek(), node, 1.0, ref_tags);
				this.id2MatchedChildren.get(idx.getAttribute(id_name).getValue()).add(new Pair<String,Double>(node.getName(),weight));
				this.indexed.peek().removeAttribute("setted_flag");
				this.indexed.peek().setAttribute("setted_flag","t");
			}
			this.graph.addVertex(node.getName());
			this.matched.push(node);
			//System.out.println(this.matched.size());
		}
		
	}
	
	
	
	public boolean xmlReader(File inputXML, String id_name, Set<String> ref_names, Set<String> search_keys){
		
		//this.init(inputXML, id_name);
		//Need to call this process first
		if(initialized == false){
			System.out.println("Initialize First!");
			return false;
		}
		
		//int max = 0, level = 1, max_size = 0;
		
		//Set<String> enames = new HashSet<String>();
		
		processing.push(doc.getRootElement());
		
			
		while(!processing.isEmpty()){
			
			Element node = processing.peek();
			//if(!enames.contains(node.getName()))
			//	enames.add(node.getName());
			
			//if(node.getName() == "time" && search_keys.contains("time"))
			//	System.currentTimeMillis();
			
			
			//System.out.println(level);
			if(node.getAttribute("closed_flag").getValue() != "t")
			{
			this.search(node, search_keys, id_name, ref_names);
			}
			
			String refv = null;
			for(String s:ref_names){
				if((refv = node.getAttributeValue(s)) != null)
					break;
			}
			Pair<String,String> new_edge = new Pair<String,String>();
			if(refv != null && node.getAttribute("closed_flag").getValue() != "t"){
				node.removeAttribute("closed_flag");
				node.setAttribute("closed_flag","t");
				Element rnode = this.id2Element.get(refv);
				Set<Pair<String,Double>> matched_children = this.id2MatchedChildren.get(refv);
				if(!this.matched.isEmpty() && rnode.getAttribute("matched_flag").getValue() == "t"){
					if(this.matched.peek().getName() != rnode.getName()){
						this.processing.push(rnode);
						//System.out.println("Referenced a matched node");
						DefaultWeightedEdge e = this.graph.addEdge(this.matched.peek().getName(), rnode.getName());
						new_edge.set(this.matched.peek().getName(), rnode.getName());
						//this.graph.setEdgeWeight(e, this.findWeight(this.matched.peek(), rnode, 1.0, ref_names));
						
						this.processing.pop();
					//continue;
					}
					
				}else
				if(rnode.getAttribute("closed_flag").getValue() == "t" && !matched_children.isEmpty() && !matched.isEmpty()){
					this.processing.push(rnode);
					for(Pair<String,Double> ref : matched_children)
						if(this.matched.peek().getName() != ref.getKey()){
							//System.out.println("Matched to reference children");
							DefaultWeightedEdge e = this.graph.addEdge(this.matched.peek().getName(), ref.getKey());
							new_edge.set(this.matched.peek().getName(), ref.getKey());
							//this.graph.setEdgeWeight(e, this.findWeight(this.matched.peek(), rnode, ref.getVal(), ref_names));
					//continue;
						}
					this.processing.pop();
				}
				else if(rnode.getAttribute("closed_flag").getValue() == "f")
				{	
					if(rnode.getAttribute(id_name) != null){
						rnode.removeAttribute("setted_flag");
						rnode.setAttribute("setted_flag", "f");
						this.indexed.push(rnode);
					}
					rnode.removeAttribute("closed_flag");
					rnode.setAttribute("closed_flag","i");
					this.processing.push(rnode);
//					level += 1;
//					if(max < level)
//						max = level;
					continue;
				}
			}
			if(new_edge.a != null && proset.contains(new_edge) == false){
				if(newStrategy(new_edge))
					return false;
			}
				
			
			boolean all_close = true;
			
			//if(node.getTextNormalize() == ""){
			
				for(Element e:node.getChildren()){
					if(e.getAttribute("closed_flag").getValue() == "f"){
						e.removeAttribute("closed_flag");
						e.setAttribute("closed_flag","i");
						
						this.processing.push(e);
						if(e.getAttribute(id_name) != null){
							e.removeAttribute("setted_flag");
							e.setAttribute("setted_flag", "f");
							this.indexed.push(e);
						}
						
						all_close = false;
						
						
//						level += 1;
//						if(max < level)
//							max = level;
//						
//						if(max > 90){
//							//System.out.println(this.processing.toString());
//						}
							
						
//						if(max_size < this.processing.size())
//							max_size = this.processing.size();
						
						break;
					}	
				}
			//}
			
			node.removeAttribute("closed_flag");
			node.setAttribute("closed_flag", "t");
			
			if(!all_close)
				continue;
			
//			level -= 1;
			
			node = this.processing.pop();
			Element tmatched, tindexed;
			if(this.matched.isEmpty())
				tmatched = null;
			else
				tmatched = this.matched.peek();

			if(this.indexed.isEmpty()){
				tindexed = null;
			}else
				tindexed = this.indexed.peek();
			
			if(tmatched != null && node.getName().equals(tmatched.getName())){
				tmatched = this.matched.pop();
				if(!this.matched.isEmpty() && this.matched.peek().getName() != tmatched.getName()){
					//System.out.println("Matched to matched");
					DefaultWeightedEdge e = this.graph.addEdge(this.matched.peek().getName(), tmatched.getName());
					new_edge.set(this.matched.peek().getName(), tmatched.getName());
					if(new_edge.a != null && proset.contains(new_edge) == false){
						if(newStrategy(new_edge))
							return false;
					}
					//this.graph.setEdgeWeight(e, this.findWeight(this.matched.peek(), tmatched, 1.0, ref_names));
				}
			}
			if(tindexed != null && node.getName().equals(tindexed.getName())){
				tindexed = this.indexed.pop();
				if(!this.indexed.isEmpty() && this.indexed.peek().getAttribute("setted_flag").getValue() == "f"){
					for(Pair<String,Double> s:this.id2MatchedChildren.get(this.indexed.peek().getAttribute(id_name).getValue())){
						this.id2MatchedChildren.get(tindexed.getAttribute(id_name).getValue()).add(s);
					}
				}
			}
			
		}
	
		
//		try {
//			OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(inputXML.getName() + ".names"));
//			PrintWriter out = new PrintWriter(output);
//			for(String s:enames){
//				out.println(s);
//			}
//			out.close();
//			try {
//				output.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
//		System.out.println("Max Level: " + max + " " + max_size);
		
		//File output = new File("/Users/SDH/Documents/EclipseCode/XMLParser/src/graph.txt");
		//System.out.println(this.graph.toString());
		return true;
	}
	

	
	private boolean newStrategy(Pair<String, String> edge) {
		proset.add(edge);
		String from = edge.a;
		String to = edge.b;
		
		if(workset.contains(from) && workset.contains(to))
			altset.add(edge);
		else if(endpoint.contains(to))
			altset.add(edge);
		else{
			endpoint.add(to);
			workset.add(edge);
		}
		
		
		if(workset.size() == key_count){
			sptrees.add(workset);
			for(Set<Pair<String,String>> t:sptrees){
				for(Pair<String,String> e:altset){
					for(Pair<String,String> ce:t){
						if(e.b == ce.b){
							Set<Pair<String,String>> new_tree = new HashSet<Pair<String,String>>(t);
							new_tree.remove(ce);
							new_tree.add(e);
							sptrees.add(new_tree);
							
						}
							
					}
				}
			}
			return true;
		}
		return false;
		
		
	}


	public DirectedWeightedMultigraph<String, DefaultWeightedEdge> GraphCombine(){
		DirectedWeightedMultigraph<String, DefaultWeightedEdge> graphc = new DirectedWeightedMultigraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Set<DefaultWeightedEdge> edges = new HashSet<DefaultWeightedEdge>(graph.edgeSet());
		Map<Pair<String, String>, Double> compressed = new HashMap<Pair<String, String>, Double>();
		for(String vtx:graph.vertexSet()){
			graphc.addVertex(vtx);
		}
		for(DefaultWeightedEdge e: edges){
			String src = this.graph.getEdgeSource(e);
			String tar = this.graph.getEdgeTarget(e);
			
			Pair<String, String> cedge = new Pair<String,String>(src,tar);
			if(compressed.containsKey(cedge)){
				Double nw = compressed.get(cedge) + this.graph.getEdgeWeight(e);
				compressed.remove(cedge);
				compressed.put(cedge, nw);
			}else{
				compressed.put(cedge, this.graph.getEdgeWeight(e));
			}
		}
		for(Map.Entry<Pair<String,String>, Double> entry: compressed.entrySet()){
//			if(!graphc.containsVertex(entry.getKey().getKey())){
//				graphc.addVertex(entry.getKey().getKey());
//			}
//			if(!graphc.containsVertex(entry.getKey().getVal())){
//				graphc.addVertex(entry.getKey().getVal());
//			}
			if(!graphc.containsEdge(entry.getKey().getKey(), entry.getKey().getVal())){
				DefaultWeightedEdge e = graphc.addEdge(entry.getKey().getKey(), entry.getKey().getVal());
				graphc.setEdgeWeight(e, entry.getValue());
			}else{
				DefaultWeightedEdge e = graphc.getEdge(entry.getKey().getKey(), entry.getKey().getVal());
				Double val = graphc.getEdgeWeight(e);
				graphc.setEdgeWeight(e, val + entry.getValue());
			}
		}
		//System.out.println(graphc.toString());
		//for(DefaultWeightedEdge e:graphc.edgeSet()){
		//	System.out.println(graphc.getEdgeSource(e) + "->" + graphc.getEdgeTarget(e) + ":" + graphc.getEdgeWeight(e));
		//}
		return graphc;
	}
	
	public int getVertexSize(){
		//Must be called after GraphCombine
		if(graphc == null){
			System.out.println("Call GraphCombine First");
			return -1;
		}
		
		return graphc.vertexSet().size();
	}
}

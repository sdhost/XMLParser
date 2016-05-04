package algowiki;

public class Edge implements Comparable<Edge> {
   
   public final Node from, to; 
   public final double weight;
   
   public Edge(final Node argFrom, final Node argTo, final double argWeight){
       from = argFrom;
       to = argTo;
       weight = argWeight;
   }
   
   public int compareTo(final Edge argEdge){
       return (int)(weight - argEdge.weight);
   }
   
   @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((from == null) ? 0 : from.hashCode());
	result = prime * result + ((to == null) ? 0 : to.hashCode());
	long temp;
	temp = Double.doubleToLongBits(weight);
	result = prime * result + (int) (temp ^ (temp >>> 32));
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
	Edge other = (Edge) obj;
	if (from == null) {
		if (other.from != null)
			return false;
	} else if (!from.equals(other.from))
		return false;
	if (to == null) {
		if (other.to != null)
			return false;
	} else if (!to.equals(other.to))
		return false;
	if (Double.doubleToLongBits(weight) != Double
			.doubleToLongBits(other.weight))
		return false;
	return true;
}

public String toString(){
	   return from.toString() + "->" + to.toString() + " w="+weight;
   }
}


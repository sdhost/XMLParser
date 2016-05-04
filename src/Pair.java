
public class Pair<T1,T2> {

	T1 a;
	T2 b;
	Pair(){
		a = null;
		b = null;
	}
	
	
	Pair(T1 a, T2 b){
		this.a = a;
		this.b = b;
	}
	public T2 getVal(){
		return b;
	}
	public T1 getKey(){
		return a;
	}
	
	public void set(T1 a, T2 b){
		this.a = a;
		this.b = b;
	}

}

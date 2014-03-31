
import java.util.ArrayList;
import java.util.Vector;
import java.util.*;

public class WorkLoadDistance {
	private int numColumn;//number of query columns
	private float w;
	
	public WorkLoadDistance(int Columns,float W){//constructor
		numColumn = Columns;
		w=W;
	}//constructor
	
	public void setW(float W){
		w=W;
	}
	
	public int cValue(Vector<Boolean> a,Vector<Boolean> b){//a b has same size
		int count = 0;
		
		for (int i = 0; i < numColumn; ++i) {
		    if (a.get(i)!=b.get(i)) {
		        count++;
		    }
		}
		return count;
	}
	
	
	public HashMap<Vector<Boolean> ,Float> subtract(HashMap<Vector<Boolean> ,Float> X, HashMap<Vector<Boolean> ,Float> Y){
		//return X-Y
		float value;
		HashMap<Vector<Boolean> ,Float> result = new HashMap<Vector<Boolean> ,Float>(X);
		for (Map.Entry<Vector<Boolean>,Float> entry: Y.entrySet())
		{
			Vector<Boolean> Key = entry.getKey();
			if (result.containsKey(Key))
				value =  (float) result.get(Key);
			else
				value = 0;
			result.put(Key, value - entry.getValue());
			
		}//end of for
		
		return result;
	}
	
	
	public float getDistance1(HashMap<Vector<Boolean> ,Float> X, HashMap<Vector<Boolean> ,Float> Y){
		//REQUIRE: queries in X,Y are in ascending order
		float subsum;
		float sum=0f;
		Vector<Boolean> col;
		
		HashMap<Vector<Boolean> ,Float> XminusY = new HashMap<Vector<Boolean> ,Float>();
		XminusY = subtract(X, Y);
		
		for (Map.Entry<Vector<Boolean>,Float> entry: XminusY.entrySet())
		{
			col=entry.getKey();
			subsum=0f;
			for (Map.Entry<Vector<Boolean>,Float> entry1: XminusY.entrySet())
			{
				subsum+=entry1.getValue() * cValue(entry1.getKey(),col);
			}
			sum+=subsum * entry.getValue();
		}
		if (sum>0)
			return sum;
		else
			return -1*sum;
	}
	
	
	public HashMap<Vector<Boolean> ,Float> randomizeY1(HashMap<Vector<Boolean> ,Float> X, float d,int k)
	{
		//d1(result-X)=+-d
		//x+y=1-subsum
		int i;
		float p,dYp;
		float x=0f,y=0f;
		float xparameter=0f;
		float yparameter=0f;
		float xyparameter=0f;
		float subsum=0f;//sum of p
		float rhs1,rhs2;
		Vector<Boolean> vec = new Vector<Boolean>(numColumn);
		Vector<Boolean> xkey = new Vector<Boolean>(numColumn);
		Vector<Boolean> ykey = new Vector<Boolean>(numColumn);
		HashMap<Vector<Boolean> ,Float> Yp = new HashMap<Vector<Boolean> ,Float>(k-2);
		HashMap<Vector<Boolean> ,Float> result = new HashMap<Vector<Boolean> ,Float>(k);
		
		while(true){
			vec=genVector();
			if (!X.containsKey(vec))
				break;
		}
		result.put(vec, 0f);
		xkey=vec;
		while(true){
			vec=genVector();
			if (!X.containsKey(vec) && !result.containsKey(vec))
				break;
		}
		ykey=vec;
		result.put(vec, 0f);
		//finish setup 2 unknown queries
		for (i=1;i<=k-2;i++){
			vec=genVector();
			if (result.containsKey(vec)){//have already generated that key
				i--;
				continue;
			}
			p=getP();
			subsum+=p;
			result.put(vec, p);
			Yp.put(vec, p);
		}//finish setup k-2 queries
		rhs1=1-subsum;
		
		HashMap<Vector<Boolean> ,Float> XminusYp = subtract(Yp,X);
		for (Map.Entry<Vector<Boolean>,Float> entry1: XminusYp.entrySet())
		{
			xparameter+=entry1.getValue() * cValue(xkey,entry1.getKey());
		}
		for (Map.Entry<Vector<Boolean>,Float> entry2: XminusYp.entrySet())
		{
			xparameter+=entry2.getValue() * cValue(entry2.getKey(),xkey);
		}
		
		for (Map.Entry<Vector<Boolean>,Float> entry1: XminusYp.entrySet())
		{
			yparameter+=entry1.getValue() * cValue(ykey,entry1.getKey());
		}
		for (Map.Entry<Vector<Boolean>,Float> entry2: XminusYp.entrySet())
		{
			yparameter+=entry2.getValue() * cValue(entry2.getKey(),ykey);
		}
		xyparameter=cValue(ykey,xkey)*2;
		dYp = getDistance1(Yp,X);
		rhs2 = d - dYp;
		if(! solveSQE(rhs1, rhs2, xparameter, xyparameter, yparameter,x, y) ){
			rhs2 = -d-dYp;
			solveSQE(rhs1, rhs2, xparameter, xyparameter, yparameter,x, y);
		}
		result.put(xkey, x);
		result.put(ykey, y);
		return result;
	}
	
	private Vector<Boolean> genVector(){
		//random generate Vector<Boolean> with size numColumn
		//every call should generate different result
	
	}
	
	private float getP(){
		//generate float 0.01<=p<1
	
	}
	boolean solveSQE(float a, float b, float k1, float k2, float k3,float x, float y){
		//solve system of quadratic eqns
//		x+y=a
//		k1*x+k2*xy+k3*y=b
		//if no solution, return 0, else return 1
		float a1,b1,c1,x1=0f,x2=0f;
		a1=-k2;
		b1=k1-k3+a*k2;
		c1=a*(k3-k1)-b+k1*a;
		if (!solveQuadraticEqn(a1,b1,c1,x1,x2))
			return false;
		else{
			y=-x1+a;
			if (y>0 && x1>0){
				x=x1;
				return true;
			}
			else{
				x=x2;
				y=-x2+a;
				return true;
			}
		}	
	}
	
	boolean solveQuadraticEqn(float a, float b, float c, float sln1,float sln2){
		//解一元二次方程！
		//if delta < 0(no solution),return 0. else return 1
		//ax^2+bx+c=0
	}
	
	public float getDistance2(HashMap<Vector<Boolean> ,Float> X, HashMap<Vector<Boolean> ,Float> Y) {
		float d1=getDistance1(X,Y);
		int sum=0;
		HashMap<Vector<Boolean> ,Float> Xcopy = X;
		HashMap<Vector<Boolean> ,Float> Ycopy = Y;
		
		for (Map.Entry<Vector<Boolean>,Float> Xentry: Xcopy.entrySet()){
			if ( !Ycopy.containsKey(Xentry.getKey()) ){//X has, Y doesn't
				sum++;
			}
			else//X,Y both have
				Ycopy.remove(Xentry.getKey());
		}
		if (!Ycopy.isEmpty()){
			for (Map.Entry<Vector<Boolean>,Float> Yentry: Ycopy.entrySet()){
				sum++;
			}
		}
		
		return d1+w*sum;
	}
	
}

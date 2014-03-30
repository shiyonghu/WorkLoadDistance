
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
		//REQUIRE: queries in X,Y are in ascending order
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
		int i;
		HashMap<Vector<Boolean> ,Float> result = new HashMap<Vector<Boolean> ,Float>(k);
		for (i=1;i<=k-2;i++){
			
		}
	}
	
	private Vector<Boolean> genVector(){
	
	}
	/*
	public float getDistance2(HashMap<Vector<Boolean> ,Float> X, HashMap<Vector<Boolean> ,Float> Y) {
		float d1=getDistance1(X,Y);
		int sum;
		
		return d1+w*sum;
	}*/
	
}

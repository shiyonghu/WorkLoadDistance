import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class test {
	private static Random r=new Random();
	private static Vector<Boolean> genVector(int numColumn){
		//random generate Vector<Boolean> with size numColumn
		Vector<Boolean> result = new Vector<Boolean>(numColumn);
		Boolean element;
		for(int i = 0; i < numColumn; i++)
		{
			element = r.nextBoolean();				
			result.add(element);
		}
		return result;
	}
	
	private static float getP(int k) {
		// generate float 0.01<=p<1/k
		float result;
		float floor =(float) 1/k;
		floor-=0.01f;
		result = (float)(r.nextFloat()*(floor) + 0.01);
		return result;
	}
	private static HashMap<Vector<Boolean>,Float> randomInsertQuery(int num,int numColumn){
		//random generate Workload with num non-zero queries
		HashMap<Vector<Boolean>,Float> W = new HashMap<Vector<Boolean>,Float>(numColumn);
		float subsum=0;
		float p;
		Vector<Boolean> vec;
		int i;
		for (i=1;i<num;i++){
			vec=genVector(numColumn);
			if (W.containsKey(vec)){//have already generated that key
				i--;
				continue;
			}
			p=getP(num-1);
			subsum+=p;
			W.put(vec, p);
		}//finish setup num-1 queries
		vec=genVector(numColumn);
		if (!W.containsKey(vec)){//don't have that key
			p=1-subsum;
			W.put(vec, p);
		}
		return W;
	}
	public static void printWorkLoad(HashMap<Vector<Boolean> ,Float> W)
	{
		if (W==null){
			System.out.println("WorkLoad is empty");
			return;
		}
		for (Map.Entry<Vector<Boolean>,Float> entry: W.entrySet())
		{
			Vector<Boolean> Key = entry.getKey();
			System.out.print("<[");
				for (int i=0;i<Key.size();i++)
					System.out.print(Bool2int(Key.elementAt(i)));
			System.out.print("],");
			System.out.print(entry.getValue());
			System.out.print(">; ");
		}
		System.out.print('\n');
	}
	
	private static int Bool2int(Boolean b){
		int value;
		if (b) value=1;
		else value=0;
		return value;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		largeCase1();
		//smallCase();
		
	}
	
	private static void smallCase(){
		float distance;
		int n=3;
		WorkLoadDistance D = new WorkLoadDistance(n,0.1f);
		
		Vector<Boolean> v1 = new Vector<Boolean>(n);
		Vector<Boolean> v3 = new Vector<Boolean>(n);
		Vector<Boolean> v4 = new Vector<Boolean>(n);
		v1.addElement(false);
		v1.addElement(false);
		v1.addElement(true);
		v3.addElement(false);
		v3.addElement(true);
		v3.addElement(true);
		v4.addElement(true);
		v4.addElement(false);
		v4.addElement(false);
		
		HashMap<Vector<Boolean> ,Float> W0 = new HashMap<Vector<Boolean> ,Float>(2);
		HashMap<Vector<Boolean> ,Float> W1 = new HashMap<Vector<Boolean> ,Float>(2);
		HashMap<Vector<Boolean> ,Float> W2 = new HashMap<Vector<Boolean> ,Float>(3);
		W0.put(v1,0.1f);
		W0.put(v4,0.9f);
		W1.put(v1,0.15f);
		W1.put(v4,0.85f);
		W2.put(v1, 0.1f);
		W2.put(v3, 0.05f);
		W2.put(v4, 0.85f);
		
		System.out.print("W0:");
		printWorkLoad(W0);
		System.out.print("W1:");
		printWorkLoad(W1);
		System.out.print("W2:");
		printWorkLoad(W2);
		System.out.print("d1(W0,W1): ");
		distance=D.getDistance1(W2, W1);
		System.out.println(distance);
		System.out.println("Given w=0.1");
		System.out.print("d2(W0,W1): ");
		distance=D.getDistance2(W1, W2);
		System.out.println(distance);
		HashMap<Vector<Boolean> ,Float> Y=D.randomizeY11(W2, 4f, 4);
		
		System.out.print("f(W0,0.015,4)=");
		printWorkLoad(Y);
		if (Y!=null){
		System.out.print("d1(W0,Y): ");
		distance=D.getDistance1(W0,Y);
		System.out.println(distance);
		}
	}

	private static void largeCase1(){
		int numColumn=20;//set numColumn
		float distance;
		float d=35f;//set d
		float w=0.1f;//set w
		WorkLoadDistance D = new WorkLoadDistance(numColumn,w);
		System.out.println("w="+w);
		HashMap<Vector<Boolean>,Float> W0=randomInsertQuery(5,numColumn);
		System.out.println("Randomly generate W0: ");
		printWorkLoad(W0);
		HashMap<Vector<Boolean>,Float> W1=randomInsertQuery(5,numColumn);
		System.out.println("Randomly generate W1: ");
		printWorkLoad(W1);
		distance=D.getDistance1(W0,W1);
		System.out.println("d1(W0,W1)= "+distance);
		distance=D.getDistance1(W0,W1);
		
		System.out.println("d1(W1,W0)= "+distance);
		distance=D.getDistance1(W1,W0);
		System.out.println("d2(W0,W1)= "+distance);
		System.out.println("Given W0 and d1(Y,W0)= "+d);
		HashMap<Vector<Boolean>,Float> Y = D.randomizeY1(W0, d, 4);
		System.out.println("Y: ");
		printWorkLoad(Y);
		if (Y!=null){
		distance=D.getDistance1(Y,W0);
		System.out.println("d1(Y,W0)= "+distance);
		}
		/*
		System.out.println("Given W0 and d2(Y,W0)= "+d);
		Y = D.randomizeY2(W0, d, 4);
		System.out.println("Y: ");
		printWorkLoad(Y);
		if (Y!=null){
		distance=D.getDistance2(Y,W0);
		System.out.println("d(Y,W0)= "+distance);
		}
		*/
	}
	
}

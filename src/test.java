import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.lang.Math;

public class test {
	private static Random r=new Random();
	private static Vector<Boolean> genVector(int numColumn){
		//random generate Vector<Boolean> with size numColumn
		Boolean element;
		Vector<Boolean> result = new Vector<Boolean>(numColumn);
		Boolean done = false;
		int count = 0;
		while(!done)
		{
			count = 0;
			Vector<Boolean> temp = new Vector<Boolean>(numColumn);
			for(int i = 0; i < numColumn; i++)
			{
				element = r.nextBoolean();				
				temp.add(element);
				if(!element) count++;
			}
			if(count < numColumn) {
				done = true;
				result = temp;
			}
		}
		return result;
	}
	
	private static Double getP(int k) {
		// generate Double 0.01<=p<1/k
		Double result;
		Double floor = (double) (1/k);
		floor-=0.01f;
		result = (Double)(r.nextDouble()*(floor) + 0.01);
		return result;
	}
	private static HashMap<Vector<Boolean>,Double> randomInsertQuery(int num,int numColumn){
		//random generate Workload with num non-zero queries
		HashMap<Vector<Boolean>,Double> W = new HashMap<Vector<Boolean>,Double>(numColumn);
		Double subsum=0d;
		Double p;
		Vector<Boolean> vec=null;
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
		while (W.containsKey(vec)){
			vec=genVector(numColumn);
		}
		
		p=1-subsum;
		W.put(vec, p);
		return W;
	}
	public static void printWorkLoad(HashMap<Vector<Boolean> ,Double> W)
	{
		if (W==null){
			System.out.println("WorkLoad is empty");
			return;
		}
		for (Map.Entry<Vector<Boolean>,Double> entry: W.entrySet())
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
		//largeCase1();
		smallCase();
		//maxdistance(3,3000);
		//testf(50);

	}
	private static void testf(int numColumn){
		System.out.println("numColumn="+numColumn);
		Double d=0.1d;//set d
		Double w=0.05d;//set w
		WorkLoadDistance D = new WorkLoadDistance(numColumn,w);
		HashMap<Vector<Boolean>,Double> W0=randomInsertQuery(6,numColumn);
		System.out.println("Randomly generate W0: ");
		printWorkLoad(W0);
		System.out.println("Given W0 and d");
		System.out.println("d=0.05, Y: ");
		HashMap<Vector<Boolean>,Double> Y = D.randomizeY1(W0, 0.05d, 7, 1000);
		System.out.println("d=0.1, Y: ");
		Y = D.randomizeY1(W0, 0.1d, 8, 1000);
		System.out.println("d=0.2, Y: ");
		Y = D.randomizeY1(W0, 0.2d, 7, 1000);
		System.out.println("d=0.3, Y: ");
		Y = D.randomizeY1(W0, 0.3d, 7, 1000);
		System.out.println("d=0.4, Y: ");
		Y = D.randomizeY1(W0, 0.4d, 7, 1000);
		System.out.println("d=0.5, Y: ");
		Y = D.randomizeY1(W0, 0.5d, 7, 1000);
		System.out.println("d=0.6, Y: ");
		Y = D.randomizeY1(W0, 0.6d, 7, 1000);
		System.out.println("d=0.7, Y: ");
		Y = D.randomizeY1(W0, 0.7d, 7, 1000);
		System.out.println("d=0.8, Y: ");
		Y = D.randomizeY1(W0, 0.8d, 7, 1000);
		System.out.println("d=0.9, Y: ");
		Y = D.randomizeY1(W0, 0.9d, 7, 1000);
		System.out.println("d=0.95, Y: ");
		Y = D.randomizeY1(W0, 0.95d, 30, 1000);
		
	}
	private static void smallCase(){
		Double distance;
		int n=3;
		WorkLoadDistance D = new WorkLoadDistance(n,0.1d);
		
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
		
		HashMap<Vector<Boolean> ,Double> W0 = new HashMap<Vector<Boolean> ,Double>(2);
		HashMap<Vector<Boolean> ,Double> W1 = new HashMap<Vector<Boolean> ,Double>(2);
		HashMap<Vector<Boolean> ,Double> W2 = new HashMap<Vector<Boolean> ,Double>(3);
		W0.put(v1,0.1d);
		W0.put(v4,0.9d);
		W1.put(v1,0.15d);
		W1.put(v4,0.85d);
		W2.put(v1, 0.1d);
		W2.put(v3, 0.05d);
		W2.put(v4, 0.85d);
		
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
		HashMap<Vector<Boolean> ,Double> Y=D.randomizeY1(W2, 4d, 4,10);
		
		System.out.print("f(W0,0.015,4)=");
		printWorkLoad(Y);
		if (Y!=null){
		System.out.print("d1(W0,Y): ");
		distance=D.getDistance1(W0,Y);
		System.out.println(distance);
		}
	}

	private static void largeCase1(){
		int numColumn=13;//set numColumn
		Double distance;
		Double d=0.05d;//set d
		Double w=0.05d;//set w
		WorkLoadDistance D = new WorkLoadDistance(numColumn,w);
		System.out.println("w="+w);
		HashMap<Vector<Boolean>,Double> W0=randomInsertQuery(3,numColumn);
		System.out.println("Randomly generate W0: ");
		printWorkLoad(W0);
		HashMap<Vector<Boolean>,Double> W1=randomInsertQuery(3,numColumn);
		System.out.println("Randomly generate W1: ");
		printWorkLoad(W1);
		distance=D.getDistance1(W0,W1);
		System.out.println("d1(W0,W1)= "+distance);
		distance=D.getDistance2(W0,W1);
		System.out.println("d2(W0,W1)= "+distance);
		distance=D.getDistance2(W1,W0);
		System.out.println("d2(W1,W0)= "+distance);
		
		System.out.println("Given W0 and d1(Y,W0)= "+d);
		HashMap<Vector<Boolean>,Double> Y = D.randomizeY1(W0, d, 4, 500);
		System.out.println("Y: ");
		printWorkLoad(Y);
		if (Y!=null){
		distance=D.getDistance1(Y,W0);
		System.out.println("d1(Y,W0)= "+distance);
		}
		
		d=0.003d;
		System.out.println("Given W0 and d2(Y,W0)= "+d);
		Y = D.randomizeY2(W0, d, 5, 500);
		System.out.println("Y: ");
		printWorkLoad(Y);
		if (Y!=null){
		distance=D.getDistance2(Y,W0);
		System.out.println("d2(Y,W0)= "+distance);
		}
		
	}

	private static void maxdistance(int numColumn, int trials){
		Double distance;
		int parameter;
		Double max=0d;
		HashMap<Vector<Boolean>,Double> X = new HashMap<Vector<Boolean>,Double>();
		HashMap<Vector<Boolean>,Double> Y=new HashMap<Vector<Boolean>,Double>();
		Double w=1d;//set w
		WorkLoadDistance D = new WorkLoadDistance(numColumn,w);
		int k=(int) (Math.pow(2,numColumn)-3);
		if (numColumn>=6)
			k=60;
 		System.out.println("numColumn="+numColumn);
		for (int i=1;i<=trials;i++){
			parameter=r.nextInt(k-1)+2;
//			System.out.println("parameter="+parameter);
			HashMap<Vector<Boolean>,Double> W0=randomInsertQuery(parameter,numColumn);
//			printWorkLoad(W0);
			parameter=r.nextInt(k-1)+2;
//			System.out.println("parameter="+parameter);
			HashMap<Vector<Boolean>,Double> W1=randomInsertQuery(parameter,numColumn);
//			printWorkLoad(W1);
			distance=D.getDistance1(W0,W1);
			if (distance>max){
				max=distance;
				X=W0;
				Y=W1;
			}
		}//for
		System.out.println("after "+trials+" trials, max distance= "+max*2*numColumn);
		System.out.println("It's distance between X: ");
		printWorkLoad(X);
		System.out.println("and Y: ");
		printWorkLoad(Y);
		System.out.println("X - Y: ");
		printWorkLoad(D.subtract(X, Y));
	}
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class test {
	private static Random r=new Random();
	private static Vector<Boolean> genVector(int numColumn){
		//random generate Vector<Boolean> with size numColumn
		//every call should generate different result
		Vector<Boolean> result = new Vector<Boolean>(numColumn);
		Boolean element;
		for(int i = 0; i < numColumn; i++)
		{
			element = r.nextBoolean();				
			result.add(element);
		}
		return result;
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
		
		smallCase();
		
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
		
		System.out.print("d1(W0,W2): ");
		distance=D.getDistance1(W0, W2);
		System.out.println(distance);
//		System.out.println("Given w=0.1");
//		System.out.print("d2(W2,W0): ");
//		distance=D.getDistance2(W2, W0);
//		System.out.println(distance);
		System.out.print("W0:");
		printWorkLoad(W0);
		HashMap<Vector<Boolean> ,Float> Y=D.randomizeY1(W0, -0.015f, 4);
		
		printWorkLoad(Y);
		if (Y!=null){
		System.out.print("d1(W0,Y): ");
		distance=D.getDistance1(W0,Y);
		System.out.println(distance);
		}
	}

	
}

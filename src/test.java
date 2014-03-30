import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class test {
	public static void printQ(ArrayList<query> W)
	{
		for (query qy:W)
		{
			System.out.print('<');
			System.out.print(qy.q);
			System.out.print(',');
			System.out.print(qy.prob);
			System.out.print("> ");
		}
		System.out.print('\n');
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float distance;
		WorkLoadDistance D = new WorkLoadDistance(3,0.1f);
		
		Vector<Boolean> v1 = new Vector<Boolean>();
		v1.addElement(false);
		v1.addElement(false);
		v1.addElement(true);
		Vector<Boolean> v3 = new Vector<Boolean>();
		v3.addElement(false);
		v3.addElement(true);
		v3.addElement(true);
		Vector<Boolean> v4 = new Vector<Boolean>();
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
		
		/*
		System.out.print("Workload0: ");
		printQ(W0);
		System.out.print("Workload1: ");
		printQ(W1);
		System.out.print("Workload2: ");
		printQ(W2);
		
		System.out.print("d1(W2,W1): ");
		distance=D.getDistance1(W2, W1);
		System.out.println(distance);
		D.w=0.1f;
		System.out.println("Given w=0.1");
		System.out.print("d2(W2,W1): ");
		distance=D.getDistance2(W2, W1);
		System.out.println(distance);
		*/
	}

	
}

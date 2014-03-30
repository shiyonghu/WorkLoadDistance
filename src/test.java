import java.util.ArrayList;

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
		WorkLoadDistance D = new WorkLoadDistance(3);
		
		ArrayList<query> W0 = new ArrayList<query>();
		query q1 = new query();
		q1.q=1;
		q1.prob=0.1f;
		W0.add(q1);
		query q2 = new query();
		q2.q=4;
		q2.prob=0.9f;
		W0.add(q2);
		
		ArrayList<query> W1 = new ArrayList<query>();
		query q3 = new query();
		q3.q=1;
		q3.prob=0.15f;
		query q4 = new query();
		q4.q=4;
		q4.prob=0.85f;
		W1.add(q3);
		W1.add(q4);
		
		ArrayList<query> W2 = new ArrayList<query>();
		query q5 = new query();
		q5.q=1;
		q5.prob=0.1f;
		query q6 = new query();
		q6.q=3;
		q6.prob=0.05f;
		query q7 = new query();
		q7.q=4;
		q7.prob=0.85f;
		W2.add(q5);
		W2.add(q6);
		W2.add(q7);
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
	}

	
}

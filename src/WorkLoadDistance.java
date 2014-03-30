
import java.util.ArrayList;

public class WorkLoadDistance {
	private short [][] C;//similarity matrix
	private short numColumn;//number of query columns
	public float w;
	
	public WorkLoadDistance(int Columns){//constructor
		int i;
		int n = (int) Math.pow(2,Columns) - 1;
		numColumn=(short) Columns;
		C = new short [n][];
		for (i=0;i<n;i++)
			C[i] = new short[n-i];
		
		for (i=0; i<n-1; i++)
			for (int j=i+1; j<n; j++)
				assignC(i,j,cValue(i+1,j+1));
		
		for (i=0; i<n; i++)
			C[i][0]=0;
		
		/* debug C
		for (i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
			{
				System.out.print(getC(i,j));
				System.out.print(' ');
			}
			System.out.print('\n');
		}*/

	}//constructor
	
	private short cValue(int a,int b){
		int x = a ^ b;
		short count = 0;
		for (short i = 0; i < numColumn; ++i) {
		    if (((x >> i) & 1) == 1) {
		        count++;
		    }
		}
		return count;
	}
	private void assignC(int i,int j, short value){
		if (i<=j)
			C[i][j-i]=value;
		else
			C[j][i-j]=value;
	}
	
	private short getC(int i,int j){
		if (i<=j)
			return C[i][j-i];
		else
			return C[j][i-j];
	}
	
	public ArrayList<query> subtract(ArrayList<query> X, ArrayList<query> Y){
		//REQUIRE: queries in X,Y are in ascending order
		int xi=0;
		int yi=0;
		int xq,yq;
		int xsize=X.size();
		int ysize=Y.size();
		ArrayList<query> result = new ArrayList<query>();
		while (xi<xsize && yi<ysize)
		{
			xq=X.get(xi).q;
			yq=Y.get(yi).q;
			if (xq < yq)
			{
				query temp = new query();
				temp.q = xq;
				temp.prob=X.get(xi).prob;
				result.add(temp);
				xi++;
			}
			else if (xq==yq)
			{
				query temp = new query();
				temp.q=xq;
				temp.prob=X.get(xi).prob - Y.get(yi).prob;
				result.add(temp);
				xi++;
				yi++;
			}
			else //xq > yq
			{
				query temp = new query();
				temp.q = yq;
				temp.prob=Y.get(yi).prob;
				result.add(temp);
				yi++;
			}
		}//end of while
		if (yi<ysize)
			while (yi<ysize)
			{
				query temp = new query();
				temp.q = Y.get(yi).q;
				temp.prob=Y.get(yi).prob;
				result.add(temp);
				yi++;
			}
		if (xi<xsize)
			while (xi<xsize)
			{
				query temp = new query();
				temp.q = X.get(xi).q;
				temp.prob=X.get(xi).prob;
				result.add(temp);
				xi++;
			}
		return result;
	}
	
	
	public float getDistance1(ArrayList<query> X, ArrayList<query> Y){
		//REQUIRE: queries in X,Y are in ascending order
		float subsum;
		float sum=0f;
		int col;
		query temp=new query();
		ArrayList<query> XminusY = new ArrayList<query>();
		XminusY = subtract(X, Y);
		int Size=XminusY.size();
		for (int i=0;i<Size;i++)
		{
			col=XminusY.get(i).q - 1;
			subsum=0f;
			for (int j=0;j<Size;j++)
			{
				temp = XminusY.get(j);
				subsum+=temp.prob * getC(temp.q-1,col);
			}
			sum+=subsum * XminusY.get(i).prob;
		}
		if (sum>0)
			return sum;
		else
			return -1*sum;
	}
	
	/*
	public ArrayList<query> randomizeY1(ArrayList<query> X, float d)
	{
		
	}*/
	
	public float getDistance2(ArrayList<query> X, ArrayList<query> Y) {
		float d1=getDistance1(X,Y);
		int xi=0;
		int yi=0;
		int sum=0;
		int xq,yq;
		int xsize=X.size();
		int ysize=Y.size();
		
		while (xi<xsize && yi<ysize)
		{
			xq=X.get(xi).q;
			yq=Y.get(yi).q;
			if (xq < yq){
				sum++;
				xi++;
			}
			else if (xq==yq){
				xi++;
				yi++;
			}
			else //xq > yq
			{
				sum++;
				yi++;
			}
		}//end of while
		if (yi<ysize)
			while (yi<ysize)
			{
				sum++;
				yi++;
			}
		if (xi<xsize)
			while (xi<xsize)
			{
				sum++;
				xi++;
			}
		return d1+w*sum;
	}
	
}

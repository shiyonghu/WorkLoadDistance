
import java.util.*;


public class WorkLoadDistance {
	private int numColumn;//number of query columns
	private float w;
	private static Random r;
	
	public WorkLoadDistance(int Columns,float W){//constructor
		numColumn = Columns;
		w=W;
		r=new Random();
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
			result.put(Key, Math.abs(value - entry.getValue()));
			
		}//end of for
		return result;
	}
	
	public float getDistance1(HashMap<Vector<Boolean> ,Float> X, HashMap<Vector<Boolean> ,Float> Y){
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
		return sum/(numColumn*2);
	}
	
	public HashMap<Vector<Boolean> ,Float> randomizeY1(HashMap<Vector<Boolean> ,Float> X, float d,int k,int trials){
		HashMap<Vector<Boolean> ,Float> result=null;
		int count=0;
		int xsize=X.size();
		if (d<=0.1f && xsize>2){
			if (k!=xsize){
				k=xsize;
				System.out.println("distance<=0.1, automatically change number of nonzero queries to "+k);
			}
			while (count<trials){
				result=randomizeY12(X,d,k);
				count++;
				if (result!=null)
					break;
			}
		}
		else{
			while (count<trials){
				result=randomizeY11(X,d,k);
				count++;
				if (result!=null)
					break;
			}
		}
		
		if (result==null){
			System.out.println("No solution in "+count+" trials");
		}
		else
			System.out.println("Found solution in "+count+" trials");
		return result;
	}
	
	public HashMap<Vector<Boolean> ,Float> randomizeY11(HashMap<Vector<Boolean> ,Float> X, float distance,int k)
	{
		//d1(result-X)=d
		//x+y=1-subsum
		float d=distance*2*numColumn;
		float arr[]=new float[2];
		float dYp,rhs1,rhs2,p;
		Float x=0f,y=0f;
		float xparameter=0f,yparameter=0f,xyparameter=0f;
		float subsum=0f;//sum of p
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
		int kk=k-2;
		int ceil=(X.size()>=kk)?kk:X.size();
		if (distance>=0.2f)
			ceil=randomint(ceil);
		if (distance>=0.9f)
			ceil=0;
		int count=0;
		for (Map.Entry<Vector<Boolean>,Float> entry: X.entrySet()){
			if (count==ceil)
				break;
			p=getP(k-2);
			subsum+=p;
			result.put(entry.getKey(), p);
			Yp.put(entry.getKey(), p);
			kk--;
			count++;
		}
		subsum+=randomInsertQuery(kk,result,Yp);
		//finish setup k-2 queries
	
		rhs1=1-subsum;
//		dYp = getDistance1(Yp,X);	
		dYp = 2*numColumn*getDistance1(Yp,X);//changed
		rhs2 = d - dYp;
		
		HashMap<Vector<Boolean> ,Float> YpminusX = subtract(Yp,X);
		xparameter=getXParameter( YpminusX,xkey);
		yparameter=getYParameter(YpminusX,ykey);
		xyparameter=(float)cValue(ykey,xkey)*2.0f;
		
		if(! solveSQE(rhs1, rhs2, xparameter, xyparameter, yparameter,arr) ){
			//System.out.println("No solution this time. You may try again or change to a larger distance.");
			return null;
		}
		x=arr[0];
		y=arr[1];
		result.put(xkey, x);
		result.put(ykey, y);
		return result;
	}
	
	public HashMap<Vector<Boolean> ,Float> randomizeY12(HashMap<Vector<Boolean> ,Float> X, float d,int k)
	{	//X.size()>=2 and quite big (close to 2^numColumn)
		//higher change to get solution if d is small
		//+-d1(result-X)=d
		//x+y=1-subsum
		int i,r1,r2;
		float arr[]=new float[2];
		float dYp,xvalue,yvalue,rhs1,rhs2,p;
		float xparameter=0f,yparameter=0f,xyparameter=0f,subsum=0f;
		Float x=0f,y=0f;
		Vector<Boolean> xkey = new Vector<Boolean>(numColumn);
		Vector<Boolean> ykey = new Vector<Boolean>(numColumn);
		HashMap<Vector<Boolean> ,Float> Yp = new HashMap<Vector<Boolean> ,Float>(k-2);
		HashMap<Vector<Boolean> ,Float> result = new HashMap<Vector<Boolean> ,Float>(k);
		r1=randomint(X.size()-1);
		r2=randomint(X.size()-1);
		while (r2==r1){
			r2=randomint(X.size()-1);
		}
		i=0;
		for (Map.Entry<Vector<Boolean>,Float> entry: X.entrySet()){
			if (i==r1){
				xkey=entry.getKey();
				result.put(xkey, entry.getValue());
				Yp.put(xkey, entry.getValue());
			}
			else if (i==r2){
				ykey=entry.getKey();
				result.put(ykey, entry.getValue());
				Yp.put(ykey, entry.getValue());
			}	
			i++;
		}
		//finish setup 2 unknown queries
		subsum =X.get(xkey)+X.get(ykey);
		float top;
		int count=1;
		int terminate=X.size()-2;
		
		for (Map.Entry<Vector<Boolean>,Float> entry: X.entrySet()){
			if (entry.getKey()!=xkey && entry.getKey()!=ykey){
				if (count==terminate){
					top = entry.getValue() - 0.01f;
					p=(float)(r.nextFloat()*(top) + 0.01);
					result.put(entry.getKey(), p);
					Yp.put(entry.getKey(), p);
					subsum+=p;
					break;
				}
				top = entry.getValue() - 0.01f;
				p=(float)(r.nextFloat()*(top) + 0.01);
				result.put(entry.getKey(), p);
				Yp.put(entry.getKey(), p);
				subsum+=p;
				count++;
			}
		}
		//finish setup k-2 queries
		//printWorkLoad(Yp);
		rhs1=1-subsum;
		dYp = getDistance1(Yp,X)*2*numColumn;
		rhs2 = (float)(d*2*numColumn - dYp);

		HashMap<Vector<Boolean> ,Float> YpminusX = subtract(Yp,X);
		xvalue=YpminusX.remove(xkey);
		xparameter=getXParameter(YpminusX,xkey);
		YpminusX.put(xkey, xvalue);
		yvalue=YpminusX.remove(ykey);
		yparameter=getYParameter(YpminusX,ykey);
		YpminusX.put(ykey, yvalue);
		xyparameter=cValue(ykey,xkey)*2.0f;
		if(! solveSQE(rhs1, rhs2, xparameter, xyparameter, yparameter,arr) ){
			//System.out.println("No solution this time. You may try again or change to a larger distance.");
			return null;
		}
		x=arr[0];
		y=arr[1];
		result.put(xkey, x+X.get(xkey));
		result.put(ykey, y+X.get(ykey));
		return result;
	}

	
	private float getXParameter(HashMap<Vector<Boolean> ,Float> YpminusX,Vector<Boolean> xkey){
		float xparameter=0f;
		for (Map.Entry<Vector<Boolean>,Float> entry1: YpminusX.entrySet())
		{
			xparameter+=entry1.getValue() * cValue(xkey,entry1.getKey());
			xparameter+=entry1.getValue() * cValue(entry1.getKey(),xkey);
		}
		return xparameter;
	}
	
	private float getYParameter(HashMap<Vector<Boolean> ,Float> YpminusX,Vector<Boolean> ykey){
		float yparameter=0f;
		for (Map.Entry<Vector<Boolean>,Float> entry1: YpminusX.entrySet())
		{
			yparameter+=entry1.getValue() * cValue(ykey,entry1.getKey());
			yparameter+=entry1.getValue() * cValue(entry1.getKey(),ykey);
		}
		return yparameter;
	}
	
	private float randomInsertQuery(int num,HashMap<Vector<Boolean>,Float> result,HashMap<Vector<Boolean>,Float> Yp){
		//used to insert num=k-2 queries with random value and random position
		//return Sum(p)
		float subsum=0;
		float p;
		Vector<Boolean> vec;
		int i;
		if (num<=0)
			return 0;
		for (i=1;i<=num;i++){
			vec=genVector();
			if (result.containsKey(vec)){//have already generated that key
				i--;
				continue;
			}
			p=getP(num);
			subsum+=p;
			result.put(vec, p);
			Yp.put(vec, p);
		}//finish setup k-2 queries
		return subsum;
	}
	
	
	public HashMap<Vector<Boolean> ,Float> randomizeY2(HashMap<Vector<Boolean> ,Float> X, float d,int k,int trials)
	{
		//d2(result-X)=d
		//x+y=1-subsum
		HashMap<Vector<Boolean> ,Float> result=null;
		int count=0;
		while (count<trials){
			result=randomizeY21(X,d,k);
			count++;
			if (result!=null)
				break;
		}
		if (result==null)
			System.out.println("No solution in "+count+" trials");
		else
			System.out.println("Found solution in "+count+" trials");
		return result;
	}
	
	public HashMap<Vector<Boolean> ,Float> randomizeY21(HashMap<Vector<Boolean> ,Float> X, float d,int k)
	{
		float arr[]=new float[2];
		float dYp,rhs1,rhs2,p;
		Float x=0f,y=0f;
		float xparameter=0f,yparameter=0f,xyparameter=0f;
		float subsum=0f;//sum of p
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
		result.put(vec, 0.1f);
		xkey=vec;
		while(true){
			vec=genVector();
			if (!X.containsKey(vec) && !result.containsKey(vec))
				break;
		}
		ykey=vec;
		result.put(vec, 0.1f);
		//finish setup 2 unknown queries
		int kk=k-2;
		int ceil=(X.size()>=kk)?kk:X.size();
		ceil=randomint(ceil);
		int count=0;
		for (Map.Entry<Vector<Boolean>,Float> entry: X.entrySet()){
			if (count==ceil)
				break;
			p=getP(k-2);
			subsum+=p;
			result.put(entry.getKey(), p);
			Yp.put(entry.getKey(), p);
			kk--;
			count++;
		}
		subsum+=randomInsertQuery(kk,result,Yp);
		//finish setup k-2 queries
		int n = (int) (Math.pow(2, numColumn)-1);
		rhs1=1-subsum;
		dYp = getDistance1(Yp,X);
		HashMap<Vector<Boolean> ,Float> resultcp = new HashMap<Vector<Boolean> ,Float>(result);
		HashMap<Vector<Boolean> ,Float> Xcp = new HashMap<Vector<Boolean> ,Float>(X);
		
		rhs2=(float) ( d*(w*n+1) - w*getSigma(resultcp,Xcp) );
		rhs2-=dYp;
		rhs2*=2*numColumn;
		if (rhs2<0) return null;
		
		HashMap<Vector<Boolean> ,Float> YpminusX = subtract(Yp,X);
		xparameter=getXParameter( YpminusX,xkey);
		yparameter=getYParameter(YpminusX,ykey);
		xyparameter=cValue(ykey,xkey)*2;
		if(! solveSQE(rhs1, rhs2, xparameter, xyparameter, yparameter,arr) ){
			return null;
		}
		x=arr[0];
		y=arr[1];
		result.put(xkey, x);
		result.put(ykey, y);
		return result;
	}
	
	private Vector<Boolean> genVector(){
		//random generate Vector<Boolean> with size numColumn
		//every call should generate different result
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
	
	private int randomint(int k){
		//randomize an integer from 0 to k
		int result;
		result = r.nextInt(k+1);
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
	
	boolean solveSQE(float a, float b, float k1, float k2, float k3,float s[] ){
		//solve system of quadratic eqns
//		x+y=a
//		k1*x+k2*xy+k3*y=b
		//if no solution, return 0, else return 1
		//s[0]=x,s[1]=y
		float a1,b1,c1;
		float x1[]=new float[2];
		boolean flag;
		a1=-k2;
		b1=k1-k3+a*k2;
		c1=a*(k3-k1)-b+k1*a;
		flag=!solveQuadraticEqn(a1,b1,c1,x1);
		if (flag)
			return false;
		else{
			s[1]=-x1[0]+a;
			if (s[1]>0 && x1[0]>0){
				s[0]=x1[0];
				return true;
			}
			else{
				s[0]=x1[1];
				s[1]=-x1[1]+a;
				if (s[0]>0 && s[1]>0)
					return true;
				else
					return false;
			}
		}	
	}
	
	boolean solveQuadraticEqn(float a, float b, float c, float sln[]){
		//if delta < 0(no solution),return 0. else return 1
		//ax^2+bx+c=0
		double delta;
		delta = b*b - 4*a*c;
		if(delta < 0)
			return false;
		else
		{
			sln[0] = (float) (-b + Math.sqrt(delta))/(2*a);
			sln[1] = (float) (-b - Math.sqrt(delta))/(2*a);
			return true;
		}
	}
	
	public float getDistance2(HashMap<Vector<Boolean> ,Float> X, HashMap<Vector<Boolean> ,Float> Y) {
		float d1=getDistance1(X,Y);
		int sum;
		int n = (int) (Math.pow(2,numColumn)-1);
		HashMap<Vector<Boolean> ,Float> Xcopy = new HashMap<Vector<Boolean> ,Float>(X);
		HashMap<Vector<Boolean> ,Float> Ycopy = new HashMap<Vector<Boolean> ,Float>(Y);
		sum=getSigma(Xcopy,Ycopy);
		return (float) ( (d1+w*sum)/(w*n+1) );
	}
	
	public int getSigma(HashMap<Vector<Boolean> ,Float> Xcopy, HashMap<Vector<Boolean> ,Float> Ycopy){
		int sum=0;
		for (Map.Entry<Vector<Boolean>,Float> Xentry: Xcopy.entrySet()){
			if ( !Ycopy.containsKey(Xentry.getKey()) ){//X has, Y doesn't
				sum++;
			}
			else//X,Y both have
				Ycopy.remove(Xentry.getKey());
		}
		sum+=Ycopy.size();
		return sum;
	}
	
	//debug
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
}

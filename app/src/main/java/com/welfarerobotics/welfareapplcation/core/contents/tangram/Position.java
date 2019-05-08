package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.util.Log;

import java.util.ArrayList;

public class Position {
	public int x;
	public int y;
	public Position(int x, int y){
		set(x,y);
	}
	
	public Position(Position pos){
		set(pos);
	}
	
	public void set(Position pos){
		this.x = pos.x;
		this.y = pos.y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void add(Position pos){
		x+=pos.x;
		y+=pos.y;
	}
	
	public void scale(int scale){
		x*=scale;
		y*=scale;
	}
	
	//seems to rotate -90 at a time when assuming up/right is positive
	//but changed to this way because Android view uses down/right positive coordinates
	public void rotate(int orientation){
		switch(orientation){
			case 0: break;
			case 1: int tx2 = x; x = y; y = -tx2; break;
			case 2: x= -x; y = -y; break;
			case 3: int tx1 = x; x = -y; y = tx1; break;
		}
	}
	
	public boolean equals(Position pos){
		return x==pos.x && y==pos.y;
	}
	
	/** check if this position is inside the polygon described by given vertices
	 * edgesInside specifies if edges/vertices count as inside
	 * basically count times a ray to the right of position crosses a edge in the polygon
	 * even means outside, odd means inside
	 * algorithm from http://paulbourke.net/geometry/insidepoly/
	 */
	public boolean inside(ArrayList <Position> vertices, boolean edgesInside){
		int size = vertices.size();
		if(size <= 2)
			return false; //if 2 or less vertices not a polygon so return false
		Position p1, p2;
		int count = 0;
		p1 = vertices.get(0);
		for(int i = 1; i <size; i++){
			p2 = vertices.get(i);
			if((x==p1.x && y==p1.y) || (x==p2.x && y==p2.y) //vertices match
					|| (y==p1.y && y==p2.y && x>Math.min(p1.x,p2.x) && x<Math.max(p1.x,p2.x))) //edges match
				return edgesInside;
			//prune out trivial cases
			if(y>Math.min(p1.y,p2.y) && y<=Math.max(p1.y,p2.y) && x<=Math.max(p1.x,p2.x) && p1.y!=p2.y){
				// x is less than x-intersect
				if(p1.x==p2.x ||
					(p2.y >= p1.y && (x-p1.x)*(p2.y-p1.y) <= (y-p1.y)*(p2.x-p1.x)) ||
					(p2.y < p1.y && (x-p1.x)*(p2.y-p1.y) >= (y-p1.y)*(p2.x-p1.x)) ){
					if((x-p1.x)*(p2.y-p1.y) == (y-p1.y)*(p2.x-p1.x)) //point on edge
						return edgesInside;
					count++;
				}
			}
			p1 = p2;
		}
		//handle ending case
		p2 = vertices.get(0);
		if((x==p1.x && y==p1.y) || (x==p2.x && y==p2.y) //vertices match
				|| (y==p1.y && y==p2.y && x>Math.min(p1.x,p2.x) && x<Math.max(p1.x,p2.x))) //edges match
			return edgesInside;
		if(y>Math.min(p1.y,p2.y) && y<=Math.max(p1.y,p2.y) && x<=Math.max(p1.x,p2.x) && p1.y!=p2.y){
			if(p1.x==p2.x ||
				(p2.y >= p1.y && (x-p1.x)*(p2.y-p1.y) <= (y-p1.y)*(p2.x-p1.x)) ||
				(p2.y < p1.y && (x-p1.x)*(p2.y-p1.y) >= (y-p1.y)*(p2.x-p1.x)) ){
				if((x-p1.x)*(p2.y-p1.y) == (y-p1.y)*(p2.x-p1.x))
					return edgesInside;
				count++;
			}
		}
		if(count%2==0)
			return false;
		else
			return true;
	}
	
	/**2 times the area of any non-crossed polygon described by given vertices
	 * using 2 times to avoid floating point operation
	 * formula from: http://www.mathopenref.com/coordpolygonarea.html
	*/
	public static int area2x(ArrayList<Position> vertices){
		int size = vertices.size();
		if(size <= 2)
			return 0; //if 2 or less vertices not a polygon so return 0
		int sum = 0;
		for(int i = 0; i < size-1; i++){
			sum+=(vertices.get(i).x*vertices.get(i+1).y - vertices.get(i).y*vertices.get(i+1).x);
		}
		sum+=vertices.get(size-1).x*vertices.get(0).y - vertices.get(size-1).y*vertices.get(0).x;
		if(sum < 0)
			sum = -sum; //absolute value
		return sum;
	}
	
	/**static helper to figure determinant for edge intersections
	 * from here: http://gpwiki.org/index.php/Polygon_Collision
	 * 
	 * @param vec1a
	 * @param vec1b
	 * @param vec2a
	 * @param vec2b
	 * @return
	 */
	public static double determinant(Position vec1a, Position vec1b, Position vec2a, Position vec2b){
	    return (vec1a.x-vec1b.x)*(vec2a.y-vec2b.y)-(vec1a.y-vec1b.y)*(vec2a.x-vec2b.x);
	}
	
	/**static helper to figure if two edges intersect
	 * one edge is a-b, the other is c-d
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static boolean edgeIntersection(Position a, Position b, Position c, Position d){
	    double det=determinant(b,a,c,d);
	    if (det==0) //determinant = 0 means lines are parallel or equal
	    	return false;
	    double t=determinant(c,a,c,d)/det;
	    double u=determinant(b,a,c,a)/det;
	    return !((t<=0)||(u<=0)||(t>=1)||(u>=1)); //don't include endpoints
	    //a*(1-t)+t*b intersection point
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public static void logVertices(ArrayList<Position> vertices){
		String msg = "";
		for(int i = 0; i < vertices.size(); i++){
			msg+=vertices.get(i).getX()+","+vertices.get(i).getY()+"; ";
		}
		Log.d("Vertices:", msg);
	}
}

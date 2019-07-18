package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.util.Log;

import com.welfarerobotics.welfareapplcation.entity.Tangram;
import com.welfarerobotics.welfareapplcation.entity.cache.TangramStageCache;

import java.util.ArrayList;
import java.util.Iterator;


public class ImagePuzzle {
	int level;
	long startedTime; //changed to a long
	long endedTime; //changed to a long
	ArrayList<Position> solution, xsolution; //xsolution transformed solution
	ArrayList<ImagePiece> pieces;
	BoundingBox bb;


	/**
	 *
	 * @param level
	 * @param solution
	 * @param pieces
	 */

	public ImagePuzzle(int level, ArrayList<Position> solution, ArrayList<ImagePiece> pieces){

		this.level = level;
		this.level = 3;
		//TODO: set startedTime and endedTime to currentTime
		startedTime = System.nanoTime();
		this.solution = solution;
		initialize();
		this.pieces = pieces;
	}

	private ArrayList<ArrayList<Position>> getsolutionList(){
		ArrayList<ArrayList<Position>> solutionList = new ArrayList<ArrayList<Position>>();

		//house with two squares and two small trianges
		ArrayList<Position>solution1 = new ArrayList<Position>();
		solution1.add(new Position(-40,0));
		solution1.add(new Position(-40,-40));
		solution1.add(new Position(0,-80));
		solution1.add(new Position(40,-40));
		solution1.add(new Position(40,0));

		//christmas tree: 2 of each triangle and 1 square
		ArrayList<Position>solution1a = new ArrayList<Position>();
		solution1a.add(new Position(0,-90));
		solution1a.add(new Position(40,-50));
		solution1a.add(new Position(0,-50));
		solution1a.add(new Position(50,0));
		solution1a.add(new Position(0,0));
		solution1a.add(new Position(60,60));
		solution1a.add(new Position(20,60));
		solution1a.add(new Position(20,100));
		solution1a.add(new Position(20,100));
		solution1a.add(new Position(-20,100));
		solution1a.add(new Position(-20,60));
		solution1a.add(new Position(-60,60));
		solution1a.add(new Position(0,0));
		solution1a.add(new Position(-50,0));
		solution1a.add(new Position(0,-50));
		solution1a.add(new Position(-40,-50));

		solutionList.add(solution1);
		solutionList.add(solution1a);
//		solutionList.add(solution6);
//		solutionList.add(solution7);
//		solutionList.add(solution8);

		return solutionList;
	}

	private ArrayList<ArrayList<ImagePiece>> getpiecesList(){
		ArrayList<ArrayList<ImagePiece>> piecesList = new ArrayList<ArrayList<ImagePiece>>();
		// 문제 지점 가설 1
		//house with two squares and two small trianges
//		ArrayList<ImagePiece>pieces1 = new ArrayList<ImagePiece>();
//		pieces1.add(new ImagePiece(TangramPiece.TRIANGLE2,new Position(0,0)));
//		pieces1.add(new ImagePiece(TangramPiece.TRIANGLE3,new Position(0,0)));
//		pieces1.add(new ImagePiece(TangramPiece.TRIANGLE4,new Position(0,0)));
//		pieces1.add(new ImagePiece(TangramPiece.TRIANGLE5,new Position(0,0)));
//		piecesList.add(pieces1);


		return piecesList;
	}


	/**
	 *
	 * @param level
	 */
	public ImagePuzzle(int level){
		this(level,null,null);		
		//TODO: load solution and pieces from database according to level
		//use a dummy triangle solution for now
		
		ArrayList<ArrayList<Position>> solutionList = getsolutionList();
		ArrayList<ArrayList<ImagePiece> >piecesList = getpiecesList();
		int solutionIndex = (level-1)%solutionList.size();
		int pieceIndex = solutionIndex;
		this.level = solutionIndex+1;
		solution = solutionList.get(solutionIndex);
		initialize();
		
		//pieces required for puzzle
//		pieces = piecesList.get(0);
		//가설2
		pieces = new ArrayList<>();
		pieces.add(new ImagePiece(TangramPiece.TRIANGLE2,new Position(0,0)));
		pieces.add(new ImagePiece(TangramPiece.TRIANGLE3,new Position(0,0)));
		pieces.add(new ImagePiece(TangramPiece.TRIANGLE4,new Position(0,0)));
		pieces.add(new ImagePiece(TangramPiece.TRIANGLE5,new Position(0,0)));
	}
	
	public void initialize(){
		if(solution != null) {
			this.xsolution  = new ArrayList<Position>();
			//copy solution to xsolution
			for(int i = 0; i < solution.size(); i++)
				xsolution.add(new Position(solution.get(i)));
			bb = new BoundingBox(solution);
		}
	}



	public void movePiece(ImagePiece piece, Position pos){
		if (pieces.contains(piece)){
			pieces.remove(piece);
			piece.moveTo(pos);
			pieces.add(piece);
		}
	}

	public void moveXSolutionTo(int x, int y){
		for(int i = 0; i < xsolution.size(); i++){
			xsolution.get(i).set(solution.get(i).getX()+x,solution.get(i).getY()+y);
		}
	}
	
	public void scaleSolution(int scale){
		for(int i = 0; i < solution.size(); i++){
			solution.get(i).scale(scale);
		}
		for(int i = 0; i < xsolution.size(); i++){
			xsolution.get(i).scale(scale);
		}
		bb.scale(scale);
		for(int i = 0; i < pieces.size(); i++){
			pieces.get(i).scale(scale);
		}
	}
	
	public int calculateScore(ArrayList<ImagePiece> pieces){
		int score = 0;
		endedTime = System.nanoTime();
		if(pieces == null || pieces.size()<=0) //can't solve puzzle with no pieces
			return 0;
		Iterator <ImagePiece> pitr;
		
		BoundingBox sbb = new BoundingBox(xsolution); //bounding box of xsolution
		BoundingBox pbb = new BoundingBox(); //bounding box of pieces
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			ImagePiece piece = pitr.next();
			Position.logVertices(piece.getXVertices());
			pbb.expand(piece);
		}
		
		//align xsolution BB to pieces BB according to min
		Position displaceSBB = new Position(pbb.getMin().x - sbb.getMin().x, pbb.getMin().y - sbb.getMin().y);
		Position alignedSBBMax = new Position(sbb.getMax().x+displaceSBB.x,
				sbb.getMax().y+displaceSBB.y);
		
		//check if bounding boxes match
		boolean bbsMatch = false;
		if(pbb.getMax().equals(alignedSBBMax))
			bbsMatch = true;
		
		//ADD 1 to score for matching bounding boxes around pieces and solution
		if(bbsMatch)
			score+=1;
		
		//if no outline, move xsolution vertices according to alignment
		if(!GlobalVariables.outlineOn) {
			Iterator<Position> slnItr = xsolution.iterator();
			while(slnItr.hasNext()){
				slnItr.next().add(displaceSBB);
			}
		}
		Position.logVertices(xsolution);
		
		//variables to support area calculation
		int slnArea = Position.area2x(xsolution);
		if(slnArea ==0) //can't solve puzzle with solution area of 0
			return 0;
		int pAreaIn = 0;
		int pAreaOut = 0;
		int pAreaOverlap = 0;
		
		//calculate if pieces inside xsolution
		boolean inside = true;
		int numPiecesInside = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			ImagePiece p = pitr.next();
			if(p.inside(xsolution)){
				pAreaIn += p.area2x();
				numPiecesInside++;
			} else {
				pAreaOut += p.area2x();
				inside = false;
			}
		}
		
		//calculate if pieces overlap
		//not needed here if done on the fly when placing pieces
		boolean overlap = false;
		pitr = pieces.iterator();
		while(pitr.hasNext() && !overlap){
			ImagePiece p1 = pitr.next();
			Iterator<ImagePiece> pitr2 = pieces.iterator();
			while(pitr2.hasNext()){
				ImagePiece p2 = pitr2.next();
				if(p1!=p2 && p1.overlap(p2)){ //don't check overlap if same object
					overlap = true;
					int p1area2x = p1.area2x();
					int p2area2x = p2.area2x();
					if(p1area2x < p2area2x)
						pAreaOverlap += p1area2x;
					else
						pAreaOverlap += p2area2x;
					break;
				}
			}
		}
		
		int slnInDiff = slnArea - pAreaIn; //calculate difference in area for solution and inside pieces
		if(slnInDiff < 0)
			slnInDiff = -slnInDiff; //absolute value to account for inside area larger than solution (means overlap)
		slnInDiff = slnInDiff + pAreaOut + pAreaOverlap; //add area of outside and first overlapping piece into the "difference"
		slnInDiff = slnArea - slnInDiff; //calculate amount of correct inside area
		if(slnInDiff < 0)
			slnInDiff = 0; //set to 0 if negative
		
		//ADD 0-9 to score depending on percentage area of pieces inside xsolution
		//you also lose points for % area of pieces outside xsolution
		//you also lose points for % area of first overlapping piece
		//Log.d("Puzzle.calculateScore (% area inside)",""+slnInDiff+"/"+slnArea);
		score += (9*slnInDiff)/slnArea;
		
		return score;
	}
	
	public void reset(){
		//TODO: set startedTime to currentTime
		//TODO: set solution and pieces back to initial state (pull from database);
	}
	
	//new classes: getters and setters
	public ArrayList<Position> getSolution(){
		return solution;
	}
	public ArrayList<Position> getXSolution(){
		return xsolution;
	}
	public synchronized ArrayList<ImagePiece> getPieces(int position){
	    pieces.clear();
		ArrayList<TangramPiece> tngram = TangramSeparater.Separate(TangramStageCache.getInstance().getTangrams().get(position));
		 for(TangramPiece p :tngram){
                pieces.add(new ImagePiece(p,new Position(0,0)));
                Log.d("탱그램","조각 분배");
            }

		return pieces;
	}
	//get time elapsed in seconds since Puzzle created
	public double getElapsedTime(){
		return (double)(System.nanoTime()-startedTime)/1000000000.0;
	}
	public int getLevel(){
		return level;
	}
	public int getCenterX(){
		return bb.getMin().getX()+(bb.getMax().getX()-bb.getMin().getX())/2;
	}
	public int getCenterY(){
		return bb.getMin().getY()+(bb.getMax().getY()-bb.getMin().getY())/2;
	}
}

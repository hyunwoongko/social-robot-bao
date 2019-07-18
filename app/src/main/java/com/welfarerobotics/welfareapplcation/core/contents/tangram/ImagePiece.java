package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;


public class ImagePiece {
    private TangramPiece type;
    private int orientation;
    private Position center;
    private ArrayList<Position> vertices, xvertices;
    private Bitmap image;
    private BoundingBox bb, xbb;
    private boolean _active;
    boolean dirty; //dirty flag to show if xvertices/xbb (transformed vertices/bb) changed

    /**
     *
     * @param type
     * @param pos
     * @param orientation
     * @param vertices
     */
    public ImagePiece(TangramPiece type, Position pos, int orientation, ArrayList <Position> vertices) {
        this.type = type;
        this.center = new Position(pos);
        this.orientation = orientation;
        this.vertices = vertices;
        initialize();
    }
    /**
     *
     * @param type
     * @param pos
     */



    public ImagePiece(TangramPiece type, Position pos) {
        /*여기 조각에 맞게 수정*/
        this.type = type;
        this.center = new Position(pos);

        //use a dummy 2x4 triangle defined with center at 0,0 for now
        orientation = 0;
        //NOTE: When you create pieces, please create ones centered at 0,0
        //if you want proper rotation and toolbox positioning behavior!
        //another thing to be careful about is to use only units of 10 if you want
        //snap to grid to work properly
        int width;
        int height;
        Log.d("탱그램 조각",type.name());
        if(type==TangramPiece.RHOMBUS){
            vertices = new ArrayList<Position>();
            //이미지 넣기
            image= Images.getImages().getRhombus();

        }else if(type==TangramPiece.TRIANGLE1){//위
            vertices = new ArrayList<Position>();
            image= Images.getImages().getTriAngle(0);
            //이미지 넣기
        }else if(type==TangramPiece.TRIANGLE2){//아래
            vertices = new ArrayList<Position>();
            image= Images.getImages().getTriAngle(1);
            //이미지 넣기
        }else if(type==TangramPiece.TRIANGLE3){//위
            vertices = new ArrayList<Position>();
            image= Images.getImages().getTriAngle(2);
            //이미지 넣기
        }else if(type==TangramPiece.TRIANGLE4){//아래
            vertices = new ArrayList<Position>();
            image= Images.getImages().getTriAngle(3);
            //이미지 넣기
        }else if(type==TangramPiece.TRIANGLE5){//아래
            vertices = new ArrayList<Position>();
            image= Images.getImages().getTriAngle(4);

            //이미지 넣기
        }else if(type==TangramPiece.PARALLELOGRAM){
            vertices = new ArrayList<Position>();
            image= Images.getImages().getParallelo();


            //이미지 넣기
        } else if(type==TangramPiece.TRIANGLE1_90){
            vertices = new ArrayList<Position>();
            image= rotater(90,Images.getImages().getTriAngle(0));


        }else if(type==TangramPiece.TRIANGLE1_180){
            vertices = new ArrayList<Position>();
            image= rotater(180,Images.getImages().getTriAngle(0));


        }else if(type==TangramPiece.TRIANGLE1_270){
            vertices = new ArrayList<Position>();
            image= rotater(270,Images.getImages().getTriAngle(0));


        }else if(type==TangramPiece.TRIANGLE2_90){
            vertices = new ArrayList<Position>();
            image= rotater(90,Images.getImages().getTriAngle(1));


        }else if(type==TangramPiece.TRIANGLE2_180){
            vertices = new ArrayList<Position>();
            image= rotater(180,Images.getImages().getTriAngle(1));


        }else if(type==TangramPiece.TRIANGLE2_270){
            vertices = new ArrayList<Position>();
            image= rotater(270,Images.getImages().getTriAngle(1));


        }else if(type==TangramPiece.TRIANGLE3_90){
            vertices = new ArrayList<Position>();
            image= rotater(90,Images.getImages().getTriAngle(2));


        }else if(type==TangramPiece.TRIANGLE3_180){
            vertices = new ArrayList<Position>();
            image= rotater(180,Images.getImages().getTriAngle(2));


        }else if(type==TangramPiece.TRIANGLE3_270){
            vertices = new ArrayList<Position>();
            image= rotater(270,Images.getImages().getTriAngle(2));


        }else if(type==TangramPiece.TRIANGLE4_90){
            vertices = new ArrayList<Position>();
            image= rotater(90,Images.getImages().getTriAngle(3));


        }else if(type==TangramPiece.TRIANGLE4_180){
            vertices = new ArrayList<Position>();
            image= rotater(180,Images.getImages().getTriAngle(3));


        }else if(type==TangramPiece.TRIANGLE4_270){
            vertices = new ArrayList<Position>();
            image= rotater(270,Images.getImages().getTriAngle(3));


        }else if(type==TangramPiece.TRIANGLE5_90){
            vertices = new ArrayList<Position>();
            image= rotater(90,Images.getImages().getTriAngle(4));


        }else if(type==TangramPiece.TRIANGLE5_180){
            vertices = new ArrayList<Position>();
            image= rotater(180,Images.getImages().getTriAngle(4));


        }else if(type==TangramPiece.TRIANGLE5_270){
            vertices = new ArrayList<Position>();
            image= rotater(270,Images.getImages().getTriAngle(4));


        }else if(type==TangramPiece.PARALLELOGRAM_90){
            vertices = new ArrayList<Position>();
            image= rotater(90,Images.getImages().getParallelo());


            //이미지 넣기
        }

        height =image.getHeight()/2;
        width =image.getWidth()/2;
        vertices.add(new Position(0,0));
        vertices.add(new Position(width,0));
        vertices.add(new Position(width,height));
        vertices.add(new Position(0,height));
        initialize();
    }

    public int getWidth(){
        return bb.getMax().getX()-bb.getMin().getX();
    }

    public int getHeight(){
        return bb.getMax().getY()-bb.getMin().getY();
    }

    private void initialize(){
        _active = false;
        xvertices = new ArrayList<Position>();
        //copy vertices to xvertices
        for(int i = 0; i < vertices.size(); i++)
            xvertices.add(new Position(vertices.get(i)));

        bb = new BoundingBox(vertices); //create bounding box
        xbb = new BoundingBox(bb); //create transformed bounding box
        if(orientation!=0 || !(center.x==0 && center.y==0))
            update();
    }

    /**
     *
     * @param newpos
     */
    public void moveTo(Position newpos){
        dirty = true;
        center = newpos;
    }

    public void moveTo(int x, int y){
        dirty = true;
        center.set(x,y);
    }

    public void rotate(){
        dirty = true;
        if(orientation < 3)
            orientation++;
        else
            orientation = 0;
    }

    //scale to help adjust for screen size
    public void scale(int scale){
        for(int i = 0; i < vertices.size(); i++){
            vertices.get(i).scale(scale);
        }
        for(int i = 0; i < xvertices.size(); i++){
            xvertices.get(i).scale(scale);
        }
        center.scale(scale);
        bb.scale(scale);
        xbb.scale(scale);
    }

    /**new class
     * Checks if vertices inside the polygon described by vertices
     *
     * @param vertices2
     * @return
     */
    public boolean inside(ArrayList<Position> vertices2){
        Iterator<Position> xvitr = getXVertices().iterator();
        while(xvitr.hasNext()){
            if(!xvitr.next().inside(vertices2, true))
                return false;
        }
        return true;
    }

    /**new class
     * Checks for overlapping pieces
     * now checks first for trivial case of piece completely contained in another
     * then checks for edge intersection,
     * so basically perfect check of overlap
     * idea from here: http://gpwiki.org/index.php/Polygon_Collision
     */
    public boolean overlap(ImagePiece piece2){
        //first check if BoundingBox overlaps
        //if not, return false

        BoundingBox xbb = getXBB();
        BoundingBox xbb2 = piece2.getXBB();
        if(!xbb.overlap(xbb2))
            return false;

        ArrayList<Position> vertices1 = getXVertices();
        ArrayList<Position> vertices2 = piece2.getXVertices();

        //check if xvertices all inside piece2
        Iterator<Position> xvitr = vertices1.iterator();
        boolean inside = true;
        while(xvitr.hasNext()){
            Position vertex1 = xvitr.next();
            if(!vertex1.inside(vertices2, true)) {
                //Log.d("overlap/inside",""+vertex1.getX()+","+vertex1.getY()+" inside:");
                //Position.logVertices(vertices2);
                inside = false;
            }
        }
        if(inside) //if inside means piece2 contains this piece so overlap is true
            return true;

        //check edge intersections
        int size1 = vertices1.size();
        if(size1 <= 2)
            return false; //if 2 or less vertices not a polygon so return false
        Position p1, p2, p3, p4;
        p1 = vertices1.get(0);
        for(int i = 1; i <size1; i++){
            p2 = vertices1.get(i);

            p3 = vertices2.get(0);
            for(int j = 1; j <vertices2.size(); j++){
                p4 = vertices2.get(j);
                if(Position.edgeIntersection(p2, p1, p4, p3))
                    return true;
                p3 = p4;
            }
            p4 = vertices2.get(0); //handle ending case
            if(Position.edgeIntersection(p2, p1, p4, p3))
                return true;

            p1 = p2;
        }
        p2 = vertices1.get(0); //handle ending case
        p3 = vertices2.get(0);
        for(int j = 1; j <vertices2.size(); j++){
            p4 = vertices2.get(j);
            if(Position.edgeIntersection(p2, p1, p4, p3))
                return true;
            p3 = p4;
        }
        p4 = vertices2.get(0); //handle ending case
        if(Position.edgeIntersection(p2, p1, p4, p3))
            return true;

        return false;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap bitmap){

        this.image = bitmap;
    }


    public void rotateBitmap(float degrees) {
        Bitmap original = this.image;
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
//			Canvas canvas = new Canvas(rotatedBitmap);
//			canvas.drawBitmap(original, 5.0f, 0.0f, null);

        this.image = rotatedBitmap;
        updateArry();
    }

    public Bitmap rotater(float degrees,Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//			Canvas canvas = new Canvas(rotatedBitmap);
//			canvas.drawBitmap(original, 5.0f, 0.0f, null);

        return  rotatedBitmap;
    }

    private void updateArry(){
        int width = image.getWidth();
        int height = image.getHeight();
        vertices.set(1,new Position(width,0));
        vertices.set(2,new Position(width,height));
        vertices.set(3,new Position(0,height));



    }
    /**new class
     * returns 2 times the area of this piece
     */


    public int area2x(){
        return Position.area2x(vertices);
    }

    /**updates (transforms) xvertices to correct orientation and positions
     */
    public void update(){
        if(dirty){
            //update xvertices
            for(int i = 0; i < xvertices.size(); i++){
                Position xVertex = xvertices.get(i);
                xVertex.set(vertices.get(i));
                xVertex.rotate(orientation);
                xVertex.add(center);
            }
            //update xbb
            xbb.update(xvertices);
            //update flag
            dirty = false;
        }
    }



    //new classes: getters and setters
    public int getOrientation(){
        return orientation;
    }
    public Position getPos(){
        return center;
    }
    public ArrayList<Position> getVertices(){
        return vertices;
    }
    public void setXVertices(ArrayList<Position> vert){
        xvertices = vert;
    }
    /*Returns updated transformed vertices for Piece (correct orientation and position)*/
    public ArrayList<Position> getXVertices(){
        update();
        return xvertices;
    }
    public BoundingBox getBB(){
        return bb;
    }
    /**Returns updated transformed bb for Piece (correct orientation and position)*/
    public BoundingBox getXBB(){
        update();
        return xbb;
    }

    public boolean isActive(){
        return _active;
    }

    public void setActive(boolean b){
        _active = b;
    }




    public static final int
            rhombus = 1,
            parallelogram =2,
            triangle1 = 3,
            triangle2 = 4,
            triangle3 = 5,
            triangle4 = 6,
            triangle5 = 7;

    public static final int
            toolbox = 6;
    public static final int board = 7, pickedUp = 8;
}

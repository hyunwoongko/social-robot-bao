package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.base.VoiceActivity;
import com.welfarerobotics.welfareapplcation.entity.cache.TangramStageCache;
import com.welfarerobotics.welfareapplcation.util.Sound;

import java.util.ArrayList;
import java.util.Random;


/*
 * surfaceview = > 실시간으로 그릴 때 사용하는 뷰. 펀치를 뚫어서 그 부분만 옮겨짐. 4개의 메소드가 필요.
 *
 *
 *
 *
 *
 * */

public class TangramActivity extends VoiceActivity {
    ImagePuzzle puzzle;
    ArrayList<ImagePiece> _board;
    LinearLayout layout;
    Boolean flag = true;
    static public Resources r;
    private int toolboxHeight, displayWidth, displayHeight, scale;//전체 캔버스 크기?
    private Bitmap stageimage;
    BitmapDrawable background;
    Bitmap backBitmap;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    BitmapDrawable stage;
    private String image;
    int imageWidth;
    int imageHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = true;
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.play_activity);


        _board = new ArrayList<ImagePiece>();//
        r = this.getResources();
        /*이미지 세팅 부분*/
        image = getIntent().getStringExtra("image");
        Glide.with(this).asBitmap().load(image).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                stageimage = resource;
                stageimage =Bitmap.createScaledBitmap(stageimage, new Float(stageimage.getWidth()*0.7).intValue(), new Float(stageimage.getHeight()*0.7).intValue(), false);
//                stageimage =Bitmap.createScaledBitmap(stageimage, stageimage.getWidth()/2, stageimage.getHeight()/2, false);
            }
        });

        backBitmap = BitmapFactory.decodeResource(r, R.drawable.background);
        background = new BitmapDrawable(r, backBitmap);

        background.setBounds(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
        background.setAntiAlias(true);

        /*세팅 끝*/

        //set variables for drawing
        Display display = getWindowManager().getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();

        if (displayWidth >= 480)
            scale = 2;
        else
            scale = 1;
        toolboxHeight = 80 * 1;

        final Panel myPanel = new Panel(this);


        layout = findViewById(R.id.line);
        //layout.setOrientation(LinearLayout.HORIZONTAL);


        layout.addView(myPanel, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        // Recall button
        // Submit button
        ImageButton backBtn = findViewById(R.id.backbutton);
        backBtn.setClickable(true);
        backBtn.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            myPanel._thread.setRunning(false);

//            finish();
        });

        ImageButton leftBtn = findViewById(R.id.forbtn);
        leftBtn.setClickable(true);
        leftBtn.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "이번엔 다른 그림을 준비했어요!, 이 것도 맞춰볼까요?");
            Random random = new Random();
            TangramStageCache tangram;
            tangram = TangramStageCache.getInstance();
            image = tangram.getURL().get(random.nextInt(tangram.getURL().size()));
            Glide.with(getApplicationContext()).asBitmap().load(image).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    stageimage =resource;
                    stageimage =Bitmap.createScaledBitmap(stageimage, new Float(stageimage.getWidth()*0.7).intValue(), new Float(stageimage.getHeight()*0.7).intValue(), false);

                    myPanel.recall();
                }
            });

        });

        ImageButton submitBtn = findViewById(R.id.rotatebtn);
        submitBtn.setClickable(true);
        submitBtn.setOnClickListener(view -> {

        });

        Drawable bg = this.getDrawable(R.drawable.background);
        layout.setBackground(bg);
        //		layout.setBackgroundColor(Color.BLUE);


    }


    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.tangram);
        Sound.get().loop(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }

    //Disable Back button
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private ActionThread _thread;
        //private ArrayList<Piece> _board = new ArrayList<Piece>();
        private ArrayList<ImagePiece> _toolbox = new ArrayList<ImagePiece>();
        private ImagePiece _currentGraphic = null;//선택한 조각
        private ImagePiece _rotatedGraphic = null;//조각 회전
        private ImagePiece rotatePieces;

        public Panel(Context context) {
            super(context);
            puzzle = new ImagePuzzle(GlobalVariables.getCurrentLevel()); // Retrieve puzzle for Level 1
            int position = getIntent().getIntExtra("Position",0);
            ArrayList<ImagePiece> pieces =puzzle.getPieces(position);
            puzzle.scaleSolution(scale);
            getHolder().addCallback(this);
            //_thread = new ActionThread(getHolder(), this);
            // that puzzle

            for (int i = 0; i < pieces.size(); i++) {
                ImagePiece p = pieces.get(i);
                p.setActive(false);
                _toolbox.add(p); // add each piece to the toolbox
            }
            updateToolbox(); //update the positions of the pieces

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // synchronized (_thread.getSurfaceHolder()) {


            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                for (int i = 0; i < _toolbox.size(); i++) {
                    ImagePiece piece = _toolbox.get(i);
                    //if pointer inside BoundingBox of piece, select it
                    if (piece.getXBB().inside((int) x, (int) y) && (new Position((int) x, (int) y)).inside(piece.getXVertices(), false)) {
                        _currentGraphic = piece;
                     //   Log.d("탱그램 조각 선택","currentGraphic");
                        break;
                    }
                }

                for (int i = 0; i < _board.size(); i++) {
                    ImagePiece piece = _board.get(i);
                    //if pointer inside BoundingBox of piece, select it
                    if (piece.getXBB().inside((int) x, (int) y) && (new Position((int) x, (int) y)).inside(piece.getXVertices(), false)) {
                        _currentGraphic = piece;
                        break;
                    }
                }


            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (_currentGraphic != null) {
                    _currentGraphic.setActive(false);
                    int posX = (int) event.getX();
                    int posY = (int) event.getY();
              //      Log.d("탱그램 조각 움직임","currentGraphic");
                    _currentGraphic.moveTo(posX, posY);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                if (_currentGraphic != null) {
                    if (event.getY() > toolboxHeight) {// within the board area and outside the toolbox
                        int posX = Math.round(event.getX() / 10) * 10;
                        int posY = Math.round(event.getY() / 10) * 10;

                        int maxY = (layout.getHeight() - _currentGraphic.getHeight());
                        if (posY > maxY) {//make sure not below the buttons layout
                            posY = maxY;
                        }

                        int minX = _currentGraphic.getWidth() / 2;
                        int maxX = layout.getWidth() - minX;
                        if (posX > maxX) {//make sure it is not too far to the right
                            posX = maxX;
                        } else if (posX < minX) {//make sure it is not too far to the left
                            posX = minX;
                        }

                        //snap to grid
                        posX = Math.round(posX / 10) * 10;
                        posY = Math.round(posY / 10) * 10;

                        _currentGraphic.moveTo(posX, posY);
                        if (!_board.contains(_currentGraphic)) {
                            _board.add(_currentGraphic);
                            _toolbox.remove(_currentGraphic);
                            updateToolbox();
                        }
                    } else if (event.getY() <= toolboxHeight) {// within the toolbox area
                        if (!_toolbox.contains(_currentGraphic)) {
                            _toolbox.add(_currentGraphic);
                            _board.remove(_currentGraphic);
                        }
                        updateToolbox(); //always keep toolbox updated if in toolbox
                    }

                    if (_currentGraphic.isActive() && event.getY() > toolboxHeight) {
                        //check in board area too before rotating
//						_currentGraphic.rotate();
//						if(_rotatedGraphic !=null){
//
//						}else{
//							_rotatedGraphic = _currentGraphic;
//						}

                    }

                    setActive(_currentGraphic);
                    rotatePieces = _currentGraphic;
                    _currentGraphic = null;


                }
            }


            return true;
        }


        // }

        /**
         * setActive sets all the pieces on the board and in the toolbox
         * inactive except for the active piece
         *
         * @param p
         */
        public void setActive(ImagePiece p) {
            for (ImagePiece piece : _toolbox) {
                piece.setActive(false);
            }
            for (ImagePiece piece : _board) {
                piece.setActive(false);
            }
            p.setActive(true);
        }

        /**
         * updateToolBox rearranges the pieces in the right position
         */
        public void updateToolbox() {
            int margin = toolboxHeight /10;
            int marginvalue = 50;
            int centerX = 40;
            int centerY = 0;
            for (int i = 0; i < _toolbox.size(); i++) {
                ImagePiece p = _toolbox.get(i);
                centerX += (p.getWidth() + margin + marginvalue); // add width of Piece + margin
                centerY = toolboxHeight / 2;
                p.moveTo(centerX, centerY);
            }
        }

        /**
         * Recall takes all of the pieces on the board and puts them back into
         * the toolbox
         */
        public void recall() {
            //move all the pieces back to toolbox
            for (int i = 0; i < _board.size(); i++) {
                ImagePiece p = _board.get(i);
                p.setActive(false);
                _toolbox.add(p);

            }
            _board.clear();
            updateToolbox();
        }

        public void rotate() {
            try {
                rotatePieces.rotateBitmap(90);
                updateToolbox();
            } catch (Exception e) {
                System.out.println("Null Exception");


            }

        }

        @Override
        public void onDraw(Canvas canvas) {
            //draw the background

            background.draw(canvas);
            Float fl = new Float(displayWidth /2.5);

            //canvas.drawBitmap(stageimage,null,ast,null);
            if(stageimage!=null)
            canvas.drawBitmap(stageimage, fl.intValue(), new Float(displayHeight /3.5).intValue() , null);

            //paint for the pieces
            //Paint piecePaint = new Paint();
            //piecePaint.setColor(Color.RED);
            //canvas.drawOval(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(),piecePaint);
            //paint for the active piece
            //Paint activePaint = new Paint();
            //activePaint.setColor(Color.BLUE);

//		Paint paint = new Paint();
//			paint.setColor(Color.BLACK);
////			canvas.drawLine(0, toolboxHeight, displayWidth, toolboxHeight, paint); //Draw the line between the toolbox and play area
//
//			if(GlobalVariables.outlineOn){
//				//Draw puzzle in the background
//				Path puzzlePath = new Path();
//				ArrayList<Posi
//				tion> puzzlePositions = puzzle.getSolution();
//				for(int i = 0; i < puzzlePositions.size();i++){
//					if(i==0){
//						puzzlePath.moveTo(puzzlePositions.get(i).getX(),
//								puzzlePositions.get(i).getY());
//					}else{
//					puzzlePath.lineTo(puzzlePositions.get(i).getX(),
//							puzzlePositions.get(i).getY());
//					}
//				}
//				puzzlePath.close();
//				//snap to grid
//				int posX = Math.round((displayWidth/2-puzzle.getCenterX()) / 10) * 10;
//				int posY = Math.round((displayHeight/2-puzzle.getCenterY()) / 10) * 10;
//				puzzlePath.offset(posX, posY);
//				//Offset the "model" too! move xsolution
//				puzzle.moveXSolutionTo(posX, posY);
//				canvas.drawPath(puzzlePath, paint);

//				Rect x  = new Rect();
//				Rect dst = new Rect(400, 800,  displayWidth / 2, displayHeight / 2);// 이미지 폭과 넓이 그리고 크기 및 위치
//			if(image!=null){
//				System.out.println("스테이지 이미지 정상작동");
//				canvas.drawBitmap(image,null,dst,null);
//
//			}else{
//				System.out.println("스테이지 이미지 비정상작동");
//
//			}

//			}else {
//				//do not display outline
//			}

            if (!_toolbox.isEmpty()) {
                Path toolboxPiecePath;

                /*path를 통해 사각형 삼각형 그림.*/
                for (int j = 0; j < _toolbox.size(); j++) { //Draw all objects in the toolbox
                    ImagePiece toolboxPiece = _toolbox.get(j);

                    toolboxPiecePath = new Path();
                    ArrayList<Position> toolboxPieceVertices = toolboxPiece.getXVertices();
                    canvas.drawBitmap(toolboxPiece.getImage(), toolboxPieceVertices.get(0).getX(), toolboxPieceVertices.get(0).getY(), null);
                    for (int i = 0; i < toolboxPieceVertices.size(); i++) {

                        if (i == 0) {

                            toolboxPiecePath.moveTo(toolboxPieceVertices.get(i).getX(),
                                    toolboxPieceVertices.get(i).getY());
                        } else {

                            toolboxPiecePath.lineTo(toolboxPieceVertices.get(i).getX(),
                                    toolboxPieceVertices.get(i).getY());
                        }

                    }

                    toolboxPiecePath.close();

                    //toolboxPiecePath.offset(10+70*j, 0);
                    //toolboxPiecePath.offset(_currentGraphic., _currentGraphic.());
                    //canvas.drawPath(toolboxPiecePath, piecePaint);
                }
            }
            if (!_board.isEmpty()) {
                Path boardPiecePath;

                for (int j = 0; j < _board.size(); j++) { //Draw all objects on the board
                    ImagePiece boardPiece = _board.get(j);

                    boardPiecePath = new Path();
                    ArrayList<Position> boardPieceVertices = boardPiece.getXVertices();
                    canvas.drawBitmap(boardPiece.getImage(), boardPieceVertices.get(0).getX(), boardPieceVertices.get(0).getY(), null);

                    for (int i = 0; i < boardPieceVertices.size(); i++) {
                        if (i == 0) {
                            boardPiecePath.moveTo(boardPieceVertices.get(i).getX(),
                                    boardPieceVertices.get(i).getY());
                        } else {
                            boardPiecePath.lineTo(boardPieceVertices.get(i).getX(),
                                    boardPieceVertices.get(i).getY());
                        }
                    }
                    //this offset is messing the model don't do this
                    //rather design the Piece itself so it is centered!
                    //boardPiecePath.offset(boardPiece.getXOffset(), boardPiece.getYOffset());
                    boardPiecePath.close();

                    //flash the rotated piece in teal
                    if (_rotatedGraphic != null && _rotatedGraphic == boardPiece) {
                        //canvas.drawPath(boardPiecePath, activePaint);
                        _rotatedGraphic = null;
                    } else {
                        //canvas.drawPath(boardPiecePath, piecePaint);
                    }
                }
            }

            // Draw the object that is being dragged (if there is one)
            if (_currentGraphic != null) {
                Path path = new Path();

                ArrayList<Position> pieceVertices = _currentGraphic.getXVertices();

                for (int i = 0; i < pieceVertices.size(); i++) {
                    if (i == 0) {
                        path.moveTo(pieceVertices.get(i).getX(),
                                pieceVertices.get(i).getY());
                    } else {
                        path.lineTo(pieceVertices.get(i).getX(),
                                pieceVertices.get(i).getY());
                    }
                }
                //path.offset(_currentGraphic.getXOffset(), _currentGraphic.getYOffset());
                path.close();
                //canvas.drawPath(path, activePaint);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
        }

        public void surfaceCreated(SurfaceHolder holder) {
            _thread = new ActionThread(getHolder(), this);
            _thread.setRunning(true);
            _thread.start();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or
            // else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                    Log.d("탱그램 에러",""+e);
                }
            }
        }


    }


    class ActionThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;

        public ActionThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        public SurfaceHolder getSurfaceHolder() {
            return _surfaceHolder;
        }

        @SuppressLint("WrongCall") @Override
        public void run() {
            Canvas c;
            while (_run) {
                System.out.println("로그로그 : " + _run);
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    _panel.onDraw(c);
                }catch (Exception e){
                    Log.d("탱그램",""+e);

                }finally
                 {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }

            finish();
        }
    }
}
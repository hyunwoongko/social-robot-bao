package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.util.Log;

import com.welfarerobotics.welfareapplcation.entity.Tangram;

import java.util.ArrayList;

public class TangramSeparater {


    public static ArrayList<TangramPiece> Separate(Tangram data){
           ArrayList<TangramPiece> pieces = new ArrayList<TangramPiece>();

            for(String piece:data.getPieces().split(",")){
                for(TangramPiece p :TangramPiece.values()){

                    if(p.getName().equals(piece)){
                        Log.d("탱그램 분리기","enum:   "+p.getName()+"    piece:   "+piece);
                        pieces.add(p);


                    }
                }

            }

    return pieces;

    }



}

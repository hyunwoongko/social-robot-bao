package com.welfarerobotics.welfareapplcation.core.contents.tangram;

public enum TangramPiece {
    RHOMBUS("R"),PARALLELOGRAM("P"),TRIANGLE1("T1"),TRIANGLE2("T2"),TRIANGLE3("T3"),TRIANGLE4("T4"),TRIANGLE5("T5"),TRIANGLE1_90("T190"),TRIANGLE1_180("T1180"),TRIANGLE1_270("T1270"),TRIANGLE2_90("T290"),
    TRIANGLE2_180("T2180"),TRIANGLE2_270("T2270"),TRIANGLE3_90("T390"),TRIANGLE3_180("T3180"),TRIANGLE3_270("T3270"),
    TRIANGLE4_90("T490"),TRIANGLE4_180("T4180"),TRIANGLE4_270("T4270"),TRIANGLE5_90("T590"),TRIANGLE5_180("T5180"),
    TRIANGLE5_270("T5270"),PARALLELOGRAM_90("P90");

    private final String piece;

     TangramPiece(String piece) {
        this.piece = piece;
    }

    public String getName(){

        return this.piece;

    }


}

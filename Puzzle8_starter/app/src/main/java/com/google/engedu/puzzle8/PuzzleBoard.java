package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class PuzzleBoard {

    Bitmap operation;
    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    int steps = 0;
    PuzzleBoard previousBoard;

    MinPQ<PuzzleBoard>minPQ;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles = new ArrayList<PuzzleTile>();
        for(int i=0;i<NUM_TILES*NUM_TILES;i++)
        {
            Bitmap bmap = Bitmap.createBitmap(bitmap,
                    i%NUM_TILES * (bitmap.getWidth()/NUM_TILES),
                    i/NUM_TILES * (bitmap.getHeight()/NUM_TILES),
                    bitmap.getWidth()/NUM_TILES,
                    bitmap.getHeight()/NUM_TILES);

            bmap = Bitmap.createScaledBitmap(bmap,parentWidth/NUM_TILES,parentWidth/NUM_TILES,false);

            if(i== (NUM_TILES*NUM_TILES)-1)
                tiles.add(null);
            else
                tiles.add(new PuzzleTile(bmap,i));

        }
    }

    ///COPY CONSTRUCTOR
    PuzzleBoard(PuzzleBoard otherBoard) {
        this.steps = otherBoard.steps + 1;
        this.previousBoard = otherBoard;
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();

    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard>boards = new ArrayList<PuzzleBoard>();
        PuzzleTile temp;
        int nullPosition = 0;
        for(int i=0;i<tiles.size();i++)
        {
            temp = tiles.get(i);
            if(temp == null)
            {
                nullPosition = i;
                break;
            }
        }

        int tileX = nullPosition%NUM_TILES;
        int tileY = nullPosition/NUM_TILES;

        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) != null)
            {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));

                //Making a copy constructor
                boards.add(new PuzzleBoard(this));

                //Re Swapping to retaining original board state
                swapTiles(XYtoIndex(tileX, tileY), XYtoIndex(nullX, nullY));
            }
        }
        return boards;
    }

    public int priority() {
        PuzzleTile currentTile;
        int num=0;
        int a = 0;//a =hamming distance
        int b = 0;//b = steps

        for(int i=0;i<NUM_TILES*NUM_TILES;i++)
        {
            //num is the number of the tile
            if(tiles.get(i)!=null) {
                currentTile = tiles.get(i);
                num = currentTile.getNumber();

                int currentX = i%NUM_TILES;
                int currentY = i/NUM_TILES;

                int actualX = num%NUM_TILES;
                int actualY =  num/NUM_TILES;

                a = a + Math.abs(actualX - currentX) + Math.abs(actualY - currentY);

                b = this.steps;
            }

            else
                continue;
        }

        Log.d("fn",String.valueOf(a+b));
        return (a+b);
    }

    public PuzzleBoard getPreviousBoard() {
        return previousBoard;
    }
}

class PuzzleBoardComparator implements Comparator<PuzzleBoard>{

    @Override
    public int compare(PuzzleBoard board1, PuzzleBoard board2) {
        if(board1.priority()<board2.priority())
            return -1;

        else if(board1.priority()>board2.priority())
            return 1;

        return 0;
    }
}

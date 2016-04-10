package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap, View parent) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something.
            for (int j = 1; j <= NUM_SHUFFLE_STEPS; j++) {
                Random random = new Random();
                int i = random.nextInt(puzzleBoard.neighbours().size());

                puzzleBoard = puzzleBoard.neighbours().get(i);
                //invalidate();

            }
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {

        this.puzzleBoard.previousBoard = null;
        this.puzzleBoard.steps = 0;

        PuzzleBoardComparator comparator = new PuzzleBoardComparator();
        this.puzzleBoard.minPQ = new MinPQ<PuzzleBoard>(10,comparator);

        puzzleBoard.minPQ.insert(this.puzzleBoard);
        int count = 0;
        while(!puzzleBoard.minPQ.isEmpty())
        {
            PuzzleBoard answer = puzzleBoard.minPQ.delMin();

            Log.d("count",String.valueOf(count++));
            //PUZZLE SOLVED!
            if(answer.resolved())
            {
                ArrayList<PuzzleBoard>list = new ArrayList<PuzzleBoard>();

                while(answer!=this.puzzleBoard)
                {
                    list.add(answer);
                    answer  = answer.getPreviousBoard();
                }


                Collections.reverse(list);
                animation = (ArrayList<PuzzleBoard>) list.clone();
                invalidate();
                break;
            }

            //answer is NOT THE SOLUTION
            else
            {
                for(PuzzleBoard neighbours : answer.neighbours())
                {
                    //CRITICAL OPTIMIZATION
                    if(neighbours!=neighbours.getPreviousBoard())
                    puzzleBoard.minPQ.insert(neighbours);
                }
            }
        }

    }

}

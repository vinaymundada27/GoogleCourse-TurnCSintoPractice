package com.example.admin.scarnesdice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import android.os.Handler;
public class MainActivity extends AppCompatActivity {

    TextView userScore,compScore ;
    ImageView imageView;
    Button roll;
    Button hold;

    int curr_userScore,curr_compScore,USER_SCORE,COMP_SCORE;
    private boolean flag,temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        flag = false;
        temp = false;
        curr_userScore=0;
        curr_compScore=0;
        USER_SCORE=0;
        COMP_SCORE=0;

        roll = (Button)findViewById(R.id.roll);
        hold = (Button)findViewById(R.id.hold);

        userScore = (TextView) findViewById(R.id.textView);
        compScore = (TextView) findViewById(R.id.textView2);
        userScore.setText("Your score: 0");
        compScore.setText("Computer score: 0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resetScore(View v){
        userScore = (TextView) findViewById(R.id.textView);
        compScore = (TextView) findViewById(R.id.textView2);
        userScore.setText("Your score: 0");
        compScore.setText("Computer score: 0");
        roll.setEnabled(true);
        hold.setEnabled(true);
        curr_compScore = 0;
        curr_userScore =0;
        USER_SCORE = 0;
        COMP_SCORE =0;
    }

    public void rollTheDie(View v){
        roll();
    }

    private int roll(){

        imageView = (ImageView) findViewById(R.id.imageView);

        Random random = new Random();
        int i = random.nextInt(6)+1;

        switch(i)
        {
            case 1:
                imageView.setImageResource(R.drawable.dice1);
                if(!flag)
                {
                    curr_userScore = 0;
                    computerTurn();
                }
                else if(flag)
                {
                    curr_compScore = 0;
                    userTurn();
                    return 1;
                }
                break;

            case 2:
                imageView.setImageResource(R.drawable.dice2);
                if(flag == false)
                    curr_userScore = curr_userScore + i;
                else
                    curr_compScore = curr_compScore + i;

                break;

            case 3:
                imageView.setImageResource(R.drawable.dice3);
                if(flag == false)
                    curr_userScore = curr_userScore + i;
                else
                    curr_compScore = curr_compScore + i;
                break;

            case 4:
                imageView.setImageResource(R.drawable.dice4);
                if(flag == false)
                    curr_userScore = curr_userScore + i;
                else
                    curr_compScore = curr_compScore + i;
                break;

            case 5:
                imageView.setImageResource(R.drawable.dice5);
                if(flag == false)
                    curr_userScore = curr_userScore + i;
                else
                    curr_compScore = curr_compScore + i;
                break;

            case 6:
                imageView.setImageResource(R.drawable.dice6);
                if(flag == false)
                    curr_userScore = curr_userScore + i;
                else
                    curr_compScore = curr_compScore + i;
                break;

        }
        return 0;
    }

    private void userTurn() {
        flag= false;
        curr_userScore = 0;

        //enable the roll and hold buttons
        roll.setEnabled(true);
        hold.setEnabled(true);
    }

    private void computerTurn() {
        flag = true;
        temp =false;
        curr_compScore = 0;

        //disable roll and hold buttons
        roll.setEnabled(false);
        hold.setEnabled(false);

       /* while(curr_compScore<20)
        {
            if(roll()==1)
                break;
            Log.d("while",String.valueOf(curr_compScore));

            if(curr_compScore>=20)
            {
                COMP_SCORE = COMP_SCORE + curr_compScore;
                //curr_compScore = 0;
                break;
            }

        }
        setCompScore();*/

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (roll() == 1) {
                    Log.d("comp", "roll");
                    //setCompScore();
                    h.removeCallbacks(this);
                    setCompScore();
                }
                else {
                    Log.d("while", "rolling");
                    COMP_SCORE = COMP_SCORE + curr_compScore;

                    if (COMP_SCORE >= 100) {
                        //setCompScore();
                        h.removeCallbacks(this);
                        setCompScore();
                    } else {
                        h.postDelayed(this, 1000);
                    }
                }
            }
        }, 1000);
        }

            /*Runnable r = new Runnable() {
            @Override
            public void run() {
                if(roll()==1) {
                    Log.d("comp",String.valueOf(COMP_SCORE));
                    //setCompScore();
                    h.removeCallbacks(this);
                }
                else {

                    Log.d("while", String.valueOf(COMP_SCORE));
                    COMP_SCORE = COMP_SCORE + curr_compScore;

                    if (COMP_SCORE >= 100) {
                        //setCompScore();
                        h.removeCallbacks(this);
                    } else {
                        h.postDelayed(this, 1000);
                    }
                }
            }
        };
        setCompScore();
        h.postDelayed(r,1000);*/



    public void holdTheDie(View v){
        USER_SCORE = USER_SCORE + curr_userScore;
        setUserScore();
    }

    private void setCompScore() {
        compScore = (TextView) findViewById(R.id.textView2);
        compScore.setText("Computer score: " + COMP_SCORE);

        if(COMP_SCORE >= 100)
        {
            alert("COMPUTER ");
            curr_compScore = 0;
        }
        else
        {
            userTurn();
        }
    }

    private void alert(String player) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle(player+" WON");

        adb.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);
                Toast.makeText(MainActivity.this,"New Game!", Toast.LENGTH_SHORT).show();
            }
        });


        adb.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        
        adb.show();
    }

    private void setUserScore() {
        userScore = (TextView) findViewById(R.id.textView);
        Log.d("user",String.valueOf(USER_SCORE));
        userScore.setText("Your score: " + USER_SCORE);

        if(USER_SCORE >= 100)
        {
            alert("YOU ");
        }
        else
            computerTurn();
    }
}

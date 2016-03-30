package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String KEY_TEXT_VALUE ="key" ;
    private SimpleDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView fragment;
    TextView status;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        char x= (char) event.getUnicodeChar();
        if(x>='a' && x<='z') {
            Log.d("if","worng");
            String s = fragment.getText().toString();
            s = s + Character.toString(x);

            fragment.setText(s);

            if(dictionary.isWord(s))
            {
                status.setText("Computer wins");
            }
            else
            {
                status.setText(COMPUTER_TURN);
                computerTurn();
            }
        }
        else
        {
            return super.onKeyUp(keyCode, event);
        }
        return true;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        fragment = (TextView) findViewById(R.id.ghostText);
        status = (TextView)findViewById(R.id.gameStatus);

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);

        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        initialise();
        onStart(null);

        if (savedInstanceState != null) {
            CharSequence savedText = savedInstanceState.getCharSequence(KEY_TEXT_VALUE);
            fragment.setText(savedText);
        }
    }

    public void initialise() {
        String s = dictionary.getAnyWordStartingWith(null);

        String upTo4Characters = s.substring(0, Math.min(s.length(), 4));
        fragment.setText(upTo4Characters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);

        //My code:
        String input =  fragment.getText().toString();
        if(input.equals(""))
        {
            String newWord;
            newWord = dictionary.getAnyWordStartingWith(input);

            if(newWord == null)
            {
                // computer wins
                status.setText("Computer wins");
            }
            else {
                char c = newWord.charAt(input.length());
                input += String.valueOf(c);

                fragment.setText(input);
            }
        }
        else
        {
            if (input.length() >= 4 && dictionary.isWord(input)) {
                status.setText("Computer wins");
            }
            else
            {
                String newWord;
                newWord = dictionary.getAnyWordStartingWith(input);

                if(newWord == null)
                {
                    // computer wins
                    status.setText("Computer wins");
                }
                else {
                    char c = newWord.charAt(input.length());
                    input += String.valueOf(c);

                    fragment.setText(input);
                }
            }
        }

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();

        initialise();
        //TextView frag = (TextView) findViewById(R.id.ghostText);
        //frag.setText("");

        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    public void challenge(View v)
    {
        String computerWord = fragment.getText().toString();
        String temp;

        if(computerWord.length()>=4 && dictionary.isWord(computerWord))
        {
            status.setText("User wins");
        }
        else if(!dictionary.isWord(computerWord)){

            if((temp = dictionary.getAnyWordStartingWith(computerWord))==null)
            {
                status.setText("User wins");
            }
            else
            {
                status.setText("Computer wins");
                fragment.setText(temp);
            }
        }
    }
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_TEXT_VALUE, fragment.getText());
    }

}

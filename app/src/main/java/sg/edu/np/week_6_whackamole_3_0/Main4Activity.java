package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    int advancedScore , level ;
    boolean flag;
    TextView scoreView;
    Button mole , back , mole2;
    UserData userData;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished / 1000);
                Toast
                        .makeText(
                                getApplicationContext(),
                                String.valueOf(millisUntilFinished / 1000),
                                Toast.LENGTH_SHORT
                        )
                        .show();
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                Toast
                        .makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT)
                        .show();

                placeMoleTimer();
                flag = true;
            }
        }.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        newMolePlaceTimer = new CountDownTimer(Long.MAX_VALUE, 11000-(level*1000)) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "New Mole Location!");
                setNewMole();
            }

            @Override
            public void onFinish() {


            }
        }.start();
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.a_button1,
            R.id.a_button2,
            R.id.a_button3,
            R.id.a_button4,
            R.id.a_button5,
            R.id.a_button6,
            R.id.a_button7,
            R.id.a_button8,
            R.id.a_button9,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        userData = (UserData) getIntent().getSerializableExtra("User");
        level = getIntent().getIntExtra("level" , 1);

        scoreView = findViewById(R.id.textView);
        scoreView.setText("0");
        back = findViewById(R.id.back);

        Log.v(TAG, "Current level: " + level);

        readyTimer();
        setNewMole();
        flag = false;

        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button button = findViewById(id);
            button.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (flag) {
                                doCheck(button);
                            } else {
                                Toast.makeText(
                                                getApplicationContext(),
                                                "Please wait till countdown",
                                                Toast.LENGTH_SHORT
                                        ).show();
                            }
                        }
                    }
            );
        }

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updateUserScore();

                Intent backToSelect = new Intent(Main4Activity.this, Main3Activity.class);
                backToSelect.putExtra("User", userData);
                startActivity(backToSelect);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        updateUserScore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserScore();

    }

    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if (mole == checkButton) {
            advancedScore++;
            Log.v(TAG, "Hit, score added!");
        } else {
            if (advancedScore > 0) {
                advancedScore--;
            }
            Log.v(TAG, "Missed, score deducted!");
        }
        scoreView.setText(String.valueOf(advancedScore));
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        int newrandloc;
        Log.v(TAG, String.valueOf(randomLocation));
        mole = findViewById(BUTTON_IDS[randomLocation]);
        mole.setText("*");

        if(level >= 6) {
            do {
                newrandloc = ran.nextInt(9);
                Log.v(TAG, String.valueOf(newrandloc));
            }while (randomLocation == newrandloc);
            mole2 = findViewById(BUTTON_IDS[newrandloc]);
            mole2.setText("*");
        }
        //Set the empty in the buttons
        for (final int i : BUTTON_IDS) {
            final Button button = findViewById(i);
            if(button != mole2 && button != mole){
                button.setText("O");
            }
        }

    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        Log.v(TAG, FILENAME + ": Update User Score...");

        MyDBHandler myDBHandler = new MyDBHandler(Main4Activity.this);
        myDBHandler.deleteAccount(userData.getMyUserName());

        userData.getScores().set(level-1,advancedScore);

        myDBHandler.addUser(userData);

    }

}

package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    EditText username , password;
    Button login;
    TextView newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        username = findViewById( R.id.usernameEdit);
        password = findViewById( R.id.passwordEdit);
        login = findViewById( R.id.Login);
        newUser = findViewById( R.id.newUser);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if (!isValidUser(name , pass)) {
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");

                    MyDBHandler myDBHandler = new MyDBHandler(MainActivity.this);
                    UserData userData = myDBHandler.findUser(name);

                    Log.v(TAG, FILENAME + ": Logging in with: " + name + ": " + pass);
                    Intent levels = new Intent(MainActivity.this , Main3Activity.class);
                    levels.putExtra("User", userData);
                    startActivity(levels);

                }else{
                    Log.v(TAG, FILENAME + ": Invalid user!");
                    Toast.makeText(MainActivity.this , "Invalid Username and Password" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent create = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(create);
            }
        });


    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */

        MyDBHandler myDBHandler = new MyDBHandler(MainActivity.this);
        UserData userData = myDBHandler.findUser(userName);

        if (userData == null) {
            return true;
        }else {
            Log.v(TAG, FILENAME + ": Running Checks..." + userData.getMyUserName() + ": " + userData.getMyPassword() +" <--> "+ userName + " " + password);
            return false;
        }

    }

}

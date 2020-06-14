package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    EditText username , password;
    Button create , cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */

        username = findViewById( R.id.usernameEdit);
        password = findViewById( R.id.passwordEdit);
        create = findViewById(R.id.create);
        cancel = findViewById(R.id.cancel);

        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();

                if(isValidUser(name ,pass)){
                    MyDBHandler myDBHandler = new MyDBHandler(Main2Activity.this);

                    ArrayList<Integer> levels = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
                    ArrayList<Integer> scores = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0));

                    myDBHandler.addUser(new UserData( name , pass , levels ,scores));

                    Log.v(TAG, FILENAME + ": New user created successfully!");
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Main2Activity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                }else{
                    Log.v(TAG, FILENAME + ": User already exist during new user creation!");
                    Toast.makeText(Main2Activity.this, "User Already Exist. Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");

                Intent back = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(back);
            }
        });
    }

    protected void onStop() {
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */

        MyDBHandler myDBHandler = new MyDBHandler(Main2Activity.this);
        UserData userData = myDBHandler.findUser(userName);
        if (userData == null) {
            return true;
        }else {
            Log.v(TAG, FILENAME + ": Running Checks..." + userData.getMyUserName() + ": " + userData.getMyPassword() +" <--> "+ userName + " " + password);
            return false;
        }



    }
}

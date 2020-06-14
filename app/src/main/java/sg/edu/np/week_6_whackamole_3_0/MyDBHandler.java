package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private static final String DATABASE_NAME = "Whack-A-Mole.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_LEVEL = "Level" ;
    private static final String COLUMN_SCORE = "Score";

    public MyDBHandler(Context context)
    {
        /* HINT:
            This is used to init the database.
         */
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* HINT:
            This is triggered on DB creation.
            Log.v(TAG, "DB Created: " + CREATE_ACCOUNTS_TABLE);
         */
        String CreateQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_LEVEL + " INTEGER,"
                + COLUMN_SCORE + " INTEGER)";
        db.execSQL(CreateQuery);
        Log.v(TAG, "DB Created: " + CreateQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /* HINT:
            This is triggered if there is a new version found. ALL DATA are replaced and irreversible.
         */
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.v(TAG, "DB DROPPED");
        onCreate(db);
    }

    public void addUser(UserData userData)
    {
            /* HINT:
                This adds the user to the database based on the information given.
                Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
             */
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            for (int i = 0; i < userData.getScores().size(); i++ ){
                values.put(COLUMN_USERNAME , userData.getMyUserName());
                values.put(COLUMN_PASSWORD , userData.getMyPassword());
                values.put(COLUMN_SCORE , userData.getScores().get(i));
                values.put(COLUMN_LEVEL , userData.getLevels().get(i));

                Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
                db.insert(TABLE_NAME , null , values);
            }

            db.close();
    }

    public UserData findUser(String username)
    {
        /* HINT:
            This finds the user that is specified and returns the data information if it is found.
            If not found, it will return a null.
            Log.v(TAG, FILENAME +": Find user form database: " + query);

            The following should be used in getting the query data.
            you may modify the code to suit your design.

            if(cursor.moveToFirst()){
                do{
                    ...
                    .....
                    ...
                }while(cursor.moveToNext());
                Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
            }
            else{
                Log.v(TAG, FILENAME+ ": No data found!");
            }
         */
        String Find_User_Query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "='" + username +"' ORDER BY " +
                COLUMN_LEVEL + " ASC;";
        Log.v(TAG, FILENAME +": Find user form database: " + Find_User_Query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor returnData = db.rawQuery(Find_User_Query , null);

        if(returnData.moveToFirst()){
            UserData userData = new UserData();

            String userName = returnData.getString(returnData.getColumnIndex(COLUMN_USERNAME));
            String password = returnData.getString(returnData.getColumnIndex(COLUMN_PASSWORD));

            userData.setMyUserName(userName);
            userData.setMyPassword(password);

            do{
                userData.getLevels().add(returnData.getInt(returnData.getColumnIndex(COLUMN_LEVEL)));
                userData.getScores().add(returnData.getInt(returnData.getColumnIndex(COLUMN_SCORE)));

            }while(returnData.moveToNext());
            Log.v(TAG, FILENAME + ": QueryData: " + userData.getLevels().toString() + userData.getScores().toString());
            return  userData;
        }
        else{
            Log.v(TAG, FILENAME+ ": No data found!");
            return null;
        }





    }

    public boolean deleteAccount(String username) {
        /* HINT:
            This finds and delete the user data in the database.
            This is not reversible.
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
         */

        SQLiteDatabase db = this.getWritableDatabase();

        String Delete_Query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_USERNAME + " = \""
                + username + "\"";

        Log.v(TAG, FILENAME + ": Database delete user: " + Delete_Query);

        Cursor returnData = db.rawQuery(Delete_Query, null);

        if (returnData.moveToFirst()) {

            do {

                String id = String.valueOf(returnData.getInt(returnData.getColumnIndexOrThrow(COLUMN_ID)));
                Log.d(TAG, "deleteAccount: " + id);

                db.delete(TABLE_NAME, COLUMN_ID + "=?",
                        new String[]{id});


            } while (returnData.moveToNext());
        }

        db.close();

        return true;
    }
}

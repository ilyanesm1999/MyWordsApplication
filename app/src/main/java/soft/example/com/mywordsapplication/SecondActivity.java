package soft.example.com.mywordsapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by admin on 21.02.2016.
 */
public class SecondActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_activity);




        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        // sleep

        /*
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
         */


    }

    public void stopApp(View v){
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        /*
        Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_HOME);
startActivity(intent);
         */

    }

    public void buttonGotoClick(View v){


        Intent myIntent = new Intent(this, MainActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivityForResult(myIntent, 1);
        //Intent i = new Intent(this, SecondActivity.class);
        //startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                //display in short period of time
                Toast.makeText(getApplicationContext(), "пришли из main по cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult
}

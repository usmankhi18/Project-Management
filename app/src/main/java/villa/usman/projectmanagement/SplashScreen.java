package villa.usman.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import es.dmoral.toasty.Toasty;
import villa.usman.projectmanagement.Common.Constants;

public class SplashScreen extends Activity {

    Handler handler;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedpreferences = getSharedPreferences(Constants.ProfilePREFERENCES, Context.MODE_PRIVATE);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadActivity();
            }
        },3000);

    }

    private void LoadActivity(){
        try {
            if(!sharedpreferences.contains("UserID")) {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }catch(Exception ex){
            Toasty.error(getApplicationContext(),ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }
}

package in.co.fantasyworld.extra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import in.co.fantasyworld.R;
import in.co.fantasyworld.login.LoginAction;

public class SplashScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this,LoginAction.class);
                startActivity(i);
                finish();
            }
        },3000);
    }
}

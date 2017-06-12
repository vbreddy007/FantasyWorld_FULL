package in.co.fantasyworld.payments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONObject;

import in.co.fantasyworld.R;
import in.co.fantasyworld.leaderboard.LeaderBoard;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by C5245675 on 4/23/2017.
 */

public class WalletActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    TextView wall_bal;
    String user_bal;
    String gift_mon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_layout);
        toolbar = (Toolbar)findViewById(R.id.mToolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btn = (Button)findViewById(R.id.leaderboard);
        Button btn2 = (Button)findViewById(R.id.dash);
        wall_bal = (TextView) findViewById(R.id.gift_money);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalletActivity.this,LeaderBoard.class);
               startActivity(i);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalletActivity.this, Payment.class);
                startActivity(i);
            }
        });

       new ProgressTask().execute();
    }
    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url("http://10.0.2.2/TEST/Latest/").build();

            try
            {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("balance");
                JSONObject tempObject = jsonArray.getJSONObject(0);
                String user_bal  =  tempObject.getString("userbal");
                String gift_mon =  tempObject.getString("giftmoney");


            }
            catch (Exception e)
            {

            }

        return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            wall_bal.setText(" ₹ 1000");

            wall_bal.setText(""+user_bal);
        }
    }
}

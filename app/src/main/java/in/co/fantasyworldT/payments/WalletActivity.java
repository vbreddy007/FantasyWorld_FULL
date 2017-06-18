package in.co.fantasyworldT.payments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import in.co.fantasyworldT.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
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
    Button btn10,btn20,btn50,btn70,btn100,addamount;
    TextInputEditText amountInput;
    private String accessToken = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_layout);

        //intializing payment gateway
        Instamojo.initialize(this);
        Instamojo.setBaseUrl("https://api.instamojo.com/");

        //preparing dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        btn10 = (Button) findViewById(R.id.btn_10);
        btn20 = (Button) findViewById(R.id.btn_20);
        btn50 = (Button) findViewById(R.id.btn_50);
        btn70 = (Button) findViewById(R.id.btn_70);
        btn100 = (Button) findViewById(R.id.btn_100);
        addamount = (Button) findViewById(R.id.btn_addamount);
        amountInput = (TextInputEditText) findViewById(R.id.enteredamount) ;

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput.setText("10.00");
            }
        });
        btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput.setText("20.00");
            }
        });
        btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput.setText("50.00");
            }
        });
        btn70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput.setText("70.00");
            }
        });
        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput.setText("100.00");
            }
        });
        amountInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput.getText().clear();
            }
        });
        addamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchTokenAndTransactionID();

            }
        });






    }
    private void fetchTokenAndTransactionID() {

        if (!dialog.isShowing()) {
            dialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url("http://www.fantasyworld.co.in/test/").build();
        try {

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            showToast("Failed to fetch the Order Tokens");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString;
                    String errorMessage = null;
                    String transactionID = null;
                    responseString = response.body().string();
                    response.body().close();
                    try {
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.has("error")) {
                            errorMessage = responseObject.getString("error");
                            showToast("it has error");
                        } else {
                            accessToken = responseObject.getString("access_token");
                            String token = responseObject.getString("token_type");
                            showToast("this is token"+token);


                            // transactionID = responseObject.getString("transaction_id");
                        }
                    } catch (JSONException e) {
                        errorMessage = "Failed to fetch Order tokens";
                    }

                    final String finalErrorMessage = errorMessage;
                    Date d = new Date();
                    final String finalTransactionID = "123456AdsfgdgdkjfBCDEqF";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            if (finalErrorMessage != null) {
                                showToast(finalErrorMessage);
                                return;
                            }

                            createOrder(accessToken, finalTransactionID);
                        }
                    });

                }
            });



        }

        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

    private void createOrder(String accessToken, String transactionID) {
        String name;
        String email;





            name = "defaultVBR";
            email = "defreddy@gmail.com";





        String phone = "9916897737";
        String amount = amountInput.getText().toString();
        String description = "payment to play";

        //Create the Order
        Order order = new Order(accessToken, transactionID, name, email, phone, amount, description);

        //set webhook
        order.setWebhook("http://your.server.com/webhook/");

        //Validate the Order
        if (!order.isValid()) {
            //oops order validation failed. Pinpoint the issue(s).


            if (!order.isValidTransactionID()) {
                showToast("Transaction is Invalid");
            }

            if (!order.isValidRedirectURL()) {
                showToast("Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
                showToast("Webhook URL is invalid");
            }

            return;
        }


        dialog.show();

        com.instamojo.android.network.Request request = new com.instamojo.android.network.Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(final Order order, final Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionError) {
                                showToast("No internet connection");
                            } else if (error instanceof Errors.ServerError) {
                                showToast("Server Error. Try again");
                            } else if (error instanceof Errors.AuthenticationError) {
                                showToast("Access token is invalid or expired. Please Update the token!!");
                            } else if (error instanceof Errors.ValidationError) {
                                // Cast object to validation to pinpoint the issue
                                Errors.ValidationError validationError = (Errors.ValidationError) error;

                                if (!validationError.isValidTransactionID()) {
                                    showToast("Transaction ID is not Unique");
                                    return;
                                }

                                if (!validationError.isValidRedirectURL()) {
                                    showToast("Redirect url is invalid");
                                    return;
                                }

                                if (!validationError.isValidWebhook()) {
                                    showToast("Webhook url is invalid");
                                    return;
                                }


                            } else {
                                showToast(error.getMessage());
                            }
                            return;
                        }

                        startPreCreatedUI(order);
                    }
                });
            }
        });

        request.execute();

    }
    private void startPreCreatedUI(Order order) {
        //Using Pre created UI
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                          }
                      }
        );
    }

    private void checkPaymentStatus(final String transactionID, final String orderID) {
        if (accessToken == null || (transactionID == null && orderID == null)) {
            showToast("access token , transcation ID and Order Id are null");
            return;
        }

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

        showToast("checking transaction status");
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = getHttpURLBuilder();
        builder.addPathSegment("status");
        if (transactionID != null){
            builder.addQueryParameter("transaction_id", transactionID);
        } else {
            builder.addQueryParameter("id", orderID);
        }
        //builder.addQueryParameter("env", currentEnv.toLowerCase());
        HttpUrl url = builder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        showToast("Failed to fetch the Transaction status");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                response.body().close();
                String status = null;
                String paymentID = null;
                String amount = null;
                String errorMessage = null;

                try {
                    JSONObject responseObject = new JSONObject(responseString);
                    System.out.println("This is response object "+responseObject.toString());
                    JSONObject payment = responseObject.getJSONArray("payments").getJSONObject(0);
                    status = payment.getString("status");
                    paymentID = payment.getString("id");
                    amount = responseObject.getString("amount");

                } catch (JSONException e) {
                    errorMessage = "Failed to fetch the Transaction status in response";
                    e.printStackTrace();
                }

                final String finalStatus = status;
                final String finalErrorMessage = errorMessage;
                final String finalPaymentID = paymentID;
                final String finalAmount = amount;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (finalStatus == null) {
                            showToast(finalErrorMessage);
                            return;
                        }

                        if (!finalStatus.equalsIgnoreCase("successful")) {
                            showToast("Transaction still pending");
                            return;
                        }

                        showToast("Transaction Successful for id - " + finalPaymentID);
                        // refundTheAmount(transactionID, finalAmount);
                    }
                });
            }
        });

    }


    private HttpUrl.Builder getHttpURLBuilder() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.instamojo.com");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (transactionID != null || paymentID != null) {
                checkPaymentStatus(transactionID, orderID);
                Log.d("payvbr",transactionID+ "and"+orderID);
            } else {
                showToast("Oops!! Payment was cancelled");
            }
        }


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

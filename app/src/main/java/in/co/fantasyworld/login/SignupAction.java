package in.co.fantasyworld.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applicationtest.vbr.designtest4.R;
import com.applicationtest.vbr.designtest4.com.vbr.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by C5245675 on 4/19/2017.
 */
public class SignupAction extends AppCompatActivity {

    Button signup_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String TAG = "signin";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.signup_layout);


        signup_button = (Button)findViewById(R.id.btn_signup);

        final TextView userName = (TextView)findViewById(R.id.input_email);
        final TextView input_password = (TextView) findViewById(R.id.input_password);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        signup_button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                String email = userName.getText().toString();
                String password = input_password.getText().toString();


               signupUser(email,password);




            }
        }

        );






    }

    private void signupUser(String usernameP ,String passwordP)
    {
        mAuth.createUserWithEmailAndPassword(usernameP, passwordP)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignupActivity.this, "account has been created.",
                                    Toast.LENGTH_SHORT).show();

                            Intent i  = new Intent(SignupActivity.this , MainActivity.class);

                            startActivity(i);



                        }

                        // ...
                    }
                });
    }
}

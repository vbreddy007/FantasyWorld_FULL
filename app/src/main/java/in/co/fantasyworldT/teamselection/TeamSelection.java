package in.co.fantasyworldT.teamselection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.okhttp.MediaType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.contests.ContestsAction;
import in.co.fantasyworldT.leaderboard.LeaderBoard;
import in.co.fantasyworldT.payments.Payment;
import okhttp3.FormBody;


public class TeamSelection extends AppCompatActivity {

    private ViewPager viewPager;
    TabLayout tabLayout;
    ImageView topimageView;
    ArrayList selectedplayerlist = new ArrayList();
    ArrayList<String> selectedPlayerListName = new ArrayList<>();
    TextView sel_count;
    int count = 0;
    Button save_team;
    String teamnameG;
    String teamOne,teamTwo;
    FirebaseUser firebaseUser;
    String teamstring,teamnamestring;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teamselection);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);


        viewPager = (ViewPager)findViewById(R.id.viewpager_teamselection2);
        setupViewPager(viewPager);

        topimageView = (ImageView) findViewById(R.id.top_image_teamselection2);
        sel_count = (TextView) findViewById(R.id.teamselection2_team_count);
        save_team = (Button)findViewById(R.id.team_save);
        save_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamnameDialog();
            }
        });

        teamOne = ContestsAction.getTeamOne();
        teamTwo = ContestsAction.getTeamTwo();

        System.out.println("this is in team selection team one"+teamOne);
        final TextView select_hint = (TextView) findViewById(R.id.selction_hint);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout_teamselection2);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0) {
                    topimageView.setImageResource(R.drawable.splashbackground);
                    select_hint.setText("Please select 1 Wicket Keeper");
                }
                else if(tab.getPosition() == 1)
                {
                    topimageView.setImageResource(R.drawable.ic_launcher);
                    select_hint.setText("Please select 3 to 5 batsman");

                }
                else if (tab.getPosition() == 2)
                {
                    topimageView.setImageResource(R.drawable.splashbackground);
                    select_hint.setText("Please select 2 to 5 bowlers");
                }
                else if (tab.getPosition() == 3)
                {

                    select_hint.setText("please select 2 to 4 all rounders");
                }


                Toast.makeText(getApplicationContext(),""+tab.getPosition(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public String getOne()
    {
        return teamOne;
    }
    public String getTwo()
    {
        return teamTwo;
    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new WicketKeepers(), "WK");
        adapter.addFrag(new BatsMan(), "BATSMAN");
        adapter.addFrag(new Bowlers(),"BOWLERS");
        adapter.addFrag(new AllRounders(),"ALL ROUNDERS");


        viewPager.setAdapter(adapter);

    }

    public void updateSelectedPlayers(int playerId ,String playerName, boolean isAdd)
    {

        if(isAdd)
        {
            if(selectedplayerlist.size() != 0) {

                if (!selectedplayerlist.contains(playerId)) {
                    selectedplayerlist.add(playerId);
                    selectedPlayerListName.add(playerName);
                    System.out.println("this is size"+selectedplayerlist.size() +""+selectedPlayerListName.toString());
                    updatecouter(true);
                }
            }
            else {
                selectedplayerlist.add(playerId);
                selectedPlayerListName.add(playerName);
                selectedplayerlist.size();
                System.out.println("this is size"+selectedplayerlist.size() +""+selectedPlayerListName.toString());
                updatecouter(true);
            }
        }
        else
        {
            if(selectedplayerlist.size() != 0)
            {
                if (selectedplayerlist.contains(playerId))
                {
                    selectedplayerlist.remove(new Integer(playerId));
                    selectedPlayerListName.remove(playerName);
                    selectedplayerlist.size();
                    System.out.println("this is size"+selectedplayerlist.size() +""+selectedPlayerListName.toString());
                    updatecouter(false);

                }

            }

        }

       System.out.println("this is inside activity");
    }
    public void updatecouter(boolean isAdded)
    {

        System.out.println("this is inside counter");
        if(isAdded)
        {
            System.out.println("inside true condition");
            count = count +1;
            sel_count.setText(count+"/11");
        }
        else if(!isAdded)
        {
            System.out.println("inside false condition");
            count = count -1;
            sel_count.setText(count+"/11");
        }

    }

    void sendTeamMethod() {

        AsyncTask<Void,Void,Void> sendteamTask = new AsyncTask<Void,Void,Void>() {

            @Override
            protected void onPreExecute() {

                if (!dialog.isShowing()) {
                    dialog.show();
                }
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... urls) {

                MediaType mt = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                String json;

                try {

                    System.out.println("background task of send team is executing");

                    // JSONObject ob = jsonObject.accumulate("team", teamStringforJSON.toString());

                   // JSONObject ob = new JSONObject();
                   // ob.put("team",teamStringforJSON.toString());
                    //ob.put("teamname",teamnameG);

                    //ystem.out.println("JSONOBJECT accumuluted " + ob.toString());



                    okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
                    //   RequestBody body = RequestBody.create(mt, ob.toString());

                    // RequestBody body = new MultipartBuilder()

                    // .addFormDataPart("team",ob.toString()).build();



                    // in okHttp 3.x
                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("team",teamstring.toString());



                    formBuilder.add("teamname",teamnamestring);

                    if(firebaseUser!=null)
                    {

                        String uName= firebaseUser.getDisplayName();
                        String uEmail = firebaseUser.getEmail();

                        if(uEmail != null)
                        {
                            formBuilder.add("useremail",uEmail);
                        }
                    }



                    okhttp3.RequestBody  body = formBuilder.build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://10.0.2.2/TEST/Latest/readteam.php")
                            .post(body)
                            .build();

                    System.out.println("This is post body "+body.toString());

                    okhttp3.Response response = client.newCall(request).execute();
                    String responseS = response.body().string();

                    Log.i("@", "" + responseS);


                } catch (Exception e) {
                    e.printStackTrace();


                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
            }
        };
        sendteamTask.execute();



    }

    public void teamnameDialog()
    {

        LayoutInflater li = LayoutInflater.from(this);
        final View promptview = li.inflate(R.layout.teamname_alertdialogview,null);

        final AlertDialog alertDialog = new AlertDialog.Builder(TeamSelection.this).create();
        alertDialog.setView(promptview);
       final EditText  _temaname = (EditText)promptview.findViewById(R.id.team_name_dialog);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                teamstring=android.text.TextUtils.join(",", selectedPlayerListName);
                teamnamestring = _temaname.getText().toString();

                Toast.makeText(getApplicationContext(),_temaname.getText().toString()+"and",Toast.LENGTH_LONG).show();

                Intent i = new Intent(TeamSelection.this, LeaderBoard.class);

                sendTeamMethod();

                Bundle b = new Bundle();
               // b.putStringArrayList("list",selectedPlayerListName);
               // i.putExtra("list",b);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();



            }
        });

        alertDialog.show();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.3f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        alertDialog.getWindow().setAttributes(layoutParams);





        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

_temaname.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(TextUtils.isEmpty(s))
        {
            ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    .setEnabled(false);

        }
        else
        {
            ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    .setEnabled(true);
        }

    }
});

    }

    public void onSuperBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    public void onBackPressed() {


        final AlertDialog alertDialog = new AlertDialog.Builder(TeamSelection.this).create();
        alertDialog.setTitle("Warning :");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Selected team will not be saved. Still want to go back?");
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                TeamSelection.this.onSuperBackPressed();

            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}

package in.co.fantasyworldT.contests;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.adapters.CustomAdapterContests;
import in.co.fantasyworldT.models.ContestsModel;

public class ContestsAction extends AppCompatActivity {

    private ViewPager viewPager;
    Toolbar toolbar;
    String match_intent_id;
    static String match_intent_one ,match_intent_two;
    TextView teams,contests;
    String sTeams,sContests;
    private ProgressDialog dialog;
    List<ContestsModel> data_list = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    int matchIDint;
    CustomAdapterContests adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contests_layout1);


        //preparing dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);


        toolbar = (Toolbar)findViewById(R.id.contests_mToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        teams = (TextView) findViewById(R.id.teamsselectedtext);
        contests =(TextView) findViewById(R.id.contestsjoinedtext);
        recyclerView = (RecyclerView)findViewById(R.id.contestsrecyclerView);
        getContestsJoinedandTeamsJoined();









        Intent i = getIntent();

         match_intent_one = i.getStringExtra("team_intent_one");
        match_intent_two = i.getStringExtra("team_intent_two");
        match_intent_id= i.getStringExtra("match_intent_id");
        String match_intent_count= i.getStringExtra("match_intent_count");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String matchID = getMatchID();

         matchIDint = Integer.parseInt(matchID);
        getContestsData(matchIDint);
    setupRecyclerView(recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAllData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });






    }
    private void reloadAllData()
    {
        data_list.clear();
        getContestsData(matchIDint);


        setupRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupRecyclerView(RecyclerView recyclerView){

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new CustomAdapterContests(this,data_list);
        System.out.println("this is inside setiprecycler view and after adapter"+adapter.toString());
        recyclerView.setAdapter(adapter);



    }
    public  String getMatchID()
    {
       return match_intent_id;
    }
    public static String getTeamOne()
    {
        return match_intent_one;
    }

    public static String getTeamTwo()
    {
        return match_intent_two;
    }







    public  void setValues(String[] a, String[] b)
    {

        System.out.println("This is teams and contests"+sTeams +""+sContests);

        teams.setText("Teams selected("+a[0]+")");
        contests.setText("Contests joined("+b[0]+")");

    }
    public void getContestsJoinedandTeamsJoined()
    {

        final String matchId = getMatchID();
        final String[] te1 = new String[1];
        final String[] te2 = new String[1];

        // final int m_id = Integer.parseInt(matchId);

        AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                setValues(te1,te2);
            }

            @Override
            protected Void doInBackground(Void... params) {


                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

                //// TODO: 6/25/2017 add the URL

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://10.0.2.2/TEST/Latest/contestsandteamsJoined.php?mid="+matchId)
                        .build();

                try
                {
                    okhttp3.Response response = client.newCall(request).execute();

                  //  System.out.println("this is teams and contests"+response.body().string());
                   JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject temp = jsonObject.getJSONObject("item");
                    te1[0] = temp.getString("teams");
                    te2[0] = temp.getString("contests");

                    System.out.println("This is steams inside back"+te1[0]);
                    System.out.println("This is steams inside back"+te2[0]);





                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }





                return null;
            }


        };
        task.execute();


    }
    public void getContestsData(final int i)
    {

        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void,Void,Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();
            }



            @Override
            protected Void doInBackground(Void... params) {


                String urlq =  "httP://10.0.2.2/TEST/Latest/contests.php?id=";
                String urlpre = "http://www.fantasyworld.co.in/fwpreprod/contests.php?id=";

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(urlq+i)
                        .build();

                try
                {
                    Response response = client.newCall(request).execute();
                    // System.out.println("this is response body"+response.body().string());

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("contests");
                    for(int i=0 ; i<jsonArray.length();i++)
                    {

                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        System.out.println("this is inside for loop"+tempObject.toString());
                        ContestsModel mydata = new ContestsModel();
                        mydata.setValue(tempObject.getString("value"));
                        mydata.setTotalUsersLimit(tempObject.getString("total_spots"));
                        mydata.setEntryfee(tempObject.getString("entryfee"));
                        System.out.println("size of the mydata" +data_list.size());

                        data_list.add(mydata);
                        System.out.println("size of the mydata" +data_list.size());

                    }
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.cancel();
            }



        };
        asyncTask.execute();
    }




}

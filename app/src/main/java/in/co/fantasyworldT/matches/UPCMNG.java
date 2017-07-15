package in.co.fantasyworldT.matches;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.contests.ContestsAction;
import in.co.fantasyworldT.models.MatchesDataModel;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class UPCMNG extends Fragment {
    RecyclerView recyclerView;
    List<MatchesDataModel> data_list = new ArrayList<>();
    CustomAdapter adapter;
    private ProgressDialog dialog;



    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
        return false;
    }

    @Override
    public void onStart() {
        if(isConnected()) {

            //preparing dialog
            dialog = new ProgressDialog(getActivity());
            dialog.setIndeterminate(true);
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);
            loadDataFromServer(0);
            Toast.makeText(getActivity().getApplicationContext(),"you are  connected",Toast.LENGTH_LONG).show();
            super.onStart();
        }
        else
        {

            Toast.makeText(getActivity().getApplicationContext(),"you are not connected",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefreshlayoutmatches);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAllData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });



        return rootView;
    }

    private void  reloadAllData()
    {
        data_list.clear();
       loadDataFromServer(1);


        setupRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupRecyclerView(RecyclerView recyclerView){

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new CustomAdapter(getActivity(),data_list);
        recyclerView.setAdapter(adapter);


    }


    private void loadDataFromServer(final int id)

    {

        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();

            }


            @Override
            protected Void doInBackground(Void... voids) {

                //httP://10.0.2.2/TEST/Latest/loadmatches.php   url for local test
                //http://www.fantasyworld.co.in/fwpreprod/loadmatches.php

                OkHttpClient client = new OkHttpClient();
                System.out.println("connection trying");
                Request request = new Request.Builder()
                        .url("httP://10.0.2.2/TEST/Latest/loadmatches.php")
                        .build();
                System.out.println("connection success");

                try
                {
                    Response response = client.newCall(request).execute();

                    System.out.println("response is "+response);

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    System.out.println("this is JSON OBJECT" + jsonObject);

                    JSONArray jsonArray = jsonObject.getJSONArray("matches");

                    System.out.println("this is JSON array"+jsonArray);

                    for(int i=0 ; i<jsonArray.length();i++)
                    {

                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        MatchesDataModel model = new MatchesDataModel();
                        model.setMatchid(tempObject.getString("match_id"));
                        model.setMatch_title(tempObject.getString("leaguename"));
                        model.setTeam_one(tempObject.getString("team_one"));
                        model.setTeam_two(tempObject.getString("team_two"));
                        model.setTeam_one_icon(tempObject.getString("team_one_image"));
                        model.setTeam_two_icon(tempObject.getString("team_two_image"));
                        model.setCountdown(tempObject.getString("start_time_ms"));

                        data_list.add(model);

                    }

                }
                catch (IOException e)
                {
System.out.println("this is IO Exception" +e.toString());

                }
                catch (JSONException e)
                {
                    System.out.println("this is JSON Exception" +e.toString());
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();

                dialog.cancel();
            }

        };

        task.execute();


    }

    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
        Context context;
        List<MatchesDataModel> my_data;
        public CustomAdapter(Context context,List<MatchesDataModel> my_data) {

            this.context = context;
            this.my_data = my_data;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_item,parent,false);

            return new ViewHolder(itemView);

        }
        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView teamOne;
            public TextView teamTwo;
            public TextView versus;
            public ImageView teamOneImage;
            public  ImageView teamTwoImage;

            public TextView countDowntime;
            public TextView league;

            public CardView cardView;
            public TextView match;
            long ln;

            public ViewHolder(View itemView) {
                super(itemView);
                this.league = (TextView)itemView.findViewById(R.id.leaguename);
                this.teamOne = (TextView)itemView.findViewById(R.id.team1);
                this.teamTwo = (TextView)itemView.findViewById(R.id.team2);
                this.teamOneImage = (ImageView)itemView.findViewById(R.id.team1_image);
                this.teamTwoImage = (ImageView)itemView.findViewById(R.id.team2_image);
                this.cardView = (CardView) itemView.findViewById(R.id.cardlist_item);
                this.countDowntime = (TextView)itemView.findViewById(R.id.count_time);
                this.match = (TextView)itemView.findViewById(R.id.matchid);

            }


        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            Glide.with(context).load(my_data.get(position).getTeam_one_icon()).into(holder.teamOneImage);
            holder.teamOne.setText(my_data.get(position).getTeam_one());
            holder.league.setText(my_data.get(position).getMatch_title());
            holder.match.setText(my_data.get(position).getMatchid());
            holder.teamTwo.setText(my_data.get(position).getTeam_two());
            Glide.with(context).load(my_data.get(position).getTeam_two_icon()).into(holder.teamTwoImage);



            String numberAsString = my_data.get(position).getCountdown();

            holder.countDowntime.setText(numberAsString);

           /* long number = Long.parseLong(numberAsString);
            CountDownTimer ct = new CountDownTimer(number,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long l = (millisUntilFinished/1000)/60;
                   // holder.countDowntime.setText(""+l);
                }

                @Override
                public void onFinish() {

                }
            }.start();*/

            holder.cardView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(),ContestsAction.class);
                    intent.putExtra("team_intent_one",holder.teamOne.getText());
                    intent.putExtra("team_intent_two",holder.teamTwo.getText());
                    intent.putExtra("match_intent_id",holder.match.getText());
                    intent.putExtra("match_intent_count",holder.countDowntime.getText());

                    view.getContext().startActivity(intent);

                   // Toast.makeText(view.getContext(),"pressed"+holder.match.getText().toString(),Toast.LENGTH_LONG).show();
                }
            }

            );


        }



        @Override
        public int getItemCount() {
            return my_data.size();
        }





    }
}

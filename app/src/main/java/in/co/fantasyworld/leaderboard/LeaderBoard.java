package in.co.fantasyworld.leaderboard;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworld.R;
import in.co.fantasyworld.models.LeaderBoardModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by C5245675 on 5/25/2017.
 */

public class LeaderBoard extends AppCompatActivity {


    List<LeaderBoardModel> leaderBoardModelList = new ArrayList<>();
    CustomAdapterDash adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_header);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbarLeaderboard);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewLeaderBoard);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loadboardDetails();
        setupRecyclerView(recyclerView);






    }

    private void setupRecyclerView(RecyclerView recyclerView){

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new CustomAdapterDash(getApplicationContext(),leaderBoardModelList);
        recyclerView.setAdapter(adapter);


    }

    public void loadboardDetails()
    {

        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try{
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://10.0.2.2/TEST/Latest/leaderboard.php")
                            .build();

                    Response response = client.newCall(request).execute();

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("board");

                    for(int i=0 ; i<jsonArray.length();i++)
                    {

                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        LeaderBoardModel leaderBoardModel = new LeaderBoardModel();

                        leaderBoardModel.setName(tempObject.getString("name"));
                        leaderBoardModel.setPoints(tempObject.getString("points"));
                        leaderBoardModel.setRank(tempObject.getString("n"));

                        leaderBoardModelList.add(leaderBoardModel);




                    }


                }
                catch (Exception e)
                {
                     e.printStackTrace();
                }



                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };
        task.execute();

    }

    public static class CustomAdapterDash extends RecyclerView.Adapter<LeaderBoard.CustomAdapterDash.ViewHolder>
    {
        Context context;



        List<LeaderBoardModel> my_data;
        public CustomAdapterDash(Context context,List<LeaderBoardModel> my_data) {

            this.context = context;
            this.my_data = my_data;

        }

        @Override
        public LeaderBoard.CustomAdapterDash.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_card,parent,false);

            return new LeaderBoard.CustomAdapterDash.ViewHolder(itemView);

        }
        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView user_rank;
            public TextView user_team;
            public TextView user_points;



            public ViewHolder(View itemView) {
                super(itemView);
                //this.teamnameDash = (TextView)itemView.findViewById(R.id.team_name_dash);

                this.user_rank = (TextView)itemView.findViewById(R.id.rank_number);
                this.user_team = (TextView) itemView.findViewById(R.id.teamname_leaderboard_card);
                this.user_points = (TextView) itemView.findViewById(R.id.leaderboard_points);



            }


        }



        @Override
        public void onBindViewHolder(final LeaderBoard.CustomAdapterDash.ViewHolder holder, int position) {





          //  holder.teamnameDash.setText("my team 1");

            holder.user_rank.setText(my_data.get(position).getRank());
            holder.user_team.setText(my_data.get(position).getName());
            holder.user_points.setText(my_data.get(position).getPoints());





        }



        @Override
        public int getItemCount() {

            System.out.println("the size of the items in leaderboard"+my_data.size());
            return my_data.size();
        }





    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}

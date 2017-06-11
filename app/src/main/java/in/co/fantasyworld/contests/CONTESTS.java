package in.co.fantasyworld.contests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicationtest.vbr.designtest4.R;
import com.applicationtest.vbr.designtest4.com.vbr.model.ContestsM;
import com.applicationtest.vbr.designtest4.com.vbr.teamselection.TeamSelection2;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CONTESTS extends Fragment {

    RecyclerView recyclerView;
    List<ContestsM> data_list = new ArrayList<>();
    CustomAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.contests_layout, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.contestsrecyclerView);
        setupRecyclerView(recyclerView);

getContestsData();
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefreshlayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAllData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });



        return rootView;
    }

    private void reloadAllData()
    {
        data_list.clear();
        getContestsData();


        setupRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private void setupRecyclerView(RecyclerView recyclerView){

        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        adapter = new CONTESTS.CustomAdapter(getActivity(),data_list);
        recyclerView.setAdapter(adapter);



    }

    public void getContestsData()
    {

        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {



                OkHttpClient client = new OkHttpClient();
                System.out.println("connection trying");
                Request request = new Request.Builder()
                        .url("httP://10.0.2.2/TEST/Latest/contests.php")
                        .build();

                try
                {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("contests");
                    for(int i=0 ; i<jsonArray.length();i++)
                    {

                        JSONObject tempObject = jsonArray.getJSONObject(i);
                       ContestsM mydata = new ContestsM();
                        mydata.setValue(tempObject.getString("value"));
                        mydata.setTotalUsersLimit(tempObject.getString("total_spots"));

                        data_list.add(mydata);

                    }
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
                return null;
            }



        };
        asyncTask.execute();
    }

    public static class CustomAdapter extends RecyclerView.Adapter<CONTESTS.CustomAdapter.ViewHolder>
    {
        Context context;
        List<ContestsM> my_data;

        public CustomAdapter(Context context,List<ContestsM> my_data) {

            this.context = context;
            this.my_data = my_data;

        }

        @Override
        public CONTESTS.CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contests_card2,parent,false);

            return new ViewHolder(itemView);

        }
        public static class ViewHolder extends RecyclerView.ViewHolder
        {


            public CardView cardView;

            public TextView percentage;
            public DonutProgress donutProgress;
            public TextView totalValue;
            public TextView spotsValue;





            public ViewHolder(View itemView) {
                super(itemView);


                this.donutProgress = (DonutProgress)itemView.findViewById(R.id.prg_bar);
                this.totalValue = (TextView) itemView.findViewById(R.id.total_value);
                this.spotsValue = (TextView)itemView.findViewById(R.id.total_spots_value);
                this.cardView = (CardView)itemView.findViewById(R.id.contests_card);






            }



        }



        @Override
        public void onBindViewHolder(final CONTESTS.CustomAdapter.ViewHolder holder, int position) {



           // holder.teamTwo.setText(my_data.get(position).getTeam_two());

           // holder.percentage.setText("90");

           int dummy = 1000;
            int i = 0;
            holder.donutProgress.setProgress(4);
            holder.donutProgress.setMax(100);
            while ( i < dummy)
            {
                holder.donutProgress.setProgress(i);
                i++;
            }




            holder.spotsValue.setText(my_data.get(position).getTotalUsersLimit());
            holder.totalValue.setText(my_data.get(position).getValue());


            holder.cardView.setOnClickListener(new View.OnClickListener()
                                               {

                                                   @Override
                                                   public void onClick(View view) {

                                                       Intent intent = new Intent(view.getContext(), TeamSelection2.class);
                                                      // intent.putExtra("team_intent_one",holder.teamOne.getText());
                                                       //intent.putExtra("team_intent_two",holder.teamTwo.getText());

                                                       view.getContext().startActivity(intent);

                                                       // Toast.makeText(view.getContext(),"pressed"+holder.teamOne.getText(),Toast.LENGTH_LONG).show();
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

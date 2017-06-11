package in.co.fantasyworld.matches;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import in.co.fantasyworld.R;
import in.co.fantasyworld.contests.ContestsAction;
import in.co.fantasyworld.models.MatchesDataModel;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class UPCMNG extends Fragment {
    RecyclerView recyclerView;
    List<MatchesDataModel> data_list = new ArrayList<>();
    CustomAdapter adapter;



    RecyclerView.LayoutManager layoutManager;


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



        return rootView;
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
            protected Void doInBackground(Void... voids) {

                OkHttpClient client = new OkHttpClient();
                System.out.println("connection trying");
                Request request = new Request.Builder()
                        .url("httP://10.0.2.2/TEST/Latest?id="+id)
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
                        MatchesDataModel mydata = new MatchesDataModel(tempObject.getString("match_title"),
                                tempObject.getString("team_one"),tempObject.getString("team_two"),tempObject.getString("team_one_icon"),tempObject.getString("team_one_icon"));
                        data_list.add(mydata);

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
            public CardView cardView;


            public ViewHolder(View itemView) {
                super(itemView);
                this.teamOne = (TextView)itemView.findViewById(R.id.team1);
                this.teamTwo = (TextView)itemView.findViewById(R.id.team2);
                this.teamOneImage = (ImageView)itemView.findViewById(R.id.team1_image);
                this.teamTwoImage = (ImageView)itemView.findViewById(R.id.team2_image);
                this.cardView = (CardView) itemView.findViewById(R.id.cardlist_item);

              /*  cardView.setOnClickListener(new
                                                    View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Toast.makeText(view.getContext(),"pressed",Toast.LENGTH_LONG).show();
                                                        }
                                                    });*/
            }


        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            Glide.with(context).load(my_data.get(position).getTeam_one_icon()).into(holder.teamOneImage);

            holder.teamOne.setText(my_data.get(position).getTeam_one());

            holder.teamTwo.setText(my_data.get(position).getTeam_two());
            Glide.with(context).load(my_data.get(position).getTeam_two_icon()).into(holder.teamTwoImage);

            holder.cardView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(),ContestsAction.class);
                    intent.putExtra("team_intent_one",holder.teamOne.getText());
                    intent.putExtra("team_intent_two",holder.teamTwo.getText());

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

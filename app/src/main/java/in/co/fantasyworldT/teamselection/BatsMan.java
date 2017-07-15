package in.co.fantasyworldT.teamselection;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.models.BatsmanModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class BatsMan extends Fragment {

    RecyclerView recyclerView;
    List<BatsmanModel> batsman_data_list = new ArrayList<>();
    CustomAdapter adapter;
    TeamSelection teamSelection2;
    private ProgressDialog dialog;


    @Override
    public void onStart() {

        //preparing dialog

        super.onStart();


        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        loadbatsman();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.batsman_layout, container, false);
        //prgbar = (ProgressBar)rootView.findViewById(R.id.prgbarteamselection);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.batsman_recyclerview);
        setupRecyclerView(recyclerView);

        return rootView;
    }
    private void setupRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        adapter = new CustomAdapter(getActivity(),batsman_data_list);
        System.out.println("method inside setup recyclerview");

        //String one = ((ContestsAction)getActivity()).getTeamOne();
        //System.out.println("this is team passed"+one);

        recyclerView.setAdapter(adapter);

    }




    public static class CustomAdapter extends RecyclerView.Adapter<BatsMan.CustomAdapter.ViewHolder>
    {
        Context context;
        List<BatsmanModel> my_data;

        public CustomAdapter(Context context,List<BatsmanModel> my_data) {

            this.context = context;
            this.my_data = my_data;

        }

        @Override
        public BatsMan.CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.teamselect2_card,parent,false);

            return new BatsMan.CustomAdapter.ViewHolder(itemView);

        }
        public static class ViewHolder extends RecyclerView.ViewHolder
        {



            public CardView cardView;
            public ImageView mimageView;
            public TextView mplayerId;
            public TextView mPlayerName;
            public ViewHolder(View itemView) {
                super(itemView);
                this.cardView = (CardView)itemView.findViewById(R.id.teamselect2_card_view);
                this.mPlayerName = (TextView) itemView.findViewById(R.id.teamselection2_player_name);
                this.mimageView = (ImageView) itemView.findViewById(R.id.teamselection2_player_image);
                this.mplayerId = (TextView) itemView.findViewById(R.id.teamselection2_player_id);

            }



        }



        @Override
        public void onBindViewHolder(final BatsMan.CustomAdapter.ViewHolder holder, int position) {



            holder.mplayerId.setText(my_data.get(position).getPlayerID());
            holder.mPlayerName.setText(my_data.get(position).getPlayerName());
            Glide.with(context).load(my_data.get(position).getPlayerImage()).into(holder.mimageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                boolean isSelected = false;
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"item clicked",Toast.LENGTH_LONG);
                    String temp =  holder.mplayerId.getText().toString();
                    String name = holder.mPlayerName.getText().toString();

                    Toast.makeText(v.getContext(),""+temp,Toast.LENGTH_LONG).show();

                    int player = Integer.parseInt(temp);
                    if(!isSelected)

                    {
                        holder.cardView.setCardBackgroundColor(Color.BLACK);
                        isSelected = true;
                        ((TeamSelection)context).updateSelectedPlayers(player,name,true);

                    }
                    else
                    {
                        holder.cardView.setCardBackgroundColor(Color.WHITE);
                        isSelected = false;
                        ((TeamSelection)context).updateSelectedPlayers(player,name,false);

                    }







                }
            });

        }


        @Override
        public int getItemCount() {

            System.out.println("This is batsman size"+my_data.size());
            return my_data.size();

        }

    }


    private void loadbatsman()


    {

        final String var = ((TeamSelection)getActivity()).getOne();
        final String var2 = ((TeamSelection)getActivity()).getTwo();
        final String var3 = "bat";
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>() {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
               // prgbar.setVisibility(View.VISIBLE);
                dialog.show();
            }



            @Override
            protected Void doInBackground(Void... params) {

                try
                {

                    String URLq = "\"http://10.0.2.2/TEST/Latest/getBatsman.php?var=\"+var+\"&var2=\"+var2+\"&var3=\"+var3";
                    String URLpre = "http://www.fantasyworld.co.in/fwpreprod/getBatsman.php?var=\"+var+\"&var2=\"+var2+\"&var3=\"+var3";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.fantasyworld.co.in/fwpreprod/getBatsman.php?+var="+var+"&var2="+var2+"&var3="+var3)
                            .build();

                    Response response = client.newCall(request).execute();
                   // System.out.println("this is batsman response"+response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("batsman");
                    for(int i=0 ; i<jsonArray.length();i++)
                    {

                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        BatsmanModel mydata = new BatsmanModel();
                        mydata.setPlayerID(tempObject.getString("player_id"));
                        mydata.setPlayerName(tempObject.getString("player_name"));
                        mydata.setPlayerImage(tempObject.getString("player_image"));


                        batsman_data_list.add(mydata);

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
               // prgbar.setVisibility(View.INVISIBLE);
                dialog.cancel();
            }
        } ;
        task.execute();
    }
}

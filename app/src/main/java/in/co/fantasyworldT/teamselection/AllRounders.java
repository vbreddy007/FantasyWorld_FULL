package in.co.fantasyworldT.teamselection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.models.BowlersModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class AllRounders extends Fragment {

    RecyclerView recyclerView;
    List<BowlersModel> bowlers_data_list = new ArrayList<>();
    Bowlers.CustomAdapter adapter;
    TeamSelection teamSelection2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.batsman_layout, container, false);
        loadbowlers();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.batsman_recyclerview);
        setupRecyclerView(recyclerView);

        return rootView;
    }
    private void setupRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));

        adapter = new Bowlers.CustomAdapter(getActivity(),bowlers_data_list);

        System.out.println("method inside setup recyclerview");

        recyclerView.setAdapter(adapter);

    }





    private void loadbowlers()

    {
        final String var = ((TeamSelection)getActivity()).getOne();
        final String var2 = ((TeamSelection)getActivity()).getTwo();
        final String var3 = "bow";
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                try
                {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2/TEST/Latest/getBatsman.php?var="+var+"&var2="+var2+"&var3="+var3)
                            .build();

                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("batsman");
                    for(int i=0 ; i<jsonArray.length();i++)
                    {

                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        BowlersModel mydata = new BowlersModel();
                        mydata.setPlayerID(tempObject.getString("player_id"));
                        mydata.setPlayerName(tempObject.getString("player_name"));
                        mydata.setPlayerImage(tempObject.getString("player_image"));


                        bowlers_data_list.add(mydata);

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
        } ;
        task.execute();
    }

}

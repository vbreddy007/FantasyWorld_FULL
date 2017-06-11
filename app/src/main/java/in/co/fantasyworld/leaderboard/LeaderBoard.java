package in.co.fantasyworld.leaderboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicationtest.vbr.designtest4.R;

import java.util.List;

/**
 * Created by C5245675 on 5/25/2017.
 */

public class LeaderBoard extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_header);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbarLeaderboard);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public static class CustomAdapterDash extends RecyclerView.Adapter<LeaderBoard.CustomAdapterDash.ViewHolder>
    {
        Context context;



        List<LeaderboardData> my_data;
        public CustomAdapterDash(Context context,List<LeaderboardData> my_data) {

            this.context = context;
            this.my_data = my_data;

        }

        @Override
        public LeaderBoard.CustomAdapterDash.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_item,parent,false);

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

            holder.user_rank.setText("");
            holder.user_team.setText("");
            holder.user_points.setText("");





        }



        @Override
        public int getItemCount() {
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

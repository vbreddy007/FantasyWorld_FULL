package in.co.fantasyworldT.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.contests.CONTESTS;
import in.co.fantasyworldT.models.ContestsModel;
import in.co.fantasyworldT.teamselection.TeamSelection;

/**
 * Created by C5245675 on 6/30/2017.
 */

public class CustomAdapterContests extends RecyclerView.Adapter<CustomAdapterContests.ViewHolder> {


    Context context;
    List<ContestsModel> my_data;

    public CustomAdapterContests(Context context,List<ContestsModel> my_data) {

        this.context = context;
        this.my_data = my_data;
        System.out.println("this is inside custructor and size of my data"+my_data.size());

    }

    @Override
    public CustomAdapterContests.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contests_card3,parent,false);

        System.out.println("this is inside son create view holder");

        return new CustomAdapterContests.ViewHolder(itemView);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {


        public CardView cardView;

        public TextView percentage;
        public DonutProgress donutProgress;
        public TextView totalValue;
        public TextView spotsValue,mEntryFee;





        public ViewHolder(View itemView) {
            super(itemView);


            this.donutProgress = (DonutProgress)itemView.findViewById(R.id.prg_bar);
            this.totalValue = (TextView) itemView.findViewById(R.id.total_value);
            this.spotsValue = (TextView)itemView.findViewById(R.id.total_spots_value);
            this.mEntryFee  = (TextView)itemView.findViewById(R.id.entryfeevalue);
            this.cardView = (CardView)itemView.findViewById(R.id.contests_card);


            System.out.println("this is inside sView holder");



        }



    }



    @Override
    public void onBindViewHolder(final CustomAdapterContests.ViewHolder holder, int position) {



        // holder.teamTwo.setText(my_data.get(position).getTeam_two());

        // holder.percentage.setText("90");


       /* int dummy = 1000;
        int i = 0;
        holder.donutProgress.setProgress(4);
        holder.donutProgress.setMax(100);
        while ( i < dummy)
        {
            holder.donutProgress.setProgress(i);
            i++;
        }*/




        holder.spotsValue.setText(my_data.get(position).getTotalUsersLimit());
        holder.totalValue.setText(my_data.get(position).getValue());
        holder.mEntryFee.setText(my_data.get(position).getEntryfee());

        System.out.println("this is inside on bindview holder");


        holder.cardView.setOnClickListener(new View.OnClickListener()
                                           {

                                               @Override
                                               public void onClick(View view) {

                                                   Intent intent = new Intent(view.getContext(), TeamSelection.class);
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

        System.out.println("this is size of the contests"+my_data.size());
        return my_data.size();


    }





}


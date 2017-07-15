package in.co.fantasyworldT.teamselection;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.models.BowlersModel;




public  class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
{
    Context context;
    List<BowlersModel> my_data;

    public CustomAdapter(Context context,List<BowlersModel> my_data) {

        this.context = context;
        this.my_data = my_data;

    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.teamselect2_card,parent,false);

        return new CustomAdapter.ViewHolder(itemView);

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
    public void onBindViewHolder(final CustomAdapter.ViewHolder holder, int position) {



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
                    ((TeamSelection)context).updateSelectedPlayers(player,name,true);

                }







            }
        });

    }


    @Override
    public int getItemCount() {

        System.out.println("this is size"+my_data.size());
        return my_data.size();

    }

}
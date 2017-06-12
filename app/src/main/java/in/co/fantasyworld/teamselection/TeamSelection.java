package in.co.fantasyworld.teamselection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworld.R;
import in.co.fantasyworld.contests.ContestsAction;
import in.co.fantasyworld.payments.Payment;

/**
 * Created by C5245675 on 6/3/2017.
 *
 */

public class TeamSelection extends AppCompatActivity {

    private ViewPager viewPager;
    TabLayout tabLayout;
    ImageView topimageView;
    ArrayList selectedplayerlist = new ArrayList();
    ArrayList<String> selectedPlayerListName = new ArrayList<>();
    TextView sel_count;
    int count = 0;
    Button save_team;
    String teamnameG;
    String teamOne;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teamselection);

        viewPager = (ViewPager)findViewById(R.id.viewpager_teamselection2);
        setupViewPager(viewPager);

        topimageView = (ImageView) findViewById(R.id.top_image_teamselection2);
        sel_count = (TextView) findViewById(R.id.teamselection2_team_count);
        save_team = (Button)findViewById(R.id.team_save);
        save_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamnameDialog();
            }
        });

        teamOne = ContestsAction.getTeamOne();

        System.out.println("this is in team selection team one"+teamOne);





        final TextView select_hint = (TextView) findViewById(R.id.selction_hint);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout_teamselection2);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0) {
                    topimageView.setImageResource(R.drawable.splashbackground);
                    select_hint.setText("Please select 1 Wicket Keeper");
                }
                else if(tab.getPosition() == 1)
                {
                    topimageView.setImageResource(R.drawable.ic_launcher);
                    select_hint.setText("Please select 3 to 5 batsman");

                }
                else if (tab.getPosition() == 2)
                {
                    topimageView.setImageResource(R.drawable.splashbackground);
                    select_hint.setText("Please select 2 to 5 bowlers");
                }
                else if (tab.getPosition() == 3)
                {

                    select_hint.setText("please select 2 to 4 all rounders");
                }


                Toast.makeText(getApplicationContext(),""+tab.getPosition(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public String getOne()
    {
        return teamOne;
    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new WicketKeepers(), "WK");
        adapter.addFrag(new BatsMan(), "BATSMAN");
        adapter.addFrag(new Bowlers(),"BOWLERS");
        adapter.addFrag(new AllRounders(),"ALL ROUNDERS");


        viewPager.setAdapter(adapter);

    }

    public void updateSelectedPlayers(int playerId ,String playerName, boolean isAdd)
    {

        if(isAdd)
        {
            if(selectedplayerlist.size() != 0) {

                if (!selectedplayerlist.contains(playerId)) {
                    selectedplayerlist.add(playerId);
                    selectedPlayerListName.add(playerName);
                    System.out.println("this is size"+selectedplayerlist.size() +""+selectedPlayerListName.toString());
                    updatecouter(true);
                }
            }
            else {
                selectedplayerlist.add(playerId);
                selectedPlayerListName.add(playerName);
                selectedplayerlist.size();
                System.out.println("this is size"+selectedplayerlist.size() +""+selectedPlayerListName.toString());
                updatecouter(true);
            }
        }
        else
        {
            if(selectedplayerlist.size() != 0)
            {
                if (selectedplayerlist.contains(playerId))
                {
                    selectedplayerlist.remove(new Integer(playerId));
                    selectedPlayerListName.remove(playerName);
                    selectedplayerlist.size();
                    System.out.println("this is size"+selectedplayerlist.size() +""+selectedPlayerListName.toString());
                    updatecouter(false);

                }

            }

        }

       System.out.println("this is inside activity");
    }
    public void updatecouter(boolean isAdded)
    {

        System.out.println("this is inside counter");
        if(isAdded)
        {
            System.out.println("inside true condition");
            count = count +1;
            sel_count.setText(count+"/11");
        }
        else if(!isAdded)
        {
            System.out.println("inside false condition");
            count = count -1;
            sel_count.setText(count+"/11");
        }

    }

    public void teamnameDialog()
    {

        LayoutInflater li = LayoutInflater.from(this);
        final View promptview = li.inflate(R.layout.teamname_alertdialogview,null);

        final AlertDialog alertDialog = new AlertDialog.Builder(TeamSelection.this).create();
        alertDialog.setView(promptview);
       final EditText  _temaname = (EditText)promptview.findViewById(R.id.team_name_dialog);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Toast.makeText(getApplicationContext(),_temaname.getText().toString(),Toast.LENGTH_LONG).show();

                Intent i = new Intent(TeamSelection.this, Payment.class);

                Bundle b = new Bundle();
                b.putStringArrayList("list",selectedPlayerListName);
                i.putExtra("list",b);
                startActivity(i);


            }
        });

        alertDialog.show();

        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

_temaname.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(TextUtils.isEmpty(s))
        {
            ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    .setEnabled(false);

        }
        else
        {
            ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    .setEnabled(true);
        }

    }
});

    }



    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}

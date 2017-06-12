package in.co.fantasyworld.contests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworld.R;

/**
 * Created by C5245675 on 5/30/2017.
 */

public class ContestsAction extends AppCompatActivity {

    private ViewPager viewPager;
    Toolbar toolbar;
    String match_intent_id;
    static String match_intent_one;
   String  match_intent_two;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contests_main);


         toolbar = (Toolbar)findViewById(R.id.contests_mToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        Intent i = getIntent();

         match_intent_one = i.getStringExtra("team_intent_one");
        match_intent_two = i.getStringExtra("team_intent_two");
        match_intent_id= i.getStringExtra("match_intent_id");
        String match_intent_count= i.getStringExtra("match_intent_count");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPager = (ViewPager)findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public String getMatchID()
    {
       return match_intent_id;
    }
    public static String getTeamOne()
    {
        return match_intent_one;
    }
    public String getTeamTwo()
    {
        return match_intent_two;
    }

    private void setupViewPager(ViewPager viewPager){
        ContestsAction.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new CONTESTS(), "CONTESTS");
        adapter.addFrag(new PREDICTIONS(), "PREDICTIONS");


        viewPager.setAdapter(adapter);

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

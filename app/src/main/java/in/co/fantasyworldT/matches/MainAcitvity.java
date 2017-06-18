package in.co.fantasyworldT.matches;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import in.co.fantasyworldT.R;
import in.co.fantasyworldT.adapters.CustomAdapterViewFlipper;
import in.co.fantasyworldT.extra.PointsActivity;
import in.co.fantasyworldT.login.LoginAction;
import in.co.fantasyworldT.payments.WalletActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainAcitvity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private AdapterViewFlipper simpleAdapterViewFlipper;

    int[] fruitImages = {R.drawable.download, R.drawable.download1};     // array of images
    String fruitNames[] = {"Apple", "Pine Apple"};
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public boolean doubleBackToExitPressedOnce = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView userName;
    TextView emailDisplay;
    FirebaseUser firebaseUser;
    NavigationView navView;
    String uName;
    String uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DesignDemo);
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acitvity);





        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()==null)
                {


                    Intent i  = new Intent(MainAcitvity.this , LoginAction.class);


                    startActivity(i);

                }
            }
        };
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        navView = (NavigationView) findViewById(R.id.navigation_view);

        View drawerHeader = navView.getHeaderView(0);


        userName = (TextView)drawerHeader.findViewById(R.id.UserNameDisplay);
        emailDisplay = (TextView) drawerHeader.findViewById(R.id.emailDisplay);
        if(firebaseUser != null)
        {
            uName= firebaseUser.getDisplayName();
            uEmail = firebaseUser.getEmail();
            Uri photoUrl = firebaseUser.getPhotoUrl();
            if(userName != null)
                userName.setText(uName);
            if(emailDisplay!=null)
                emailDisplay.setText(uEmail);
            sendUserDetails();

            System.out.println("This is photo URL "+photoUrl +"and "+userName+" uName"+uName);

        }






        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);



        if (navView != null){
            setupDrawerContent(navView);
        }

        viewPager = (ViewPager)findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }


        simpleAdapterViewFlipper = (AdapterViewFlipper)findViewById(R.id.simpleAdapterViewFlipper);
        CustomAdapterViewFlipper customAdapter = new CustomAdapterViewFlipper(getApplicationContext(), fruitNames, fruitImages);

        simpleAdapterViewFlipper.setAdapter(customAdapter);
        simpleAdapterViewFlipper.setFlipInterval(3000);
        simpleAdapterViewFlipper.setAutoStart(true);


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

    void sendUserDetails()
    {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try
                {


                    OkHttpClient client = new OkHttpClient();
                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("useremail",uEmail);
                    RequestBody body = formBuilder.build();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2/TEST/Latest/userupdateinDB.php")
                            .post(body)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String responseS = response.body().string();
                    Log.i("@", "" + responseS);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }


        };
        task.execute();

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // adapter.addFrag(new FloatingLabelsFragment(), "Floating Labels");
        // adapter.addFrag(new FABLayoutFragment(), "FAB");
        adapter.addFrag(new UPCMNG(), "UPCOMING");
        adapter.addFrag(new LIVE(), "LIVE");

        adapter.addFrag(new RESULTS(),"RESULTS");
        viewPager.setAdapter(adapter);

    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    // case R.id.drawer_labels:
                    //   viewPager.setCurrentItem(0);
                    // break;
                    // case R.id.drawer_fab:
                    //   viewPager.setCurrentItem(1);
                    // break;
                    case R.id.drawer_snackbar:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.drawer_coordinator:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.drawer_share:
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Here is the share content body";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                    case R.id.wallet:
                        Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                        startActivity(walletIntent);

                        break;
                    case R.id.pointingsystem:
                        Intent in  = new Intent(getApplicationContext(),PointsActivity.class);
                        startActivity(in);
                        break;

                    case R.id.logout:
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();

                        break;



                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();




    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.addAuthStateListener(mAuthListener);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id){
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

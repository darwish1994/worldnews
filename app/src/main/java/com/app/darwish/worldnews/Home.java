package com.app.darwish.worldnews;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.app.darwish.worldnews.data.SourceItem;


public class Home  extends AppCompatActivity implements HomeFragment.Callback   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(SourceItem news_Channel) {
        //Toast.makeText(this ,"its work" ,Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putParcelable("JsonObject", news_Channel);
        Fragment detailFragment = new TheNews();
        detailFragment.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.replaceFragment, detailFragment).addToBackStack("home").commit();

    }
}

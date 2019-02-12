package com.chubbymobile.wwh.hawk.landing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.chubbymobile.wwh.hawk.Adapters.ModelAdapter;
import com.chubbymobile.wwh.hawk.Bean.Vehicle;
import com.chubbymobile.wwh.hawk.Logon.LogQQActivity;
import com.chubbymobile.wwh.hawk.Logon.LoginActivity;
import com.chubbymobile.wwh.hawk.R;
import com.chubbymobile.wwh.hawk.services.BroadcastService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements PullToRefreshListener, ModelAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    private PullToRefreshRecyclerView recyclerView;
    private ModelAdapter adapter;
    int counter = 0;
    private List<Vehicle> vData;

    private Intent intent;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        initService();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Model");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // This method can be used when a query is submitted eg. creating search history using SQLite DB
        Toast.makeText(this, "Query Inserted", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText, vData);
        return true;
    }

    private void initService() {
        intent = new Intent(this, BroadcastService.class);
    }

    private void initView() {
        initVehicles();
        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);
        View headView = View.inflate(this, R.layout.layout_head_view, null);
        recyclerView.addHeaderView(headView);
        View footerView = View.inflate(this, R.layout.layout_foot_view, null);
        recyclerView.addFooterView(footerView);
        View emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setEmptyView(emptyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ModelAdapter(this, vData);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);

        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.displayLastRefreshTime(false);
        recyclerView.setPullToRefreshListener(this);
        //recyclerView.onRefresh();

        recyclerView.setRefreshingResource(R.drawable.volvologo);
        recyclerView.setLoadMoreResource(R.drawable.volvologo);
    }

    private void initVehicles() {
        vData = new ArrayList<Vehicle>();
        vData.add(new Vehicle(R.drawable.fe, "FE"));
        vData.add(new Vehicle(R.drawable.fh, "FH"));
        vData.add(new Vehicle(R.drawable.fm, "FM"));
        vData.add(new Vehicle(R.drawable.fmx, "FMX"));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            ImageView img = (ImageView) findViewById(R.id.vh_image);
            if (img != null) {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
    }

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        Log.d(TAG, counter);
        Log.d(TAG, time);

        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        if(txtDateTime != null && txtCounter!=null) {
            txtDateTime.setText(time);
            txtCounter.setText(counter);
        }
    }

    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshComplete();
                //data.clear(); //to simulate empty dataset
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                counter++;
                if(counter >= 4){
                    recyclerView.setLoadMoreFail();
                    return;
                }
                vData.add(new Vehicle(R.drawable.fe, "FE"+counter));
                vData.add(new Vehicle(R.drawable.fh, "FH"+counter));
                vData.add(new Vehicle(R.drawable.fm, "FM"+counter));
                vData.add(new Vehicle(R.drawable.fmx, "FMX"+counter));
                recyclerView.setLoadMoreComplete();
                adapter.notifyDataSetChanged();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setRefreshComplete();
        recyclerView.setLoadMoreComplete();
        recyclerView.loadMoreEnd();
    }

    @Override
    public void onItemClick(String str, int position) {
        Log.d(TAG, "--->" + str + position);
        Intent i;
        if( position %2 == 1 ){
            i = new Intent(MainActivity.this, LoginActivity.class);
        }
        else {
            i = new Intent(MainActivity.this, LogQQActivity.class);
        }
        startActivity(i);
    }
}

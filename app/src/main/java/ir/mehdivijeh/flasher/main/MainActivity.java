package ir.mehdivijeh.flasher.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.main.adapter.AdapterCollection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewCollection;
    private ItemAdapter mItemAdapter;
    private FastAdapter mFastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRecyclerView();

        mItemAdapter.add(new AdapterCollection("504 Essential Words" , 504 , 25 , getResources().getColor(R.color.colorBlue), adapterCollection -> {
            //System.out.println();
        }));

        mItemAdapter.add(new AdapterCollection("1024 Good words" , 1024 , 900 , getResources().getColor(R.color.colorGreen), adapterCollection -> {
           // System.out.println();
        }));

        mItemAdapter.add(new AdapterCollection("My words" , 10 , 10 , getResources().getColor(R.color.colorRed), adapterCollection -> {
           // System.out.println();
        }));


        mFastAdapter.notifyAdapterDataSetChanged();
    }

    private void initView() {
        mRecyclerViewCollection = findViewById(R.id.recycler_view_collection);
    }

    private void initRecyclerView(){
        mItemAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(mItemAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2 , StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewCollection.setLayoutManager(layoutManager);
        mRecyclerViewCollection.setAdapter(mFastAdapter);
    }
}

package ir.mehdivijeh.flasher.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.TextUtil;
import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.adapter.AdapterAddCollection;
import ir.mehdivijeh.flasher.main.adapter.AdapterCollection;
import ir.mehdivijeh.flasher.main.presenter.MainPresenterImpl;
import ir.mehdivijeh.flasher.main.repo.LocalCollectionRepo;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.word.WordActivity;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    private MainContract.MainPresenter mPresenter;
    private RecyclerView mRecyclerViewCollection;
    private ItemAdapter mItemAdapter;
    private FastAdapter mFastAdapter;
    private int[] mRainbowColors;
    private int mColorIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRecyclerView();
        initPresenter();
        mPresenter.loadCollectionFromDb();
    }

    private void initView() {
        mRecyclerViewCollection = findViewById(R.id.recycler_view_collection);
        mRainbowColors = getResources().getIntArray(R.array.rainbow);
        TextUtil.setFonts(getWindow().getDecorView());
    }

    private void initRecyclerView() {
        mItemAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(mItemAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewCollection.setLayoutManager(layoutManager);
        mRecyclerViewCollection.setAdapter(mFastAdapter);
    }

    private void initPresenter() {
        CollectionDao collectionDao = LocalDb.getInstance(this).collectionDao();
        LocalCollectionRepo localCollectionRepo = new LocalCollectionRepo(collectionDao);
        mPresenter = new MainPresenterImpl(this, localCollectionRepo);
    }


    @Override
    public void onCollectionLoaded(List<CollectionDb> collectionDbList) {
        if (mItemAdapter != null && mItemAdapter.getAdapterItemCount() > 0) {
            mItemAdapter.clear();
        }

        for (CollectionDb collectionDb : collectionDbList) {
            if (mColorIndex + 1 == mRainbowColors.length)
                mColorIndex = 0;

            mItemAdapter.add(new AdapterCollection(collectionDb.getName(), collectionDb.getSize(), collectionDb.getLearned(), mRainbowColors[++mColorIndex], adapterCollection -> {
                Intent intent = new Intent(MainActivity.this, WordActivity.class);
                intent.putExtra(GeneralConstants.COLLECTION_ID, collectionDb.getId());
                startActivity(intent);
            }));
        }


        mItemAdapter.add(new AdapterAddCollection(getResources().getColor(R.color.colorGray), adapterCollection -> {
        }));


        mFastAdapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void addCollectionSuccessfully(CollectionDb addedCollectionDb) {

    }

    @Override
    public void onDeleteSuccessfully(CollectionDb deletedCollectionDb) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null && mItemAdapter != null) {
            mColorIndex = 0;
            mPresenter.loadCollectionFromDb();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null)
            mPresenter.onDestroy();
    }
}

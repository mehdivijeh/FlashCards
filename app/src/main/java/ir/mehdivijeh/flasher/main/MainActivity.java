package ir.mehdivijeh.flasher.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;
import java.util.Objects;

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
            if (mColorIndex == mRainbowColors.length)
                mColorIndex = 0;

            mItemAdapter.add(new AdapterCollection(collectionDb.getName(), collectionDb.getSize(), collectionDb.getLearned(), mRainbowColors[mColorIndex++]
                    , adapterCollection -> {
                Intent intent = new Intent(MainActivity.this, WordActivity.class);
                intent.putExtra(GeneralConstants.COLLECTION_ID, collectionDb.getId());
                startActivity(intent);
            }, adapterCollection -> {

            }));
        }


        mItemAdapter.add(new AdapterAddCollection(getResources().getColor(R.color.colorGray), adapterCollection -> {
            showDialogCreatePack();
        }));


        mFastAdapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void addCollectionSuccessfully(CollectionDb addedCollectionDb) {

        //delete add new collection and then after add collection add it
        if (mItemAdapter != null && mItemAdapter.getAdapterItemCount() > 0) {
            mItemAdapter.remove(mItemAdapter.getAdapterItemCount() - 1);
        }

        if (mColorIndex == mRainbowColors.length)
            mColorIndex = 0;

        mItemAdapter.add(new AdapterCollection(addedCollectionDb.getName(), addedCollectionDb.getSize(), addedCollectionDb.getLearned(), mRainbowColors[mColorIndex++], adapterCollection -> {
            Intent intent = new Intent(MainActivity.this, WordActivity.class);
            intent.putExtra(GeneralConstants.COLLECTION_ID, addedCollectionDb.getId());
            startActivity(intent);
        }, adapterCollection -> {

        }));

        mItemAdapter.add(new AdapterAddCollection(getResources().getColor(R.color.colorGray), adapterCollection -> {
            showDialogCreatePack();
        }));

        mFastAdapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void onDeleteSuccessfully(CollectionDb deletedCollectionDb) {

    }

    private void showDialogCreatePack() {
        final Dialog mDialog = new Dialog(this);
        mDialog.setCancelable(true);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_create_collection);
        TextUtil.setFonts(mDialog.getWindow().getDecorView());
        mDialog.setCancelable(true);

        EditText edtName = mDialog.findViewById(R.id.edt_pack_name);
        Button btnCreate = mDialog.findViewById(R.id.btn_create);
        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);

        btnCreate.setOnClickListener(v -> {
            if (edtName.getText() != null && edtName.getText().toString().length() > 0) {
                mDialog.dismiss();
                CollectionDb collectionDb = new CollectionDb();
                collectionDb.setName(edtName.getText().toString());
                collectionDb.setLearned(0);
                collectionDb.setSize(0);
                mPresenter.addCollectionToDb(collectionDb);
            } else {
                edtName.setError(getString(R.string.no_input));
            }
        });

        btnCancel.setOnClickListener(v -> mDialog.dismiss());
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.WRAP_CONTENT);

        mDialog.show();
    }

    private void showDialogDeleteCollection(CollectionDb collectionDb) {
        final Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_delete_pack);
        TextUtil.setFonts(mDialog.getWindow().getDecorView());
        mDialog.setCancelable(true);

        Button btnDelete = mDialog.findViewById(R.id.btn_delete);
        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnDelete.setOnClickListener(v -> {
            mDialog.dismiss();
            mPresenter.deleteStickerPack(packId);
        });

        btnCancel.setOnClickListener(v -> mDialog.dismiss());
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.WRAP_CONTENT);

        mDialog.show();
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

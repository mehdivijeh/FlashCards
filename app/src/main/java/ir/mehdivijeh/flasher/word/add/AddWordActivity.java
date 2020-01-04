package ir.mehdivijeh.flasher.word.add;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.TextUtil;
import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDao;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import ir.mehdivijeh.flasher.splash.repo.LocalInsertAllRepo;
import ir.mehdivijeh.flasher.word.add.adapter.AdapterAddExample;
import ir.mehdivijeh.flasher.word.add.adapter.AdapterAddedExample;
import ir.mehdivijeh.flasher.word.add.presenter.AddWordPresenterImpl;

public class AddWordActivity extends AppCompatActivity implements AddWordContract.AddWordView {

    private AddWordContract.AddWordPresenter mPresenter;
    private ImageView mImgBack;
    private EditText mEdtWord;
    private EditText mEdtPronounce;
    private EditText mEdtRootMeaning;
    private EditText mEdtTranslateMeaning;
    private RecyclerView mRecyclerViewExample;
    private Button mBtnAdd;
    private long collectionId;
    private ItemAdapter mItemAdapter;
    private FastAdapter mFastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        initView();
        getExtras();
        initRecyclerView();
        initPresenter();
        initOnClick();
    }

    private void initView() {
        mImgBack = findViewById(R.id.img_back);
        mEdtWord = findViewById(R.id.edt_word);
        mEdtPronounce = findViewById(R.id.edt_pronounce);
        mEdtRootMeaning = findViewById(R.id.edt_root_meaning);
        mEdtTranslateMeaning = findViewById(R.id.edt_translate_meaning);
        mRecyclerViewExample = findViewById(R.id.recycler_view_example);
        mBtnAdd = findViewById(R.id.btn_add);
    }

    private void getExtras() {
        collectionId = getIntent().getLongExtra(GeneralConstants.COLLECTION_ID, -1);
    }

    private void initRecyclerView() {
        mItemAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(mItemAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewExample.setLayoutManager(layoutManager);
        mRecyclerViewExample.setAdapter(mFastAdapter);

        mItemAdapter.add(new AdapterAddExample(aVoid -> showDialogAddExample(null, null)));
    }

    private void initPresenter() {
        LocalDb localDb = LocalDb.getInstance(this);
        CollectionDao collectionDao = localDb.collectionDao();
        WordDao wordDao = localDb.wordDao();
        ExampleDao exampleDao = localDb.exampleDao();
        LocalInsertAllRepo localInsertAllRepo = new LocalInsertAllRepo(localDb, collectionDao, wordDao, exampleDao);

        mPresenter = new AddWordPresenterImpl(this, localInsertAllRepo);
    }


    private void initOnClick() {
        mImgBack.setOnClickListener(v -> onBackPressed());

        mBtnAdd.setOnClickListener(v -> {
            if (checkInputData()) {
                WordDb wordDb = new WordDb();
                wordDb.setWord(mEdtWord.getText().toString());
                if (mEdtPronounce.getText() != null) {
                    wordDb.setPronounce(mEdtPronounce.getText().toString());
                }
                wordDb.setRoot_definition(mEdtRootMeaning.getText().toString());
                wordDb.setTranslate(mEdtTranslateMeaning.getText().toString());
                wordDb.setILearnIt(false);
                wordDb.setCollectionId(collectionId);

                List<ExampleDb> exampleDbs = new ArrayList<>();
                for (int i = 0; i < mItemAdapter.getAdapterItems().size(); i++) {
                    if (mItemAdapter.getAdapterItem(i) instanceof AdapterAddedExample) {
                        ExampleDb exampleDb = new ExampleDb();
                        exampleDb.setRootExample(((AdapterAddedExample) mItemAdapter.getAdapterItem(i)).getRootExample());
                        exampleDb.setTranslateExample(((AdapterAddedExample) mItemAdapter.getAdapterItem(i)).getTranslateExample());
                        exampleDbs.add(exampleDb);
                    }
                }

                mPresenter.addWordToDb(collectionId, wordDb, exampleDbs);
            }
        });
    }

    private boolean checkInputData() {
        if (mEdtWord.getText() != null && mEdtWord.getText().toString().length() > 0) {
            return true;
        } else {
            mEdtWord.setError(getString(R.string.no_input));
        }

       /* if (mEdtPronounce.getText() != null && mEdtPronounce.getText().toString().length() > 0) {
            return true;
        } else {
            mEdtPronounce.setError(getString(R.string.no_input));
        }*/

        if (mEdtRootMeaning.getText() != null && mEdtRootMeaning.getText().toString().length() > 0) {
            return true;
        } else {
            mEdtRootMeaning.setError(getString(R.string.no_input));
        }

        if (mEdtTranslateMeaning.getText() != null && mEdtTranslateMeaning.getText().toString().length() > 0) {
            return true;
        } else {
            mEdtTranslateMeaning.setError(getString(R.string.no_input));
        }

        return false;

    }

    private void showDialogAddExample(String rootText, String translateText) {
        final Dialog mDialog = new Dialog(this);
        mDialog.setCancelable(true);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_add_example);
        TextUtil.setFonts(mDialog.getWindow().getDecorView());
        mDialog.setCancelable(true);

        EditText edtRootExample = mDialog.findViewById(R.id.edt_root_example);
        EditText edtTranslateExample = mDialog.findViewById(R.id.edt_translate_example);

        Button btnCreate = mDialog.findViewById(R.id.btn_create);
        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);

        if (rootText != null) {
            edtRootExample.setText(rootText);
        }

        if (translateText != null) {
            edtTranslateExample.setText(translateText);
        }

        btnCreate.setOnClickListener(v -> {
            if (edtRootExample.getText() != null && edtRootExample.getText().toString().length() > 0) {
                if (edtTranslateExample.getText() != null && edtTranslateExample.getText().toString().length() > 0) {
                    mDialog.dismiss();
                    addExample(edtRootExample.getText().toString(), edtTranslateExample.getText().toString());
                } else {
                    edtTranslateExample.setError(getString(R.string.no_input));
                }
            } else {
                edtRootExample.setError(getString(R.string.no_input));
            }
        });

        btnCancel.setOnClickListener(v -> mDialog.dismiss());
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.WRAP_CONTENT);

        mDialog.show();
    }

    private void addExample(String rootText, String translateText) {

        //delete add new example and then after add example add it
        if (mItemAdapter != null && mItemAdapter.getAdapterItemCount() > 0) {
            mItemAdapter.remove(mItemAdapter.getAdapterItemCount() - 1);
        }

        mItemAdapter.add(new AdapterAddedExample(rootText, translateText, integer -> {
            mItemAdapter.remove(integer);
        }));

        mItemAdapter.add(new AdapterAddExample(aVoid -> showDialogAddExample(null, null)));

    }

    @Override
    public void onWordAdded() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null)
            mPresenter.onDestroy();
    }

}

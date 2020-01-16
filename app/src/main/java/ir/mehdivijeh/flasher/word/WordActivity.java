package ir.mehdivijeh.flasher.word;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.repo.LocalWordRepo;
import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import ir.mehdivijeh.flasher.study.StudyActivity;
import ir.mehdivijeh.flasher.word.adapter.AdapterWord;
import ir.mehdivijeh.flasher.word.add.AddWordActivity;
import ir.mehdivijeh.flasher.word.presenter.WordPresenterImpl;

public class WordActivity extends AppCompatActivity implements WordContract.WordView {

    private WordContract.WordPresenter mPresenter;
    private long collectionId;
    private Group groupNoWords;
    private ShimmerFrameLayout loadingView;
    private FloatingActionButton fabAddWord;
    private RecyclerView mRecyclerViewWords;
    private ItemAdapter mItemAdapter;
    private FastAdapter mFastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        initView();
        getExtras();
        initPresenter();
        initOnClick();
        initRecyclerView();
        getWords();
    }

    private void initView() {
        groupNoWords = findViewById(R.id.group_no_word);
        loadingView = findViewById(R.id.shimmer_view_container);
        mRecyclerViewWords = findViewById(R.id.recycler_view_word);
        fabAddWord = findViewById(R.id.fab_add_word);
    }

    private void getExtras() {
        collectionId = getIntent().getLongExtra(GeneralConstants.COLLECTION_ID, -1);
    }

    private void initPresenter() {
        WordDao wordDao = LocalDb.getInstance(this).wordDao();
        LocalWordRepo localWordRepo = new LocalWordRepo(wordDao);
        mPresenter = new WordPresenterImpl(this, localWordRepo);
    }

    private void initOnClick() {
        fabAddWord.setOnClickListener(v -> {
            Intent intent = new Intent(this , AddWordActivity.class);
            intent.putExtra(GeneralConstants.COLLECTION_ID , collectionId);
            startActivity(intent);
        });

    }

    private void initRecyclerView() {
        mItemAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(mItemAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewWords.setLayoutManager(layoutManager);
        mRecyclerViewWords.setAdapter(mFastAdapter);
    }

    private void getWords() {
        loadingView.setVisibility(View.VISIBLE);
        mRecyclerViewWords.setVisibility(View.GONE);
        groupNoWords.setVisibility(View.GONE);
        loadingView.startShimmer();

        mPresenter.loadWordsWithCollectionId(collectionId);
    }


    @Override
    public void onWordsLoaded(List<WordDb> wordDbs) {
        if (mItemAdapter != null && mItemAdapter.getAdapterItemCount() > 0) {
            mItemAdapter.clear();
        }

        loadingView.stopShimmer();
        loadingView.setVisibility(View.GONE);

        if (wordDbs.size() > 0) {
            mRecyclerViewWords.setVisibility(View.VISIBLE);

            for (int i = 0 ; i < wordDbs.size() ; i++) {
                WordDb wordDb = wordDbs.get(i);
                mItemAdapter.add(new AdapterWord(wordDb.getWord(), wordDb.getTranslate(), wordDb.isILearnIt() , i + 1, adapterCollection -> {
                    Intent intent = new Intent(WordActivity.this, StudyActivity.class);
                    intent.putExtra(GeneralConstants.COLLECTION_ID, collectionId);
                    intent.putExtra(GeneralConstants.WORD_ID, wordDb.getId());
                    startActivity(intent);
                }));
            }

        } else {
            groupNoWords.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            getWords();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null)
            mPresenter.onDestroy();
    }

}

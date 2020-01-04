package ir.mehdivijeh.flasher.study;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.progresviews.ProgressLine;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;
import java.util.Locale;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.OnSwipeTouchListener;
import ir.mehdivijeh.flasher.general.TextUtil;
import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.repo.LocalCollectionRepo;
import ir.mehdivijeh.flasher.main.repo.LocalExampleRepo;
import ir.mehdivijeh.flasher.main.repo.LocalWordRepo;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDao;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import ir.mehdivijeh.flasher.study.adapter.AdapterExample;
import ir.mehdivijeh.flasher.study.presenter.StudyPresenterImpl;

public class StudyActivity extends AppCompatActivity implements StudyContract.StudyView, TextToSpeech.OnInitListener {

    private StudyContract.StudyPresenter mPresenter;
    private TextView mTxtSeeMore;
    private TextView mTxtName;
    private TextView mTxtWord;
    private TextView mTxtShowLess;
    private TextView mTxtCount;
    private TextView mTxtPronounce;
    private TextView mTxtRootMeaning;
    private TextView mTxtTranslateMeaning;
    private TextView mTxtExamples;
    private ImageView mImgPronounce;
    private ImageView mImgBack;
    private Button mBtnNext;
    private Button mBtnPrevious;
    private Button mBtnLearn;
    private Group mGroupStudy;
    private CardView mCrdDetails;
    private ConstraintLayout mCstStudyRoot;
    private ShimmerFrameLayout loadingView;
    private ProgressLine mProgressLine;
    private TextToSpeech mTts;
    private boolean isTtsInit = false;
    private long collectionId;
    private long wordId;
    private long nextWordId;
    private long previousWordId;
    private List<WordDb> wordDbs;
    private CollectionDb collectionDb;
    private List<ExampleDb> exampleDbs;
    private WordDb wordDb;
    private RecyclerView mRecyclerViewExample;
    private ItemAdapter mItemAdapter;
    private FastAdapter mFastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        initView();
        getExtras();
        initPresenter();
        initRecyclerView();
        initTts();
        initOnClick();

        mPresenter.loadCollectionFromDb(collectionId);
    }

    private void initView() {
        mRecyclerViewExample = findViewById(R.id.recycler_view_example);
        mTxtSeeMore = findViewById(R.id.txt_see_more);
        mImgPronounce = findViewById(R.id.img_pronounce);
        mTxtPronounce = findViewById(R.id.txt_pronounce);
        mTxtRootMeaning = findViewById(R.id.txt_root_meaning);
        mTxtExamples = findViewById(R.id.txt_examples);
        mTxtWord = findViewById(R.id.txt_word);
        mCrdDetails = findViewById(R.id.card_view_details);
        mCstStudyRoot = findViewById(R.id.cst_study_root);
        mTxtName = findViewById(R.id.txt_name);
        mTxtShowLess = findViewById(R.id.txt_show_less);
        mImgBack = findViewById(R.id.img_back);
        mProgressLine = findViewById(R.id.progress_line);
        mTxtCount = findViewById(R.id.txt_count);
        mBtnNext = findViewById(R.id.btn_next);
        mBtnPrevious = findViewById(R.id.btn_previous);
        mBtnLearn = findViewById(R.id.btn_learn);
        mTxtTranslateMeaning = findViewById(R.id.txt_translate_meaning);
        mGroupStudy = findViewById(R.id.group_study);
        loadingView = findViewById(R.id.shimmer_view_container);
        TextUtil.setFonts(getWindow().getDecorView());
    }

    private void getExtras() {
        collectionId = getIntent().getLongExtra(GeneralConstants.COLLECTION_ID, -1);
        wordId = getIntent().getLongExtra(GeneralConstants.WORD_ID, -1);
    }

    private void initPresenter() {
        LocalDb localDb = LocalDb.getInstance(this);

        WordDao wordDao = localDb.wordDao();
        ExampleDao exampleDao = localDb.exampleDao();
        CollectionDao collectionDao = localDb.collectionDao();

        LocalWordRepo localWordRepo = new LocalWordRepo(wordDao);
        LocalExampleRepo localExampleRepo = new LocalExampleRepo(exampleDao);
        LocalCollectionRepo localCollectionRepo = new LocalCollectionRepo(collectionDao);

        loadingView.startShimmer();
        mPresenter = new StudyPresenterImpl(this, localWordRepo, localExampleRepo, localCollectionRepo);
    }

    private void initTts() {
        mTts = new TextToSpeech(this, this);
    }

    private void initOnClick() {
        mTxtSeeMore.setOnClickListener(v -> showDetailsView());

        mTxtShowLess.setOnClickListener(v -> hideDetailsView());



        mImgPronounce.setOnClickListener(v -> {
            if (isTtsInit) {
                String text = mTxtWord.getText().toString();
                mTts.setLanguage(Locale.US);
                mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "please wait and try again.", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnNext.setOnClickListener(v -> onNext());

        mBtnPrevious.setOnClickListener(v -> onPrevious());

        mBtnLearn.setOnClickListener(v -> {
            if (collectionDb != null && wordDb != null && mPresenter != null) {
                wordDb.setILearnIt(!wordDb.isILearnIt());
                mPresenter.setILearnt(collectionDb, wordDb);
            }
        });

        mCstStudyRoot.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                if (!isShowMoreLessVisible()) {
                    showDetailsView();
                }
            }

            public void onSwipeRight() {
                onPrevious();
            }

            public void onSwipeLeft() {

                onNext();
            }

            public void onSwipeBottom() {
                if (isShowMoreLessVisible()) {
                    hideDetailsView();
                }
            }

        });
    }

    private void initRecyclerView() {
        mItemAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(mItemAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewExample.setLayoutManager(layoutManager);
        mRecyclerViewExample.setAdapter(mFastAdapter);
    }

    private boolean isShowMoreLessVisible() {
        return mTxtShowLess.getVisibility() == View.VISIBLE;
    }

    private void showDetailsView() {

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mCstStudyRoot);

        constraintSet.connect(mImgPronounce.getId(), ConstraintSet.TOP, mTxtName.getId(), ConstraintSet.BOTTOM, 12);
        constraintSet.connect(mImgPronounce.getId(), ConstraintSet.BOTTOM, -1, ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(mCstStudyRoot);

        Transition transitionDetails = new Fade();
        transitionDetails.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transitionDetails.setDuration(666);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(666);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mTxtSeeMore.setVisibility(View.INVISIBLE);
                mCrdDetails.setVisibility(View.VISIBLE);
                mTxtShowLess.setVisibility(View.VISIBLE);

                //TransitionManager.beginDelayedTransition(mCrdDetails, transition);
            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.beginDelayedTransition(mCstStudyRoot, transition);

    }

    private void hideDetailsView() {
        if (!isShowMoreLessVisible())
            return;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mCstStudyRoot);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(666);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mTxtSeeMore.setVisibility(View.VISIBLE);
                mCrdDetails.setVisibility(View.INVISIBLE);
                mTxtShowLess.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(mCstStudyRoot, transition);

        constraintSet.connect(mImgPronounce.getId(), ConstraintSet.TOP, mCstStudyRoot.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(mImgPronounce.getId(), ConstraintSet.BOTTOM, mCstStudyRoot.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(mCstStudyRoot);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInit = true;
            mTts.setLanguage(Locale.US);
        } else
            Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null)
            mPresenter.onDestroy();
    }

    @Override
    public void onCollectionLoaded(CollectionDb collectionDb) {
        this.collectionDb = collectionDb;
        mPresenter.loadWordsFromDb(collectionId);
    }

    @Override
    public void onWordsLoaded(List<WordDb> wordDbs) {
        this.wordDbs = wordDbs;
        mPresenter.loadExampleWithWordId(wordId);
    }

    @Override
    public void onExampleLoaded(List<ExampleDb> exampleDbs) {
        this.exampleDbs = exampleDbs;
        showDataOnView();
        mGroupStudy.setVisibility(View.VISIBLE);
        loadingView.stopShimmer();
        loadingView.setVisibility(View.GONE);

        if (mItemAdapter != null && mItemAdapter.getAdapterItemCount() > 0) {
            mItemAdapter.clear();
        }

        if(exampleDbs.size() > 0){
            mTxtExamples.setVisibility(View.VISIBLE);
            mRecyclerViewExample.setVisibility(View.VISIBLE);
        }else {
            mTxtExamples.setVisibility(View.INVISIBLE);
            mRecyclerViewExample.setVisibility(View.INVISIBLE);
        }

        for (ExampleDb exampleDb : exampleDbs) {
            mItemAdapter.add(new AdapterExample(exampleDb.getRootExample(), exampleDb.getTranslateExample()));
        }
    }

    private void showDataOnView() {
        mTxtName.setText(collectionDb.getName());
        mTxtCount.setText(String.valueOf(collectionDb.getSize()));

        for (int i = 0; i < wordDbs.size(); i++) {
            if (wordDbs.get(i).getId() == wordId) {

                wordDb = wordDbs.get(i);

                if (i - 1 >= 0) {
                    previousWordId = wordDbs.get(i - 1).getId();
                } else {
                    previousWordId = -1;
                }

                if (i + 1 < wordDbs.size()) {
                    nextWordId = wordDbs.get(i + 1).getId();
                } else {
                    nextWordId = -1;
                }

                int percentage = (int) ((i + 1) * (100.0 / (float) collectionDb.getSize()));
                mProgressLine.setmPercentage(percentage);
                mProgressLine.setmValueText(i + 1);
                mTxtWord.setText(wordDbs.get(i).getWord());
                if(wordDbs.get(i).getPronounce() != null) {
                    mTxtPronounce.setVisibility(View.VISIBLE);
                    mTxtPronounce.setText(wordDbs.get(i).getPronounce());
                }else {
                    mTxtPronounce.setVisibility(View.INVISIBLE);
                }
                mTxtRootMeaning.setText(wordDbs.get(i).getRoot_definition());
                mTxtTranslateMeaning.setText(wordDbs.get(i).getTranslate());
                changeLearnButton(wordDbs.get(i).isILearnIt());
            }
        }
    }

    private void onNext() {
        if (nextWordId != -1) {
            wordId = nextWordId;
            mPresenter.loadExampleWithWordId(wordId);
            //showDataOnView();
        } else {
            Toast.makeText(this, "this is last word", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPrevious() {
        if (previousWordId != -1) {
            wordId = previousWordId;
            mPresenter.loadExampleWithWordId(wordId);
            //showDataOnView();
        } else {
            Toast.makeText(this, "this is first word", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeLearnButton(boolean isILearn) {
        if (isILearn) {
            mBtnLearn.setText("Forgot");
            mBtnLearn.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorRed));
        } else {
            mBtnLearn.setText("Learn");
            mBtnLearn.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorGreen));
        }
    }

    @Override
    public void onLearntSaved(boolean isILearnt) {
        changeLearnButton(isILearnt);
    }


}


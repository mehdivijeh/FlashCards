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

import com.app.progresviews.ProgressLine;

import java.util.Locale;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.OnSwipeTouchListener;

public class StudyActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView mTxtSeeMore;
    private TextView mTxtName;
    private TextView mTxtWord;
    private TextView mTxtShowLess;
    private TextView mTxtCount;
    private ImageView mImgPronounce;
    private ImageView mImgBack;
    private Button mBtnNext;
    private Button mBtnPrevious;
    private Button mBtnLearn;
    private CardView mCrdDetails;
    private ConstraintLayout mCstStudyRoot;
    private ProgressLine mProgressLine;
    private TextToSpeech mTts;
    private boolean isTtsInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        initView();
        initTts();
        initOnClick();

    }

    private void initView() {
        mTxtSeeMore = findViewById(R.id.txt_see_more);
        mImgPronounce = findViewById(R.id.img_pronounce);
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
    }

    private void initTts() {
        mTts = new TextToSpeech(this, this);
    }

    private void initOnClick() {
        mTxtSeeMore.setOnClickListener(v -> showDetailsView());

        mTxtShowLess.setOnClickListener(v -> hideDetailsView());

        mImgBack.setOnClickListener(v -> onBackPressed());

        mImgPronounce.setOnClickListener(v -> {
            if(isTtsInit) {
                String text = mTxtWord.getText().toString();
                mTts.setLanguage(Locale.US);
                mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }else {
                Toast.makeText(this, "please wait and try again.", Toast.LENGTH_SHORT).show();
            }
        });

        mCstStudyRoot.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                if(!isShowMoreLessVisible()){
                    showDetailsView();
                }
            }

            public void onSwipeRight() {
                Toast.makeText(StudyActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(StudyActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                if(isShowMoreLessVisible()){
                    hideDetailsView();
                }
            }

        });
    }

    private boolean isShowMoreLessVisible(){
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
}


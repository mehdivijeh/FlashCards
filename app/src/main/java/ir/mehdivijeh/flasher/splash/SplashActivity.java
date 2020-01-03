package ir.mehdivijeh.flasher.splash;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.PreferenceUtils;
import ir.mehdivijeh.flasher.general.TextUtil;
import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.MainActivity;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.splash.presenter.SplashPresenterImpl;
import ir.mehdivijeh.flasher.splash.repo.LocalInsertAllRepo;

public class SplashActivity extends AppCompatActivity implements SplashContract.SplashView {

    private SplashContract.SplashPresenter mPresenter;
    private LottieAnimationView mLottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PreferenceUtils.initPreferenceUtils(this);
        initView();
        initPresenter();

        mLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mPresenter != null) {
                    mPresenter.checkIsFirstTimeOpenApp();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initView() {
        mLottie = findViewById(R.id.lottie_0);
        TextUtil.setFonts(getWindow().getDecorView());
    }

    private void initPresenter() {
        LocalDb localDb = LocalDb.getInstance(this);
        CollectionDao collectionDao = localDb.collectionDao();
        WordDao wordDao = localDb.wordDao();
        ExampleDao exampleDao = localDb.exampleDao();
        LocalInsertAllRepo localInsertAllRepo = new LocalInsertAllRepo(localDb, collectionDao, wordDao, exampleDao);
        mPresenter = new SplashPresenterImpl(this, localInsertAllRepo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null)
            mPresenter.onDestroy();
    }

    @Override
    public void onDataSetsLoaded() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void isFirstTimeOpenApp(boolean isFirstTime) {
        if (isFirstTime)
            mPresenter.loadDataSetsOnDb();
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

package ir.mehdivijeh.flasher.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.repo.LocalCollectionRepo;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.splash.presenter.SplashPresenterImpl;
import ir.mehdivijeh.flasher.word.WordActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.SplashView {

    private SplashContract.SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initPresenter();
        mPresenter.checkIsFirstTimeOpenApp();

        startActivity(new Intent(this, WordActivity.class));
    }

    private void initView() {

    }

    private void initPresenter() {
        CollectionDao collectionDao = LocalDb.getInstance(this).collectionDao();
        LocalCollectionRepo localCollectionRepo = new LocalCollectionRepo(collectionDao);
        mPresenter = new SplashPresenterImpl(this, localCollectionRepo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null)
            mPresenter.onDestroy();
    }

    @Override
    public void onDataSetsLoaded() {

    }

    @Override
    public void isFirstTimeOpenApp(boolean isFirstTime) {

    }
}

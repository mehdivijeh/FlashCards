package ir.mehdivijeh.flasher.splash.presenter;

import java.util.HashSet;

import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.PreferenceUtils;
import ir.mehdivijeh.flasher.main.repo.LocalCollectionRepo;
import ir.mehdivijeh.flasher.splash.SplashContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashContract.SplashPresenter {

    private SplashContract.SplashView mView;
    private LocalCollectionRepo mLocalCollectionRepo;
    private HashSet<Subscription> subscriptions = new HashSet<>();

    public SplashPresenterImpl(SplashContract.SplashView mView, LocalCollectionRepo mLocalCollectionRepo) {
        this.mView = mView;
        this.mLocalCollectionRepo = mLocalCollectionRepo;
    }

    @Override
    public void loadDataSetsOnDb() {
        /*subscriptions.add(mLocalCollectionRepo.add()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectionDbs -> {
                            mView.onDataSetsLoaded();
                        }
                ));*/
    }

    @Override
    public void checkIsFirstTimeOpenApp() {
        if (PreferenceUtils.getBooleanPreference(GeneralConstants.ISN_NOT_FIRST_TIME_OPEN_APP))
            mView.isFirstTimeOpenApp(false);
        else
            mView.isFirstTimeOpenApp(true);
    }

    @Override
    public void onDestroy() {
        for (Subscription subscription : subscriptions)
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
    }
}

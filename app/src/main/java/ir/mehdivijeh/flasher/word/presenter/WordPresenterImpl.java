package ir.mehdivijeh.flasher.word.presenter;

import java.util.HashSet;

import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.PreferenceUtils;
import ir.mehdivijeh.flasher.main.repo.LocalWordRepo;
import ir.mehdivijeh.flasher.word.WordContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WordPresenterImpl implements WordContract.WordPresenter {

    private WordContract.WordView mView;
    private LocalWordRepo mLocalWordRepo;
    private HashSet<Subscription> subscriptions = new HashSet<>();

    public WordPresenterImpl(WordContract.WordView mView, LocalWordRepo mLocalWordRepo) {
        this.mView = mView;
        this.mLocalWordRepo = mLocalWordRepo;
    }

    @Override
    public void loadWordsWithCollectionId(long collectionId) {
        subscriptions.add(mLocalWordRepo
                .getAllWithCollectionId(collectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordDbs  -> {
                            mView.onWordsLoaded(wordDbs);
                        }
                ));
    }

    @Override
    public void onDestroy() {
        for (Subscription subscription : subscriptions)
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
    }
}

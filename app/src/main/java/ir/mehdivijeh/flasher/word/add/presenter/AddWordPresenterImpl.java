package ir.mehdivijeh.flasher.word.add.presenter;

import java.util.HashSet;
import java.util.List;

import ir.mehdivijeh.flasher.main.repo.LocalExampleRepo;
import ir.mehdivijeh.flasher.main.repo.LocalWordRepo;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import ir.mehdivijeh.flasher.splash.repo.LocalInsertAllRepo;
import ir.mehdivijeh.flasher.word.add.AddWordContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddWordPresenterImpl implements AddWordContract.AddWordPresenter {

    private AddWordContract.AddWordView mView;
    private LocalInsertAllRepo mLocalInsertAllRepo;
    private HashSet<Subscription> subscriptions = new HashSet<>();

    public AddWordPresenterImpl(AddWordContract.AddWordView mView, LocalInsertAllRepo mLocalInsertAllRepo) {
        this.mView = mView;
        this.mLocalInsertAllRepo = mLocalInsertAllRepo;
    }

    @Override
    public void addWordToDb(long collectionId , WordDb wordDb , List<ExampleDb> exampleDbs) {
        subscriptions.add(mLocalInsertAllRepo
                .transactionAddWord(collectionId , wordDb , exampleDbs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                            mView.onWordAdded();
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

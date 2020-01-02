package ir.mehdivijeh.flasher.main.presenter;

import java.util.HashSet;

import ir.mehdivijeh.flasher.main.MainContract;
import ir.mehdivijeh.flasher.main.repo.LocalCollectionRepo;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenterImpl implements MainContract.MainPresenter {

    private MainContract.MainView mView;
    private LocalCollectionRepo mLocalCollectionRepo;
    private HashSet<Subscription> subscriptions = new HashSet<>();


    public MainPresenterImpl(MainContract.MainView mView, LocalCollectionRepo mLocalCollectionRepo) {
        this.mView = mView;
        this.mLocalCollectionRepo = mLocalCollectionRepo;
    }

    @Override
    public void loadCollectionFromDb() {
        subscriptions.add(mLocalCollectionRepo.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectionDbs -> {
                            mView.onCollectionLoaded(collectionDbs);
                        }
                ));
    }

    @Override
    public void deleteCollectionFromDb(CollectionDb collectionDb) {
        subscriptions.add(mLocalCollectionRepo.delete(collectionDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid  -> {
                            mView.onDeleteSuccessfully(collectionDb);
                        }
                ));
    }

    @Override
    public void addCollectionToDb(CollectionDb collectionDb) {
        subscriptions.add(mLocalCollectionRepo.add(collectionDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid   -> {
                            mView.addCollectionSuccessfully(collectionDb);
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

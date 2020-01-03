package ir.mehdivijeh.flasher.study.presenter;

import java.util.HashSet;

import ir.mehdivijeh.flasher.main.repo.LocalCollectionRepo;
import ir.mehdivijeh.flasher.main.repo.LocalExampleRepo;
import ir.mehdivijeh.flasher.main.repo.LocalWordRepo;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import ir.mehdivijeh.flasher.study.StudyContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StudyPresenterImpl implements StudyContract.StudyPresenter {

    private StudyContract.StudyView mView;
    private LocalCollectionRepo mCollectionRepo;
    private LocalWordRepo mLocalWordRepo;
    private LocalExampleRepo mLocalExampleRepo;
    private HashSet<Subscription> subscriptions = new HashSet<>();

    public StudyPresenterImpl(StudyContract.StudyView mView, LocalWordRepo mLocalWordRepo, LocalExampleRepo mLocalExampleRepo, LocalCollectionRepo mCollectionRepo) {
        this.mView = mView;
        this.mLocalWordRepo = mLocalWordRepo;
        this.mLocalExampleRepo = mLocalExampleRepo;
        this.mCollectionRepo = mCollectionRepo;
    }

    @Override
    public void loadCollectionFromDb(long collectionId) {
        subscriptions.add(mCollectionRepo
                .getWithId(collectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectionDb -> {
                            mView.onCollectionLoaded(collectionDb);
                        }
                ));
    }

    @Override
    public void loadWordsFromDb(long collectionId) {
        subscriptions.add(mLocalWordRepo
                .getAllWithCollectionId(collectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordDbs -> {
                            mView.onWordsLoaded(wordDbs);
                        }
                ));
    }

    @Override
    public void loadExampleWithWordId(long wordId) {
        subscriptions.add(mLocalExampleRepo
                .getAllExampleWithWordId(wordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exampleDbs -> {
                            mView.onExampleLoaded(exampleDbs);
                        }
                ));
    }

    @Override
    public void setILearnt(CollectionDb collectionDb , WordDb wordDb) {
        subscriptions.add(mLocalWordRepo
                .setILearned(wordDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                            subscriptions.add(mCollectionRepo
                                    .changeLearn(collectionDb , wordDb.isILearnIt())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(aVoid1 -> {
                                                mView.onLearntSaved(wordDb.isILearnIt());
                                            }
                                    ));
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

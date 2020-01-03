package ir.mehdivijeh.flasher.splash.repo;

import java.util.List;
import java.util.concurrent.Callable;

import ir.mehdivijeh.flasher.general.repo.db.LocalDb;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDao;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import rx.Observable;

public class LocalInsertAllRepo {

    private LocalDb localDb;
    private CollectionDao collectionDao;
    private WordDao wordDao;
    private ExampleDao exampleDao;

    public LocalInsertAllRepo(LocalDb localDb, CollectionDao collectionDao, WordDao wordDao, ExampleDao exampleDao) {
        this.localDb = localDb;
        this.collectionDao = collectionDao;
        this.wordDao = wordDao;
        this.exampleDao = exampleDao;
    }

    public Observable<Void> transactionCollectionAndWordsInSameTime(CollectionDb collectionDb, List<WordDb> wordDbs, List<ExampleDb> exampleDbs) {
        return execute(() -> {
            localDb.runInTransaction(() -> {
                collectionDao.insert(collectionDb);
                wordDao.insertAll(wordDbs);
                exampleDao.insertAll(exampleDbs);
            });
            return null;
        });
    }

    private <T> Observable<T> execute(Callable act) {
        return Observable.fromCallable(act);
    }

}

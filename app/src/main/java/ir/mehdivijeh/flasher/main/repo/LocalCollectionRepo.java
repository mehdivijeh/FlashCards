package ir.mehdivijeh.flasher.main.repo;

import java.util.List;
import java.util.concurrent.Callable;

import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import rx.Observable;

public class LocalCollectionRepo {

    private CollectionDao collectionDao;

    public LocalCollectionRepo(CollectionDao collectionDao) {
        this.collectionDao = collectionDao;
    }


    public Observable<Void> add(CollectionDb collectionDb) {
        return execute(() -> {
            collectionDao.insert(collectionDb);
            return null;
        });
    }

    public Observable<List<CollectionDb>> getAll() {
        return execute(() -> collectionDao.getAll());
    }


    public Observable<Void> delete(CollectionDb collectionDb) {
        return execute(() -> {
            collectionDao.delete(collectionDb.getId());
            return null;
        });
    }


    private <T> Observable<T> execute(Callable act) {
        return Observable.fromCallable(act);
    }

}

package ir.mehdivijeh.flasher.main.repo;

import java.util.List;
import java.util.concurrent.Callable;

import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import rx.Observable;

public class LocalWordRepo {
    private WordDao wordDao;

    public LocalWordRepo(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    public Observable<List<WordDb>> getAllWithCollectionId(long collectionId) {
        return execute(() -> wordDao.getAllWithCollectionId(collectionId));
    }

    public Observable<Void> addWord(WordDb wordDb) {
        return execute(() ->{
            wordDao.insert(wordDb);
            return null;
        });
    }

    public Observable<Void> setILearned(WordDb wordDb) {
        return execute(() ->{
            wordDao.insert(wordDb);
            return null;
        });
    }

    private <T> Observable<T> execute(Callable act) {
        return Observable.fromCallable(act);
    }

}

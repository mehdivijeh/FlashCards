package ir.mehdivijeh.flasher.main.repo;

import java.util.List;
import java.util.concurrent.Callable;

import ir.mehdivijeh.flasher.main.repo.db.ExampleDao;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import rx.Observable;

public class LocalExampleRepo {
    private ExampleDao exampleDao;

    public LocalExampleRepo(ExampleDao exampleDao) {
        this.exampleDao = exampleDao;
    }

    public Observable<List<ExampleDb>> getAllExampleWithWordId(long wordId) {
        return execute(() -> exampleDao.getAllWithWordId(wordId));
    }

    private <T> Observable<T> execute(Callable act) {
        return Observable.fromCallable(act);
    }

}

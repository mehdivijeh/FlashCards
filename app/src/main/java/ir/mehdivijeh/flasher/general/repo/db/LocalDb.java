package ir.mehdivijeh.flasher.general.repo.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDao;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDao;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@Database(entities = {CollectionDb.class , WordDb.class}, version = 1, exportSchema = false)
public abstract class LocalDb extends RoomDatabase {

    public abstract CollectionDao collectionDao();

    public abstract WordDao wordDao();

    private static volatile LocalDb db;

    public static LocalDb getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, LocalDb.class, GeneralConstants.DB_NAME).build();
        }
        return db;
    }


    public static void deleteAll() {
        Observable.fromCallable(() -> {
            db.clearAllTables();
            return null;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}

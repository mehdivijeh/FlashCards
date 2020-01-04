package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Dao
public interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CollectionDb collectionDb);

    @Query("SELECT COUNT(*) FROM " + GeneralConstants.TABLE_COLLECTION)
    Integer count();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_COLLECTION)
    List<CollectionDb> getAll();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_COLLECTION + " WHERE id = :id")
    CollectionDb getWithId(long id);

    @Query("DELETE FROM " + GeneralConstants.TABLE_COLLECTION + " WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM " + GeneralConstants.TABLE_COLLECTION)
    void deleteAll();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_COLLECTION + " WHERE id = :id")
    CollectionDb getCollectionWithId(long id);

    @Query("UPDATE " + GeneralConstants.TABLE_COLLECTION + " SET learned = learned+1 WHERE id = :id")
    void increaseLearn(long id);

    @Query("UPDATE " + GeneralConstants.TABLE_COLLECTION + " SET learned = learned-1 WHERE id = :id")
    void decreaseLearn(long id);

    @Query("UPDATE " + GeneralConstants.TABLE_COLLECTION + " SET size = size+1 WHERE id = :id")
    void increaseSize(long id);

    @Query("UPDATE " + GeneralConstants.TABLE_COLLECTION + " SET size = size-1 WHERE id = :id")
    void decreaseSize(long id);

}

package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Dao
public interface CollectionDao {

    @Query("SELECT COUNT(*) FROM " + GeneralConstants.TABLE_COLLECTION)
    Integer count();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_COLLECTION)
    List<CollectionDb> getAll();

    @Query("DELETE FROM " + GeneralConstants.TABLE_COLLECTION + " WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM " + GeneralConstants.TABLE_COLLECTION)
    void deleteAll();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_COLLECTION + " WHERE id = :id")
    CollectionDb getCollectionWithId(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CollectionDb collectionDb);
}

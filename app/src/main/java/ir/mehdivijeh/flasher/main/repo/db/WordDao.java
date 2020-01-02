package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Dao
public interface WordDao {

    @Query("SELECT COUNT(*) FROM " + GeneralConstants.TABLE_WORD)
    Integer count();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_WORD + " WHERE collection_id = :collectionId")
    List<WordDb> getAllWithCollectionId(long collectionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WordDb wordDb);
}

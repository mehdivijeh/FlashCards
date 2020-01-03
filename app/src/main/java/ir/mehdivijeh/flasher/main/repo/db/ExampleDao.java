package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Dao
public interface ExampleDao {

    @Query("SELECT COUNT(*) FROM " + GeneralConstants.TABLE_EXAMPLE)
    Integer count();

    @Query("SELECT * FROM " + GeneralConstants.TABLE_EXAMPLE + " where word_id = :wordId")
    List<ExampleDb> getAllWithWordId(long wordId);

    @Query("DELETE FROM " + GeneralConstants.TABLE_EXAMPLE + " WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM " + GeneralConstants.TABLE_EXAMPLE + " where word_id = :wordId")
    void deleteAllWithId(long wordId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExampleDb exampleDb);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExampleDb> exampleDbs);


}

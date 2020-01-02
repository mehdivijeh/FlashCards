package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Entity(tableName = GeneralConstants.TABLE_WORD)
public class WordDb {

    @ColumnInfo(name = "id")
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "collection_id")
    @PrimaryKey
    private long CollectionId;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "translate")
    private String translate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(long collectionId) {
        CollectionId = collectionId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}

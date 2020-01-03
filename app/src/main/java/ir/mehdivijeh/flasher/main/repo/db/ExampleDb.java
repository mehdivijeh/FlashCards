package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Entity(tableName = GeneralConstants.TABLE_EXAMPLE)
public class ExampleDb {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "word_id")
    private long wordId;

    @ColumnInfo(name = "root_example")
    private String rootExample;

    @ColumnInfo(name = "translate_example")
    private String translateExample;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public String getRootExample() {
        return rootExample;
    }

    public void setRootExample(String rootExample) {
        this.rootExample = rootExample;
    }

    public String getTranslateExample() {
        return translateExample;
    }

    public void setTranslateExample(String translateExample) {
        this.translateExample = translateExample;
    }
}

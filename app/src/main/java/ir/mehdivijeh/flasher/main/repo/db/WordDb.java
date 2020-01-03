package ir.mehdivijeh.flasher.main.repo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.mehdivijeh.flasher.general.GeneralConstants;

@Entity(tableName = GeneralConstants.TABLE_WORD)
public class WordDb {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "collection_id")
    private long collectionId;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "pronounce")
    private String pronounce;

    @ColumnInfo(name = "root_definition")
    private String root_definition;

    @ColumnInfo(name = "translate")
    private String translate;

    @ColumnInfo(name = "isILearnIt")
    private boolean isILearnIt;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
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

    public boolean isILearnIt() {
        return isILearnIt;
    }

    public void setILearnIt(boolean ILearnIt) {
        isILearnIt = ILearnIt;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public String getRoot_definition() {
        return root_definition;
    }

    public void setRoot_definition(String root_definition) {
        this.root_definition = root_definition;
    }
}

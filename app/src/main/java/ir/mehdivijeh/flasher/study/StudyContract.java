package ir.mehdivijeh.flasher.study;

import java.util.List;

import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;

public class StudyContract {

    public interface StudyPresenter{
        void loadCollectionFromDb(long collectionId);

        void loadWordsFromDb(long collectionId);

        void loadExampleWithWordId(long wordId);

        void setILearnt(CollectionDb collectionDb , WordDb wordDb);

        void onDestroy();
    }

    public interface StudyView{
        void onCollectionLoaded(CollectionDb collectionDb);

        void onWordsLoaded(List<WordDb> wordDbs);

        void onExampleLoaded(List<ExampleDb> exampleDbs);

        void onLearntSaved(boolean isILearnt);
    }
}

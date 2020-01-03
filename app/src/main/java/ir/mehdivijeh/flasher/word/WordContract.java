package ir.mehdivijeh.flasher.word;

import java.util.List;

import ir.mehdivijeh.flasher.main.repo.db.WordDb;

public class WordContract {
    public interface WordPresenter{
        void loadWordsWithCollectionId(long collectionId);

        void onDestroy();
    }

    public interface WordView{
        void onWordsLoaded(List<WordDb> wordDbs);
    }
}

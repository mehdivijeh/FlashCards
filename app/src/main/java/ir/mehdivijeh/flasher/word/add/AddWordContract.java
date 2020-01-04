package ir.mehdivijeh.flasher.word.add;

import java.util.List;

import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;

public class AddWordContract {
    public interface AddWordPresenter {
        void addWordToDb(long collectionId , WordDb wordDb , List<ExampleDb> exampleDbs);

        void onDestroy();
    }

    public interface AddWordView {
        void onWordAdded();
    }
}

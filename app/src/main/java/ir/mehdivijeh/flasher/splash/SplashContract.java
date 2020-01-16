package ir.mehdivijeh.flasher.splash;

import java.util.List;

import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;

public class SplashContract {

    public interface SplashPresenter{
        void loadDataSetsOnDb();

        CollectionDb loadLocalCollectionFromDisk();

        List<WordDb> loadLocalWordsFromDb();

        List<ExampleDb> loadLocalExampleFromDb();

        void checkIsFirstTimeOpenApp();

        void generateUUId();

        void onDestroy();
    }

    public interface SplashView{
        void onDataSetsLoaded();

        void isFirstTimeOpenApp(boolean isFirstTime);
    }
}

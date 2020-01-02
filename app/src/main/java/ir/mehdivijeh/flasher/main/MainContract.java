package ir.mehdivijeh.flasher.main;

import java.util.List;

import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;

public class MainContract {

    public interface MainPresenter {
        void addCollectionToDb(CollectionDb collectionDb);

        void loadCollectionFromDb();

        void deleteCollectionFromDb(CollectionDb collectionDb);

        void onDestroy();
    }

    public interface MainView {
        void onCollectionLoaded(List<CollectionDb> collectionDbList);

        void addCollectionSuccessfully(CollectionDb addedCollectionDb);

        void onDeleteSuccessfully(CollectionDb deletedCollectionDb);

    }

}

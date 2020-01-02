package ir.mehdivijeh.flasher.study;

public class StudyContract {

    public interface StudyPresenter{
        void loadWordsFromDb(long collectionId);

        void setILearnt(boolean iLearnt);

        void onDestroy();
    }

    public interface StudyView{
        void onWordsLoaded();
    }
}

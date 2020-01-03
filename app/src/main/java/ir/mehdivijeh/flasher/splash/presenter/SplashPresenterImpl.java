package ir.mehdivijeh.flasher.splash.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.PreferenceUtils;
import ir.mehdivijeh.flasher.main.repo.db.CollectionDb;
import ir.mehdivijeh.flasher.main.repo.db.ExampleDb;
import ir.mehdivijeh.flasher.main.repo.db.WordDb;
import ir.mehdivijeh.flasher.splash.SplashContract;
import ir.mehdivijeh.flasher.splash.repo.LocalInsertAllRepo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashContract.SplashPresenter {

    private SplashContract.SplashView mView;
    private LocalInsertAllRepo mLocalInsertAllRepo;
    private HashSet<Subscription> subscriptions = new HashSet<>();

    public SplashPresenterImpl(SplashContract.SplashView mView, LocalInsertAllRepo mLocalInsertAllRepo) {
        this.mView = mView;
        this.mLocalInsertAllRepo = mLocalInsertAllRepo;
    }

    @Override
    public void loadDataSetsOnDb() {
        subscriptions.add(mLocalInsertAllRepo
                .transactionCollectionAndWordsInSameTime(loadLocalCollectionFromDisk(), loadLocalWordsFromDb(), loadLocalExampleFromDb())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectionDbs -> {
                            PreferenceUtils.putBooleanPreference(GeneralConstants.ISN_NOT_FIRST_TIME_OPEN_APP , true);
                            mView.onDataSetsLoaded();
                        }
                ));
    }

    @Override
    public CollectionDb loadLocalCollectionFromDisk() {
        CollectionDb collectionDb = new CollectionDb();
        collectionDb.setId(1);
        collectionDb.setName("504 essential words");
        collectionDb.setSize(2);
        collectionDb.setLearned(0);
        return collectionDb;
    }

    @Override
    public List<WordDb> loadLocalWordsFromDb() {
        List<WordDb> wordDbs = new ArrayList<>();

        WordDb wordDb = new WordDb();
        wordDb.setCollectionId(1);
        wordDb.setId(1);
        wordDb.setWord("Abandon");
        wordDb.setPronounce("ə-ˈban-dən");
        wordDb.setRoot_definition("desert, leave without planning to come back, quit");
        wordDb.setTranslate("ترک کردن، ترک کردن بدون قصد بازگشت، دست کیشدن از");
        wordDb.setILearnIt(false);

        wordDbs.add(wordDb);


        WordDb wordDb1 = new WordDb();
        wordDb1.setCollectionId(1);
        wordDb1.setId(2);
        wordDb1.setWord("Keen");
        wordDb1.setPronounce("ˈkēn");
        wordDb1.setRoot_definition("sharp, eager, intense, sensitive");
        wordDb1.setTranslate("تیز، مشتاق، شدید، حساس");
        wordDb1.setILearnIt(false);

        wordDbs.add(wordDb1);

        return wordDbs;
    }

    @Override
    public List<ExampleDb> loadLocalExampleFromDb() {
        List<ExampleDb> exampleDbs = new ArrayList<>();

        ExampleDb exampleDb = new ExampleDb();
        exampleDb.setId(1);
        exampleDb.setWordId(1);
        exampleDb.setRootExample("When Roy abandoned his family, the police went looking for him.");
        exampleDb.setTranslateExample("وقتی <<روی>> خانواده اش را ترک کرد، پلیس به جستجوی او پرداخت.");

        exampleDbs.add(exampleDb);

        ExampleDb exampleDb1 = new ExampleDb();
        exampleDb1.setId(2);
        exampleDb1.setWordId(1);
        exampleDb1.setRootExample("The soldier could not abandon his friends who were hurt in battle.");
        exampleDb1.setTranslateExample("سرباز نتوانست دوستانش را که در جنگ زخمی شده بودند رها کند.");

        exampleDbs.add(exampleDb1);

        return exampleDbs;
    }

    @Override
    public void checkIsFirstTimeOpenApp() {
        if (PreferenceUtils.getBooleanPreference(GeneralConstants.ISN_NOT_FIRST_TIME_OPEN_APP))
            mView.isFirstTimeOpenApp(false);
        else
            mView.isFirstTimeOpenApp(true);
    }

    @Override
    public void onDestroy() {
        for (Subscription subscription : subscriptions)
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
    }
}

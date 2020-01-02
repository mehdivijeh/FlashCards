package ir.mehdivijeh.flasher.splash;

public class SplashContract {

    public interface SplashPresenter{
        void loadDataSetsOnDb();

        void checkIsFirstTimeOpenApp();

        void onDestroy();
    }

    public interface SplashView{
        void onDataSetsLoaded();

        void isFirstTimeOpenApp(boolean isFirstTime);
    }
}

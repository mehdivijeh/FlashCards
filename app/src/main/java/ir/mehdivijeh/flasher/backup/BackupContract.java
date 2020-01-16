package ir.mehdivijeh.flasher.backup;

public class BackupContract {

    public interface BackupPresenter {
        void backup();

        void restore();

        void onDestroy();
    }

    public interface BackupView {
        void onBackupCompleted();

        void onRestoreCompleted();

        void onFailed(Throwable throwable);
    }
}

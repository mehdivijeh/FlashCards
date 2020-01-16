package ir.mehdivijeh.flasher.backup.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.HashSet;

import ir.mehdivijeh.flasher.backup.BackupContract;
import ir.mehdivijeh.flasher.backup.repo.BackupAndRestoreRepo;
import ir.mehdivijeh.flasher.general.DateHelper;
import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.PreferenceUtils;
import ir.mehdivijeh.flasher.general.RetrofitProvider;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ir.mehdivijeh.flasher.general.GeneralConstants.BASE_URL;

public class BackupPresenterImpl implements BackupContract.BackupPresenter {

    private BackupContract.BackupView mView;
    private Context context;
    private HashSet<Subscription> subscriptions = new HashSet<>();


    public BackupPresenterImpl(BackupContract.BackupView mView, Context context) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void backup() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDbPath = "//data//ir.mehdivijeh.flasher//databases//flash_db";
                String backupPath = "backup.db";
                File currentDb = new File(data, currentDbPath);
                File backupDb = new File(sd, backupPath);
                FileChannel src = new FileInputStream(currentDb).getChannel();
                FileChannel dst = new FileOutputStream(backupDb).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                RequestBody backupFile = RequestBody.create(MediaType.parse("multipart/form-data"), backupDb);
                MultipartBody.Part backupFileBody = MultipartBody.Part.createFormData("userfile", getUiId()+".db", backupFile);
                RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), getUiId());

               /* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), backupDb);
                MultipartBody.Part backupFile = MultipartBody.Part.createFormData("userfile", "userfile", requestFile);
                RequestBody nameFile  = RequestBody.create(MediaType.parse("text"), getUiId());*/

                subscriptions.add(
                        RetrofitProvider.provideRetrofit(
                                HttpUrl.parse(BASE_URL), context).create(BackupAndRestoreRepo.class)
                                .backup(backupFileBody  , fileName)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(backupModel  -> {
                                    if(!backupModel.getError()) {
                                        addBackupDate();
                                        mView.onBackupCompleted();
                                    }else {
                                        mView.onFailed(new Throwable("Error"));
                                    }
                                }, throwable -> mView.onFailed(throwable)));
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }


    }



    private void addBackupDate() {
        PreferenceUtils.putStringPreference(GeneralConstants.LAST_BACKUP, DateHelper.getCurrentDate());
    }


    private String getUiId() {
        return PreferenceUtils.getStringPreference(GeneralConstants.UUID);
    }

    @Override
    public void restore() {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.execute(getUiId()+".db");
    }


    @Override
    public void onDestroy() {
        for (Subscription subscription : subscriptions)
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
    }



    private class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(String... fileName){
            try {
                URL u = new URL(BASE_URL + fileName[0]);
                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;

                File restoreFile = new File(Environment.getExternalStorageDirectory() + "/" + "backup.db");
                FileOutputStream fos = new FileOutputStream(restoreFile);
                while ((length = dis.read(buffer))>0) {
                    fos.write(buffer, 0, length);
                }

                try {
                    File sd = Environment.getDataDirectory();
                    if (sd.canWrite()) {
                        String currentDbPath = "//data//ir.mehdivijeh.flasher//databases//flash_db";
                        File currentDb = new File(sd, currentDbPath);
                        FileChannel src = new FileInputStream(restoreFile).getChannel();
                        FileChannel dst = new FileOutputStream(currentDb).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();


                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }

            } catch (IOException | SecurityException mue) {
                Log.e("SYNC getUpdate", "malformed url error", mue);

            }

            return "";
        }

        @Override
        protected void onPostExecute(String result){
            mView.onRestoreCompleted();
        }
    }
}

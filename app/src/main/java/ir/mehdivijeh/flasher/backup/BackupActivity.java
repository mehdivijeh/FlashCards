package ir.mehdivijeh.flasher.backup;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.backup.presenter.BackupPresenterImpl;
import ir.mehdivijeh.flasher.general.GeneralConstants;
import ir.mehdivijeh.flasher.general.PreferenceUtils;
import ir.mehdivijeh.flasher.general.TextUtil;
import retrofit2.HttpException;

public class BackupActivity extends AppCompatActivity implements BackupContract.BackupView {

    private static final String TAG = "BackupActivity";


    private BackupContract.BackupPresenter mPresenter;

    private Button btnRestore;
    private Button btnBackup;
    private TextView txtLastUpdate;
    private LottieAnimationView lottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        initView();
        initPresenter();
        initOnClick();
    }

    private void initView() {
        btnRestore = findViewById(R.id.btn_restore);
        btnBackup = findViewById(R.id.btn_backup);
        txtLastUpdate = findViewById(R.id.txt_last_update);
        lottie = findViewById(R.id.lottie);

        if (getLastBackupDate() != null && getLastBackupDate().length() > 0) {
            txtLastUpdate.setText("Last backup in " +getLastBackupDate());
        } else {
            txtLastUpdate.setText("there is no backup found!");
        }

        TextUtil.setFonts(getWindow().getDecorView());
    }

    private String getLastBackupDate() {
        return PreferenceUtils.getStringPreference(GeneralConstants.LAST_BACKUP);
    }

    private void initPresenter() {
        mPresenter = new BackupPresenterImpl(this, this);
    }

    private void initOnClick() {
        btnBackup.setOnClickListener(v -> {
            load();
            getPermissionAccess();
        });

        btnRestore.setOnClickListener(v -> {
            load();
            mPresenter.restore();
        });
    }

    private void getPermissionAccess() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = getString(R.string.permission_denied_title);
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle(getString(R.string.permission_dialog_title))
                .setSettingsDialogTitle(getString(R.string.permission_denied_title));

        Permissions.check(this, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                mPresenter.backup();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                getPermissionAccess();
            }
        });
    }

    private void load() {
        lottie.setAnimation("anim_upload.json");
        lottie.playAnimation();
        txtLastUpdate.setText("please wait...");
        btnBackup.setEnabled(false);
        btnRestore.setEnabled(false);
        mPresenter.backup();
    }

    private void onLoadComplete() {
        lottie.setAnimation("anim_backup.json");
        lottie.playAnimation();

        if (getLastBackupDate() != null && getLastBackupDate().length() > 0) {
            txtLastUpdate.setText("Last backup in " +getLastBackupDate());
        } else {
            txtLastUpdate.setText("there is no backup found!");
        }

        btnBackup.setEnabled(true);
        btnRestore.setEnabled(true);
    }

    @Override
    public void onBackupCompleted() {
        onLoadComplete();
    }

    @Override
    public void onRestoreCompleted() {
        onLoadComplete();
    }

    @Override
    public void onFailed(Throwable throwable) {
        onLoadComplete();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).code() == 404) {
                Toast.makeText(this, "no backup found!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "error, please check your internet.", Toast.LENGTH_SHORT).show();
            }


        } else{
            Toast.makeText(this, "error, please check your internet.", Toast.LENGTH_SHORT).show();

        }
        Log.d(TAG, "onFailed: " + throwable);
    }

        @Override
        protected void onDestroy () {
            super.onDestroy();

            if (mPresenter != null)
                mPresenter.onDestroy();
        }
    }

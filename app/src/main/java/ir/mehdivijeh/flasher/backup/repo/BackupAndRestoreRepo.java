package ir.mehdivijeh.flasher.backup.repo;

import ir.mehdivijeh.flasher.backup.model.BackupModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface BackupAndRestoreRepo {

    @POST("add_db.php")
    @Multipart
    Observable<BackupModel> backup(@Part MultipartBody.Part dbFile, @Part("uid") RequestBody uid);

    @GET("upload/{name}")
    Observable<String> restore(@Path(value = "name") String name);

}

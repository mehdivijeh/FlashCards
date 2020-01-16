
package ir.mehdivijeh.flasher.backup.model;

import com.google.gson.annotations.SerializedName;

public class BackupModel {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("error_msg")
    private String mErrorMsg;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        mErrorMsg = errorMsg;
    }

}

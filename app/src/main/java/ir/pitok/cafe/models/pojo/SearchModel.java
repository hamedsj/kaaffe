package ir.pitok.cafe.models.pojo;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.pojo.dataModels.SearchDataModel;

public class SearchModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private SearchDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SearchDataModel getData() {
        return data;
    }

    public void setData(SearchDataModel data) {
        this.data = data;
    }

}

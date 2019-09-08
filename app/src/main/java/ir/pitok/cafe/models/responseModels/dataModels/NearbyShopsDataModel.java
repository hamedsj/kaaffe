package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyShopsDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("results")
    private List<NearbyShopsItemDataModel> results;

    public List<NearbyShopsItemDataModel> getResults() {
        return results;
    }

    public void setResults(List<NearbyShopsItemDataModel> results) {
        this.results = results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

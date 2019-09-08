package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("results")
    private List<SearchResultItemDataModel> results;

    public List<SearchResultItemDataModel> getResults() {
        return results;
    }

    public void setResults(List<SearchResultItemDataModel> results) {
        this.results = results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

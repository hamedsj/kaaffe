package ir.pitok.cafe.viewModels;

import androidx.databinding.BaseObservable;

public class ShowPictureViewModel extends BaseObservable {

    private String link;

    public ShowPictureViewModel(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

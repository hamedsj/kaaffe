package ir.pitok.cafe.views.Interfaces;

import java.util.List;

import ir.pitok.cafe.viewModels.ShowPictureViewModel;

public interface LoadShowPhotoFragmentListener {
    public void onLoadShowPicturesFragment(int position, List<ShowPictureViewModel> list);
}

package ir.pitok.cafe.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.MainBinding;
import ir.pitok.cafe.viewModels.MainViewModel;
import ir.pitok.cafe.viewModels.ShowPictureViewModel;
import ir.pitok.cafe.views.Interfaces.BackCallListener;
import ir.pitok.cafe.views.Interfaces.LoadAboutFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentInLatLngListener;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadChoosedOrdersFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadMessagesFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadOrdersFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadPaymentsFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadProfileFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadSearchFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadShowPhotoFragmentListener;
import ir.pitok.cafe.views.fragments.AboutFrag;
import ir.pitok.cafe.views.fragments.MapFrag;
import ir.pitok.cafe.views.fragments.ChoosedOrdersFrag;
import ir.pitok.cafe.views.fragments.MessagesFrag;
import ir.pitok.cafe.views.fragments.OrdersFrag;
import ir.pitok.cafe.views.fragments.PaymentsFrag;
import ir.pitok.cafe.views.fragments.PermissionsFrag;
import ir.pitok.cafe.views.fragments.ProfileFrag;
import ir.pitok.cafe.views.fragments.SearchFrag;
import ir.pitok.cafe.views.fragments.ShowShopPhotoFrag;
import ir.pitok.cafe.views.fragments.SignUpLoginFrag;

import static ir.pitok.cafe.utility.CafeUtilities.changeLocale;
import static ir.pitok.cafe.utility.CafeUtilities.checkPermissions;

public class MainActivity extends AppCompatActivity {


    SearchFrag searchFrag;
    PermissionsFrag permissionsFrag;
    SignUpLoginFrag signUpFrag;
    MapFrag mapFrag;
    ProfileFrag profileFrag;
    MessagesFrag messagesFrag;
    AboutFrag aboutFrag;
    ChoosedOrdersFrag choosedOrdersFrag;
    OrdersFrag ordersFrag;
    PaymentsFrag paymentsFrag;
    Fragment activeFrag;
    private List<Integer> last_items_selected_for_payment;
    private LoadMapFragmentListener loadMapFragFromSignupLogin,loadMapFragFromPermission;
    private LoadSearchFragmentListener loadSearchFragFromMap;
    MainBinding binding;
    int counterToExitApp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLocale(getBaseContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainViewModel(new MainViewModel(getApplicationContext()));
        init();
        showFirstFragment();
    }

    private void init() {
        initListeners();
        signUpFrag = SignUpLoginFrag.newInstance(loadMapFragFromSignupLogin);
        mapFrag = MapFrag.newInstance(new LoadProfileFragmentListener() {
            @Override
            public void onLoadProfileListener() {
                showProfileFragFromMap();
            }
        }, new LoadAboutFragmentListener() {
            @Override
            public void onLoadAboutListener() {
                showAboutFragFromMap();
            }
        }, new LoadMessagesFragmentListener() {
            @Override
            public void onLoadMessagesListener() {
                showMessagesFragFromMap();
            }
        }, new LoadOrdersFragmentListener() {
            @Override
            public void onLoadOrdersListener() {
                showOrdersFragFromMap();
            }
        }, new LoadChoosedOrdersFragmentListener() {
            @Override
            public void onLoadChoosedOrdersListener(List<Integer> items) {
                showChoosedOrdersFragFromMap(items);
            }
        }, new LoadShowPhotoFragmentListener() {
            @Override
            public void onLoadShowPicturesFragment(int position, List<ShowPictureViewModel> list) {
                showShowPicturesFragFromMap(position, list);
            }
        }, loadSearchFragFromMap, -1, -1);
        permissionsFrag = PermissionsFrag.newInstance(loadMapFragFromPermission);
        searchFrag = SearchFrag.newInstance(new LoadMapFragmentInLatLngListener() {
            @Override
            public void onLoadMapFragment(double lat, double lng) {
                showMapInLatLngFragFromSearch(new LatLng(lat,lng));
            }
        }, new LoadMapFragmentListener() {
            @Override
            public void onLoadMapFragment() {
                showMapFragFromSearch();
            }
        });
        ordersFrag = OrdersFrag.newInstance(new BackCallListener() {
            @Override
            public void onBackCall() {
                showMapFragFromOrders();
            }
        });
        messagesFrag = MessagesFrag.newInstance(new BackCallListener() {
            @Override
            public void onBackCall() {
                showMapFragFromMessages();
            }
        });
        aboutFrag = AboutFrag.newInstance(new BackCallListener() {
            @Override
            public void onBackCall() {
                showMapFragFromAbout();
            }
        });
        profileFrag = ProfileFrag.newInstance(new BackCallListener() {
            @Override
            public void onBackCall() {
                showMapFragFromProfile();
            }
        });
    }

    private void showFirstFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(),signUpFrag);
        activeFrag = signUpFrag;
        ft.commit();
    }

    private void showMapFragFromSignupLogin(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    private void showMapFragFromPermissions(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    private void showMapFragFromSearch(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    private void showMapInLatLngFragFromSearch(LatLng latLng){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(latLng);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    private void showPermissionsFragFromSignupLogin(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(), permissionsFrag);
        activeFrag = permissionsFrag;
        ft.commit();
    }

    private void showSearchFragFromMap(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(), searchFrag);
        activeFrag = searchFrag;
        ft.commit();
    }

    private void showChoosedOrdersFragFromMap(List<Integer> items){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        choosedOrdersFrag = ChoosedOrdersFrag.newInstance(items, new LoadMapFragmentListener() {
            @Override
            public void onLoadMapFragment() {
                showMapFragFromChoosedOrders();
            }
        }, new LoadPaymentsFragmentListener() {
            @Override
            public void onLoadPaymentsListener(List<Integer> items) {
                showPaymentsFragFromMap(items);
            }
        });
        ft.replace(binding.mainFillFrameLayout.getId(), choosedOrdersFrag);
        activeFrag = choosedOrdersFrag;
        ft.commit();
    }

    private void showPaymentsFragFromMap(List<Integer> items){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        paymentsFrag = PaymentsFrag.newInstance(items, new LoadChoosedOrdersFragmentListener() {
            @Override
            public void onLoadChoosedOrdersListener(List<Integer> items) {
                showChoosedOrdersFragFromMap(items);
            }
        });
        last_items_selected_for_payment = items;
        ft.replace(binding.mainFillFrameLayout.getId(),paymentsFrag);
        activeFrag = paymentsFrag;
        ft.commit();
    }

    public void showMapFragFromChoosedOrders(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    public void showOrdersFragFromMap(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(),ordersFrag);
        activeFrag = ordersFrag;
        ft.commit();
    }

    private void showMapFragFromOrders(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    public void showMapFragFromMessages(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    public void showMessagesFragFromMap(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(),messagesFrag);
        activeFrag = messagesFrag;
        ft.commit();
    }

    public void showMapFragFromAbout(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    public void showAboutFragFromMap(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(),aboutFrag);
        activeFrag = aboutFrag;
        ft.commit();
    }

    public void showMapFragFromProfile(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mapFrag.setSearchLatLng(null);
        ft.replace(binding.mainFillFrameLayout.getId(),mapFrag);
        activeFrag = mapFrag;
        ft.commit();
    }

    public void showProfileFragFromMap(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.mainFillFrameLayout.getId(),profileFrag);
        activeFrag = profileFrag;
        ft.commit();
    }

    private void showShowPicturesFragFromMap(int position, List<ShowPictureViewModel> list){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        ShowShopPhotoFrag showShopPhotosFrag = ShowShopPhotoFrag.newInstance(list, position, new BackCallListener() {
            @Override
            public void onBackCall() {
                removeShowShopPhotoFrag();
            }
        });
        ft.add(binding.mainFillFrameLayout.getId(), showShopPhotosFrag);
        activeFrag = showShopPhotosFrag;
        ft.commit();
    }

    private void initListeners() {
        loadMapFragFromSignupLogin = new LoadMapFragmentListener() {
            @Override
            public void onLoadMapFragment() {
                if (!checkPermissions(getApplicationContext())) {
                    showPermissionsFragFromSignupLogin();
                    return;
                }
                showMapFragFromSignupLogin();
            }
        };

        loadMapFragFromPermission = new LoadMapFragmentListener() {
            @Override
            public void onLoadMapFragment() {
                showMapFragFromPermissions();
            }
        };

        loadSearchFragFromMap = new LoadSearchFragmentListener() {
            @Override
            public void onLoadSearchFragment() {
                showSearchFragFromMap();
            }
        };

    }

    @Override
    public void onBackPressed() {
        if (activeFrag instanceof SearchFrag){
            showMapFragFromSearch();
        }else if (activeFrag instanceof PermissionsFrag) {
            showFirstFragment();
        }else if(activeFrag instanceof ShowShopPhotoFrag){
            removeShowShopPhotoFrag();
        }else if (activeFrag instanceof ProfileFrag){
            showMapFragFromProfile();
        }else if (activeFrag instanceof OrdersFrag){
            showMapFragFromOrders();
        }else if (activeFrag instanceof MessagesFrag){
            showMapFragFromMessages();
        }else if (activeFrag instanceof AboutFrag){
            showMapFragFromAbout();
        }else if (activeFrag instanceof ChoosedOrdersFrag){
            showMapFragFromChoosedOrders();
        }else if (activeFrag instanceof PaymentsFrag){
            showChoosedOrdersFragFromMap(last_items_selected_for_payment);
        }else if (activeFrag instanceof MapFrag){
            if (((MapFrag) activeFrag).getBottomSheetState() == BottomSheetBehavior.STATE_HIDDEN || ((MapFrag) activeFrag).getBottomSheetState() == BottomSheetBehavior.STATE_COLLAPSED){
                counterToExitApp+=1;
                if (counterToExitApp==1) Toast.makeText(getApplicationContext(),"برای خروج دوباره کلیک کنید",Toast.LENGTH_SHORT).show();
                if (counterToExitApp==2) super.onBackPressed();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counterToExitApp=0;
                    }
                },500);
            }else{
                ((MapFrag) activeFrag).hideBottomSheet();
            }
        }
    }

    private void removeShowShopPhotoFrag() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        ft.remove(activeFrag);
        activeFrag = mapFrag;
        ft.commit();
    }
}

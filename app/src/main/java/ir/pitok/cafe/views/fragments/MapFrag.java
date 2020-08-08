package ir.pitok.cafe.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.MapBinding;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.models.pojo.dataModels.ItemShopMenuDataModel;
import ir.pitok.cafe.models.pojo.dataModels.NearbyShopsItemDataModel;
import ir.pitok.cafe.models.pojo.NearbyShopsModel;
import ir.pitok.cafe.models.pojo.ShopDetailModel;
import ir.pitok.cafe.models.pojo.ShopPicturesItemModel;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.MapViewModel;
import ir.pitok.cafe.viewModels.ShowPictureViewModel;
import ir.pitok.cafe.views.Interfaces.LoadAboutFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadChoosedOrdersFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadMessagesFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadOrdersFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadProfileFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadSearchFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadShowPhotoFragmentListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.adapters.ShopMenuAdapter;
import ir.pitok.cafe.views.adapters.ShopPicturesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;
import static ir.pitok.cafe.utils.CafeUtilities.en2prText;
import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.getLastLocation;
import static ir.pitok.cafe.utils.CafeUtilities.getStandardUrl;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;
import static ir.pitok.cafe.utils.CafeUtilities.isUrlValid;
import static ir.pitok.cafe.utils.CafeUtilities.setLastLocation;

public class MapFrag extends CafeFragment implements OnMapReadyCallback {

    private LatLng lastLatLng;
    private LatLng searchLatLng;
    private GoogleMap globalMap;
    private LoadSearchFragmentListener loadSearchListener;
    private Requests requests;
    private List<NearbyShopsItemDataModel> shopsInMap = new ArrayList<>();
    private MapBinding binding;
    private MapViewModel mvm;
    private ShopPicturesAdapter picturesAdapter;
    private ShopMenuAdapter menuAdapter;
    private List<MapViewModel> picturesList = new ArrayList<>();
    private List<MapViewModel> menuList = new ArrayList<>();
    private Bitmap mainPicture;
    private LoadShowPhotoFragmentListener loadShowPhotoListener;
    private LoadChoosedOrdersFragmentListener loadChoosedOrdersFragmentListener;
    private LoadOrdersFragmentListener loadOrdersFragmentListener;
    private LoadMessagesFragmentListener loadMessagesFragmentListener;
    private LoadAboutFragmentListener loadAboutFragmentListener;
    private LoadProfileFragmentListener loadProfileFragmentListener;
    private DrawerFrag drawerFrag;

    public static MapFrag newInstance(LoadProfileFragmentListener loadProfileFragmentListener,
                                      LoadAboutFragmentListener loadAboutFragmentListener,
                                      LoadMessagesFragmentListener loadMessagesFragmentListener,
                                      LoadOrdersFragmentListener loadOrdersFragmentListener,
                                      LoadChoosedOrdersFragmentListener loadChoosedOrdersFragmentListener,
                                      LoadShowPhotoFragmentListener loadShowPhotoListener,
                                      LoadSearchFragmentListener loadSearchListener, double lat, double lng) {
        Bundle args = new Bundle();
        MapFrag fragment = new MapFrag();
        fragment.setArguments(args);
        fragment.loadSearchListener = loadSearchListener;
        fragment.loadShowPhotoListener = loadShowPhotoListener;
        fragment.loadOrdersFragmentListener = loadOrdersFragmentListener;
        fragment.loadChoosedOrdersFragmentListener = loadChoosedOrdersFragmentListener;
        fragment.loadMessagesFragmentListener = loadMessagesFragmentListener;
        fragment.loadAboutFragmentListener = loadAboutFragmentListener;
        fragment.loadProfileFragmentListener = loadProfileFragmentListener;
        if (lat!=-1&&lng!=-1)
            fragment.searchLatLng = new LatLng(lat,lng);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        mvm = new MapViewModel(allContext,binding.MFBottomSheet.CSBSRoot,allActivity,binding.MFBottomSheet.CSBSShowMenuButtomSheet.SMBSRoot);
        binding.setMapVM(mvm);
        init(savedInstanceState);
        setListeners();
        return binding.getRoot();
    }

    private void init(Bundle savedInstanceState) {
        drawerFrag = (DrawerFrag) getChildFragmentManager().findFragmentById(R.id.MFDrawer);
        mvm.setShop_bootomSheetBehaviorState(BottomSheetBehavior.STATE_HIDDEN);
        ViewCompat.setElevation(binding.MFToolbar,dp2Px(8));
        ViewCompat.setElevation(binding.MFDisableView,dp2Px(16));
        LinearLayoutManager llm = new LinearLayoutManager(allContext);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        llm.setReverseLayout(true);
        binding.MFBottomSheet.CSBSMorePictures.setLayoutManager(llm);
        LinearLayoutManager llm1 = new LinearLayoutManager(allContext);
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        llm1.setReverseLayout(true);
        binding.MFBottomSheet.CSBSShowMenuButtomSheet.SMBSItemsRv.setLayoutManager(llm1);

        lastLatLng = getLastLocation(allContext);
        requests = new Requests();
        setLocationListener();
        binding.MFMapView.onCreate(savedInstanceState);
        binding.MFMapView.onResume();
        MapsInitializer.initialize(allContext);
        binding.MFMapView.getMapAsync(this);
    }

    private void setListeners() {
        binding.MFMyLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalMap == null)return;
                globalMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,18));
            }
        });
        binding.MFSearchIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSearchListener.onLoadSearchFragment();
            }
        });
        binding.MFDisableView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        binding.MFDrawerIc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View view) {
                binding.MFDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        binding.MFBottomSheet.CSBSOrdersBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChoosedOrdersFragmentListener.onLoadChoosedOrdersListener(mvm.getActive_orders());
            }
        });
        drawerFrag.setOnOrdersClick(loadOrdersFragmentListener);
        drawerFrag.setOnMessagesClick(loadMessagesFragmentListener);
        drawerFrag.setOnAboutClick(loadAboutFragmentListener);
        drawerFrag.setOnProfileClick(loadProfileFragmentListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        globalMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(allContext, R.raw.map_style));
        googleMap.setMyLocationEnabled(true);
        if (searchLatLng == null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 17));
        }else{
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchLatLng, 16));
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (NearbyShopsItemDataModel shop : shopsInMap){
                    if (marker.getPosition().latitude == Double.parseDouble(shop.getLat()) && marker.getPosition().longitude == Double.parseDouble(shop.getLng())){
                        showShopDetails(shop.getShop_id());
                    }
                }
                return true;
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng currentLatLng = googleMap.getCameraPosition().target;
                requests.getNearbyCoffees(getApiToken(allContext),
                        String.valueOf(currentLatLng.latitude),
                        String.valueOf(currentLatLng.longitude), new Callback<NearbyShopsModel>() {
                            @Override
                            public void onResponse(Call<NearbyShopsModel> call, Response<NearbyShopsModel> response) {
                                if (!response.isSuccessful()){
                                    Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                int status = response.body().getStatus();
                                String message = response.body().getData().getMessage();
                                if (status==401 && message.equals("api_token")){
                                    //todo: login again
                                    return;
                                }
                                if (status!=200){
                                    Toast.makeText(allContext,"خطا در روند جستجو",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                shopsInMap.clear();
                                shopsInMap = response.body().getData().getResults();
                                googleMap.clear();
                                for (NearbyShopsItemDataModel shop: shopsInMap){
                                    Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(shop.getLat()),Double.parseDouble(shop.getLng()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.shop)));
                                    marker.setSnippet(shop.getName());
                                }
                            }
                            @Override
                            public void onFailure(Call<NearbyShopsModel> call, Throwable t) {
                                handleFailor(t,allContext);
                            }
                        });
            }
        });

    }

    private void showShopDetails(int shop_id) {
        showLoading();
        requests.getShopDetails(getApiToken(allContext), shop_id, new Callback<ShopDetailModel>() {
            @Override
            public void onResponse(Call<ShopDetailModel> call, final Response<ShopDetailModel> response) {
                if (!response.isSuccessful()){
                    hideLoading();
                    Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                    return;
                }
                int status = response.body().getStatus();
                String message = response.body().getData().getMessage();
                if (status==401 && message.equals("api_token")){
                    //todo: login again
                    return;
                }
                if (status!=200){
                    hideLoading();
                    Toast.makeText(allContext,"خطا در روند دریافت اطلاعات؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                    return;
                }

                String mainPicLink = response.body().getData().getMainPicture();
                final String details = response.body().getData().getDetails();
                final String workTime = response.body().getData().getWrok_time();

                List<ShopPicturesItemModel> pics = response.body().getData().getPictures();
                picturesList.clear();

                for (ShopPicturesItemModel pic : pics){
                    if (!isUrlValid(pic.getLink()))continue;
                    String link = getStandardUrl(pic.getLink());
                    picturesList.add(new MapViewModel(link));
                }

                List<ItemShopMenuDataModel> items = response.body().getData().getMenu();
                menuList.clear();

                for (ItemShopMenuDataModel item:items){
                    if (!isUrlValid(item.getPicture_link()))continue;
                    String link = getStandardUrl(item.getPicture_link());
                    menuList.add(new MapViewModel(item.getItem_id(),en2prText(item.getName()),en2prText(item.getPrice()+" تومان"),link,en2prText(item.getItem_type())));
                }

                if (!mainPicLink.isEmpty()){
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            hideLoading();
                            mainPicture = bitmap;
                            ImageView mainPicture = binding.MFBottomSheet.CSBSMainPicture;
                            binding.MFBottomSheet.CSBSDetails.setText(en2prText(details));
                            binding.MFBottomSheet.CSBSWorkTime.setText(en2prText(workTime));
                            mainPicture.setImageBitmap(bitmap);
                            mvm.setShop_bootomSheetBehaviorState(BottomSheetBehavior.STATE_EXPANDED);
                            mvm.setMenu_bootomSheetBehaviorState(BottomSheetBehavior.STATE_COLLAPSED);
                            setPicturesAdapter();
                            setMenuAdapter();
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            hideLoading();
                            Toast.makeText(allContext,"خطا در روند دریافت اطلاعات؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    binding.MFBottomSheet.CSBSMainPicture.setTag(target);
                    Picasso.get().load(mainPicLink).into(target);
                }
                mvm.setActive_orders(new ArrayList<Integer>());
                binding.MFBottomSheet.CSBSOrders.setText(en2prText("سفارش ها : " + mvm.getActive_orders_count()));
                binding.MFBottomSheet.CSBSOrdersBg.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ShopDetailModel> call, Throwable t) {
                hideLoading();
                handleFailor(t,allContext);
            }
        });
    }

    private void setMenuAdapter() {
        menuAdapter = new ShopMenuAdapter(menuList, new RecyclerViewClickListener() {
            @Override
            public void onClickListener(int position) {
                mvm.setNew_active_order(menuList.get(position).getMenu_item_id());
                binding.MFBottomSheet.CSBSOrders.setText(en2prText("سفارش ها : " + mvm.getActive_orders_count()));
                if (mvm.getActive_orders_count()>0) {
                    binding.MFBottomSheet.CSBSOrdersBg.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.MFBottomSheet.CSBSShowMenuButtomSheet.SMBSItemsRv.setAdapter(menuAdapter);
    }

    private void setPicturesAdapter(){
        picturesAdapter = new ShopPicturesAdapter(picturesList, new RecyclerViewClickListener() {
            @Override
            public void onClickListener(int position) {
                List<ShowPictureViewModel> list = new ArrayList<>();
                for(MapViewModel pic : picturesList){
                    list.add(new ShowPictureViewModel(pic.getShop_link()));
                }
                loadShowPhotoListener.onLoadShowPicturesFragment(position,list);
            }
        });
        binding.MFBottomSheet.CSBSMorePictures.setAdapter(picturesAdapter);
    }

    public void hideBottomSheet(){
        mvm.setShop_bootomSheetBehaviorState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public int getBottomSheetState(){
        return mvm.getShop_bootomSheetBehaviorState();
    }

    @SuppressLint("MissingPermission")
    private void setLocationListener(){
        LocationManager locationManager = (LocationManager) allContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L,10L, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                setLastLocation(lastLatLng,allContext);
//                if (globalMap != null)globalMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,18));
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    public LatLng getSearchLatLng() {
        return searchLatLng;
    }

    public void setSearchLatLng(LatLng searchLatLng) {
        this.searchLatLng = searchLatLng;
    }

    @Override
    public void onLowMemory() {
        binding.MFMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onPause() {
        binding.MFMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        binding.MFMapView.onStop();
        super.onStop();
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onResume() {
        binding.MFMapView.onResume();
        binding.MFDrawerLayout.closeDrawer(Gravity.RIGHT);
        super.onResume();
    }

    private void showLoading(){
        binding.MFDisableView.setVisibility(View.VISIBLE);
        binding.MFLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        binding.MFDisableView.setVisibility(View.GONE);
        binding.MFLoading.setVisibility(View.INVISIBLE);
    }

}

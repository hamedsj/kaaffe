package ir.pitok.cafe.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ir.pitok.cafe.utility.CafeUtilities;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if (retrofit==null){

//            CertificatePinner certificatePinner = new CertificatePinner.Builder()
//            .add("robo-khabar.ir", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
//            .build();

            OkHttpClient client1 = new OkHttpClient.Builder()
//                    .certificatePinner(certificatePinner)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder().baseUrl(CafeUtilities.path)
                    .client(client1)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }
        return retrofit;
    }
}

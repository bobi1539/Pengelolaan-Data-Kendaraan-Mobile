package zero.programmer.data.kendaraan.api;

public class GetConnection {

    /**
     * class dan variable untuk ambil koneksi retrofit
     * */
    public static final ApiRequest apiRequest = RetroServer
            .getRetrofit()
            .create(ApiRequest.class);

}

package zero.programmer.data.kendaraan.api;

import retrofit2.Call;
import retrofit2.http.GET;
import zero.programmer.data.kendaraan.model.ResponseVehicle;

public interface ApiRequest {

    @GET("api/vehicles")
    Call<ResponseVehicle> listVehicle();

}

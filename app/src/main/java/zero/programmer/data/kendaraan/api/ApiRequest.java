package zero.programmer.data.kendaraan.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import zero.programmer.data.kendaraan.model.ResponseGetVehicle;
import zero.programmer.data.kendaraan.model.ResponseVehicle;

public interface ApiRequest {

    @GET("api/vehicles")
    Call<ResponseVehicle> listVehicle();

    @GET("api/vehicles/{registrationNumber}")
    Call<ResponseGetVehicle> getVehicle(
            @Path("registrationNumber") String registrationNumber
    );

}

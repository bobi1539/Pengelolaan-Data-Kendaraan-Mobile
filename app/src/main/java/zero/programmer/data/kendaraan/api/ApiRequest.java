package zero.programmer.data.kendaraan.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import zero.programmer.data.kendaraan.model.ResponseGetVehicle;
import zero.programmer.data.kendaraan.model.ResponseVehicle;
import zero.programmer.data.kendaraan.model.VehicleData;

public interface ApiRequest {

    @GET("api/vehicles")
    Call<ResponseVehicle> listVehicle();

    @GET("api/vehicles/{registrationNumber}")
    Call<ResponseGetVehicle> getVehicle(
            @Path("registrationNumber") String registrationNumber
    );

    @POST("api/vehicles")
    Call<ResponseGetVehicle> createVehicle(
            @Body VehicleData vehicleData
    );

    @DELETE("api/vehicles/{registrationNumber}")
    Call<ResponseGetVehicle> deleteVehicle(
            @Path("registrationNumber") String registrationNumber
    );

    @PUT("api/vehicles")
    Call<ResponseGetVehicle> updateVehicle(
            @Body VehicleData vehicleData
    );

}

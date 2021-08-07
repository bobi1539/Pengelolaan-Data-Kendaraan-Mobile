package zero.programmer.data.kendaraan.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import zero.programmer.data.kendaraan.response.ResponseGetVehicle;
import zero.programmer.data.kendaraan.response.ResponseVehicle;
import zero.programmer.data.kendaraan.model.VehicleData;

public interface ApiRequest {

    @GET("api/vehicles")
    Call<ResponseVehicle> listVehicle(@Header("X-Api-Key") String apiKey);

    @GET("api/vehicles/{registrationNumber}")
    Call<ResponseGetVehicle> getVehicle(
            @Header("X-Api-Key") String apiKey,
            @Path("registrationNumber") String registrationNumber
    );

    @POST("api/vehicles")
    Call<ResponseGetVehicle> createVehicle(
            @Header("X-Api-Key") String apiKey,
            @Body VehicleData vehicleData
    );

    @DELETE("api/vehicles/{registrationNumber}")
    Call<ResponseGetVehicle> deleteVehicle(
            @Header("X-Api-Key") String apiKey,
            @Path("registrationNumber") String registrationNumber
    );

    @PUT("api/vehicles")
    Call<ResponseGetVehicle> updateVehicle(
            @Header("X-Api-Key") String apiKey,
            @Body VehicleData vehicleData
    );

}

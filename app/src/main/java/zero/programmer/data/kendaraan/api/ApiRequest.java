package zero.programmer.data.kendaraan.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.response.ResponseListData;
import zero.programmer.data.kendaraan.model.VehicleData;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public interface ApiRequest {

    @GET("api/vehicles")
    Call<ResponseListData<Vehicle>> listVehicle(@Header("X-Api-Key") String apiKey);

    @GET("api/vehicles/{registrationNumber}")
    Call<ResponseOneData<Vehicle>> getVehicle(
            @Header("X-Api-Key") String apiKey,
            @Path("registrationNumber") String registrationNumber
    );

    @POST("api/vehicles")
    Call<ResponseOneData<Vehicle>> createVehicle(
            @Header("X-Api-Key") String apiKey,
            @Body VehicleData vehicleData
    );

    @DELETE("api/vehicles/{registrationNumber}")
    Call<ResponseOneData<Vehicle>> deleteVehicle(
            @Header("X-Api-Key") String apiKey,
            @Path("registrationNumber") String registrationNumber
    );

    @PUT("api/vehicles")
    Call<ResponseOneData<Vehicle>> updateVehicle(
            @Header("X-Api-Key") String apiKey,
            @Body VehicleData vehicleData
    );

    @GET("api/users")
    Call<ResponseListData<User>> listUser(@Header("X-Api-Key") String apiKey);

    @GET("api/users/{username}")
    Call<ResponseOneData<User>> getUser(
            @Header("X-Api-Key") String apiKey,
            @Path("username") String username
    );

    @POST("api/users")
    Call<ResponseOneData<User>> createUser(
            @Header("X-Api-Key") String apiKey,
            @Body User userData
    );

}

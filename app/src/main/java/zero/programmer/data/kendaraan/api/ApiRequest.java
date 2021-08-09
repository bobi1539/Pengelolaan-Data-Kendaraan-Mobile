package zero.programmer.data.kendaraan.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.model.LoginData;
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

    @DELETE("api/users/{username}")
    Call<ResponseOneData<User>> deleteUser(
            @Header("X-Api-Key") String apiKey,
            @Path("username") String username
    );

    @PATCH("api/users/{username}")
    Call<ResponseOneData<User>> updateUser(
            @Header("X-Api-Key") String apiKey,
            @Path("username") String username,
            @Body Map<Object, Object> userData
            );

    @POST("api/users/login")
    Call<ResponseOneData<LoginData>> loginUser(
            @Header("X-Api-Key") String apiKey,
            @Body LoginData loginData
    );

    @GET("api/drivers")
    Call<ResponseListData<Driver>> listDriver(@Header("X-Api-Key") String apiKey);

    @GET("api/drivers/{idDriver}")
    Call<ResponseOneData<Driver>> getDriver(
            @Header("X-Api-Key") String apiKey,
            @Path("idDriver") String idDriver
    );

    @POST("api/drivers")
    Call<ResponseOneData<Driver>> createDriver(
            @Header("X-Api-Key") String apiKey,
            @Body Driver driverData
    );

    @DELETE("api/drivers/{idDriver}")
    Call<ResponseOneData<Driver>> deleteDriver(
            @Header("X-Api-Key") String apiKey,
            @Path("idDriver") String idDriver
    );

    @PUT("api/drivers")
    Call<ResponseOneData<Driver>> updateDriver(
            @Header("X-Api-Key") String apiKey,
            @Body Driver driverData
    );

}

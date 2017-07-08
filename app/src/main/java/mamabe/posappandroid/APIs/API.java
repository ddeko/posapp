package mamabe.posappandroid.APIs;


import java.util.ArrayList;

import mamabe.posappandroid.Models.EmployeeBody;
import mamabe.posappandroid.Models.EmployeePostResponse;
import mamabe.posappandroid.Models.EmployeeResponse;
import mamabe.posappandroid.Models.MenuBody;
import mamabe.posappandroid.Models.MenuCategoryResponse;
import mamabe.posappandroid.Models.MenuPostResponse;
import mamabe.posappandroid.Models.MenuResponse;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderDetailBody;
import mamabe.posappandroid.Models.OrderDetailPostResponse;
import mamabe.posappandroid.Models.OrderPostResponse;
import mamabe.posappandroid.Models.OrderResponse;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.Models.RoleResponse;
import mamabe.posappandroid.Models.Setting;
import mamabe.posappandroid.Models.SettingPostResponse;
import mamabe.posappandroid.Models.SettingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DedeEko on 5/18/2016.
 */
public interface API {

    //Employee
    @GET("getAllEmployee")
    Call<EmployeeResponse>
    getEmployeeList();

    @POST("insertEmployee")
    Call<EmployeePostResponse>
    postEmployee(@Body EmployeeBody employeeBody);

    @POST("updateEmployee")
    Call<EmployeePostResponse>
    updateEmployee(@Body EmployeeBody employeeBody);

    @POST("deleteEmployee")
    Call<EmployeePostResponse>
    deleteEmployee(@Body EmployeeBody employeeBody);

    //Role
    @GET("getAllRole")
    Call<RoleResponse>
    getRoleList();

    //Setting
    @GET("getSetting")
    Call<SettingResponse>
    getSetting();

    @POST("updateSetting")
    Call<SettingPostResponse>
    updateSetting(@Body Setting Setting);

    //Menu Category
    @GET("getMenuCategory")
    Call<MenuCategoryResponse>
    getMenuCategory(@Query("menu_type") String menu_type);

    @GET("getMenuType")
    Call<MenuCategoryResponse>
    getMenuType();

    //Menu
    @GET("getMenuBy")
    Call<MenuResponse>
    getMenuBy(@Query("menu_type") String menu_type,
              @Query("menuCategory_name") String menuCategory_name);

    @POST("insertMenu")
    Call<MenuPostResponse>
    postMenu(@Body MenuBody menuBody);

    @POST("updateMenu")
    Call<MenuPostResponse>
    updateMenu(@Body MenuBody menuBody);

    @POST("deleteMenu")
    Call<MenuPostResponse>
    deleteMenu(@Body MenuBody menuBody);

    @GET("getMenuByAvailability")
    Call<MenuResponse>
    getMenuByAvailability(@Query("menu_type") String menu_type,
                          @Query("menuCategory_name") String menuCategory_name);


    //ORDER

    @POST("insertOrderDetail")
    Call<OrderDetailPostResponse>
    postOrderDetail(@Body OrderDetailBody orderDetailBody);

    @POST("insertOrder")
    Call<OrderPostResponse>
    postOrder(@Body OrderBody orderBody);

    @GET("getAllOrder")
    Call<OrderResponse>
    getAllOrder();

    @GET("getOrderDetailbyId")
    Call<OrderTableResponse>
    getOrderDetailbyId(@Query("order_id") String order_id);
//    //Penyakit
//    @GET("penyakitall/{page}/{count_page}")
//    Call<DiseaseResponse>
//    getDiseaseList(@Path("page") String page, @Path("count_page") String count_page);
//
//    @GET("penyakit/{page}/{count_page}")
//    Call<DiseaseResponse>
//    getDiseaseListByLokasi(@Path("page") String page, @Path("count_page") String count_page, @Query("id_desa") String id_desa);
//
//    @POST("penyakit")
//    Call<DiseasePostResponse>
//    postDisease(@Body DiseaseBody diseaseBody);
//    @DELETE("penyakit")
//    Call<DeletePenyakitResponse> deletePenyakit(@Query("Id_penyakit") String id_penyakit);
//
//    @PUT("penyakit")
//    Call<PutPenyakitResponse> editPenyakit(@Query("Id_penyakit") String id_penyakit,
//                                           @Body PutPenyakitBody putPenyakitBody);
//
//    @GET("tipepenyakit/{page}/{count_page}")
//    Call<TipePenyakitResponse>
//    getTipePenyakitList(@Path("page") String page, @Path("count_page") String count_page);
//
//    @POST("multimedia")
//    Call<MultimediaPostResponse>
//    postMultimedia(@Body MultimediaBody multimediaBody);
}

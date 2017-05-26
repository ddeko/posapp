package mamabe.posappandroid.APIs;


import mamabe.posappandroid.Models.EmployeeBody;
import mamabe.posappandroid.Models.EmployeePostResponse;
import mamabe.posappandroid.Models.EmployeeResponse;
import mamabe.posappandroid.Models.RoleResponse;
import mamabe.posappandroid.Models.Setting;
import mamabe.posappandroid.Models.SettingPostResponse;
import mamabe.posappandroid.Models.SettingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

package com.kingdomredherous.notesapp.apiFolder;

import com.kingdomredherous.notesapp.Note;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    @FormUrlEncoded
    @POST("saveNote.php")
    Call<Note> saveNote(
            @Field("title") String title,
            @Field("popis") String popis
    );

    @GET("showNote.php")
    Call<List<Note>> getNote();

    @FormUrlEncoded
    @POST("updateNote.php")
    Call<Note> updateNote(
            @Field("id_seznam") int id,
            @Field("title") String title,
            @Field("popis") String popis
    );

}

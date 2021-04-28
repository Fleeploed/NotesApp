package com.kingdomredherous.notesapp.ApiFolder;

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
            @Field("id_user") int id_user,
            @Field("title") String title,
            @Field("popis") String popis
    );

    @FormUrlEncoded
    @POST("showNote.php")
    Call<List<Note>> getNote(
            @Field("id_user") int id_user
    );

    @FormUrlEncoded
    @POST("updateNote.php")
    Call<Note> updateNote(
            @Field("id_seznam") int id,
            @Field("title") String title,
            @Field("popis") String popis
    );

    @FormUrlEncoded
    @POST("deleteNote.php")
    Call<Note> deleteNote(
            @Field("id_seznam") int id
    );

    @FormUrlEncoded
    @POST("signin.php")
    Call<Note> signIn(
            @Field("login") String login,
            @Field("heslo") String heslo
    );

    @FormUrlEncoded
    @POST("signup.php")
    Call<Note> signUp(
            @Field("jmeno") String jmeno,
            @Field("login") String login,
            @Field("email") String email,
            @Field("heslo") String heslo
    );
}

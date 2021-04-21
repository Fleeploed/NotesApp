package com.kingdomredherous.notesapp.trash;

import com.kingdomredherous.notesapp.apiFolder.API;
import com.kingdomredherous.notesapp.apiFolder.APIClient;
import com.kingdomredherous.notesapp.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }
    void getData(){
        mainView.showLoading();
        API api = APIClient.getClient().create(API.class);
        Call<List<Note>> listCall = api.getNote();
        listCall.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                mainView.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    mainView.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                mainView.hideLoading();
                mainView.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}

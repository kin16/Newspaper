package com.program.newspaper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.database.DatabaseError;
import com.program.newspaper.model.Item;
import com.program.newspaper.model.Model;
import com.program.newspaper.view.DetailView;

import java.util.ArrayList;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {
    private final Model model;

    public DetailPresenter(){
        model = new Model();
    }

    //при начале загрузки отобразить это на фрагменте и запустить поиск
    public void loadItem(int id) {
        getViewState().startLoad();
        model.findItemFromDbRx(id, this);
    }

    // сохранение элемента
    public void saveItem(Item item){
        model.saveItem(item);
    }

    // обновление элемента
    public void updateItem(int id, Item item){
        model.deleteItem(id);
        model.saveItem(item);
    }

    // Если элемент загружен, то обновить данные фрагмента
    public void itemLoaded(Item item){
        getViewState().dataLoaded(item);
    }

    // Показ ошибки
    public void showErrorMsg(DatabaseError error) {
        getViewState().showError(error.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.destroy();
    }
}

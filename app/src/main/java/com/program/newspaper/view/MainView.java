package com.program.newspaper.view;

import com.arellomobile.mvp.MvpView;
import com.program.newspaper.model.Item;

import java.util.List;

// Интерфейс описывающий какие методы вызванные из презентера фрагмент должен поддерживать
public interface MainView extends MvpView {
    void startLoad();
    void updateData(List<Item> data);
    void showError(String msg);
    void showEmpty();
}

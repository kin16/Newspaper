package com.program.newspaper.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.program.newspaper.presenters.DetailPresenter;
import com.program.newspaper.presenters.MainPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.sax.SAXResult;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class Model {
    private static DatabaseReference database;
    private static String TAG = "NEWSPAPEER";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Model(){
        // Получение бд
        database = FirebaseDatabase.getInstance("https://newspaper-2f598-default-rtdb.europe-west1.firebasedatabase.app").getReference(TAG);
    }

    // Проверка интернета и бд
    public void checkDataExists(MainPresenter presenter){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    presenter.dataNotExists();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "not woek");
            }
        });
    }

    // Загрузка элементов, вызов updateData презентера при успехе
    public void loadItemsFromDbRx(MainPresenter presenter){
        compositeDisposable.add(RxFirebaseDatabase.observeSingleValueEvent(database, dataSnapshot -> {
            Log.d(TAG, dataSnapshot.toString());
            List<Item> data = new ArrayList();
            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                Item item = snapshot.getValue(Item.class);
                assert item != null;
                data.add(item);
            }

            return data;
        }).subscribe(presenter::updateData, presenter::showErrorMsg));
    }

    //Загрузка элемента по id, вызов itemLoaded презентера при успехе
    public void findItemFromDbRx(int id, DetailPresenter presenter){
        compositeDisposable.add(RxFirebaseDatabase.observeSingleValueEvent(database, dataSnapshot ->
        {
            Item item = null;
            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                Item currentItem = snapshot.getValue(Item.class);
                assert currentItem != null;
                if (currentItem.getId() == id){
                    item = currentItem;
                    break;
                }
            }
            return item;
        }).subscribe(presenter::itemLoaded));
    }

    // Загрузка элемента по заголовку
    public void searchItemsFromDbRx(String title, MainPresenter presenter){
        compositeDisposable.add(RxFirebaseDatabase.observeSingleValueEvent(database, dataSnapshot -> searching(dataSnapshot, title))
                .subscribe(presenter::updateData));
    }

    // Удаление элемента по id
    public void deleteItem(int id){
        compositeDisposable.add(RxFirebaseDatabase.observeSingleValueEvent(database, dataSnapshot -> {
            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                Item item = snapshot.getValue(Item.class);
                assert item != null;
                if (item.getId() == id){
                    snapshot.getRef().removeValue();
                }
            }
            return true;
        }).subscribe());
    }

    // Сохранение элемента в бд
    public void saveItem(Item item){
        database.push().setValue(item);
    }

    // Вспомогательный метод поиска элементов по заголовку
    private List<Item> searching(DataSnapshot dataSnapshot, String title){
        List<Item> data = new ArrayList();
        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
            Item item = snapshot.getValue(Item.class);;
            assert item != null;
            if (item.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT))) {
                data.add(item);
            }
        }
        return data;
    }

    // Уничтожение всех запросов при уничтожении этого обьекта
    public void destroy(){
        compositeDisposable.dispose();
    }
}

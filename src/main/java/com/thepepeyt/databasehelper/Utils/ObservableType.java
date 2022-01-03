package com.thepepeyt.databasehelper.Utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.internal.operators.observable.ObservableCollect;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObservableType<E> {


    private List<E> observable = new ArrayList<>();


    public void removeValue(E value){
        observable.remove(value);
    }

    public void setData(E value){
        if(observable.isEmpty()){
            observable.add(value);
        }
        else{
            observable.removeAll(observable);
            observable.add(value);
        }
    }



    public void addValue(E... values) {
        for (E e : values) {
            observable.add(e);
        }
    }

    public Observable<E> getObservable() {
        return Observable.fromIterable(observable);

    }
}

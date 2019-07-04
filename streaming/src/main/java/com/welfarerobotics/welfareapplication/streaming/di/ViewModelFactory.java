package com.welfarerobotics.welfareapplication.streaming.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:23
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Parameterized Factory Pattern 으로 구현하기
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static ViewModelFactory instance;
    private Class<?>[] classes = null;
    private Object[] params = null;

    @NonNull public static ViewModelFactory getInstance() {
        if (instance == null) instance = new ViewModelFactory();
        return instance;
    }

    public ViewModelFactory setClasses(Class<?>... classes) {
        this.classes = classes;
        return this;
    }

    public ViewModelFactory setParams(Object... params) {
        this.params = params;
        return this;
    }

    @NonNull public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (params != null && classes != null) {
            try {
                return modelClass.getDeclaredConstructor(classes).newInstance(params);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return super.create(modelClass);
    }
}
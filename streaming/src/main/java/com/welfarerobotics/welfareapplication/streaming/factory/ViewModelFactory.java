package com.welfarerobotics.welfareapplication.streaming.factory;

import android.app.Application;
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
public class ViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private Class[] classes = null;
    private Object[] params = null;

    public ViewModelFactory(@NonNull Application application) {
        super(application);
    }

    public ViewModelFactory setClasses(Class... classes) {
        this.classes = classes;
        return this;
    }

    public ViewModelFactory setParams(Object... params) {
        this.params = params;
        return this;
    }

    @NonNull public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            if (classes == null || params == null) return super.create(modelClass);
            else return modelClass.getDeclaredConstructor(classes).newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

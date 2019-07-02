package com.welfarerobotics.welfareapplication.streaming.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:59
 * @homepage : https://github.com/gusdnd852
 */
public class ObjectFactory {

    private Class[] classes = null;
    private Object[] params = null;

    public ObjectFactory setClasses(Class... classes) {
        this.classes = classes;
        return this;
    }

    public ObjectFactory setParams(Object... params) {
        this.params = params;
        return this;
    }

    @NonNull public <T> T create(@NonNull Class<T> modelClass) {
        try {
            if (classes == null || params == null) return modelClass.newInstance();
            else return modelClass.getDeclaredConstructor(classes).newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

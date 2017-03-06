package blossom.core;


import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Blossom {

    public static void tie(Activity target) {

        View contentView = target.findViewById(android.R.id.content);
        executeTie(target, contentView);
    }

    private static void executeTie(Object target, View source) {
        try {

            Class<?> tieHandlerClass = Class.forName(target.getClass().getName() + "_TieHandler");
            Constructor<?> constructor = tieHandlerClass.getConstructor(target.getClass(), View.class);
            constructor.setAccessible(true);
            constructor.newInstance(target, source);

        } catch (ClassNotFoundException e) {
            throw new ClassCastException();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void tie(Object target, View source) {
        executeTie(target, source);
    }

}

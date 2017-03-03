package blossom.core;


import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Blossom {

    public static void tie(Activity target) {

        Resources res = target.getResources();
        try {

            Class<?> tieHandlerClass = Class.forName(target.getClass().getName() + "_TieHandler");
            Constructor<?> constructor = tieHandlerClass.getConstructor(target.getClass(), Resources.class);
            constructor.setAccessible(true);
            constructor.newInstance(target, res);

        } catch (ClassNotFoundException e) {
            throw new ClassCastException();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }

    public static void tie(Fragment fragment, View view) {
        // TODO: 17/02/24 tie fragment field
    }
}

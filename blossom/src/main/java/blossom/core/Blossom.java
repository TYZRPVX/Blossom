package blossom.core;


import android.app.Activity;
import android.content.res.Resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Blossom {

    public static void tie(Activity target) {

        Resources res = target.getResources();
        try {

            Class<?> blossomNeo = Class.forName("blossom.example.Blossom$$Neo");
            Constructor<?> constructor = blossomNeo.getConstructors()[0];
            constructor.setAccessible(true);
            constructor.newInstance(target, res);

        } catch (ClassNotFoundException e) {
            throw new ClassCastException();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException();
        }
    }
}

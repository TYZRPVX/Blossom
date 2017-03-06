package blossom.core;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

/**
 * For supporting Fragment and reducing redundant findViewById statement, all find view actions should be unified in Finder
 */

public class Finder {

    public static <T> T findViewAsType(View targetView, int id, Class<T> cls) {
        View view = targetView.findViewById(id);
        return cls.cast(view);
    }

    public static View findContentView(Object target) {
        if (target instanceof Activity) {
            return ((Activity) target).findViewById(android.R.id.content);
        } else if (target instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) target).getActivity().findViewById(android.R.id.content);
        } else if (target instanceof android.app.Fragment) {
            return ((android.app.Fragment) target).getActivity().findViewById(android.R.id.content);
        }
        throw new IllegalArgumentException("target is not allowed. ");
    }

    public static Resources getResources(Object target) {
        if (target instanceof Activity) {
            return ((Activity) target).getResources();
        } else if (target instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) target).getResources();
        } else if (target instanceof android.app.Fragment) {
            return ((android.app.Fragment) target).getResources();
        }
        throw new IllegalArgumentException("target is not allowed. ");
    }

}

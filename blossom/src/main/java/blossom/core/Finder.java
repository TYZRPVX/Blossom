package blossom.core;

import android.view.View;

/**
 * For supporting Fragment and reducing redundant findViewById statement, all find view actions should be unified in Finder
 */

public class Finder {

    public static <T> T findViewAsType(View targetView, int id, Class<T> cls) {
        View view = targetView.findViewById(id);
        return cls.cast(view);
    }
}

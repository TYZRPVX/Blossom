package blossom.compiler;


import android.view.View;

import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

import blossom.annotations.OnClick;
import blossom.annotations.OnLongClick;
import blossom.annotations.TieString;
import blossom.annotations.TieView;

public class TypeHolder {

    private Map<Element, Class<? extends Annotation>> holder;

    public TypeHolder() {
        holder = new HashMap<>();
    }

    public void put(Element element, Class<? extends Annotation> annoClass) {
        holder.put(element, annoClass);
    }

    public void appendAssignStatements(MethodSpec.Builder ctorBuilder) {

        IdMap idMap = new IdMap();
        for (Map.Entry<Element, Class<? extends Annotation>> entry : holder.entrySet()) {
            Element element = entry.getKey();
            Class<? extends Annotation> annoClass = entry.getValue();

            if (annoClass == TieString.class) {
                int id = element.getAnnotation(TieString.class).value();
                ctorBuilder.addStatement("target.$L = res.getString($L)", element.getSimpleName(), id);
            } else if (annoClass == TieView.class) {
                int id = element.getAnnotation(TieView.class).value();
                if (!idMap.containsKey(id)) {
                    idMap.put(id, TieView.class);
                } else {
                    ProcessMessager.error(element, "Attempt to use @%s for an already ID %d"
                            , TieView.class.getSimpleName(), id);
                }
                ctorBuilder.addStatement("target.$L = ($T) target.findViewById($L)"
                        , element.getSimpleName(), element.asType(), id);
            } else if (annoClass == OnClick.class) {
                int id = element.getAnnotation(OnClick.class).value();
                // not tie yet
                ctorBuilder.addStatement("$T $L = target.findViewById($L)"
                        , View.class, onClickName(id), id);
                ctorBuilder.beginControlFlow("$L.setOnClickListener(new $T.OnClickListener() ", onClickName(id), View.class)
                        .beginControlFlow("@Override public void onClick($T v) ", View.class)
                        .addStatement("target.$L(v)", element.getSimpleName())
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement(")");
            } else if (annoClass == OnLongClick.class) {
                int id = element.getAnnotation(OnLongClick.class).value();
                // TODO: 17/02/19
            }
        }
    }

    private String onClickName(int id) {
        return "onClick" + id;
    }
}

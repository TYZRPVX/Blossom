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

        RepetitiveTieChecker checker = new RepetitiveTieChecker();
        for (Map.Entry<Element, Class<? extends Annotation>> entry : holder.entrySet()) {
            Element element = entry.getKey();
            Class<? extends Annotation> annoClass = entry.getValue();

            if (annoClass == TieString.class) {
                int id = element.getAnnotation(TieString.class).value();
                ctorBuilder.addStatement("target.$L = res.getString($L)", element.getSimpleName(), id);
            } else if (annoClass == TieView.class) {
                int id = element.getAnnotation(TieView.class).value();
                checker.check(id, TieView.class, element);

                ctorBuilder.addStatement("target.$L = ($T) target.findViewById($L)"
                        , element.getSimpleName(), element.asType(), id);
            } else if (annoClass == OnClick.class) {
                int id = element.getAnnotation(OnClick.class).value();
                checker.check(id, OnClick.class, element);
                // TODO: 17/02/20 check this id has been tied
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
                checker.check(id, OnLongClick.class, element);
                ctorBuilder.addStatement("$T $L = target.findViewById($L)"
                        , View.class, onLongClickName(id), id);
            }
        }
    }

    private String onClickName(int id) {
        return "onClick" + id;
    }

    private String onLongClickName(int id) {
        return "onLongClick" + id;
    }
}

package blossom.compiler;


import android.view.MotionEvent;
import android.view.View;

import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

import blossom.annotations.OnClick;
import blossom.annotations.OnLongClick;
import blossom.annotations.OnTouch;
import blossom.annotations.TieColor;
import blossom.annotations.TieDrawable;
import blossom.annotations.TieString;
import blossom.annotations.TieView;

/**
 * Store all annotation context in a class
 */
public class TypeElementContext {

    private Map<Element, Class<? extends Annotation>> context;

    public TypeElementContext() {
        context = new HashMap<>();
    }

    public void put(Element element, Class<? extends Annotation> annoClass) {
        context.put(element, annoClass);
    }

    public void addStatementsTo(MethodSpec.Builder ctorBuilder) {

        RedundantTieChecker checker = new RedundantTieChecker();
        for (Map.Entry<Element, Class<? extends Annotation>> entry : context.entrySet()) {
            Element element = entry.getKey();
            Class<? extends Annotation> annoClass = entry.getValue();

            if (annoClass == TieString.class) {
                int id = element.getAnnotation(TieString.class).value();
                ctorBuilder.addStatement("target.$L = res.getString($L)", element.getSimpleName(), id);
            } else if (annoClass == TieDrawable.class) {
                int id = element.getAnnotation(TieDrawable.class).value();
                ctorBuilder.addStatement("target.$L = res.getDrawable($L)", element.getSimpleName(), id);
            } else if (annoClass == TieColor.class) {
                int id = element.getAnnotation(TieColor.class).value();
                ctorBuilder.addStatement("target.$L = res.getColor($L)", element.getSimpleName(), id); // TODO: 17/02/21 may hint deprecated
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
                ctorBuilder.beginControlFlow("$L.setOnLongClickListener(new $T.OnLongClickListener() "
                        , onLongClickName(id), View.class)
                        .beginControlFlow("@Override public boolean onLongClick($T v) ", View.class)
                        .addStatement("return target.$L(v)", element.getSimpleName())
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement(")");
            } else if (annoClass == OnTouch.class) {
                int id = element.getAnnotation(OnTouch.class).value();
                checker.check(id, OnTouch.class, element);
                ctorBuilder.addStatement("$T $L = target.findViewById($L)"
                        , View.class, onTouchName(id), id);
                ctorBuilder.beginControlFlow("$L.setOnTouchListener(new $T.OnTouchListener() "
                        , onTouchName(id), View.class)
                        .beginControlFlow("@Override public boolean onTouch($T v, $T event) ", View.class, MotionEvent.class)
                        .addStatement("return target.$L(v, event)", element.getSimpleName())
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement(")");
            }
        }
    }

    private String onTouchName(int id) {
        return "onTouch" + id;
    }

    private String onClickName(int id) {
        return "onClick" + id;
    }

    private String onLongClickName(int id) {
        return "onLongClick" + id;
    }
}

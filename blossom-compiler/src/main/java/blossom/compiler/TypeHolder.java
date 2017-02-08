package blossom.compiler;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

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

    public List<String> assembleStatements() {

        List<String> statements = new ArrayList<>();
        for (Element element : holder.keySet()) {
            TypeMirror typeMirror = element.asType();
            Element enclosingElement = element.getEnclosingElement();

            Class<? extends Annotation> annoClass = holder.get(element);
            String statement = "";
            if (annoClass == TieString.class) {
                int id = element.getAnnotation(TieString.class).value();
                statement = String.format(Locale.getDefault(), "target.%s = res.getString(%d)", element.getSimpleName(), id);
            } else if (annoClass == TieView.class) {
                int id = element.getAnnotation(TieView.class).value();
                statement = String.format(Locale.getDefault(), "target.%s = (%s) target.findViewById(%d)"
                        , element.getSimpleName(), element.asType(), id);
            }
            statements.add(statement);
        }
        return statements;
    }
}

package blossom.compiler;


import javax.lang.model.element.Element;

public class ElementTool {

    public static String getMethodReturnType(Element element) {
        String typeInfo = element.asType().toString(); // (android.view.View,int)boolean
        return typeInfo.substring(typeInfo.indexOf(")") + 1, typeInfo.length());
    }
}

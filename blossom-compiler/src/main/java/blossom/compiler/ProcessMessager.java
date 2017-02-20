package blossom.compiler;


import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import static android.R.id.message;
import static javax.tools.Diagnostic.Kind.ERROR;

public class ProcessMessager {

    private static Messager sMessager;

    private ProcessMessager() {

    }

    public static void init(Messager messager) {
        sMessager = messager;
    }

    public static void error(Element element, String message, Object... args) {
        printMessage(ERROR, element, message, args);
    }

    private static void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        sMessager.printMessage(kind, message, element);
    }
}

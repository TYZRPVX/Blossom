package blossom.compiler;


import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

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

    public static void note(Element element, String message, Object... args) {
        printMessage(NOTE, element, message, args);
    }

    private static void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        String fMessage = message;
        if (args.length > 0) {
            fMessage = String.format(message, args);
        }
        sMessager.printMessage(kind, fMessage, element);
    }
}

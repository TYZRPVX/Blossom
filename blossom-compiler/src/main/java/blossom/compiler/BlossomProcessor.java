package blossom.compiler;


import android.content.res.Resources;
import android.view.View;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import blossom.annotations.OnClick;
import blossom.annotations.OnLongClick;
import blossom.annotations.OnTouch;
import blossom.annotations.TieColor;
import blossom.annotations.TieDrawable;
import blossom.annotations.TieString;
import blossom.annotations.TieView;

@AutoService(Processor.class)
public class BlossomProcessor extends AbstractProcessor {

    public static List<Class<? extends Annotation>> FIELD_ANNOTATIONS = Arrays.asList(
            TieString.class,
            TieView.class,
            TieDrawable.class,
            TieColor.class
    );
    public static List<Class<? extends Annotation>> LISTENER_ANNOTATIONS = Arrays.asList(
            OnClick.class,
            OnLongClick.class,
            OnTouch.class
    );

    public static List<Class<? extends Annotation>> ALL_ANNOTATIONS = new ArrayList<>();

    static {
        // Construct ALL_ANNOTATIONS
        ALL_ANNOTATIONS.addAll(FIELD_ANNOTATIONS);
        ALL_ANNOTATIONS.addAll(LISTENER_ANNOTATIONS);
    }

    private Filer filer;

    private static String handlerClassName(String targetName) {
        return targetName + "_TieHandler";
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : FIELD_ANNOTATIONS) {
            types.add(annotation.getCanonicalName());
        }
        for (Class<? extends Annotation> annotation : LISTENER_ANNOTATIONS) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        ProcessMessager.init(processingEnv.getMessager());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Map<Element, Class<? extends Annotation>> annotationContext = new HashMap<>();
        for (Class<? extends Annotation> annotationClass : ALL_ANNOTATIONS) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotationClass);
            for (Element element : elements) {
                annotationContext.put(element, annotationClass);
            }
        }

        brewJava(annotationContext);
        return false;
    }

    private void brewJava(Map<Element, Class<? extends Annotation>> annotationContext) {

        Map<TypeElement, TypeElementContext> classifiedStatement
                = classifyAnnotationContext(annotationContext);

        for (Map.Entry<TypeElement, TypeElementContext> entry : classifiedStatement.entrySet()) {
            TypeElement typeElement = entry.getKey();
            TypeElementContext typeElementContext = entry.getValue();

            ClassName className = ClassName.get(typeElement);
            TypeVariableName typeVariableName = TypeVariableName.get("T", className);
            MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(typeVariableName, "target", Modifier.FINAL)
                    .addParameter(Resources.class, "res");

            ctorBuilder.addStatement("$T contentView = target.findViewById(android.R.id.content)", View.class);
            typeElementContext.addStatementsTo(ctorBuilder);

            String targetClassName = typeElement.getSimpleName().toString();
            String targetPackageName = typeElement.getEnclosingElement().toString();

            TypeSpec handler = TypeSpec.classBuilder(handlerClassName(targetClassName))
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(typeVariableName)
                    .addMethod(ctorBuilder.build())
                    .build();

            JavaFile file = JavaFile.builder(targetPackageName, handler)
                    .addFileComment("Auto generated by Blossom")
                    .build();

            try {
                file.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Classify each TypeElementContext according to its class full path
     *
     * @param annotationContext: all annotations in project source with their context
     * @return KEY: full path class element
     * VALUE: annotation stuff of this class element
     */
    private Map<TypeElement, TypeElementContext>
    classifyAnnotationContext(Map<Element, Class<? extends Annotation>> annotationContext) {

        Map<TypeElement, TypeElementContext> classifiedAnnotationContext = new HashMap<>();
        for (Map.Entry<Element, Class<? extends Annotation>> entry : annotationContext.entrySet()) {
            Element element = entry.getKey();
            Class<? extends Annotation> annoClass = entry.getValue();

            TypeElement typeElement = (TypeElement) element.getEnclosingElement(); // package Element, eg. blossom.example.MainActivity
            if (!classifiedAnnotationContext.containsKey(typeElement)) {
                TypeElementContext typeElementContext = new TypeElementContext();
                typeElementContext.put(element, annoClass);
                classifiedAnnotationContext.put(typeElement, typeElementContext);
            } else {
                TypeElementContext existedTypeElementContext = classifiedAnnotationContext.get(typeElement);
                existedTypeElementContext.put(element, annoClass);
                classifiedAnnotationContext.put(typeElement, existedTypeElementContext);
            }
        }
        return classifiedAnnotationContext;
    }

}

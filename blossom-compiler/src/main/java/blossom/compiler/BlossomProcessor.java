package blossom.compiler;


import android.content.res.Resources;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import blossom.annotations.OnClick;
import blossom.annotations.OnLongClick;
import blossom.annotations.TieLayout;
import blossom.annotations.TieString;
import blossom.annotations.TieView;

@AutoService(Processor.class)
public class BlossomProcessor extends AbstractProcessor {

    public static List<Class<? extends Annotation>> FIELD_ANNOTATIONS = Arrays.asList(
            TieLayout.class,
            TieString.class,
            TieView.class
    );
    public static List<Class<? extends Annotation>> LISTENER_ANNOTATIONS = Arrays.asList(
            OnClick.class,
            OnLongClick.class
    );
    private Elements elementUtils;
    private Filer filer;

    static String nameHandler(String targetName) {
        // // MainActivity_TieHandler
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
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        // @TieString
        // {MainActivity: [TieHolder1, TieHolder2]}
        // TypeHolder: <TieString-[appName, buttonName]>
        Map<Element, Class<? extends Annotation>> statementSummary = new HashMap<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(TieString.class)) {

            statementSummary.put(element, TieString.class);
        }

        // @TieView
        for (Element element : roundEnvironment.getElementsAnnotatedWith(TieView.class)) {

            statementSummary.put(element, TieView.class);
        }

        brewJava(statementSummary);
        return false;
    }

    private void brewJava(Map<Element, Class<? extends Annotation>> statementSummary) {

        Map<TypeElement, TypeHolder> classifiedStatement
                = classifyStatementSummary(statementSummary);
        for (TypeElement typeElement : classifiedStatement.keySet()) {

            ClassName className = ClassName.get(typeElement);
            TypeVariableName typeVariableName = TypeVariableName.get("T", className);
            MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(typeVariableName, "target")
                    .addParameter(Resources.class, "res");

            TypeHolder typeHolder = classifiedStatement.get(typeElement);
            typeHolder.addStatements(ctorBuilder);

            String targetClassName = typeElement.getSimpleName().toString();
            String packageName = typeElement.getEnclosingElement().toString();

            TypeSpec handler = TypeSpec.classBuilder(nameHandler(targetClassName))
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(typeVariableName)
                    .addMethod(ctorBuilder.build())
                    .build();

            JavaFile file = JavaFile.builder(packageName, handler)
                    .addFileComment("Auto generated by Blossom")
                    .build();

            try {
                file.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<TypeElement, TypeHolder> classifyStatementSummary(Map<Element, Class<? extends Annotation>> statementSummary) {

        Map<TypeElement, TypeHolder> classifiedStatement = new HashMap<>();
        for (Element element : statementSummary.keySet()) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement(); // package Element
            Class<? extends Annotation> annoClass = statementSummary.get(element);

            if (!classifiedStatement.containsKey(typeElement)) {
                TypeHolder typeHolder = new TypeHolder();
                typeHolder.put(element, annoClass);
                classifiedStatement.put(typeElement, typeHolder);
            } else {
                TypeHolder existedTypeHolder = classifiedStatement.get(typeElement);
                existedTypeHolder.put(element, annoClass);
                classifiedStatement.put(typeElement, existedTypeHolder);
            }
        }
        return classifiedStatement;
    }

}

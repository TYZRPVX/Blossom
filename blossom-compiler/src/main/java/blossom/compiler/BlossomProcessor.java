package blossom.compiler;


import android.content.res.Resources;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import blossom.annotations.TieString;

@AutoService(Processor.class)
public class BlossomProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> types = new LinkedHashSet<>();
        types.add(TieString.class.getCanonicalName());
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

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TieString.class);
        int value = 0;
        for (Element element : elements) {
            TieString annotation = element.getAnnotation(TieString.class);
            value = annotation.value();
        }
        ClassName className = ClassName.get("blossom.example", "MainActivity");
        TypeVariableName typeVariableName = TypeVariableName.get("T", className);

        MethodSpec build = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(typeVariableName, "target")
                .addParameter(Resources.class, "res")
                .addStatement("target.appName = res.getString($L)", value)
                .addJavadoc("$S", elements.toString())
                .build();

        TypeSpec blossom$$Neo = TypeSpec.classBuilder("Blossom$$Neo")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(typeVariableName)
                .addMethod(build)
                .build();

//        MethodSpec main = MethodSpec.methodBuilder("main")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .returns(void.class)
//                .addParameter(String[].class, "args")
//                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
//                .build();
//        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld1")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addMethod(main)
//                .build();
        JavaFile javaFile = JavaFile.builder("blossom.example", blossom$$Neo)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

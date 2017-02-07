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
        for (Element element : elements) {
            TieString annotation = element.getAnnotation(TieString.class);
            int value = annotation.value();

            ClassName className = ClassName.get("blossom.example", "MainActivity");
            TypeVariableName typeVariableName = TypeVariableName.get("T", className);

            MethodSpec build = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(typeVariableName, "target")
                    .addParameter(Resources.class, "res")
                    .addStatement("target.$L = res.getString($L)", element.getSimpleName(), value)
                    .addJavadoc("$S", element.getEnclosingElement())
                    .build();

            TypeSpec blossom$$Neo = TypeSpec.classBuilder("Blossom$$Neo")
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(typeVariableName)
                    .addMethod(build)
                    .build();

            JavaFile javaFile = JavaFile.builder("blossom.example", blossom$$Neo)
                    .build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

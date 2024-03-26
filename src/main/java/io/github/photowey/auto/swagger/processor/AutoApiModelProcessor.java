/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.photowey.auto.swagger.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import io.github.photowey.auto.swagger.annotation.AutoApiModel;
import io.github.photowey.auto.swagger.builder.AutoApiModelBuilder;
import io.github.photowey.auto.swagger.constant.AutoConstants;
import io.github.photowey.auto.swagger.context.AutoContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code AutoApiModelProcessor}
 *
 * @author photowey
 * @date 2024/03/24
 * @since 1.0.0
 */
@AutoService(Processor.class)
public class AutoApiModelProcessor extends AbstractProcessor {

    private AutoApiModelBuilder apiModelBuilder;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        Filer filer = processingEnv.getFiler();
        Messager messager = processingEnv.getMessager();
        Types types = processingEnv.getTypeUtils();
        Elements elements = processingEnv.getElementUtils();

        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        Trees trees = Trees.instance(processingEnv);
        TreeMaker treeMaker = TreeMaker.instance(context);
        Names names = Names.instance(context);

        AutoContext ctx = AutoContext.builder()
                .filer(filer)
                .messager(messager)
                .types(types)
                .elements(elements)
                .trees(trees)
                .treeMaker(treeMaker)
                .names(names)
                .build();

        this.apiModelBuilder = new AutoApiModelBuilder(ctx);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(
                AutoApiModel.class.getName()
        ));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (AutoConstants.determineAutoSwaggerAnnotationIsEnabled()) {
            this.processAutoApiModels(annotations, env);

            return true;
        }

        this.removeProxyAnnotation(annotations, env);

        return true;
    }

    // ----------------------------------------------------------------

    private void processAutoApiModels(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(AutoApiModel.class);

        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                this.processAutoApiModel(element);
            }
        }
    }

    private void processAutoApiModel(Element element) {
        this.apiModelBuilder.build(element);
    }

    // ----------------------------------------------------------------

    private void removeProxyAnnotation(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(AutoApiModel.class);

        for (Element element : elements) {
            if (element.getKind().isClass()) {
                this.removeAutoApiModel(element);
            }
        }
    }

    private void removeAutoApiModel(Element element) {
        this.apiModelBuilder.remove(element);
    }
}
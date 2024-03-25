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
import io.github.photowey.auto.swagger.annotation.AutoApiModelProperty;
import io.github.photowey.auto.swagger.builder.AutoApiModelPropertyBuilder;
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
 * {@code AutoSwaggerAnnotationProcessor}
 *
 * @author photowey
 * @date 2024/03/24
 * @since 1.0.0
 */
@AutoService(Processor.class)
public class AutoApiModelPropertyProcessor extends AbstractProcessor {

    private AutoApiModelPropertyBuilder apiModelPropertyBuilder;

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

        this.apiModelPropertyBuilder = new AutoApiModelPropertyBuilder(ctx);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(
                AutoApiModelProperty.class.getName()
        ));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (AutoConstants.determineAutoSwaggerAnnotationIsEnabled()) {
            this.processAutoApiModelProperties(annotations, env);
        }

        return true;
    }

    // ----------------------------------------------------------------

    private void processAutoApiModelProperties(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(AutoApiModelProperty.class);

        for (Element element : elements) {
            if (element.getKind() == ElementKind.FIELD) {
                this.processAutoApiModelProperty(element);
            }
        }
    }

    private void processAutoApiModelProperty(Element element) {
        this.apiModelPropertyBuilder.build(element);
    }
}
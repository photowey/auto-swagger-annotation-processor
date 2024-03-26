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
package io.github.photowey.auto.swagger.builder;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import io.github.photowey.auto.swagger.annotation.AutoApiModelProperty;
import io.github.photowey.auto.swagger.context.AutoContext;

import javax.lang.model.element.Element;

/**
 * {@code AutoApiModelPropertyBuilder}
 *
 * @author photowey
 * @date 2024/03/24
 * @since 1.0.0
 */
public class AutoApiModelPropertyBuilder extends AbstractAutoBuilder {

    private static final String API_MODEL_PROPERTY_FULL_QUALIFIED_NAME = "io.swagger.annotations.ApiModelProperty";
    private static final String API_MODEL_PROPERTY_FULL_SIMPLE_NAME = "ApiModelProperty";

    public AutoApiModelPropertyBuilder(AutoContext context) {
        super(context);
    }

    @Override
    public void build(Element element) {
        this.handleAddAnnotation(element);
        this.handleVariableDeclareClassImportAdd(element, API_MODEL_PROPERTY_FULL_QUALIFIED_NAME);
    }

    @Override
    public void remove(Element element) {
        // Remove: @AutoApiModelProperty
        super.doRemoveIfNecessary(element, AutoApiModelProperty.class);
    }

    private void handleAddAnnotation(Element element) {
        JCTree.JCVariableDecl variable = this.context.toVariable(element);
        AutoApiModelProperty auto = element.getAnnotation(AutoApiModelProperty.class);

        Name vn = this.context.fromString("value");
        JCTree.JCExpression vv = this.context.literal(auto.value());

        Name en = this.context.fromString("example");
        JCTree.JCExpression ev = this.context.literal(auto.example());

        JCTree.JCExpression valueExpr = this.context.assign(vn, vv);
        JCTree.JCExpression exampleExpr = this.context.assign(en, ev);

        JCTree.JCAnnotation annotation = this.context.treeMaker().Annotation(
                this.context.treeMaker().Ident(this.context.fromString(API_MODEL_PROPERTY_FULL_SIMPLE_NAME)),
                List.of(valueExpr, exampleExpr)
        );

        ListBuffer<JCTree.JCAnnotation> includes = new ListBuffer<>();
        includes.add(annotation);

        this.removeAutoAnnotationIfNecessary(variable, includes, AutoApiModelProperty.class);
    }
}
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

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import io.github.photowey.auto.swagger.context.AutoContext;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * {@code AbstractAutoBuilder}
 *
 * @author photowey
 * @date 2024/03/24
 * @since 1.0.0
 */
public abstract class AbstractAutoBuilder implements AutoBuilder {

    protected final AutoContext context;

    public AbstractAutoBuilder(AutoContext context) {
        this.context = context;
    }

    protected String determineTargetPackageName(String fullQualifiedName) {
        return fullQualifiedName.substring(0, fullQualifiedName.lastIndexOf("."));
    }

    protected String determineTargetSimpleName(String fullQualifiedName) {
        return fullQualifiedName.substring(fullQualifiedName.lastIndexOf(".") + 1);
    }

    protected void handleVariableDeclareClassImportAdd(Element element, String fullQualifiedName) {
        VariableElement variable = (VariableElement) element;
        JCTree.JCClassDecl clazz = this.tryFindVariableDeclareClass(variable);
        JCTree.JCCompilationUnit unit = (JCTree.JCCompilationUnit) this.context.trees().getPath(clazz.sym).getCompilationUnit();

        ListBuffer<JCTree> imports = new ListBuffer<>();
        for (int i = 0; i < unit.defs.size(); i++) {
            imports.append(unit.defs.get(i));
        }

        JCTree.JCIdent packageIdent = this.context.treeMaker()
                .Ident(this.context.names().fromString(this.determineTargetPackageName(fullQualifiedName)));
        JCTree.JCFieldAccess access = this.context.treeMaker()
                .Select(packageIdent, this.context.names().fromString(this.determineTargetSimpleName(fullQualifiedName)));
        JCTree.JCImport ipt = this.context.treeMaker().Import(access, false);
        imports.append(ipt);

        unit.defs = imports.toList();
    }

    protected JCTree.JCClassDecl tryFindVariableDeclareClass(VariableElement variable) {
        TreePath treePath = this.context.trees().getPath(variable);

        CompilationUnitTree ut = treePath.getCompilationUnit();

        Tree tree = this.context.trees().getTree(variable);
        if (tree instanceof JCTree.JCVariableDecl) {
            JCTree.JCVariableDecl vd = (JCTree.JCVariableDecl) tree;

            TreePath classTreePath = this.context.trees().getPath(ut, vd);
            while (classTreePath != null) {
                Tree classTree = classTreePath.getLeaf();
                if (classTree instanceof JCTree.JCClassDecl) {
                    JCTree.JCClassDecl clazz = (JCTree.JCClassDecl) classTree;
                    return clazz;
                }
                classTreePath = classTreePath.getParentPath();
            }
        }

        return null;
    }

    // ----------------------------------------------------------------

    public void doRemoveIfNecessary(Element element, Class<?> clazz) {
        JCTree.JCVariableDecl variable = this.context.toVariable(element);
        ListBuffer<JCTree.JCAnnotation> includes = new ListBuffer<>();

        // Remove: @AutoApiModelProperty
        this.removeAutoAnnotationIfNecessary(variable, includes, clazz);
    }

    protected void removeAutoAnnotationIfNecessary(JCTree.JCVariableDecl variable, ListBuffer<JCTree.JCAnnotation> includes, Class<?> clazz) {
        List<JCTree.JCAnnotation> annotations = variable.mods.annotations;
        for (JCTree.JCAnnotation bro : annotations) {
            if (this.determineIsNotMyself(bro, clazz)) {
                includes.add(bro);
            }
        }

        variable.mods.annotations = includes.toList();
    }

    protected boolean determineIsNotMyself(JCTree.JCAnnotation annotation, Class<?> clazz) {
        return !this.determineIsMyself(annotation, clazz);
    }

    protected boolean determineIsMyself(JCTree.JCAnnotation annotation, Class<?> clazz) {
        return annotation.getAnnotationType().toString().equals(clazz.getSimpleName());
    }
}
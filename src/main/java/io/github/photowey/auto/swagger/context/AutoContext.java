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
package io.github.photowey.auto.swagger.context;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.Serializable;

/**
 * {@code AutoContext}
 *
 * @author photowey
 * @date 2024/03/25
 * @since 1.0.0
 */
public class AutoContext implements Serializable {

    protected Filer filer;
    protected Messager messager;
    private Types types;
    private Elements elements;

    private Trees trees;

    private TreeMaker treeMaker;
    private Names names;

    public static AutoContextBuilder builder() {
        return new AutoContextBuilder();
    }

    public Filer filer() {
        return this.filer;
    }

    public Messager messager() {
        return this.messager;
    }

    public Types types() {
        return this.types;
    }

    public Elements elements() {
        return this.elements;
    }

    public Trees trees() {
        return this.trees;
    }

    public TreeMaker treeMaker() {
        return this.treeMaker;
    }

    public Names names() {
        return this.names;
    }

    public AutoContext filer(Filer filer) {
        this.filer = filer;
        return this;
    }

    public AutoContext messager(Messager messager) {
        this.messager = messager;
        return this;
    }

    public AutoContext types(Types types) {
        this.types = types;
        return this;
    }

    public AutoContext elements(Elements elements) {
        this.elements = elements;
        return this;
    }

    public AutoContext trees(Trees trees) {
        this.trees = trees;
        return this;
    }

    public AutoContext treeMaker(TreeMaker treeMaker) {
        this.treeMaker = treeMaker;
        return this;
    }

    public AutoContext names(Names names) {
        this.names = names;
        return this;
    }

    public AutoContext() {
    }

    public AutoContext(Filer filer, Messager messager, Types types, Elements elements, Trees trees, TreeMaker treeMaker, Names names) {
        this.filer = filer;
        this.messager = messager;
        this.types = types;
        this.elements = elements;
        this.trees = trees;
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public static class AutoContextBuilder {
        private Filer filer;
        private Messager messager;
        private Types types;
        private Elements elements;
        private Trees trees;
        private TreeMaker treeMaker;
        private Names names;

        AutoContextBuilder() {
        }

        public AutoContextBuilder filer(Filer filer) {
            this.filer = filer;
            return this;
        }

        public AutoContextBuilder messager(Messager messager) {
            this.messager = messager;
            return this;
        }

        public AutoContextBuilder types(Types types) {
            this.types = types;
            return this;
        }

        public AutoContextBuilder elements(Elements elements) {
            this.elements = elements;
            return this;
        }

        public AutoContextBuilder trees(Trees trees) {
            this.trees = trees;
            return this;
        }

        public AutoContextBuilder treeMaker(TreeMaker treeMaker) {
            this.treeMaker = treeMaker;
            return this;
        }

        public AutoContextBuilder names(Names names) {
            this.names = names;
            return this;
        }

        public AutoContext build() {
            return new AutoContext(this.filer, this.messager, this.types, this.elements, this.trees, this.treeMaker, this.names);
        }
    }
}

/*
 * Copyright (C) 2014 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.internal.codegen;

import static com.google.auto.common.MoreElements.isAnnotationPresent;
import static javax.lang.model.util.ElementFilter.methodsIn;
import static javax.lang.model.util.ElementFilter.typesIn;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Messager;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * A {@link ProcessingStep} that validates module classes and generates factories for binding
 * methods.
 */
final class ModuleProcessingStep implements ProcessingStep {
  private final Messager messager;
  private final ModuleValidator moduleValidator;
  private final BindingFactory bindingFactory;
  private final FactoryGenerator factoryGenerator;
  private final ProducerFactoryGenerator producerFactoryGenerator;
  private final Set<TypeElement> processedModuleElements = Sets.newLinkedHashSet();

  @Inject
  ModuleProcessingStep(
      Messager messager,
      ModuleValidator moduleValidator,
      BindingFactory bindingFactory,
      FactoryGenerator factoryGenerator,
      ProducerFactoryGenerator producerFactoryGenerator) {
    this.messager = messager;
    this.moduleValidator = moduleValidator;
    this.bindingFactory = bindingFactory;
    this.factoryGenerator = factoryGenerator;
    this.producerFactoryGenerator = producerFactoryGenerator;
  }

  @Override
  public Set<? extends Class<? extends Annotation>> annotations() {
    return ImmutableSet.of(Module.class, ProducerModule.class);
  }

  @Override
  public Set<Element> process(
      SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    List<TypeElement> modules = typesIn(elementsByAnnotation.values());
    moduleValidator.addKnownModules(modules);
    for (TypeElement module : modules) {
      if (processedModuleElements.add(module)) {
        processModule(module);
      }
    }
    return ImmutableSet.of();
  }

  private void processModule(TypeElement module) {
    ValidationReport<TypeElement> report = moduleValidator.validate(module);
    report.printMessagesTo(messager);
    if (report.isClean()) {
      for (ExecutableElement method : methodsIn(module.getEnclosedElements())) {
        if (isAnnotationPresent(method, Provides.class)) {
          factoryGenerator.generate(bindingFactory.providesMethodBinding(method, module), messager);
        } else if (isAnnotationPresent(method, Produces.class)) {
          producerFactoryGenerator.generate(
              bindingFactory.producesMethodBinding(method, module), messager);
        }
      }
    }
  }
}

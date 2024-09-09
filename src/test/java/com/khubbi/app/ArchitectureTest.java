package com.khubbi.app;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.conditions.ArchConditions.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.khubbi.app.adapter.in.memory.UserAutoRegistration;
import com.khubbi.app.adapter.in.web.security.SecurityConfiguration;
import com.khubbi.app.adapter.in.web.security.jwt.JwtService;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.domain.model.ValueObject;
import com.khubbi.app.application.port.in.AddUserUseCase;
import com.khubbi.app.application.port.out.ListUsersPort;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(
    packagesOf = AppApplication.class,
    importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTest {

  private static final String INPUT_ADAPTERS = "Input Adapters";
  private static final String INPUT_PORTS = "Input Ports";
  private static final String DOMAIN_SERVICES = "Domain Services";
  private static final String MODEL = "Model";
  private static final String OUTPUT_PORTS = "Output Ports";
  private static final String OUTPUT_ADAPTERS = "Output Adapters";

  @ArchTest
  static final ArchRule services_are_package_private_and_depend_only_on_input_ports =
      classes()
          .that()
          .resideInAPackage("..domain.service..")
          .and()
          .haveSimpleNameEndingWith("Service")
          .and()
          .doNotHaveModifier(JavaModifier.ABSTRACT)
          .should()
          .bePackagePrivate()
          .andShould(implement(resideInAPackage("..port.in..")));

  @ArchTest
  static final ArchRule controllers_are_annotated_and_named_properly =
      classes()
          .that()
          .haveSimpleNameEndingWith("Controller")
          .or()
          .areAnnotatedWith(RestController.class)
          .should()
          .beAnnotatedWith(RestController.class)
          .andShould(haveSimpleNameEndingWith("Controller"))
          .andShould(resideInAnyPackage("..adapter.in.web.."));

  @ArchTest
  static final ArchRule input_ports_are_named_use_cases =
      classes()
          .that()
          .resideInAPackage("..port.in..")
          .and()
          .areNotNestedClasses()
          .should()
          .haveSimpleNameEndingWith("UseCase");

  @ArchTest
  static final ArchRule ports_should_return_try =
      methods()
          .that()
          .areDeclaredInClassesThat(
              resideInAPackage("..port.in..").or(resideInAPackage("..port.out..")))
          .and()
          .areDeclaredInClassesThat()
          .areNotNestedClasses()
          .should()
          .haveRawReturnType(Try.class);

  @ArchTest
  static final ArchRule user_aware_services_should_have_user_email_parameter =
      methods()
          .that()
          .areDeclaredInClassesThat()
          .areAssignableTo("com.khubbi.app.application.domain.service.UserAwareService")
          .and()
          .arePublic()
          .should(
              have(
                  describe(
                      "at least one UserEmail parameter",
                      method ->
                          method.getRawParameterTypes().stream()
                              .anyMatch(parameter -> parameter.isEquivalentTo(UserEmail.class)))));

  @ArchTest
  static final ArchRule domain_services_should_only_have_port_fields =
      fields()
          .that()
          .areDeclaredInClassesThat()
          .resideInAnyPackage("..domain.service..")
          .and()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("Service")
          .should()
          .beFinal()
          .andShould()
          .bePrivate()
          .andShould(
              haveRawType(
                  JavaClass.Predicates.resideInAPackage("..port.in..")
                      .or(
                          JavaClass.Predicates.resideInAPackage("..port.out..")
                              .or(JavaClass.Predicates.resideInAPackage("..domain.service..")))
                      .or(JavaClass.Predicates.type(org.slf4j.Logger.class))));

  @ArchTest
  static final ArchRule output_ports_are_named_ports =
      classes().that().resideInAPackage("..port.out..").should().haveSimpleNameEndingWith("Port");

  @ArchTest
  static final ArchRule value_objects_have_private_constructors_and_factory_method =
      classes()
          .that()
          .areAnnotatedWith(ValueObject.class)
          .should()
          .haveOnlyPrivateConstructors()
          .andShould(
              have(
                  describe(
                      "static method of that returns Validation",
                      cls ->
                          cls.getAllMethods().stream()
                              .filter(method -> method.getName().equals("of"))
                              .anyMatch(
                                  method ->
                                      method
                                          .getRawReturnType()
                                          .isEquivalentTo(Validation.class)))));

  //
  // .andShould(have(JavaMethod.Predicates.method().na.areStatic().and().haveRawReturnType(Validation.class)));

  @ArchTest
  static final ArchRule dependencies_are_respected =
      layeredArchitecture()
          .consideringAllDependencies()
          .layer(INPUT_ADAPTERS)
          .definedBy("com.khubbi.app.adapter.in.web")
          .layer(INPUT_PORTS)
          .definedBy("com.khubbi.app.application.port.in..")
          .layer(DOMAIN_SERVICES)
          .definedBy("com.khubbi.app.application.domain.service..")
          .layer(MODEL)
          .definedBy("com.khubbi.app.application.domain.model..")
          .layer(OUTPUT_PORTS)
          .definedBy("com.khubbi.app.application.port.out..")
          .layer(OUTPUT_ADAPTERS)
          .definedBy("com.khubbi.app.adapter.out..")
          .whereLayer(INPUT_ADAPTERS)
          .mayNotBeAccessedByAnyLayer()
          .whereLayer(OUTPUT_PORTS)
          .mayOnlyBeAccessedByLayers(DOMAIN_SERVICES, OUTPUT_ADAPTERS)
          .ignoreDependency(JwtService.class, ListUsersPort.class)
          .ignoreDependency(SecurityConfiguration.class, ListUsersPort.class)
          .whereLayer(INPUT_PORTS)
          .mayOnlyBeAccessedByLayers(DOMAIN_SERVICES, INPUT_ADAPTERS)
          .ignoreDependency(UserAutoRegistration.class, AddUserUseCase.class)
          .whereLayer(DOMAIN_SERVICES)
          .mayOnlyBeAccessedByLayers(INPUT_PORTS, INPUT_ADAPTERS);
  // FIXME mateusz.brycki fix below
  //              .whereLayer(MODEL).mayOnlyBeAccessedByLayers(OUTPUT_ADAPTERS);

}

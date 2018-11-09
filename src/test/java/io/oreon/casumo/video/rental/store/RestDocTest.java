package io.oreon.casumo.video.rental.store;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.UriModifyingOperationPreprocessor;

import javax.validation.Valid;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

public abstract class RestDocTest extends SpringIntegrationTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");


    protected RequestSpecification spec;

    @Before
    public void initializeRestDocSpec() {
        spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(this.restDocumentation))
                .build();
    }

    //Hardcoded port
    protected static UriModifyingOperationPreprocessor setPort() {
        return modifyUris().port(4200);
    }
}

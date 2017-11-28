package io.github.yoshikawaa.sample.app.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/test-context.xml")
public class ExistInCodeListValidatorTest {

    @Inject
    Validator validator;
    
    @Test
    public void testExistInCodeList() {
        // setup
        TestForm form = new TestForm();
        form.setValue("2");

        // execute
        Set<ConstraintViolation<TestForm>> violations = validator.validate(form);

        // assert
        assertThat(violations).hasSize(0);
    }

    @Test
    public void testExistInCodeListNotFound() {
        // setup
        TestForm form = new TestForm();
        form.setValue("4");

        // execute
        Set<ConstraintViolation<TestForm>> violations = validator.validate(form);

        // assert
        assertThat(violations).hasSize(1);
        violations.forEach(v -> {
            v.getPropertyPath().forEach(p -> assertThat(p.getName()).isEqualTo("value"));
            assertThat(v.getMessage()).isEqualTo("Does not exist in CL_ORDERSTATUS");
        });
    }
    
    private static class TestForm {
        @ExistInCodeList(codeListId = "CL_ORDERSTATUS")
        private String value;

        public void setValue(String value) {
            this.value = value;
        }
    }

}

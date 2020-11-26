package am.bdgexame.bancapplication.validator.constraints;

import am.bdgexame.bancapplication.validator.annotations.ValidPassword;
import com.google.common.base.Joiner;
import org.passay.*;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(3, 16),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        //ete comentenq storev grvac 4 toxer@ , apa ktpvi mer grac message@ field-i verevum grvac
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                Joiner.on(",").join(validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}

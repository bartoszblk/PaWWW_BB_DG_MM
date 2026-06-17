package pawww.projekt.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeselValidator implements ConstraintValidator<ValidPesel, String> {

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext context) {
        if (pesel == null || !pesel.matches("\\d{11}")) {
            return false;
        }

        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;

        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt(pesel.substring(i, i + 1)) * weights[i];
        }

        int controlNumber = Integer.parseInt(pesel.substring(10, 11));
        int calculatedControlNumber = 10 - (sum % 10);
        if (calculatedControlNumber == 10) {
            calculatedControlNumber = 0;
        }

        return controlNumber == calculatedControlNumber;
    }
}
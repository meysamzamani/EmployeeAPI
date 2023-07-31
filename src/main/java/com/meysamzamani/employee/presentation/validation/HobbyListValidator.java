package com.meysamzamani.employee.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HobbyListValidator implements ConstraintValidator<HobbyListNotDuplicateAndEmpty, List<String>> {
    @Override
    public boolean isValid(List<String> hobbies, ConstraintValidatorContext context) {
        if (hobbies == null) {
            return true;
        } else if (hobbies.isEmpty()) {
            return true;
        } else {
            return hasDuplicateHobbies(hobbies);
        }
    }

    private boolean hasDuplicateHobbies(List<String> hobbies) {
        Set<String> uniqueHobbies = hobbies.stream()
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
        return uniqueHobbies.size() == hobbies.size();
    }
}

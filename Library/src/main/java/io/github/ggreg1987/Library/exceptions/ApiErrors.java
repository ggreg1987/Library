package io.github.ggreg1987.Library.exceptions;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors {

    private List<String> errors;
    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error ->
                this.errors.add(error.getDefaultMessage()));
    }
}

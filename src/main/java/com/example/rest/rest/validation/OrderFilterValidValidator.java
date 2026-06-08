package com.example.rest.rest.validation;
import com.example.rest.rest.web.model.OrderFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class OrderFilterValidValidator implements ConstraintValidator<OrderFilterValid, OrderFilter> {
    @Override
    public boolean isValid(OrderFilter filter, ConstraintValidatorContext context) {
        if (ObjectUtils.anyNull(filter.getPageSize(), filter.getPageNumber())) return false;
        if ((filter.getMinCost() == null && filter.getMaxCost() != null)
                || (filter.getMinCost() != null && filter.getMaxCost() == null)) return false;
            return true;
    }
}
package com.dochiri.tooltestlab.catalog.product.application.port.out;

import java.util.Set;

public interface CategoryReferenceValidationPort {

    boolean allActive(Set<CategoryReferenceId> categoryReferences);

}

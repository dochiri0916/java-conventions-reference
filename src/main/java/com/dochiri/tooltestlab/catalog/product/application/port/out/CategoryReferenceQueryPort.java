package com.dochiri.tooltestlab.catalog.product.application.port.out;

import java.util.List;
import java.util.Set;

public interface CategoryReferenceQueryPort {

    List<CategoryReferenceData> findByIds(Set<CategoryReferenceId> categoryReferences);

}

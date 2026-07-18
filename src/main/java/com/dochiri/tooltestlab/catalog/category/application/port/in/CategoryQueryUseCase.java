package com.dochiri.tooltestlab.catalog.category.application.port.in;

import java.util.Set;

public interface CategoryQueryUseCase {

    boolean allActive(Set<String> categoryIds);

}

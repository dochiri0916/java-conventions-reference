package com.dochiri.tooltestlab.global.error;

import java.util.Map;

@FunctionalInterface
public interface ErrorCodeMappingProvider {
    Map<String, ApiExceptionMapper.Mapping> errorCodeMappings();
}

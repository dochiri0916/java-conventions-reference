package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ApiExceptionMapper {

    public record Mapping(HttpStatus status, ProblemType problemType) {
    }

    public Mapping map(final String errorCode) {
        return switch (errorCode) {
            // Domain error codes — Product
            case "LOW_STOCK" -> new Mapping(HttpStatus.CONFLICT, ProblemType.BUSINESS_RULE_VIOLATION);
            case "BAD_PRODUCT_ID", "INVALID_PRODUCT", "BAD_PRODUCT_NAME", "INVALID_STOCK" ->
                    new Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);

            // Domain error codes — Order
            case "BAD_ORDER_STATUS" -> new Mapping(HttpStatus.BAD_REQUEST, ProblemType.BUSINESS_RULE_VIOLATION);
            case "INVALID_ORDER_ID", "INVALID_QUANTITY", "BAD_SHIPPING_ADDR", "BAD_ORDER_LINES" ->
                    new Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);

            // Domain error codes — Cart
            case "INVALID_CART_ID", "INVALID_CART_ITEM", "INVALID_CART", "TOO_MANY_ITEMS" ->
                    new Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);

            // Domain error codes — Member
            case "INVALID_MEMBER_ID", "INVALID_EMAIL", "INVALID_PASSWORD", "INVALID_MBR_NAME",
                 "INVALID_MEMBER" ->
                    new Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);

            // Domain error codes — Category
            case "INVALID_CAT_ID", "INVALID_CAT_NAME" ->
                    new Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);

            // Domain error codes — Review
            case "INVALID_REVIEW_ID", "INVALID_RATING", "BAD_RVW_CONTENT", "INVALID_REVIEW" ->
                    new Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);

            // Application error codes — Order
            case "ORDER_NOT_FOUND" -> new Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
            case "NOT_ORDER_OWNER" -> new Mapping(HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN);
            case "EMPTY_CART" -> new Mapping(HttpStatus.BAD_REQUEST, ProblemType.BUSINESS_RULE_VIOLATION);

            // Application error codes — Cart
            case "CART_NOT_FOUND" -> new Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
            case "CART_ITEM_NOT_FOUND" -> new Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
            case "NOT_CART_OWNER" -> new Mapping(HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN);

            // Application error codes — Member
            case "MEMBER_NOT_FOUND" -> new Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
            case "LOGIN_FAILED" -> new Mapping(HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED);
            case "DUPLICATE_EMAIL" -> new Mapping(HttpStatus.CONFLICT, ProblemType.CONFLICT);

            // Application error codes — Product
            case "PRODUCT_NOT_FOUND" -> new Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);

            // Application error codes — Review
            case "DUPLICATE_REVIEW" -> new Mapping(HttpStatus.CONFLICT, ProblemType.CONFLICT);
            case "REVIEW_NOT_FOUND" -> new Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
            case "NOT_REVIEW_OWNER" -> new Mapping(HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN);

            default -> new Mapping(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.INTERNAL_ERROR);
        };
    }
}

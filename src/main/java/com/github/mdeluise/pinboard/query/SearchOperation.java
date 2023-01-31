package com.github.mdeluise.pinboard.query;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, SORT_ASC, SORT_DESC;

    public static final String[] SIMPLE_OPERATION_SET = {":", "!", ">", "<", "+", "-"};


    public static SearchOperation getSimpleOperation(String input) {
        return switch (input) {
            case ":" -> EQUALITY;
            case "!" -> NEGATION;
            case ">" -> GREATER_THAN;
            case "<" -> LESS_THAN;
            case "+", " " -> // + is encoded in query strings as a space
                SORT_ASC;
            case "-" -> SORT_DESC;
            default -> null;
        };
    }
}

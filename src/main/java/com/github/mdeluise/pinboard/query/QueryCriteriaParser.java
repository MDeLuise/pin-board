package com.github.mdeluise.pinboard.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryCriteriaParser {
    private static final String WORD_REGEX = "[a-zA-Z]\\w*";
    private static final String VALUE_REGEX = "(\\w|\\s)+";
    private static final String OPERATOR_REGEX = "(:|<|>|!|\\+|-|\\s)";
    private static final String TIMESTAMP_REGEX =
        "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0 -9]{2}:[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2}";
    private static final String ID_REGEX = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";
    private static final String FULL_REGEX =
        "(" + WORD_REGEX + ")" + OPERATOR_REGEX + "(" + TIMESTAMP_REGEX + "|" + ID_REGEX + "|" + VALUE_REGEX + ")?,";
    private static final Pattern SEARCH_PATTERN = Pattern.compile(FULL_REGEX);


    public static List<QueryCriteria> parse(String searchString) {
        List<QueryCriteria> queryCriteriaList = new ArrayList<>();
        if (searchString != null) {
            Matcher matcher = SEARCH_PATTERN.matcher(searchString + ",");
            while (matcher.find()) {
                QueryCriteria queryCriteria = new QueryCriteria();
                queryCriteria.setKey(matcher.group(1));
                queryCriteria.setOperation(SearchOperation.getSimpleOperation(matcher.group(2)));
                queryCriteria.setValue(matcher.group(3));
                if (queryCriteria.getOperation() != SearchOperation.SORT_DESC &&
                         queryCriteria.getOperation() != SearchOperation.SORT_ASC ||
                        queryCriteria.getValue() == null) {
                    queryCriteriaList.add(queryCriteria);
                }
            }
        }
        return queryCriteriaList;
    }
}


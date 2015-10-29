package com.groupbyinc.api;

import com.groupbyinc.api.model.PartialMatchRule;
import com.groupbyinc.api.model.MatchStrategy;
import com.groupbyinc.api.model.Sort;
import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Query extends AbstractQuery<Request, Query> {

    private List<Sort> sort = new ArrayList<Sort>();
    private MatchStrategy matchStrategy;
    private boolean wildcardSearchEnabled;

    protected static com.groupbyinc.api.request.MatchStrategy convertPartialMatchStrategy(MatchStrategy strategy) {
        com.groupbyinc.api.request.MatchStrategy convertedStrategy = null;
        if (strategy != null) {
            if (CollectionUtils.isNotEmpty(strategy.getRules())) {
                convertedStrategy = new com.groupbyinc.api.request.MatchStrategy();
                for (PartialMatchRule r : strategy.getRules()) {
                    convertedStrategy.getRules().add(convertPartialMatchRule(r));
                }
            }
        }
        return convertedStrategy;
    }

    protected static com.groupbyinc.api.request.PartialMatchRule convertPartialMatchRule(PartialMatchRule rule) {
        return rule == null ? null : new com.groupbyinc.api.request.PartialMatchRule().setTerms(rule.getTerms())
                                                                                      .setTermsGreaterThan(
                                                                                              rule.getTermsGreaterThan())
                                                                                      .setMustMatch(rule.getMustMatch())
                                                                                      .setPercentage(
                                                                                              rule.getPercentage());
    }

    @Override
    protected Request generateRequest() {
        Request request = new Request();
        request.setWildcardSearchEnabled(isWildcardSearchEnabled());
        if (CollectionUtils.isNotEmpty(sort)) {
            for (Sort s : sort) {
                request.setSort(convertSort(s));
            }
        }
        request.setMatchStrategy(convertPartialMatchStrategy(matchStrategy));
        return request;
    }

    @Override
    protected RefinementsRequest<Request> populateRefinementRequest() {
        return new RefinementsRequest<Request>().setOriginalQuery(generateRequest());
    }

    public boolean isWildcardSearchEnabled() {
        return wildcardSearchEnabled;
    }

    /**
     * <code>
     * Indicate if the *(star) character in the search string should be treated as a wildcard prefix search.
     * For example, `sta*` will match `star` and `start`.
     *
     * JSON Reference:
     *
     *     { "wildcardSearchEnabled" : true }
     *
     * </code>
     *
     * @param wildcardSearchEnabled true to enable wildcard search, false otherwise.
     * @return the Query object itself
     */
    public Query setWildcardSearchEnabled(boolean wildcardSearchEnabled) {
        this.wildcardSearchEnabled = wildcardSearchEnabled;
        return this;
    }

    /**
     * @return The current list of sort parameters
     */
    public List<Sort> getSort() {
        return sort;
    }

    /**
     * <code>
     * Specifies the sort order applied to the fields in the order specified. If no sort criteria are specified, the
     * default is to sort by relevance. There is a special sort field `_relevance`, which also specifies sorting by
     * relevance. It is possible to specify multiple sort criteria. The criteria order matters, as the records will be
     * sorted by the first criteria and then any matches will be tie-broken using the next criteria. Given an example
     * where the sort is specified as `category` then `_relevance`, results will be sorted first by `category` and
     * relevance will only affect the order between records that have the same category.
     *
     * Please note, sorting is based on the actual value in the record. For example, if sorting on `price`, and
     * `price` is a `Range` navigation, the records will be sorted according to the actual price value in the record
     * and not the bucket value.
     *
     * The order field can be set to either `Ascending` or `Descending`. When sorting by relevance, the order is always
     * `Descending`. For any other field, the default order is `Ascending`.
     *
     * JSON Reference:
     *
     *     { "sort": { "field": "price", "order": "Descending" } }
     *     { "sort": [{ "field": "_relevance" }, { "field": "price", "order": "Descending" }] }
     *     { "sort": [{ "field": "brand", "order":"Ascending" }, { "field": "_relevance" }, { "field": "price" }] }
     *
     * </code>
     *
     * @param sort Any number of sort criteria.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Query setSort(Sort... sort) {
        CollectionUtils.addAll(this.sort, sort);
        return this;
    }

    /**
     * <code>
     * A match strategy allows you to explicitly manage recall on a per query basis. There must always be one term
     * matching in a query, thus `termsGreaterThan` can only be defined from 1 upwards and `terms` can only be defined
     * from 2 upwards. It is not possible to match more terms than passed into the query. Relative `mustMatch` values
     * can be used in conjunction with `termsGreaterThan`. A `"percentage": true` flag denotes a relative `mustMatch`
     * to the portion of the terms and will always round down (i.e. 50% must match of 3 terms, means that 1 term must
     * match).
     *
     * The following is the default match strategy:
     *
     * ```
     * { "matchStrategy": { "rules":[{ "terms": 2, "mustMatch": 2 },
     *                               { "terms": 3, "mustMatch": 2 },
     *                               { "terms": 4, "mustMatch": 3 },
     *                               { "terms": 5, "mustMatch": 3 },
     *                               { "terms": 6, "mustMatch": 4 },
     *                               { "terms": 7, "mustMatch": 4 },
     *                               { "terms": 8, "mustMatch": 5 },
     *                               { "termsGreaterThan": 8, "mustMatch": 60, "percentage": true }] } }
     * ```
     *
     * An exact matching strategy would be:
     *
     * ```
     * { "matchStrategy": { "rules": { "termsGreaterThan": 1, "mustMatch": 100, "percentage": true } } }
     * ```
     *
     * Please note, it is highly recommended that the highest rule is defined with `termsGreaterThan`
     * and a relative `mustMatch` as that guarantees that the number of matches required grows with the number of terms
     * passed into the query.
     *
     * JSON Reference
     *
     *     { "matchStrategy": { "rules":[{ "terms": 2, "mustMatch": 2 },
     *                                   { "terms": 3, "mustMatch": 2 },
     *                                   { "terms": 4, "mustMatch": 3 },
     *                                   { "terms": 5, "mustMatch": 3 },
     *                                   { "terms": 6, "mustMatch": 4 },
     *                                   { "terms": 7, "mustMatch": 4 },
     *                                   { "terms": 8, "mustMatch": 5 },
     *                                   { "termsGreaterThan": 8, "mustMatch": 60, "percentage": true }] } }
     *     { "matchStrategy": { "rules": { "termsGreaterThan": 1, "mustMatch": 100, "percentage": true } } }
     *     { "matchStrategy": { "rules":[{ "terms": 2, "mustMatch": 1 },
     *                                   { "termsGreaterThan": 2, "mustMatch": 75, "percentage": true }] } }
     *
     * </code>
     *
     * @param matchStrategy A match strategy composed of partial matching rules.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Query setMatchStrategy(MatchStrategy matchStrategy) {
        this.matchStrategy = matchStrategy;
        return this;
    }

}

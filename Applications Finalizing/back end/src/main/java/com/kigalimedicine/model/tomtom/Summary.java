
package com.kigalimedicine.model.tomtom;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query",
    "queryType",
    "queryTime",
    "numResults",
    "offset",
    "totalResults",
    "fuzzyLevel"
})
public class Summary {

    @JsonProperty("query")
    private String query;
    @JsonProperty("queryType")
    private String queryType;
    @JsonProperty("queryTime")
    private Integer queryTime;
    @JsonProperty("numResults")
    private Integer numResults;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("totalResults")
    private Integer totalResults;
    @JsonProperty("fuzzyLevel")
    private Integer fuzzyLevel;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("query")
    public String getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(String query) {
        this.query = query;
    }

    @JsonProperty("queryType")
    public String getQueryType() {
        return queryType;
    }

    @JsonProperty("queryType")
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    @JsonProperty("queryTime")
    public Integer getQueryTime() {
        return queryTime;
    }

    @JsonProperty("queryTime")
    public void setQueryTime(Integer queryTime) {
        this.queryTime = queryTime;
    }

    @JsonProperty("numResults")
    public Integer getNumResults() {
        return numResults;
    }

    @JsonProperty("numResults")
    public void setNumResults(Integer numResults) {
        this.numResults = numResults;
    }

    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty("offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @JsonProperty("totalResults")
    public Integer getTotalResults() {
        return totalResults;
    }

    @JsonProperty("totalResults")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("fuzzyLevel")
    public Integer getFuzzyLevel() {
        return fuzzyLevel;
    }

    @JsonProperty("fuzzyLevel")
    public void setFuzzyLevel(Integer fuzzyLevel) {
        this.fuzzyLevel = fuzzyLevel;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

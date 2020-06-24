
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
    "topLeftPoint",
    "btmRightPoint"
})
public class Viewport {

    @JsonProperty("topLeftPoint")
    private TopLeftPoint topLeftPoint;
    @JsonProperty("btmRightPoint")
    private BtmRightPoint btmRightPoint;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("topLeftPoint")
    public TopLeftPoint getTopLeftPoint() {
        return topLeftPoint;
    }

    @JsonProperty("topLeftPoint")
    public void setTopLeftPoint(TopLeftPoint topLeftPoint) {
        this.topLeftPoint = topLeftPoint;
    }

    @JsonProperty("btmRightPoint")
    public BtmRightPoint getBtmRightPoint() {
        return btmRightPoint;
    }

    @JsonProperty("btmRightPoint")
    public void setBtmRightPoint(BtmRightPoint btmRightPoint) {
        this.btmRightPoint = btmRightPoint;
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

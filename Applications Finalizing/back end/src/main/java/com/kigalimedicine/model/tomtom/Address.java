
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
    "streetNumber",
    "streetName",
    "municipalitySubdivision",
    "municipality",
    "countrySecondarySubdivision",
    "countryTertiarySubdivision",
    "countrySubdivision",
    "postalCode",
    "countryCode",
    "country",
    "countryCodeISO3",
    "freeformAddress",
    "localName",
    "extendedPostalCode"
})
public class Address {

    @JsonProperty("streetNumber")
    private String streetNumber;
    @JsonProperty("streetName")
    private String streetName;
    @JsonProperty("municipalitySubdivision")
    private String municipalitySubdivision;
    @JsonProperty("municipality")
    private String municipality;
    @JsonProperty("countrySecondarySubdivision")
    private String countrySecondarySubdivision;
    @JsonProperty("countryTertiarySubdivision")
    private String countryTertiarySubdivision;
    @JsonProperty("countrySubdivision")
    private String countrySubdivision;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("country")
    private String country;
    @JsonProperty("countryCodeISO3")
    private String countryCodeISO3;
    @JsonProperty("freeformAddress")
    private String freeformAddress;
    @JsonProperty("localName")
    private String localName;
    @JsonProperty("extendedPostalCode")
    private String extendedPostalCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("streetNumber")
    public String getStreetNumber() {
        return streetNumber;
    }

    @JsonProperty("streetNumber")
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @JsonProperty("streetName")
    public String getStreetName() {
        return streetName;
    }

    @JsonProperty("streetName")
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @JsonProperty("municipalitySubdivision")
    public String getMunicipalitySubdivision() {
        return municipalitySubdivision;
    }

    @JsonProperty("municipalitySubdivision")
    public void setMunicipalitySubdivision(String municipalitySubdivision) {
        this.municipalitySubdivision = municipalitySubdivision;
    }

    @JsonProperty("municipality")
    public String getMunicipality() {
        return municipality;
    }

    @JsonProperty("municipality")
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    @JsonProperty("countrySecondarySubdivision")
    public String getCountrySecondarySubdivision() {
        return countrySecondarySubdivision;
    }

    @JsonProperty("countrySecondarySubdivision")
    public void setCountrySecondarySubdivision(String countrySecondarySubdivision) {
        this.countrySecondarySubdivision = countrySecondarySubdivision;
    }

    @JsonProperty("countryTertiarySubdivision")
    public String getCountryTertiarySubdivision() {
        return countryTertiarySubdivision;
    }

    @JsonProperty("countryTertiarySubdivision")
    public void setCountryTertiarySubdivision(String countryTertiarySubdivision) {
        this.countryTertiarySubdivision = countryTertiarySubdivision;
    }

    @JsonProperty("countrySubdivision")
    public String getCountrySubdivision() {
        return countrySubdivision;
    }

    @JsonProperty("countrySubdivision")
    public void setCountrySubdivision(String countrySubdivision) {
        this.countrySubdivision = countrySubdivision;
    }

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("countryCodeISO3")
    public String getCountryCodeISO3() {
        return countryCodeISO3;
    }

    @JsonProperty("countryCodeISO3")
    public void setCountryCodeISO3(String countryCodeISO3) {
        this.countryCodeISO3 = countryCodeISO3;
    }

    @JsonProperty("freeformAddress")
    public String getFreeformAddress() {
        return freeformAddress;
    }

    @JsonProperty("freeformAddress")
    public void setFreeformAddress(String freeformAddress) {
        this.freeformAddress = freeformAddress;
    }

    @JsonProperty("localName")
    public String getLocalName() {
        return localName;
    }

    @JsonProperty("localName")
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @JsonProperty("extendedPostalCode")
    public String getExtendedPostalCode() {
        return extendedPostalCode;
    }

    @JsonProperty("extendedPostalCode")
    public void setExtendedPostalCode(String extendedPostalCode) {
        this.extendedPostalCode = extendedPostalCode;
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

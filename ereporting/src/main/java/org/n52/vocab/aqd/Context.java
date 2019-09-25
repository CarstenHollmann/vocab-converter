/*
 * Copyright (C) 2019-2019 52Â°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */

package org.n52.vocab.aqd;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@base",
    "skos",
    "concepts",
    "prefLabel",
    "broader",
    "narrower",
    "@language"
})
public class Context {

    @JsonProperty("@base")
    private String base;
    @JsonProperty("skos")
    private String skos;
    @JsonProperty("concepts")
    private String concepts;
    @JsonProperty("prefLabel")
    private String prefLabel;
    @JsonProperty("broader")
    private String broader;
    @JsonProperty("narrower")
    private String narrower;
    @JsonProperty("@language")
    private String language;

    @JsonProperty("@base")
    public String getBase() {
        return base;
    }

    @JsonProperty("@base")
    public void setBase(String base) {
        this.base = base;
    }

    @JsonProperty("skos")
    public String getSkos() {
        return skos;
    }

    @JsonProperty("skos")
    public void setSkos(String skos) {
        this.skos = skos;
    }

    @JsonProperty("concepts")
    public String getConcepts() {
        return concepts;
    }

    @JsonProperty("concepts")
    public void setConcepts(String concepts) {
        this.concepts = concepts;
    }

    @JsonProperty("prefLabel")
    public String getPrefLabel() {
        return prefLabel;
    }

    @JsonProperty("prefLabel")
    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    @JsonProperty("broader")
    public String getBroader() {
        return broader;
    }

    @JsonProperty("broader")
    public void setBroader(String broader) {
        this.broader = broader;
    }

    @JsonProperty("narrower")
    public String getNarrower() {
        return narrower;
    }

    @JsonProperty("narrower")
    public void setNarrower(String narrower) {
        this.narrower = narrower;
    }

    @JsonProperty("@language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("@language")
    public void setLanguage(String language) {
        this.language = language;
    }

}

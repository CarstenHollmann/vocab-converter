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
package org.n52.vocab.convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.n52.janmayen.Json;
import org.n52.sos.predefined.AbstractPredefined;
import org.n52.sos.predefined.Phenomenon;
import org.n52.sos.predefined.PhenomenonPredefined;
import org.n52.vocab.aqd.Concept;
import org.n52.vocab.aqd.Data;
import org.n52.vocab.aqd.PrefLabel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AqdPollutantConverter extends AqdPredefinedConverter<PhenomenonPredefined> {

    @Override
    protected PhenomenonPredefined getPredefined(String name) {
        PhenomenonPredefined phenomenonPredefined = new PhenomenonPredefined();
        phenomenonPredefined.setName(name);
        phenomenonPredefined.setDescription("Predefined pollutant data for AQD e-Reporting");
        return phenomenonPredefined;
    }

    @Override
    protected void addValues(PhenomenonPredefined predefined, Concept concept, String baseURL) {
        Phenomenon phenomenonEntity = new Phenomenon();
        phenomenonEntity.setIdentifier(baseURL + concept.getId());
        PrefLabel label = getDefaultLabel(concept.getPrefLabel());
        phenomenonEntity.setName(label.getValue());
        phenomenonEntity.setDescription(label.getValue());
        concept.getPrefLabel().remove(label);
        if (concept.getPrefLabel().size() > 1) {
            phenomenonEntity.setTranslations(createI18N(concept.getPrefLabel()));
        } else if (concept.getId().equals("7")) {
            PrefLabel l = new PrefLabel();
            l.setLanguage("de");
            l.setValue("Ozon (Luft)");
            concept.getPrefLabel().add(l);
            phenomenonEntity.setTranslations(createI18N(concept.getPrefLabel()));
        }
        predefined.addValue(phenomenonEntity);
    }

    public static void main(String[] args) {
        try {
            AqdPollutantConverter converter = new AqdPollutantConverter();
            JsonNode node = Json.loadURL(AqdPollutantConverter.class.getResource("/pollutant.json"));
            Data data = new ObjectMapper().treeToValue(node, Data.class);
            AbstractPredefined<?> convert = converter.convert(data, "ereporting");
            File file = Paths.get("target", "aqd_pollutant.json").toAbsolutePath().toFile();
            if (file.exists()) {
                file.delete();
             }
             file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MAPPER.writeValue(fileOutputStream, convert);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

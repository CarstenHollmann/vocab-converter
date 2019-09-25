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
import org.n52.sos.predefined.Unit;
import org.n52.sos.predefined.UnitPredefined;
import org.n52.vocab.aqd.Concept;
import org.n52.vocab.aqd.Data;
import org.n52.vocab.aqd.PrefLabel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AqdUntisConverter extends AqdPredefinedConverter<UnitPredefined> {

    @Override
    protected UnitPredefined getPredefined(String name) {
        UnitPredefined unitPredefined = new UnitPredefined();
        unitPredefined.setName(name);
        unitPredefined.setDescription("Predefined unit data for AQD e-Reporting");
        return unitPredefined;
    }

    @Override
    protected void addValues(UnitPredefined predefined, Concept concept, String baseURL) {
        Unit unitEntity = new Unit();
        unitEntity.setSymbol(concept.getId());
        PrefLabel label = getDefaultLabel(concept.getPrefLabel());
        unitEntity.setName(label.getValue());
        unitEntity.setLink(baseURL + concept.getId());
        concept.getPrefLabel().remove(label);
        if (concept.getPrefLabel().size() > 1) {
            unitEntity.setTranslations(createI18N(concept.getPrefLabel()));
        }
        predefined.addValue(unitEntity);
    }

    public static void main(String[] args) {
        try {
            AqdUntisConverter converter = new AqdUntisConverter();
            JsonNode node = Json.loadURL(AqdPollutantConverter.class.getResource("/uom_concentration.json"));
            Data data = new ObjectMapper().treeToValue(node, Data.class);
            AbstractPredefined<?> convert = converter.convert(data, "ereporting");
            File file = Paths.get("target", "aqd_concentration.json").toAbsolutePath().toFile();
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

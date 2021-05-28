/*
 * Copyright (C) 2019-2021 52Â°North Spatial Information Research GmbH
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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.sos.predefined.AbstractPredefined;
import org.n52.sos.predefined.Unit;
import org.n52.sos.predefined.UnitPredefined;
import org.n52.svalbard.util.XmlHelper;
import org.n52.voca.convert.AbstractConverter;
import org.unitsofmeasure.ucumEssence.BaseUnitType;
import org.unitsofmeasure.ucumEssence.RootDocument;
import org.unitsofmeasure.ucumEssence.RootType;
import org.unitsofmeasure.ucumEssence.UnitType;

public class UcumUnitConverter extends AbstractConverter<AbstractPredefined<?>, XmlObject> {

    private static final String UCUM_URL = "http://unitsofmeasure.org/ucum-essence.xml";

    @Override
    protected AbstractPredefined<?> convert(XmlObject xml, String name) {
        UnitPredefined predefined = getPredefined(name);
        RootDocument doc = (RootDocument) xml;
        RootType type = doc.getRoot();
        processBaseUnits(type, predefined);
        processUnits(type, predefined);
        return predefined;
    }

    private void processBaseUnits(RootType root, UnitPredefined predefined) {
        if (root.getBaseUnitArray() != null) {
            for (BaseUnitType type : root.getBaseUnitArray()) {
                Unit unit = new Unit();
                unit.setSymbol(checkCodeForBrackets(type.getCode()));
                unit.setName(type.getName());
                unit.setLink(UCUM_URL);
                predefined.addValue(unit);
            }
        }
    }

    private void processUnits(RootType root, UnitPredefined predefined) {
        if (root.getUnitArray() != null) {
            for (UnitType type : root.getUnitArray()) {
                Unit unit = new Unit();
                unit.setSymbol(checkCodeForBrackets(type.getCode()));
                unit.setName(type.getNameArray().length > 0 ? type.getNameArray()[0] : "");
                unit.setLink(UCUM_URL);
                predefined.addValue(unit);
            }
        }
    }

    private String checkCodeForBrackets(String code) {
        return code.replace("[", "").replace("]", "");
    }

    private UnitPredefined getPredefined(String collection) {
        UnitPredefined predefined = new UnitPredefined();
        predefined.setName("ucum");
        predefined.setDescription("Predefined unit data for UCUM!");
        return predefined;
    }

    public static void main(String[] args) {
        try {
            UcumUnitConverter converter = new UcumUnitConverter();
            File xmlFile = Paths.get("src", "main", "resources", "ucum.xml").toAbsolutePath().toFile();
            XmlObject xmlObject = XmlHelper.loadXmlDocumentFromFile(xmlFile);
            AbstractPredefined<?> convert = converter.convert(xmlObject, "ucum");
            File file = Paths.get("target", "ucum_units.json").toAbsolutePath().toFile();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MAPPER.writeValue(fileOutputStream, convert);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OwsExceptionReport e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

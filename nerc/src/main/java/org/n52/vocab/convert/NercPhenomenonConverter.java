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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import org.n52.sos.predefined.AbstractPredefined;
import org.n52.sos.predefined.Phenomenon;
import org.n52.sos.predefined.PhenomenonPredefined;
import org.n52.voca.convert.AbstractConverter;

public class NercPhenomenonConverter extends AbstractConverter<AbstractPredefined<?>, Model> {

    @Override
    protected AbstractPredefined<?> convert(Model model, String name) {
        PhenomenonPredefined phenomenonPredefined = getPredefined(name);
        ResIterator iter = model.listResourcesWithProperty(SKOS.notation);
        while (iter.hasNext()) {
            addValues(phenomenonPredefined, iter.next());
        }
        return phenomenonPredefined;
    }

    protected void addValues(PhenomenonPredefined predefined, Resource resource) {
        Phenomenon phenomenonEntity = new Phenomenon();
        phenomenonEntity.setIdentifier(resource.getURI());
        phenomenonEntity.setName(resource.getProperty(SKOS.prefLabel).getString());
        phenomenonEntity.setDescription( resource.getProperty(SKOS.definition).getString());
        predefined.addValue(phenomenonEntity);
    }

    private PhenomenonPredefined getPredefined(String collection) {
        PhenomenonPredefined phenomenonPredefined = new PhenomenonPredefined();
        phenomenonPredefined.setName("nerc_" + collection);
        phenomenonPredefined.setDescription("Predefined phenomenon data for vocab.nerc.ac.uk " + collection + " collection");
        return phenomenonPredefined;
    }

    public static void main(String[] args) {
        try {
            NercPhenomenonConverter converter = new NercPhenomenonConverter();
            String collection = "P02";
            Model model = ModelFactory.createDefaultModel();
            model.read("http://vocab.nerc.ac.uk/collection/" + collection);
            AbstractPredefined<?> convert = converter.convert(model, collection);
            File file = Paths.get("target", "nerc_" + collection + "_parameter.json").toAbsolutePath().toFile();
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

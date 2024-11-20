package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.util.model.BaseModel;

public class ModelTrainerUtil {
    private static final Logger LOGGER = Logger.getLogger(ModelTrainerUtil.class.getName());

    // Private constructor to hide the implicit public one
    private ModelTrainerUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void saveModel(BaseModel model, String modelFilePath) {
        if (model != null) {
            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath))) {
                model.serialize(modelOut);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to save model", e);
            }
        } else {
            LOGGER.severe("Model is null, training failed.");
        }
    }
}
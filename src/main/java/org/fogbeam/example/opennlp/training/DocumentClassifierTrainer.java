package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class DocumentClassifierTrainer {
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierTrainer.class.getName());

	public static void main(String[] args) throws Exception {
		DoccatModel model = null;

		// Try-with-resources to ensure InputStream is closed automatically
		try (InputStream dataIn = new FileInputStream("training_data/en-doccat.train")) {
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
			model = DocumentCategorizerME.train("en", sampleStream);
		} catch (IOException e) {
			// Failed to read or parse training data, training failed
			LOGGER.log(Level.SEVERE, "Failed to read or parse training data", e);
		}

		// Check if model is not null before using it
		if (model != null) {
			// Try-with-resources to ensure OutputStream is closed automatically
			try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream("models/en-doccat.model"))) {
				model.serialize(modelOut);
			} catch (IOException e) {
				// Failed to save model
				LOGGER.log(Level.SEVERE, "Failed to save model", e);
			}
		} else {
			LOGGER.severe("Model is null, training failed.");
		}

		System.out.println("done");
	}
}
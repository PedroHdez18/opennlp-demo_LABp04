package org.fogbeam.example.opennlp.training;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

	public static void main(String[] args) {
		DoccatModel model = null;

		try (InputStream dataIn = new FileInputStream("training_data/en-doccat.train")) {
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
			model = DocumentCategorizerME.train("en", sampleStream);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to read or parse training data", e);
		}

		ModelTrainerUtil.saveModel(model, "models/en-doccat.model");
		System.out.println("done");
	}
}
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

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSTaggerFactory;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class PartOfSpeechTaggerTrainer {
	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerTrainer.class.getName());

	public static void main(String[] args) {
		POSModel model = null;

		// Try-with-resources to ensure InputStream is closed automatically
		try (InputStream dataIn = new FileInputStream("training_data/en-pos.train")) {
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);
			model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), new POSTaggerFactory());
		} catch (IOException e) {
			// Failed to read or parse training data, training failed
			LOGGER.log(Level.SEVERE, "Failed to read or parse training data", e);
		}

		if (model != null) { // Verificación para evitar NullPointerException
			// Try-with-resources to ensure OutputStream is closed automatically
			try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream("models/en-pos.model"))) {
				model.serialize(modelOut);
			} catch (IOException e) {
				// Failed to save model
				LOGGER.log(Level.SEVERE, "Failed to save model", e);
			}
		} else {
			LOGGER.severe("Training failed. Model is null.");
		}

		System.out.println("done");
	}
}
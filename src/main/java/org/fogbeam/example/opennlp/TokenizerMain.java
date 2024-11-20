package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.io.BufferedWriter;
import java.util.*;
import java.io.FileWriter;


import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class TokenizerMain {
	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("Uso: java TokenizerMain <modelo> <salida> <archivo1> <archivo2> ...");
			return;
		}

		// Ruta del modelo y archivo de salida
		String modelPath = args[0];
		String outputPath = args[1];
		String[] inputFiles = Arrays.copyOfRange(args, 2, args.length);

		// Cargar el modelo
		try (InputStream modelIn = new FileInputStream(modelPath)) {
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);

			// Procesar los archivos de entrada
			List<String> tokens = new ArrayList<>();
			for (String filePath : inputFiles) {
				System.out.println("Procesando archivo: " + filePath);
				tokens.addAll(tokenizeFile(filePath, tokenizer));
			}

			// Escribir los tokens en el archivo de salida
			writeTokensToFile(tokens, outputPath);
			System.out.println("Tokens escritos en: " + outputPath);
		} catch (IOException e) {
			System.err.println("Error al procesar: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Tokeniza el contenido de un archivo utilizando el modelo dado.
	 */
	private static List<String> tokenizeFile(String filePath, Tokenizer tokenizer) throws IOException {
		List<String> tokens = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));

		for (String line : lines) {
			String[] lineTokens = tokenizer.tokenize(line);
			tokens.addAll(Arrays.asList(lineTokens));
		}
		return tokens;
	}

	/**
	 * Escribe los tokens en un archivo de salida, separados por espacios.
	 */
	private static void writeTokensToFile(List<String> tokens, String outputPath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
			for (String token : tokens) {
				writer.write(token);
				writer.newLine(); // Tokens en líneas separadas
			}
		}
	}
}
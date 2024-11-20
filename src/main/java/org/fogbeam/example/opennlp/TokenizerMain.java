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

/**
 * Clase principal para la tokenización de texto utilizando el modelo OpenNLP.
 */
public class TokenizerMain {

	/**
	 * Método principal que se ejecuta al iniciar el programa.
	 *
	 * @param args Argumentos de la línea de comandos: <modelo> <salida> <archivo1> <archivo2> ...
	 * @throws Exception Si ocurre algún error durante la ejecución.
	 */
	public static void main(String[] args) throws Exception {
		// Verifica que se hayan pasado al menos 3 argumentos
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
	 *
	 * @param filePath Ruta del archivo a tokenizar.
	 * @param tokenizer Instancia del tokenizador a utilizar.
	 * @return Lista de tokens obtenidos del archivo.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	private static List<String> tokenizeFile(String filePath, Tokenizer tokenizer) throws IOException {
		List<String> tokens = new ArrayList<>();
		// Leer todas las líneas del archivo
		List<String> lines = Files.readAllLines(Paths.get(filePath));

		// Tokenizar cada línea y agregar los tokens a la lista
		for (String line : lines) {
			String[] lineTokens = tokenizer.tokenize(line);
			tokens.addAll(Arrays.asList(lineTokens));
		}
		return tokens;
	}

	/**
	 * Escribe los tokens en un archivo de salida, separados por espacios.
	 *
	 * @param tokens Lista de tokens a escribir.
	 * @param outputPath Ruta del archivo de salida.
	 * @throws IOException Si ocurre un error al escribir el archivo.
	 */
	private static void writeTokensToFile(List<String> tokens, String outputPath) throws IOException {
		// Usar BufferedWriter para escribir los tokens en el archivo de salida
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
			for (String token : tokens) {
				writer.write(token);
				writer.newLine(); // Tokens en líneas separadas
			}
		}
	}
}
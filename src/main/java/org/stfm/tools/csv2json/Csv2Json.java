package org.stfm.tools.csv2json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * options :
 * <ul>
 * <li>-f : input csv filename
 * <li>-d : csv field delimiter (optional, default is ",")
 * <li>-i : comma separated lines numbers to ignore (line numbers start with 1),
 * for instance : 1,2,3
 * <li>-o : JSON output file name
 * <li>-a : output JSON array variable name (optional)
 * <ul>
 * 
 * 
 * @author stfm
 * 
 */
public class Csv2Json {

	private String csvfieldDelimiter = ",";
	private String csvFileName = null;
	private List<String> csvIgnoredLineNumbers;
	private String jsonFileName = null;
	private String jsonArrayVarName = null;
	private String newline = System.getProperty("line.separator");

	public void setCsvfieldDelimiter(String csvfieldDelimiter) {
		this.csvfieldDelimiter = csvfieldDelimiter;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public void setCsvIgnoredLineNumbers(List<String> csvIgnoredLineNumbers) {
		this.csvIgnoredLineNumbers = csvIgnoredLineNumbers;
	}

	public void setJsonFileName(String jsonFileName) {
		this.jsonFileName = jsonFileName;
	}

	public void setJsonArrayVarName(String jsonArrayVarName) {
		this.jsonArrayVarName = jsonArrayVarName;
	}

	public void setNewline(String newline) {
		this.newline = newline;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Csv2Json converter = new Csv2Json();

		parseArgs(args, converter);

		converter.convert();

	}

	public void convert() {

		try {

			String fileOut = jsonFileName == null ? csvFileName.replace(".csv",
					".json") : jsonFileName;
			Writer output = new BufferedWriter(new FileWriter(fileOut));

			if (jsonArrayVarName != null) {
				output.write(jsonArrayVarName + "=");
			}

			output.write("[" + newline);

			FileInputStream fstream = new FileInputStream(csvFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			int lineNumber = 1;
			while ((strLine = br.readLine()) != null) {

				if (strLine.trim().equals("")
						|| csvIgnoredLineNumbers.contains(lineNumber + "")) {
					lineNumber++;
					continue;
				}

				output.write("[");

				StringTokenizer tok = new StringTokenizer(strLine,
						csvfieldDelimiter, true);
				boolean start = true;

				while (tok.hasMoreTokens()) {

					String token = tok.nextToken().trim();

					if (!token.equals(csvfieldDelimiter)) {

						if (!start) {
							output.write(",");
						}
						start = false;

						handleToken(token, output);

					} else {

						if (tok.hasMoreTokens()) {
							String tokenNext = tok.nextToken().trim();

							if (!start) {
								output.write(",");
							}
							start = false;

							if (!tokenNext.equals(csvfieldDelimiter)) {

								handleToken(tokenNext, output);

							} else {

								output.write("\"\"");

							}
						}

					}

				}
				output.write("]," + newline);

				lineNumber++;
			}

			in.close();

			output.write("]");

			output.close();

			System.out.println("Done! Output file written to : "
					+ new File(fileOut).getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void handleToken(String token, Writer output)
			throws IOException {

		if (!token.startsWith("\"")) {
			output.write("\"");
		}

		output.write(token);

		if (!token.endsWith("\"")) {
			output.write("\"");
		}

	}

	protected static void parseArgs(String args[], Csv2Json converter) {

		if (args == null || args.length == 0) {
			System.err.println("to few args");
			System.exit(0);
		}

		int i = 0;
		while (i < args.length) {

			if (args[i].equals("-") || !args[i].startsWith("-")) {
				System.err.println("error: unknown arg or option : " + args[i]);
				System.exit(0);
			} else {
				for (int c = 1; c < args[i].length(); ++c) {

					char cChar = args[i].charAt(c);
					if (cChar == 'f') {
						if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
							converter.setCsvFileName(args[++i]);
							break;
						} else {
							System.err
									.println("error: missing filename for option -f");
							System.exit(0);
						}
					} else if (cChar == 'd') {
						if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
							converter.setCsvfieldDelimiter(args[++i]);
							break;
						} else {
							System.err
									.println("error: missing delimiter for option -d");
							System.exit(0);
						}
					} else if (cChar == 'i') {
						if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
							converter.setCsvIgnoredLineNumbers(Arrays
									.asList(args[++i].split(",")));
							break;
						} else {
							System.err
									.println("error: missing line numbers for option -i");
							System.exit(0);
						}
					} else if (cChar == 'a') {
						if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
							converter.setJsonArrayVarName(args[++i]);
							break;
						} else {
							System.err
									.println("error: missing delimiter for option -a");
							System.exit(0);
						}
					} else if (cChar == 'o') {
						if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
							converter.setJsonFileName(args[++i]);
							break;
						} else {
							System.err
									.println("error: missing filename for option -o");
							System.exit(0);
						}
					} else {
						System.err.println("error: unknown option "
								+ args[i].charAt(c));
						System.exit(0);
					}
				}
			}
			i++;
		}

	}
}

package Compilador;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.JOptionPane;

public class Lexico {
	int fila = 1, col;
	String ecuacion = "";
	boolean error = false;
	ArrayList<Token> PalabrasAnalizadas = new ArrayList<Token>();

	public Lexico(String dir) {
		AnalisisLexico(dir);
		if (error)
			JOptionPane.showMessageDialog(null, "Ocurrio Un Error Lexico.", "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public void AnalisisLexico(String dir) {
		String linea = "", cadena = "";
		int contador;
		ecuacion = "";
		StringTokenizer tokenizer; // Tokenizer -> Divide las cadenas delimitadas por espacios
		try {
			// Acceder al archivo
			FileReader file = new FileReader(dir);
			BufferedReader archivo = new BufferedReader(file);
			// Asigna la primera linea del archivo
			linea = archivo.readLine();
			while (linea != null) {
				col = 1;
				// Toma la primera ecuacion
				encuentraEcuacion(linea);
				tokenizer = new StringTokenizer(linea);
				contador = tokenizer.countTokens(); // cuanta los tokens que contiene
				// Recorre el tokenizer
				for (int i = 0; i < contador; i++) {
					cadena = tokenizer.nextToken(); // Obtiene el siguiente token
					verificaToken(cadena);
					col++;
				}
				linea = archivo.readLine(); // asigna la siguiente linea
				fila++;
			}
			archivo.close(); // cierra el archivo
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Archivo no encontrado.");
		}
	}

	public void verificaToken(String token) {
		if (palabraReservada(token))
			return;
		if (finalizador(token))
			return;
		if (parentesisApertura(token))
			return;
		if (parentesisCierre(token))
			return;
		if (llaveApertura(token))
			return;
		if (llaveCierre(token))
			return;
		if (operadorLogico(token))
			return;
		if (operadorAritmetico(token))
			return;
		if (asignacion(token))
			return;
		if (numero(token))
			return;
		if (cadena(token))
			return;
		Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");// Expresion regular(alfabeto del resto de identificadores)
		Matcher match = patron.matcher(token);

		if (match.find())
			PalabrasAnalizadas.add(new Token("Identificador", token, fila, 23));
		else {
			System.out.println("Token no valido en la fila: " + fila + " Columna: " + col);
			System.out.println(token);
			error = true;
		}
	}

	public boolean palabraReservada(String token) {
		if (token.equals("if") || token.equals("else") || token.equals("while") || token.equals("imprime")
				|| token.equals("int") || token.equals("double") || token.equals("String") || token.equals("class")) {
			int id = 0;
			switch (token) {
			case "class":
				id = 1;
				break;
			case "int":
				id = 2;
				break;
			case "double":
				id = 3;
				break;
			case "String":
				id = 4;
				break;
			case "if":
				id = 5;
				break;
			case "else":
				id = 6;
				break;
			case "while":
				id = 7;
				break;
			case "imprime":
				id = 8;
				break;
			}
			PalabrasAnalizadas.add(new Token("Palabra Reservada", token, fila, id));
			return true;
		}
		return false;
	}

	public boolean finalizador(String token) {
		if (token.equals(";")) {
			PalabrasAnalizadas.add(new Token("Finalizador", token, fila, 9));
			return true;
		}
		return false;
	}

	public boolean asignacion(String token) {
		if (token.equals("=")) {
			PalabrasAnalizadas.add(new Token("Operador Asignacion", token, fila, 10));
			return true;
		}
		return false;
	}

	public boolean parentesisApertura(String token) {
		if (token.equals("(")) {
			PalabrasAnalizadas.add(new Token("Parentesis Apertura", token, fila, 11));
			return true;
		}
		return false;
	}

	public boolean parentesisCierre(String token) {
		if (token.equals(")")) {
			PalabrasAnalizadas.add(new Token("Parentesis Cierre", token, fila, 12));
			return true;
		}
		return false;
	}

	public boolean llaveApertura(String token) {
		if (token.equals("{")) {
			PalabrasAnalizadas.add(new Token("Llave Apertura", token, fila, 13));
			return true;
		}
		return false;
	}

	public boolean llaveCierre(String token) {
		if (token.equals("}")) {
			PalabrasAnalizadas.add(new Token("Llave Cierre", token, fila, 14));
			return true;
		}
		return false;
	}

	public boolean operadorLogico(String token) {
		if (token.equals("<") || token.equals(">") || token.equals("==")) {
			int id = 0;
			switch (token) {
			case "<":
				id = 15;
				break;
			case ">":
				id = 16;
				break;
			case "==":
				id = 17;
				break;
			}
			PalabrasAnalizadas.add(new Token("Operador Logico", token, fila, id));
			return true;
		}
		return false;
	}

	public boolean numero(String token) {
		if (Pattern.matches("^(\\d+)$", token) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)", token)) {
			PalabrasAnalizadas.add(new Token("Numero", token, fila, 18));
			return true;
		}
		/*
		 * if(token.equals("1") || token.equals("2") || token.equals("3") ||
		 * token.equals("4") || token.equals("5") || token.equals("6") ||
		 * token.equals("7") || token.equals("8") || token.equals("9") ||
		 * token.equals("0")) { PalabrasAnalizadas.add(new
		 * Token("Numero",token,fila,18)); return true; }
		 */
		return false;
	}

	public boolean operadorAritmetico(String token) {
		if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
			int id = 0;
			switch (token) {
			case "+":
				id = 19;
				break;
			case "-":
				id = 20;
				break;
			case "/":
				id = 21;
				break;
			case "*":
				id = 22;
				break;
			}
			PalabrasAnalizadas.add(new Token("Operador Aritmetico", token, fila, id));
			return true;
		}
		return false;
	}

	public boolean cadena(String token) {
		if (Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$", token)) {
			PalabrasAnalizadas.add(new Token("cadena", token, fila, 24));
			return true;
		}
		return false;
	}

	public void encuentraEcuacion(String linea) {
		long mas = 0, menos = 0, multiplicacion = 0, division = 0;

		if (linea.contains("=")) {
			if (linea.contains("+")) {
				mas = linea.chars().filter(ch -> ch == '+').count();
			}
			if (linea.contains("-")) {
				menos = linea.chars().filter(ch -> ch == '-').count();
			}
			if (linea.contains("*")) {
				multiplicacion = linea.chars().filter(ch -> ch == '*').count();
			}
			if (linea.contains("/")) {
				division = linea.chars().filter(ch -> ch == '/').count();
			}
		}

		if ((mas + menos + multiplicacion + division) >= 2) {
			if (ecuacion == "") {
				ecuacion = linea.replace(" ;", "");
				ecuacion = ecuacion.substring(1, ecuacion.length());
			}
		}
	}
}


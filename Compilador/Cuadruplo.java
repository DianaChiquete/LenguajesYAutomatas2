package Compilador;

import java.util.ArrayList;
import java.util.Arrays;

// Operadores jerarquia: * / + -

public class Cuadruplo {

	ArrayList<String> niveles;
	private ArrayList<String> conMultiplicacion;
	private ArrayList<String> conDivision;
	private ArrayList<String> conSuma;
	private ArrayList<String> conResta;
	private ArrayList<String> conConsecutivo;
	private String finalEcu = "";
	private String iniEcu = "";
	private ArrayList<String> modEcuacion;

	public void resultado(String ecuacion) {
		niveles = new ArrayList<String>();

		conMultiplicacion = new ArrayList<String>();
		conDivision = new ArrayList<String>();
		conSuma = new ArrayList<String>();
		conResta = new ArrayList<String>();
		conConsecutivo = new ArrayList<String>();
		finalEcu = "";
		iniEcu = ecuacion;

		modEcuacion = new ArrayList<String>(Arrays.asList(ecuacion.split(" ")));

		if (busquedaOperadores(ecuacion)) {
			System.out.println("--- Ecuacion valida ---");

			if (conMultiplicacion.size() != 0) {
				int cont = 1;
				for (String valor : conMultiplicacion) {
					System.out.println("--- Vuelta: " + cont + " ---");
					int valPos = Integer.parseInt(valor);
					int verfConse = valPos + 1;
					if (conConsecutivo.contains(Integer.toString(verfConse))) {
						System.out.println("--- Tiene un consecutivo ---");
						agregaConsecutivo(modEcuacion, verfConse);
						agregaValor(modEcuacion, valPos, true);
					} else {
						if (modEcuacion.get(valPos).equals("*")) {
							agregaValor(modEcuacion, valPos, false);
						}
					}
					cont++;
				}
			}

			if (conDivision.size() != 0) {
				for (String valor : conDivision) {
					int valPos = Integer.parseInt(valor);
					int verfConse = valPos + 1;
					if (conConsecutivo.contains(Integer.toString(verfConse))) {
						System.out.println("--- Tiene un consecutivo ---");
						agregaConsecutivo(modEcuacion, verfConse);
						agregaValor(modEcuacion, valPos, true);
					} else {
						if (modEcuacion.get(valPos).equals("/")) {
							agregaValor(modEcuacion, valPos, false);
						}
					}
				}
			}

			if (conSuma.size() != 0) {
				for (String valor : conSuma) {
					int valPos = Integer.parseInt(valor);
					int verfConse = valPos + 1;
					if (conConsecutivo.contains(Integer.toString(verfConse))) {
						System.out.println("--- Tiene un consecutivo ---");
						agregaConsecutivo(modEcuacion, verfConse);
						agregaValor(modEcuacion, valPos, true);
					} else {
						if (modEcuacion.get(valPos).equals("+")) {
							agregaValor(modEcuacion, valPos, false);
						}
					}
				}
			}

			if (conResta.size() != 0) {
				for (String valor : conResta) {
					int valPos = Integer.parseInt(valor);
					int verfConse = valPos + 1;
					if (conConsecutivo.contains(Integer.toString(verfConse))) {
						System.out.println("--- Tiene un consecutivo ---");
						agregaConsecutivo(modEcuacion, verfConse);
						agregaValor(modEcuacion, valPos, true);
					} else {
						if (modEcuacion.get(valPos).equals("-")) {
							agregaValor(modEcuacion, valPos, false);
						}
					}
				}
			}

			// Agrega ultimo nivel
			int lstLv = niveles.size();
			niveles.add("=" + " " + "T" + lstLv + " " + "" + " " + finalEcu);

			imprimeNiveles();
		}
	}

	// Busqueda de las posiciones de los operadores
	public boolean busquedaOperadores(String ecuacion) {
		String[] nuevaEcuacion = ecuacion.split(" ");
		int tamaño = nuevaEcuacion.length;
		boolean sgIgual = false;

		for (int i = 0; i < tamaño; i++) {
			// Si ya salio el signo igual terminamos el ciclo
			if (sgIgual) {
				break;
			}

			// Variables
			int posicion = tamaño - (i + 1);
			String valor = nuevaEcuacion[posicion];

			// Valor obtenido por linea
			System.out.println("Vuelta(" + i + ")" + valor);

			// Obtener la posicion de los operadores
			if (operadorIdentificado(valor, posicion)) {
				System.out.println("--- Es un operador : " + valor + " ---");

				switch (valor) {
				case "=":
					finalEcu = nuevaEcuacion[posicion - 1];
					sgIgual = true;
					break;
				case "-":
					if (operadorConsecutivo(nuevaEcuacion[posicion - 1])) {
						System.out.println(
								"Operadores consecutivos: " + nuevaEcuacion[posicion - 1] + nuevaEcuacion[posicion]);
						System.out.println(
								"Es un valor negativo: " + nuevaEcuacion[posicion] + nuevaEcuacion[posicion + 1]);
						conConsecutivo.add(Integer.toString(posicion));
					}
				default:
					break;
				}
			}
		}

		if (conMultiplicacion.size() != 0 || conDivision.size() != 0 || conSuma.size() != 0 || conResta.size() != 0
				|| conConsecutivo.size() != 0) {
			return true;
		}
		return false;
	}

	private boolean operadorIdentificado(String opr, int posicion) {
		String pos = Integer.toString(posicion);

		switch (opr) {
		case "*":
			conMultiplicacion.add(pos);
			return true;
		case "/":
			conDivision.add(pos);
			return true;
		case "+":
			conSuma.add(pos);
			return true;
		case "-":
			conResta.add(pos);
			return true;
		case "=":
			return true;
		}

		return false;
	}

	// En caso de encontrar un caracter negativo ( - )
	// Verifica una posicion anterior y si encuentra otro operador,
	// entonces lo toma como valor negativo
	private boolean operadorConsecutivo(String opr) {
		System.out.println("Busca operador consecutivo");
		switch (opr) {
		case "*":
			return true;
		case "/":
			return true;
		case "+":
			return true;
		case "-":
			return true;
		case "=":
			return true;
		}

		System.out.println("No tiene operadores consecutivos");
		return false;
	}

	private boolean agregaConsecutivo(ArrayList<String> ecuacion, int posicion) {
		// Agregamos el valor
		int lstLv = niveles.size();

		String val = ecuacion.get(posicion) + " " + ecuacion.get(posicion + 1) + " " + "" + " " + "T" + (lstLv + 1);
		niveles.add(val);
		lstLv = niveles.size();
		modEcuacion.set(posicion, "T" + lstLv);
		modEcuacion.set(posicion + 1, "T" + lstLv);

		System.out.println();
		System.out.println("--- Nivel agregado ---");
		System.out.println();
		return true;
	}

	private void agregaValor(ArrayList<String> ecuacion, int posicion, boolean consecutivo) {
		if (consecutivo) {
			int posCon = niveles.size();
			boolean negativo = false;

			if (ecuacion.get(posicion - 2).equals("-")) {
				System.out.println("-- Valor negativo ---");
				agregaConsecutivo(modEcuacion, posicion - 2);
				negativo = true;
			}

			String val = ecuacion.get(posicion) + " " + ecuacion.get(posicion - 1) + " " + "T" + (posCon) + " " + "T" + (posCon + 1);

			niveles.add(val);
			posCon = niveles.size();

			if (negativo) {
				modEcuacion.set(posicion - 2, "T" + (posCon));
			}

			modEcuacion.set(posicion - 1, "T" + (posCon));
			modEcuacion.set(posicion, "T" + (posCon));
			modEcuacion.set(posicion + 1, "T" + (posCon));
			modEcuacion.set(posicion + 2, "T" + (posCon));

			buscaRemplaza("T" + (posCon - 1), "T" + (posCon));
		} else {
			int lstLv = niveles.size();

			if (lstLv == 0) {
				lstLv++;
			}

			if (ecuacion.get(posicion - 2).equals("-")) {
				System.out.println("-- Valor negativo ---");
				agregaConsecutivo(modEcuacion, posicion - 2);
			}

			niveles.add(ecuacion.get(posicion) + " " + ecuacion.get(posicion - 1) + " " + ecuacion.get(posicion + 1)
					+ " " + "T" + (lstLv + 1));
			lstLv = niveles.size();
			
			modEcuacion.set(posicion - 1, "T" + (lstLv));
			modEcuacion.set(posicion, "T" + (lstLv));
			modEcuacion.set(posicion + 1, "T" + (lstLv));

			buscaRemplaza("T" + (posicion + 1), "T" + (lstLv));
		}

		System.out.println();
		System.out.println("--- Nivel agregado ---");
		System.out.println();
	}

	private void buscaRemplaza(String actualVal, String nuevoVal) {
		for (int i = 0; i < modEcuacion.size(); i++) {
			if (modEcuacion.get(i).equals(actualVal)) {
				modEcuacion.set(i, nuevoVal);
			}
		}
	}

	private void imprimeNiveles() {
		int con = 1;

		System.out.println(iniEcu);
		System.out.println(modEcuacion);

		System.out.println();
		System.out.println("--- Imprimir niveles ---");
		System.out.println();

		for (String nivel : niveles) {
			System.out.println("NivelT" + con + ": " + nivel);
			con++;
		}
	}
}


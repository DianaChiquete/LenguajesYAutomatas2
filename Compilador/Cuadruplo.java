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
	private ArrayList<String> modEcuacion;

	public void resultado(String ecuacion) {
		niveles = new ArrayList<String>();

		conMultiplicacion = new ArrayList<String>();
		conDivision = new ArrayList<String>();
		conSuma = new ArrayList<String>();
		conResta = new ArrayList<String>();
		conConsecutivo = new ArrayList<String>();
		finalEcu = "";

		modEcuacion = new ArrayList<String>(Arrays.asList(ecuacion.split(" ")));

		if (busquedaOperadores(ecuacion)) {
			if (conMultiplicacion.size() != 0) {
				for (String valor : conMultiplicacion) {
					int valPos = Integer.parseInt(valor);
					int verfConse = valPos + 1;
					if (conConsecutivo.contains(Integer.toString(verfConse))) {
						agregaConsecutivo(modEcuacion, verfConse);
						agregaValor(modEcuacion, valPos, true);
					} else {
						if (modEcuacion.get(valPos).equals("*")) {
							agregaValor(modEcuacion, valPos, false);
						}
					}
				}
			}

			if (conDivision.size() != 0) {
				for (String valor : conDivision) {
					int valPos = Integer.parseInt(valor);
					int verfConse = valPos + 1;
					if (conConsecutivo.contains(Integer.toString(verfConse))) {
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
			int posCon = niveles.size();
			niveles.add("=" + " " + "T" + posCon + " " + "" + " " + finalEcu);
		}
	}

	// Busqueda de las posiciones de los operadores
	private boolean busquedaOperadores(String ecuacion) {
		String[] nuevaEcuacion = ecuacion.split(" ");
		int tamaño = nuevaEcuacion.length;
		boolean sgIgual = false;

		for (int i = 0; i < tamaño; i++) {
			if (sgIgual) {
				break;
			}

			int posicion = tamaño - (i + 1);
			String valor = nuevaEcuacion[posicion];

			if (operadorIdentificado(valor, posicion)) {
				switch (valor) {
				case "=":
					finalEcu = nuevaEcuacion[posicion - 1];
					sgIgual = true;
					break;
				case "-":
					if (operadorConsecutivo(nuevaEcuacion[posicion - 1])) {
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

	private boolean operadorConsecutivo(String opr) {
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
		return false;
	}

	private boolean agregaConsecutivo(ArrayList<String> ecuacion, int posicion) {
		int posCon = niveles.size();

		String val = ecuacion.get(posicion) + " " + ecuacion.get(posicion + 1) + " " + "" + " " + "T" + (posCon + 1);
		niveles.add(val);
		posCon = niveles.size();
		modEcuacion.set(posicion, "T" + posCon);
		modEcuacion.set(posicion + 1, "T" + posCon);

		return true;
	}

	private void agregaValor(ArrayList<String> ecuacion, int posicion, boolean consecutivo) {
		if (consecutivo) {
			int posCon = niveles.size();
			boolean negativo = false;

			if (ecuacion.get(posicion - 2).equals("-")) {
				agregaConsecutivo(modEcuacion, posicion - 2);
				negativo = true;
			}

			String val = ecuacion.get(posicion) + " " + ecuacion.get(posicion - 1) + " " + "T" + (posCon) + " " + "T"
					+ (posCon + 1);

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
			int posCon = niveles.size();

			if (posCon == 0) {
				posCon++;
			}

			if (ecuacion.get(posicion - 2).equals("-")) {
				agregaConsecutivo(modEcuacion, posicion - 2);
			}

			niveles.add(ecuacion.get(posicion) + " " + ecuacion.get(posicion - 1) + " " + ecuacion.get(posicion + 1)
					+ " " + "T" + (posCon + 1));
			posCon = niveles.size();

			modEcuacion.set(posicion - 1, "T" + (posCon));
			modEcuacion.set(posicion, "T" + (posCon));
			modEcuacion.set(posicion + 1, "T" + (posCon));

			buscaRemplaza("T" + (posicion + 1), "T" + (posCon));
		}
	}

	private void buscaRemplaza(String actualVal, String nuevoVal) {
		for (int i = 0; i < modEcuacion.size(); i++) {
			if (modEcuacion.get(i).equals(actualVal)) {
				modEcuacion.set(i, nuevoVal);
			}
		}
	}
}

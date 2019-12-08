package Compilador;

import java.util.ArrayList;
import java.util.Arrays;

// Operadores jerarquia: ( ) * / + -

public class Triplo {

	ArrayList<String> niveles;
	private ArrayList<String> conParentesisA;
	private ArrayList<String> conParentesisC;
	private ArrayList<String> conMultiplicacion;
	private ArrayList<String> conDivision;
	private ArrayList<String> conSuma;
	private ArrayList<String> conResta;
	private ArrayList<String> conConsecutivo;
	private String finalEcu = "";
	private ArrayList<String> modEcuacion;
	private ArrayList<String> tmp;

	public void resultado(String ecuacion) {
		niveles = new ArrayList<String>();

		limpiarVariables();

		tmp = new ArrayList<String>();
		finalEcu = "";

		modEcuacion = new ArrayList<String>(Arrays.asList(ecuacion.split(" ")));

		if (busquedaOperadores(modEcuacion)) {
			if (conParentesisA.size() != 0 && conParentesisC.size() != 0) {
				boolean parentA = false;

				int pA = Integer.parseInt(conParentesisA.get(0));
				int pC = Integer.parseInt(conParentesisC.get(0));

				for (int i = 0; i < modEcuacion.size(); i++) {
					if (pC == i) {
						break;
					}

					if (parentA) {
						tmp.add(modEcuacion.get(i));
					}

					if (pA == i) {
						parentA = true;
					}
				}

				if (tmp.size() != 0) {
					limpiarVariables();
					if (entreParentesis(modEcuacion, pA, pC)) {
						evaluaEcuacion();
					}
				}

				// Remplazar parentesis
				int lv = niveles.size();
				modEcuacion.set(pA, "(" + (lv - 1) + ")");
				modEcuacion.set(pC, "(" + (lv - 1) + ")");
			}
		}

		limpiarVariables();

		if (busquedaOperadores(modEcuacion)) {
			evaluaEcuacion();

			// Agrega ultimo nivel
			int lstLv = niveles.size();
			niveles.add(finalEcu + " " + "(" + (lstLv - 1) + ")");
		}
	}

	private void limpiarVariables() {
		conParentesisA = new ArrayList<String>();
		conParentesisC = new ArrayList<String>();
		conMultiplicacion = new ArrayList<String>();
		conDivision = new ArrayList<String>();
		conSuma = new ArrayList<String>();
		conResta = new ArrayList<String>();
		conConsecutivo = new ArrayList<String>();
	}

	private void evaluaEcuacion() {
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
	}

	// Busqueda de las posiciones de los operadores
	private boolean busquedaOperadores(ArrayList<String> nuevaEcuacion) {
		int tamaño = nuevaEcuacion.size();
		boolean sgIgual = false;

		for (int i = 0; i < tamaño; i++) {
			if (sgIgual) {
				break;
			}

			int posicion = tamaño - (i + 1);
			String valor = nuevaEcuacion.get(posicion);

			if (operadorIdentificado(valor, posicion)) {
				switch (valor) {
				case "=":
					finalEcu = valor + " " + nuevaEcuacion.get(posicion - 1);
					sgIgual = true;
					break;
				case "-":
					if (operadorConsecutivo(nuevaEcuacion.get(posicion - 1))) {
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

	private boolean entreParentesis(ArrayList<String> nuevaEcuacion, int posA, int posC) {
		boolean sgIgual = false;

		for (int i = posA; i < posC; i++) {
			if (sgIgual) {
				break;
			}

			String valor = nuevaEcuacion.get(i);

			if (operadorIdentificado(valor, i)) {
				switch (valor) {
				case "-":
					if (operadorConsecutivo(nuevaEcuacion.get(i - 1))) {
						conConsecutivo.add(Integer.toString(i));
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
		case "(":
			conParentesisA.add(pos);
			return true;
		case ")":
			conParentesisC.add(pos);
			return true;
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
		String val = ecuacion.get(posicion) + " " + ecuacion.get(posicion + 1);
		niveles.add(val);
		int posCon = niveles.size();
		modEcuacion.set(posicion, "(" + (posCon - 1) + ")");
		modEcuacion.set(posicion + 1, "(" + (posCon - 1) + ")");

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

			String val = ecuacion.get(posicion) + " " + ecuacion.get(posicion - 1) + " " + "(" + (posCon - 1) + ")";
			niveles.add(val);
			int posCon2 = niveles.size();

			if (negativo) {
				modEcuacion.set(posicion - 2, "(" + (posCon2 - 1) + ")");
			}

			modEcuacion.set(posicion - 1, "(" + (posCon2 - 1) + ")");
			modEcuacion.set(posicion, "(" + (posCon2 - 1) + ")");

			buscaRemplaza("(" + (posCon - 1) + ")", "(" + (posCon2 - 1) + ")");
		} else {
			if (ecuacion.get(posicion - 2).equals("-")) {
				agregaConsecutivo(modEcuacion, posicion - 2);
			}

			niveles.add(ecuacion.get(posicion) + " " + ecuacion.get(posicion - 1) + " " + ecuacion.get(posicion + 1));
			int posCon = niveles.size();

			modEcuacion.set(posicion - 1, "(" + (posCon - 1) + ")");
			modEcuacion.set(posicion, "(" + (posCon - 1) + ")");
			modEcuacion.set(posicion + 1, "(" + (posCon - 1) + ")");

			buscaRemplaza("(" + (posicion + 1) + ")", "(" + (posCon - 1) + ")");
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
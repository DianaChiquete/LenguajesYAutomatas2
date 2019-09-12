package proyectoaut;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.regex.*;
import javax.swing.JOptionPane;
public class Analizador
{
	int renglon=1,columna=1;
	ArrayList<String> impresion; //Aqui los que voy analizar sintacticamente
	ArrayList<Identificador> identi = new ArrayList<Identificador>();
	ListaDoble<Token> tokens;//Aqui voy metiendo los que ya aplique el analisis lexico
	Token vacio=new Token("", 9,0);//Utilizo esto para identificar el final en mi lista doble
	boolean bandera=true;//para saber si hubo un error

	public ArrayList<Identificador> getIdenti() {
		return identi;
	}
	public Analizador(String ruta) {//Recibe el nombre del archivo de texto
		analisaCodigo(ruta);
		if(bandera) {//Si la bandera sigue true quiere decir que no hay errores
			impresion.add("No hay errores lexicos");
			analisisSintactio(tokens.getInicio());//Y mando analizar sintacticamente los token
		}
		if(impresion.get(impresion.size()-1).equals("No hay errores lexicos"))//Si el ultimo token dice que no hay errores sintacticos
			impresion.add("No hay errores sintacticos");//Entonces no hay errores sintacticos
		for (Identificador identificador : identi) {
			if (identificador.getTipo().equals("")) {
				String x =buscar(identificador.getNombre());
				identificador.setTipo(x);
			}
		}
	}
	public void analisaCodigo(String ruta) {//Recibe la ruta del archivo
		String linea="", token="";
		StringTokenizer tokenizer;
		try{
			FileReader file = new FileReader(ruta);//Acceso a la ruta
			BufferedReader archivoEntrada = new BufferedReader(file);//Abro el flujo del archivo
			linea = archivoEntrada.readLine();//Saco la linea
			impresion=new ArrayList<String>();//Inicializo mis arreglos
			tokens = new ListaDoble<Token>();//Y listas
			while (linea != null){//Recorro el archivo
				columna++;
				linea = separaDelimitadores(linea);//Checo si en la linea hay operadores o identificadores combinados y le agrego espacios
				tokenizer = new StringTokenizer(linea);//Luego parto la linea en diferentes partes 
				while(tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken();
					analisisLexico(token);//Y lo mando a analizar
				}
				linea=archivoEntrada.readLine();
				renglon++;//Cuento el renglon
			}
			archivoEntrada.close();
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null,"No se encontro el archivo favor de checar la ruta","Alerta",JOptionPane.ERROR_MESSAGE);
		}
	}
	public Token analisisSintactio(NodoDoble<Token> nodo) {
		Token tokensig, aux;
		if(nodo!=null) {
			aux =  nodo.dato;
			tokensig=analisisSintactio(nodo.siguiente);
			switch (aux.getTipo()) {
			case 0://Modificador
				int sig=tokensig.getTipo();
				if(sig!=2 && sig!=8)//Tipo de dato, clase comparamos
					impresion.add("Error sintactico en la linea "+aux.getLinea()+" se esparaba un tipo de dato o indentificacion de clase");
				break;
			case 7://Identificador
				if(!(Arrays.asList("{","=",";").contains(tokensig.getValor()))) 
					impresion.add("Error sintactico en la linea "+aux.getLinea()+" se esparaba un simbolo");
				else
					if(nodo.anterior.dato.getValor().equals("class")){
						identi.add( new Identificador(aux.getValor(), " ", "class"));
					}
				break;
			case 2://Tipo de dato
			case 8://Definicion de clase
				if(nodo.anterior.dato.getTipo()==0) {
					if(tokensig.getTipo()!=7) 
						impresion.add("Error sintactico en la linea "+aux.getLinea()+" se esparaba un identificador");
				}else
					impresion.add("Error sintactico en la linea "+aux.getLinea()+" se esperaba un modificador");
				break;
			case 3://Simbolo
				if(aux.getValor().equals("}")) {
					if(cuenta("{")!=cuenta("}"))
						impresion.add("Error sintactico en la linea "+aux.getLinea()+ " falta un {");
				}else if(aux.getValor().equals("{")) {
					if(cuenta("{")!=cuenta("}"))
						impresion.add("Error sintactico en la linea "+aux.getLinea()+ " falta un }");
				}
				else if(aux.getValor().equals("(")) {
					if(cuenta("(")!=cuenta(")"))
						impresion.add("Error sintactico en la linea "+aux.getLinea()+ " falta un )");
					else
					{
						if(!(nodo.anterior.dato.getValor().equals("if")&&tokensig.getTipo()==6)) {
							impresion.add("Error sintactico en la linea "+aux.getLinea()+ " se esperaba un valor");
						}
					}
				}else if(aux.getValor().equals(")")) {
					if(cuenta("(")!=cuenta(")"))
						impresion.add("Error sintactico en la linea "+aux.getLinea()+ " falta un (");
				}
				else if(aux.getValor().equals("=")){
					if(nodo.anterior.dato.getTipo()==7) {
						if(tokensig.getTipo()!=6)
							impresion.add("Error sintactico en la linea "+aux.getLinea()+ " se esperaba una constante");
						else {
							if(nodo.anterior.anterior.dato.getTipo()==2)
								identi.add(new Identificador(nodo.anterior.dato.getValor(),tokensig.getValor(),nodo.anterior.anterior.dato.getValor()));
							else
								impresion.add("Error sintactico en linea "+aux.getLinea()+ " se esperaba un tipo de dato");

						}
					}else
						impresion.add("Error sintactico en linea "+aux.getLinea()+ " se esperaba un identificador");
				}
				break;
			case 6://Constante
				if(nodo.anterior.dato.getValor().equals("="))
					if(tokensig.getTipo()!=5 && tokensig.getTipo()!=6 && !tokensig.getValor().equals(";"))
						impresion.add("Error sintactico en linea "+aux.getLinea()+ " asignacion no valida");
				break;
			case 1://Palabra reservada
				if(aux.getValor().equals("if"))
				{
					if(!tokensig.getValor().equals("(")) {
						impresion.add("Error sintactico en linea "+aux.getLinea()+ " se esperaba un (");
					}
				}
				else {
					NodoDoble<Token> aux2 = nodo.anterior;
					boolean bandera=false;
					while(aux2!=null&&!bandera) {
						if(aux2.dato.getValor().equals("if"))
							bandera=true;
						aux2 =aux2.anterior;
					}
					if(!bandera)
						impresion.add("Error sintactico en linea "+aux.getLinea()+ " else no valido");
				}
				break;
			case 4://Operador logico
				if(nodo.anterior.dato.getTipo()!=6) 
					impresion.add("Error sinatactico en linea "+aux.getLinea()+ " se esperaba una constante");
				if(tokensig.getTipo()!=6)
					impresion.add("Error sintactico en linea "+aux.getLinea()+ " se esperaba una constante");
				break;
			}
			return aux;
		}
		return  vacio;
	}
	public void analisisLexico(String token) {
		int tipo=-1;
		if(Arrays.asList("public","private").contains(token)) 
			tipo=0;//Modificador
		if(Arrays.asList("if","while").contains(token)) 
			tipo =1;//Palabra reservada
		if(Arrays.asList("int","boolean").contains(token))
			tipo =2;//Tipo de datos
		if(Arrays.asList("(",")","{","}","=",";").contains(token))
			tipo =3;//Simbolo
		if(Arrays.asList("<",">",">=","<=","==","!=").contains(token))
			tipo =4;//Operador logico
		if(Arrays.asList("+","-","*","/").contains(token))
			tipo =5;//Operador aritmetico
		if(Arrays.asList("true","false").contains(token)||Pattern.matches("[\\d][\\d]$",token)||Pattern.matches("[\\d]$",token)) 
			tipo =6;
		if(token.equals("class")) 
			tipo =8;
		//Cadenas validas
		if(tipo==-1) {//Quiere decir que no es ninguna de las anteriores y paso analizarla
			Pattern pat = Pattern.compile("^[a-z_]+$");//Checo si cumple con esta expresion regular
			Matcher mat = pat.matcher(token);
			if(mat.find()) //Si cumple es un identificador
				tipo =7;//Identificador
			else {
				impresion.add("Error en la linea "+renglon+" columna "+columna+" token "+token);//Es un error y guardo el donde se produjo el error
				bandera = false;
				return;
			}
		}
		tokens.insertar(new Token(token,tipo,renglon));
		impresion.add(new Token(token,tipo,renglon).toString());
	}
	private String buscar(String id) 
	{
		for (int i = identi.size()-1; i >=0; i--) {
			Identificador identificador = identi.get(i);
			if(identificador.getNombre().equals(id))
				return identificador.tipo;
		}
		return "";
	}
	public String separaDelimitadores(String linea){
		for (String string : Arrays.asList("(",")","{","}","=",";")) {
			if(string.equals("=")) {
				//Si en medio de los parentesis hay este operador doy espaciado para que los tome y los identifique
				if(linea.indexOf(">=")>=0) {
					linea = linea.replace(">=", " >= ");
					break;
				}
				if(linea.indexOf("<=")>=0) {
					linea = linea.replace("<=", " <= ");
					break;
				}
				if(linea.indexOf("==")>=0)
				{
					linea = linea.replace("==", " == ");
					break;
				}
				if(linea.indexOf("<")>=0) {
					linea = linea.replace("<", " < ");
					break;
				}
				if(linea.indexOf(">")>=0) {
					linea = linea.replace(">", " > ");
					break;
				}
			}
			if(linea.contains(string)) 
				linea = linea.replace(string, " "+string+" ");
		}
		return linea;
	}
	public int cuenta (String token) {
		int conta=0;
		NodoDoble<Token> Aux=tokens.getInicio();
		while(Aux !=null){
			if(Aux.dato.getValor().equals(token))
				conta++;
			Aux=Aux.siguiente;
		}	
		return conta;
	}
	public ArrayList<String> getmistokens() {
		return impresion;
	}
}
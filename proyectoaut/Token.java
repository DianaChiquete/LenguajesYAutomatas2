package proyectoaut;

public class Token 
{
	private int tipo;
	String definicion[]={"Modificador","Palabra resevada","Tipo de dato",
			"Simbolo","Operador logico","Operador aritmetico","Constante","Identificador","Declaracion de clase"};
	private String valor;
	private int linea;
	public Token(String valor, int tipo, int linea) {
		this.tipo=tipo;
		this.valor=valor;
		this.linea=linea;
	}
	public int getTipo() {
		return tipo;
	}
	public String getValor() {
		return valor;
	}
	public int getLinea() {
		return linea;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String toString() {
		return "Token encontrado.... " +definicion[tipo]+": "+valor;
	}
}

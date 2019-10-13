package Compilador;

public class Variables
{
	private String variable;
	private int tipo;
	private String valor;
	private int linea;
	
	public Variables(String variable, int tipo,String valor,int linea)
	{
		this.variable=variable;
		this.tipo=tipo;
		this.valor=valor;
		this.linea=linea;
	}
	
	public String getVariable()
	{
		return variable;
	}
	public int getTipo()
	{
		return tipo;
	}
	public String getValor()
	{
		return valor;
	}
	public int getLinea()
	{
		return linea;
	}
	
	public void setVariable(String variable)
	{
		this.variable = variable;
	}
	public void setTipo(int tipo)
	{
		this.tipo = tipo;
	}
	public void setValor(String valor)
	{
		this.valor = valor;
	}
	public void setLinea(int linea)
	{
		this.linea = linea;
	}
	
	public String toString() {
		return getVariable()+"\n"+getTipo()+"\n"+getValor()+"\n"+getLinea()+"\n";
	}
}
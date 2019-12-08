package Compilador;

public class Variables
{
	private String variable;
	private int tipo;
	private String valor;
	private int linea;
	private int uso;
	//Uso En variables 	0 = declaracion / 	1 = asignacion
	//Uso En Ifs		0 = inicio		/	1 = fin
	
	public Variables(String variable, int tipo,String valor,int linea, int uso)
	{
		this.variable=variable;
		this.tipo=tipo;
		this.valor=valor;
		this.linea=linea;
		this.uso=uso;
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
	public int getUso()
	{
		return uso;
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
	public void setUso(int uso)
	{
		this.uso = uso;
	}
	
	public String toString() {
		return "variable="+getVariable()+", Tipo="+getTipo()+", Valor="+getValor()+", Linea="+getLinea()+", Uso="+getUso()+"\n";
	}
}

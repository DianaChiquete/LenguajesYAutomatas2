package Compilador;

public class Token
{
	private String tipo;
	private String valor;
	private int linea;
	private int id;
	public Token(String tipo, String valor,int linea,int id)
	{
		this.tipo=tipo;
		this.valor=valor;
		this.linea=linea;
		this.id=id;
	}
	public String getTipo()
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
	public int getId()
	{
		return id;
	}
	public void setTipo(String tipo)
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
	public void setId(int id)
	{
		this.id = id;
	}
	public String toString() {
		return getTipo()+"\n"+getValor()+"\n"+getId()+"\n";
	}
}
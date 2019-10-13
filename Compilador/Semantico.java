package Compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Semantico {
	boolean ErrorSem=false;
	String Mensaje="";
	
	public Semantico(ArrayList<Variables> declaradas, ArrayList<Variables> asignadas){
		verificaVariables(declaradas,asignadas);//Verifica que las variables que se usan existan y si se usan esten abajo de su declaracion.
		asignarTipo(declaradas,asignadas);
		verificaTipodeDato(declaradas,asignadas);
		if(ErrorSem)
			JOptionPane.showMessageDialog(null, "Ocurrio Un Error Semantico:\n"+Mensaje, "ERROR",JOptionPane.WARNING_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Completado, codigo sin errorres");//muestra cuadro de dialogo
	}
	//clase que verifica si las variables existen
	public void verificaVariables(ArrayList<Variables> declaradas, ArrayList<Variables> asignada){
		boolean existe;
		for(int i=0;i<asignada.size();i++){//ciclo para ir recorriendo la cadena
			existe=false;
			for(int j=0;j<declaradas.size();j++){
				if(asignada.get(i).getVariable().equals(declaradas.get(j).getVariable()))
					existe=true;
			}
			if(!existe){
				ErrorSem=true;
				Mensaje=Mensaje+"Variable utilizada no existe.\n";//cuadro de dialogo
				return;//sale
			}
		}
		
		String[] tokens;
		//Verifica el valor asignado a las variables declaradas
		for(int i=0;i<declaradas.size();i++)
		{
			tokens = declaradas.get(i).getValor().split(" ");
			for(int j=0;j<tokens.length;j++)
			{
				if(!tokens[j].isEmpty())
				{
					if(Arrays.asList("+","*","/","-").contains(tokens[j]))
						continue;
					//el Pattern.matches hace coincidir secuencias de caracteres arbitrarias con la expresión regular. 
					if(!Pattern.matches("^(\\d+)$",tokens[j]) && !Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]) && !Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
					{
						if(declaradas.get(i).getVariable().equals(tokens[j]))
						{
							ErrorSem=true;
							Mensaje=Mensaje+"Variable utilizada no existe.\n";//mensaje de salida en cuadro de texto
							return;
						}
					}
				}
			}
		}
		
		//Verificar que el orden de asignadas este abajo de la variable declarada
		for(int i=0;i<asignada.size();i++)
		{
			for(int j=0;j<declaradas.size();j++)
			{
				if(asignada.get(i).getVariable().equals(declaradas.get(j).getVariable()))
					if(asignada.get(i).getLinea()<declaradas.get(j).getLinea())
					{
						ErrorSem=true;
						Mensaje=Mensaje+"Variable utilizada no existe.\n";
						return;
					}
			}
		}
		
		for(int i=0;i<asignada.size();i++)
		{
			tokens = asignada.get(i).getValor().split(" ");
			for(int j=0;j<tokens.length;j++)
			{
				if(!tokens[j].isEmpty())
				{
					if(Arrays.asList("+","*","/","-").contains(tokens[j]))
						continue;
					if(!Pattern.matches("^(\\d+)$",tokens[j]) && !Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]) && !Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
					{
						ErrorSem=true;
						for(int k=0;k<declaradas.size();k++)
						{
							if(declaradas.get(k).getVariable().equals(tokens[j]))
							{
								ErrorSem=false;
							}
						}
						if(ErrorSem){
							Mensaje=Mensaje+"Variable utilizada no existe.\n";
							return;
						}
					}
				}
			}
		}
	}
	public void asignarTipo(ArrayList<Variables> declaradas, ArrayList<Variables> asignada){
		for(int i=0;i<declaradas.size();i++){
			for(int j=0;j<asignada.size();j++){
				if(asignada.get(j).getVariable().equals(declaradas.get(i).getVariable()))
					asignada.get(j).setTipo(declaradas.get(i).getTipo());
			}
		}
	}
	public void verificaTipodeDato(ArrayList<Variables> declaradas, ArrayList<Variables> asignada){
		String[] tokens;
		for(int i=0;i<declaradas.size();i++){//Declaradas
			if(!declaradas.get(i).getValor().isEmpty())
			{
				tokens = declaradas.get(i).getValor().split(" ");
				for(int j=0;j<tokens.length;j++)
				{
					switch(declaradas.get(i).getTipo()){
						//Entero
						case 2:
							if(Pattern.matches("^(\\d+)$",tokens[j]))
								break;
							if(tokens[j].equals("/"))
							{
								if(tokens[j+1].equals("0"))
								{
									ErrorSem=true;
									//Mensaje=Mensaje+"División por cero.\n";
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(declaradas,tokens[j],2)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								break;
							}
							break;
						//Doble
						case 3:
							if(Pattern.matches("^(\\d+)$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]))
								break;
							if(tokens[j].equals("/"))
							{
								if(tokens[j+1].equals("0") || Pattern.matches("(^[0]+([.][0]+)?$)",tokens[j+1]))
								{
									ErrorSem=true;
									//Mensaje=Mensaje+"División por cero.\n";
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(declaradas,tokens[j],3)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								break;
							}
							break;
						//Cadena
						case 4:
							if(Pattern.matches("^(\\d+)$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]) || Arrays.asList("+","*","/","-").contains(tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								//return;
								break;
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(declaradas,tokens[j],4)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									break;
								}
							}
							break;
					}
				}
			}
		}
		
		for(int i=0;i<asignada.size();i++){//Asignadas
			if(!asignada.get(i).getValor().isEmpty())
			{
				tokens = asignada.get(i).getValor().split(" ");
				for(int j=0;j<tokens.length;j++)
				{
					switch(asignada.get(i).getTipo()){
						//Entero
						case 2:
							if(Pattern.matches("^(\\d+)$",tokens[j]))
								break;
							if(tokens[j].equals("/"))
							{
								if(tokens[j+1].equals("0"))
								{
									ErrorSem=true;
									//Mensaje=Mensaje+"División por cero.\n";
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(declaradas,tokens[j],2)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								break;
							}
							break;
						//Doble
						case 3:
							if(Pattern.matches("^(\\d+)$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]))
								break;
							if(tokens[j].equals("/"))
							{
								if(tokens[j+1].equals("0") || Pattern.matches("(^[0]+([.][0]+)?$)",tokens[j+1]))
								{
									ErrorSem=true;
									//Mensaje=Mensaje+"División por cero.\n";
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(declaradas,tokens[j],3)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								break;
							}
							break;
						//Cadena
						case 4:
							if(Pattern.matches("^(\\d+)$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]) || Arrays.asList("+","*","/","-").contains(tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								break;
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(declaradas,tokens[j],4)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									break;
								}
							}
							break;
					}
				}
			}
		}
	}
	
	public boolean checkTipo(ArrayList<Variables> declaradas,String variable,int tipo){//checa el tipo de las variables
		for(int i=0;i<declaradas.size();i++)
		{
			if(declaradas.get(i).getVariable().equals(variable))
			{
				if(tipo==3 && declaradas.get(i).getTipo()==2)
					return false;
				if(declaradas.get(i).getTipo()==tipo)
					return false;
				
				break;
			}
		}
		return true;
	}
}
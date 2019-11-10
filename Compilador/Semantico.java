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
			JOptionPane.showMessageDialog(null, "Ocurrio Un Error Semantico:\n"+Mensaje, "Warning",JOptionPane.WARNING_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Exito");
	}
	
	public void verificaVariables(ArrayList<Variables> dec, ArrayList<Variables> asi){
		boolean existe;
		for(int i=0;i<asi.size();i++){
			existe=false;
			for(int j=0;j<dec.size();j++){
				if(asi.get(i).getVariable().equals(dec.get(j).getVariable()))
					existe=true;
			}
			if(!existe){
				ErrorSem=true;
				Mensaje=Mensaje+"Variable utilizada no existe.\n";
				return;
			}
		}
		
		String[] tokens;
		//Verifica el valor asignado a las variables declaradas
		for(int i=0;i<dec.size();i++)
		{
			tokens = dec.get(i).getValor().split(" ");
			for(int j=0;j<tokens.length;j++)
			{
				if(!tokens[j].isEmpty())
				{
					if(Arrays.asList("+","*","/","-").contains(tokens[j]))
						continue;
					if(!Pattern.matches("^(\\d+)$",tokens[j]) && !Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]) && !Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
					{
						if(dec.get(i).getVariable().equals(tokens[j]))
						{
							ErrorSem=true;
							Mensaje=Mensaje+"Variable utilizada no existe.\n";
							return;
						}
					}
				}
			}
		}
		
		//Verificar que el orden de asignadas este abajo de la variable declarada
		for(int i=0;i<asi.size();i++)
		{
			for(int j=0;j<dec.size();j++)
			{
				if(asi.get(i).getVariable().equals(dec.get(j).getVariable()))
					if(asi.get(i).getLinea()<dec.get(j).getLinea())
					{
						ErrorSem=true;
						Mensaje=Mensaje+"Variable utilizada no existe.\n";
						return;
					}
			}
		}
		
		for(int i=0;i<asi.size();i++)
		{
			tokens = asi.get(i).getValor().split(" ");
			for(int j=0;j<tokens.length;j++)
			{
				if(!tokens[j].isEmpty())
				{
					if(Arrays.asList("+","*","/","-").contains(tokens[j]))
						continue;
					if(!Pattern.matches("^(\\d+)$",tokens[j]) && !Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]) && !Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
					{
						ErrorSem=true;
						for(int k=0;k<dec.size();k++)
						{
							if(dec.get(k).getVariable().equals(tokens[j]))
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
	public void asignarTipo(ArrayList<Variables> dec, ArrayList<Variables> asi){
		for(int i=0;i<dec.size();i++){
			for(int j=0;j<asi.size();j++){
				if(asi.get(j).getVariable().equals(dec.get(i).getVariable()))
					asi.get(j).setTipo(dec.get(i).getTipo());
			}
		}
	}
	public void verificaTipodeDato(ArrayList<Variables> dec, ArrayList<Variables> asi){
		String[] tokens;
		for(int i=0;i<dec.size();i++){//Declaradas
			if(!dec.get(i).getValor().isEmpty())
			{
				tokens = dec.get(i).getValor().split(" ");
				for(int j=0;j<tokens.length;j++)
				{
					switch(dec.get(i).getTipo()){
						//Entero
						case 2:
							if(Pattern.matches("^(\\d+)$",tokens[j]))
								break;
							if(tokens[j].equals("/"))
							{
								if(tokens[j+1].equals("0"))
								{
									ErrorSem=true;
									Mensaje=Mensaje+"División por cero.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(dec,tokens[j],2)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								//return;
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
									Mensaje=Mensaje+"División por cero.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(dec,tokens[j],3)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								//return;
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
								if(checkTipo(dec,tokens[j],4)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									//return;
									break;
								}
							}
							break;
					}
				}
			}
		}
		
		for(int i=0;i<asi.size();i++){//Asignadas
			if(!asi.get(i).getValor().isEmpty())
			{
				tokens = asi.get(i).getValor().split(" ");
				for(int j=0;j<tokens.length;j++)
				{
					switch(asi.get(i).getTipo()){
						//Entero
						case 2:
							if(Pattern.matches("^(\\d+)$",tokens[j]))
								break;
							if(tokens[j].equals("/"))
							{
								if(tokens[j+1].equals("0"))
								{
									ErrorSem=true;
									Mensaje=Mensaje+"División por cero.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(dec,tokens[j],2)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]) || Pattern.matches("(^[0-9]+([.][0-9]+)?$)",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								//return;
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
									Mensaje=Mensaje+"División por cero.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^[a-zA-Z0-9]+$",tokens[j])){
								if(checkTipo(dec,tokens[j],3)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									//return;
									break;
								}
							}
							if(Pattern.matches("^['][a-zA-Z0-9.\\s]+[']$",tokens[j]))
							{
								ErrorSem=true;
								Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
								//return;
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
								if(checkTipo(dec,tokens[j],4)){
									ErrorSem=true;
									Mensaje=Mensaje+"El tipo de dato asignado es incorrecto.\n";
									//return;
									break;
								}
							}
							break;
					}
				}
			}
		}
	}
	
	public boolean checkTipo(ArrayList<Variables> dec,String variable,int tipo){
		for(int i=0;i<dec.size();i++)
		{
			if(dec.get(i).getVariable().equals(variable))
			{
				if(tipo==3 && dec.get(i).getTipo()==2)
					return false;
				if(dec.get(i).getTipo()==tipo)
					return false;
				
				break;
			}
		}
		return true;
	}
}
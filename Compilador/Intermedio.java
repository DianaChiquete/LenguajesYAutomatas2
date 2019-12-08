package Compilador;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Intermedio {
	String Ensamblador="";
	int TemporalesCount=0,contadorIf=1;
	boolean ElseExist = true;
	ArrayList<String> valores = new ArrayList<String>();
	
	public Intermedio(ArrayList<Variables> TablaSimbolos){
		cuentaTemporales(TablaSimbolos);
		analisa(TablaSimbolos);
		
		
		//imprime simbolos
		for(int i=0;i<TablaSimbolos.size();i++){
			System.out.print(TablaSimbolos.get(i).toString());
		}
	}
	public void cuentaTemporales(ArrayList<Variables> tab){
		for(int i=0;i<tab.size();i++)
		{
			if(tab.get(i).getTipo()==2 || tab.get(i).getTipo()==3)
			{
				String[] tokens;
				tokens = tab.get(i).getValor().split(" ");
				for(int j=0;j<tokens.length;j++)
					valores.add(tokens[j]);
				valores.remove(0);
				
				
				for(int j=0;j<valores.size();j++){
					if(valores.get(j).equals("*") || valores.get(j).equals("/") || valores.get(j).equals("+") || valores.get(j).equals("-"))
						TemporalesCount++;
				}
				valores.clear();
			}
		}
	}
	public void analisa(ArrayList<Variables> tab){
		//Default Ensamblador
		Ensamblador+="TITLE	Codigo Intermedio\n";
		Ensamblador+="PAGE	60,132\n";				
		Ensamblador+=".586\n";						
		Ensamblador+=".MODEL FLAT, STDCALL\n\n";
		Ensamblador+="ExitProcess	PROTO	CodigoSalida:DWORD\n\n";
		Ensamblador+=".STACK\n";	
		Ensamblador+=".DATA\n";
		
		//temporales-auxiliares
		for(int i=1;i<=TemporalesCount;i++)
		{
			Ensamblador+="T"+i+"		DWORD ?\n";
		}
		
		for(int i=0;i<tab.size();i++){
			if(tab.get(i).getTipo()==2 && tab.get(i).getUso()==0)
				Ensamblador+=tab.get(i).getVariable()+"		DWORD ?\n";
		}
		
		Ensamblador+=".CODE\n";
		Ensamblador+="Principal		PROC\n\n";
		int contTemp=1;
		for(int i=0;i<tab.size();i++)
		{
			if(tab.get(i).getTipo()==2 || tab.get(i).getTipo()==3)//entero y doble
			{
				String[] tokens;
				tokens = tab.get(i).getValor().split(" ");
					
				llenaValores(tokens);
				for(int j=0;j<tokens.length;j++)
					valores.add(tokens[j]);
					
				valores.remove(0);
				if(valores.size()==1){
					Ensamblador+="MOV "+valores.get(0)+", EAX\n";
					Ensamblador+="MOV EAX, "+tab.get(i).getVariable()+"\n";
				}
				else
				{
					for(int j=0;j<valores.size();j++)
					{//Multiplicacion y division
						if(valores.get(j).equals("*") || valores.get(j).equals("/")){
							String op="";
							if(valores.get(j).equals("*"))
								op="MUL";
							else
								op="DIV";
								
							Ensamblador+="MOV "+valores.get(j-1)+", EAX\n";
							Ensamblador+=op+" "+valores.get(j+1)+", EAX\n";
							Ensamblador+="MOV EAX, T"+contTemp+"\n";
								
							valores.add(j,"T"+contTemp);
							valores.remove(j+1);
							valores.remove(j+1);
							valores.remove(j-1);
								
							contTemp++;
						}
					}
						
					int indice=1;
					while(valores.size()!=1){//Sumas y Restas
						String op="";
						if(valores.get(indice).equals("+"))
							op="ADD";
						else
							op="SUB";
							
						Ensamblador+="MOV "+valores.get(indice-1)+", EAX\n";
						Ensamblador+=op+" "+valores.get(indice+1)+", EAX\n";
						Ensamblador+="MOV EAX, T"+contTemp+"\n";
							
						valores.add(indice,"T"+contTemp);
						valores.remove(indice+1);
						valores.remove(indice+1);
						valores.remove(indice-1);
							
						contTemp++;
					}

					Ensamblador+="MOV T"+(contTemp-1)+", EAX\n";
					Ensamblador+="MOV EAX, "+tab.get(i).getVariable()+"\n";
				}
			}
			
			if(tab.get(i).getTipo()==5 || tab.get(i).getTipo()==6)//if y else
			{
				if(tab.get(i).getTipo()==5 && tab.get(i).getUso()==0)
				{
					String[] tokens;
					tokens = tab.get(i).getValor().split(" ");
					llenaValores(tokens);
					//Ensamblador+="\n";
					
					if(Pattern.matches("(^[0-9]+([.][0-9]+)?$)",valores.get(0)))
					{
						Ensamblador+="MOV "+valores.get(0)+", EAX\n";
						Ensamblador+="CMP EAX, "+valores.get(2)+"\n";
					}
					else{
						Ensamblador+="CMP "+valores.get(0)+", "+valores.get(2)+"\n";
					}
					String nameElse = "else"+contadorIf, aux="";
					ElseExist = existeElse(tab,nameElse,i);
					
					switch(valores.get(1)){
					case "==":
						aux="JNE";
						break;
					case ">":
						aux="JL";
						break;
					case "<":
						aux="JG";
						break;
					}
					
					if(ElseExist)
					{
						Ensamblador+=aux+" falso"+contadorIf+"\n";
					}
					else
					{
						Ensamblador+=aux+" fin"+contadorIf+"\n";
						//contadorIf++;
					}
					
				}
				if(tab.get(i).getTipo()==5 && tab.get(i).getUso()==1)
				{
					if(!ElseExist){
						Ensamblador+="fin"+contadorIf+":\n";
						contadorIf++;
						Ensamblador+="\n";
					}
					else{
						Ensamblador+="JMP fin"+contadorIf+"\n";
					}
				}
				if(tab.get(i).getTipo()==6 && tab.get(i).getUso()==0)
				{
					Ensamblador+="falso"+contadorIf+":\n";
				}
				if(tab.get(i).getTipo()==6 && tab.get(i).getUso()==1)
				{
					Ensamblador+="fin"+contadorIf+":\n";
					contadorIf++;
					Ensamblador+="\n";
				}
				valores.clear();
				continue;
			}
			Ensamblador+="\n";
			valores.clear();
		}
		Ensamblador+="\n;Regresar al SO\n";
		Ensamblador+="INVOKE ExitProcess, 0\n";
		Ensamblador+="Principal		ENDP\n";
		Ensamblador+="END		Principal\n";
	}
	
	public void llenaValores(String[] tokens)
	{
		for(int j=0;j<tokens.length;j++)
			valores.add(tokens[j]);
	}
	public boolean existeElse(ArrayList<Variables> tab, String var, int pos){
		for(int i=pos;i<tab.size();i++)
			if(tab.get(i).getVariable().equals(var))
				return true;
		return false;
	}
}
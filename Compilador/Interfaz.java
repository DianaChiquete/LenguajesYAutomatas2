package Compilador;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class Interfaz extends JFrame implements KeyListener,ActionListener
{
	JButton btnEvalu,btnSim,btnSem,btnAbrir,btnGuardar,btnLimpia;//Nombre de los botones
	JTextArea codigo,tok;
	JScrollPane sCod,sTok;
	
	DefaultTableModel modelo;
	String titulos[] ={"Tipo", "Valor"};//Valores de la tabla de simbolos
	JTable tabla;//crea la tabla de simbolos.
	
	JFileChooser archivoSeleccionado;
	File archivo;
	Lexico lex;
	Sintactico sin;
	Semantico sem;
	public Interfaz()
	{
		Interfaz();
	}
	public void Interfaz()
	{
		setSize(500,600);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		
		archivoSeleccionado= new JFileChooser("Abrir");
		archivoSeleccionado.setDialogTitle("Seleccionar Archivo");
		archivoSeleccionado.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		modelo = new DefaultTableModel(null,titulos);//Modelo de la tabla
		tabla = new JTable(modelo);//Tabla de tokens
		sTok = new JScrollPane(tabla);//Scroll para la tabla de simbolos
		
		//butones implementados en la interfaz 
		btnAbrir = new JButton("Abrir Archivo");
		btnGuardar = new JButton("Guardar Archivo");
		btnLimpia = new JButton("Limpiar Pantalla");
		btnEvalu = new JButton("Evaluar");
		btnSim = new JButton("Sintactico");
		btnSem = new JButton("Semantico");
		codigo = new JTextArea();//cuadro de texto para insertar el codigo.
		tok = new JTextArea();//tabla generada en la tabla de simbolos que contiene como atributo tipo y valor
		
		sCod = new JScrollPane(codigo);
		
		JLabel lblProg = new JLabel("CODIGO");
		JLabel lblTok = new JLabel("TABLA DE SIMBOLOS");
		
		
		
		//Boton.setBounds(x,y,largo,alto);
		btnAbrir.setBounds(10,10,130,20);//BotonAbrir
		btnGuardar.setBounds(140,10,130,20);//BotonGuardar
		btnLimpia.setBounds(270,10,130,20);//BotonLimpiar
		lblProg.setBounds(10,30,60,20);//Label Programa
		sCod.setBounds(10,50,260,200);//TextAreaCodigo
		btnEvalu.setBounds(10,260,80,30);//BotonScan
		lblTok.setBounds(10,290,130,20);//Label Tokens
		sTok.setBounds(10,310,260,240);//TextAreaTokens
		btnSim.setBounds(90,260,93,30);//BotonParser
		btnSem.setBounds(170,260,100,30);
		//Con el comando add se agregan los botones a la interfaz
		add(btnGuardar);
		add(btnAbrir);
		add(btnLimpia);
		add(lblProg);
		add(sCod);
		add(btnEvalu);
		add(sTok);
		add(lblTok);
		add(btnSim);
		add(btnSem);
		
		Escuchadores();
		deshabilita();
		
		setVisible(true);//Comando para que los botones puedan verce.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//Clase usada para cuando se inicia el programa dichos botones no puedan ser usados hasta que es insertado el codigo
	public void deshabilita(){
		btnEvalu.setEnabled(false);
		btnSim.setEnabled(false);
		btnSem.setEnabled(false);
		btnGuardar.setEnabled(false);
	}
	public void Escuchadores(){
		codigo.addKeyListener(this);
		
		btnAbrir.addActionListener(this);
		btnEvalu.addActionListener(this);
		btnSim.addActionListener(this);
		btnSem.addActionListener(this);
		btnGuardar.addActionListener(this);
		btnLimpia.addActionListener(this);
	}
	//Clase usada para cuento se extrae un archivo de la computadora o si es nuevo
	//el poder guardarla en alguna parte de nuestra computadora
	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource()==btnAbrir)
		{
			if(archivoSeleccionado.showOpenDialog(this)==JFileChooser.CANCEL_OPTION)
				return;
			else
			{
				//System.out.print(archivo.getAbsolutePath());
				//new Analizador(archivo.getAbsolutePath()+"");
				//System.out.print(archivo.getName().toString());
				archivo=archivoSeleccionado.getSelectedFile();
				abrir();
				limpiarTabla();
				btnEvalu.setEnabled(true);
				btnGuardar.setEnabled(true);
				return;
			}
		}
		if(evento.getSource()==btnEvalu){
			limpiarTabla();
			lex = new Lexico(archivo.getAbsolutePath()+"");//proporciona la ruta absoluta del archivo.
			if(!lex.error)//Si no ocurre ningun error lexico llena la tabla
			{
				btnEvalu.setEnabled(false);//desactiva boton evaluar
				llenarTabla();
				btnSim.setEnabled(true);//activa el boton sintactico
			}
			return;//sale
			
		}
		if(evento.getSource()==btnSim){//.getsource encuentra qué botón hace clic
			sin = new Sintactico(lex.PalabrasAnalizadas);
			if(!sin.ErrorS){
				btnSim.setEnabled(false);//el boton se desactiva
				btnSem.setEnabled(true);//y se activa el boton semantico
			}
			return;
		}
		if(evento.getSource()==btnSem){
			sem = new Semantico(sin.declaradas,sin.asignadas);
			return;
		}
		if(evento.getSource()==btnGuardar){
			guardar();
			return;
		}
		if(evento.getSource()==btnLimpia){
			btnGuardar.setEnabled(false);//se desactivan todos los demas botones
			btnEvalu.setEnabled(false);
			btnSim.setEnabled(false);
			btnSem.setEnabled(false);
			codigo.setText("");
			limpiarTabla();
			return;
		}
	}
	public boolean abrir() {
		String texto="",linea;
		try {
			FileReader fr = new FileReader(archivo) ; //crea un clase para leer archivos llamada archivo
			BufferedReader br= new BufferedReader(fr);//utilizado para leer el texto llamado br
			while((linea=br.readLine())!=null) 
				texto+=linea+"\n";
			codigo.setText(texto);//llena el TextArea de Codigo
			return true;
		}catch (Exception e) {//excepcion para atrapar los archivos que no sean de texto
			archivo=null;
			JOptionPane.showMessageDialog(null, "El archivo no es de tipo texto", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	public boolean guardar() {
		try {
			FileWriter fw = new FileWriter(archivo);
			BufferedWriter bf = new BufferedWriter(fw);
			bf.write(codigo.getText());
			bf.close();
			fw.close();
			JOptionPane.showMessageDialog(null, "Se ha guardado el archivo");
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se ha podido guardar el archivo", "ERROR",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	public void llenarTabla(){//clase utilizada para ir llenado la tabla de simbolos
		//mientras se van analizando las lineas
		for(int i = 0;i<lex.PalabrasAnalizadas.size();i++)
			modelo.insertRow(modelo.getRowCount(),new Object[]{lex.PalabrasAnalizadas.get(i).getTipo(),lex.PalabrasAnalizadas.get(i).getValor()});
		//inserta los caracteres a la tabla distribuyendolas por tipo y valor 
	}
	public void limpiarTabla(){
		while (tabla.getRowCount()!=0){
			((DefaultTableModel)tabla.getModel()).removeRow(0);
		}
	}
	//Interface KeyListener
	public void keyPressed(KeyEvent evento){//Se invoca cuando se resiona una tecla
	}
	public void keyReleased(KeyEvent evento){//Se invoca cuando se libera una clave.
	}
	public void keyTyped(KeyEvent evento) {//Se invoca cuando se ha escrito una clave.
		if(codigo.getText().length()!=0)
			btnEvalu.setEnabled(true);
		else
			btnEvalu.setEnabled(false);
		
	}
	
	public static void main(String[] args)
	{
		new Interfaz();
	}
}
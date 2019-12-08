package Compilador;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

public class Formulario extends JFrame implements KeyListener, ActionListener {
	JButton btnScan, btnPars, btnSem, btnAbrir, btnGuardar, btnLimpia, btnTriplo, btnCuadruplo, btnIntermedio;
	JTextArea codigo, tok, Inter;
	JScrollPane sCod, sTok, sTrpl, sCrpl,sInter;

	DefaultTableModel modelo;
	DefaultTableModel tbTriplo;
	DefaultTableModel tbCuadruplo;
	String titulos[] = { "Tipo", "Valor" };
	String titulosTriplo[] = { "#R", "OP", "ARG1", "ARG2" };
	String titulosCuadruplos[] = { "#R", "OP", "ARG1", "ARG2", "RES" };
	JTable tabla, tabla1, tabla2;
	JLabel lblEcu;

	JFileChooser archivoSeleccionado;
	File archivo;
	Lexico lex;
	Sintactico sin;
	Semantico sem;
	Triplo trpl = new Triplo();
	Intermedio Interm;
	Cuadruplo crpl = new Cuadruplo();

	public Formulario() {
		Interfaz();
	}

	public void Interfaz() {
		setSize(700, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		archivoSeleccionado = new JFileChooser("Abrir");
		archivoSeleccionado.setDialogTitle("Abrir");
		archivoSeleccionado.setFileSelectionMode(JFileChooser.FILES_ONLY);

		modelo = new DefaultTableModel(null, titulos);// Modelo de la tabla
		tabla = new JTable(modelo);// Tabla de tokens
		sTok = new JScrollPane(tabla);// Scroll para la tabla de tokens

		// Tabla Triplo
		tbTriplo = new DefaultTableModel(null, titulosTriplo);
		tabla1 = new JTable(tbTriplo);
		sTrpl = new JScrollPane(tabla1);

		// Tabla Cuadruplo
		tbCuadruplo = new DefaultTableModel(null, titulosCuadruplos);
		tabla2 = new JTable(tbCuadruplo);
		sCrpl = new JScrollPane(tabla2);

		// Otros
		btnAbrir = new JButton("Abrir");
		btnGuardar = new JButton("Guardar");
		btnLimpia = new JButton("Limpiar");
		btnScan = new JButton("Analizar");
		btnPars = new JButton("Sintactico");
		btnSem = new JButton("Semantico");
		btnTriplo = new JButton("Triplo");
		btnCuadruplo = new JButton("Cuadruplo");
		btnIntermedio = new JButton("Ensamblador");
		codigo = new JTextArea();
		tok = new JTextArea();
		Inter = new JTextArea();

		sCod = new JScrollPane(codigo);
		sInter = new JScrollPane(Inter);

		JLabel lblProg = new JLabel("Programa");
		JLabel lblTok = new JLabel("Tokens");
		JLabel lblTrpl = new JLabel("Triplos");
		JLabel lblCrpl = new JLabel("Cuadruplos");
		JLabel lblIntermedio = new JLabel("Codigo Ensamblador");
		lblEcu = new JLabel("Ecuacion: ");

		// Boton.setBounds(x,y,largo,alto);
		btnAbrir.setBounds(10, 10, 70, 20);// BotonAbrir
		btnGuardar.setBounds(90, 10, 80, 20);// BotonGuardar
		btnLimpia.setBounds(180, 10, 80, 20);// BotonLimpiar
		lblProg.setBounds(10, 30, 60, 20);// Label Programa
		sCod.setBounds(10, 50, 260, 200);// TextAreaCodigo
		btnScan.setBounds(10, 260, 80, 30);// BotonScan
		lblTok.setBounds(10, 350, 60, 20);// Label Tokens
		sTok.setBounds(10, 380, 260, 240);// TextAreaTokens
		btnPars.setBounds(95, 260, 95, 30);// BotonParser
		btnSem.setBounds(195, 260, 95, 30);
		lblEcu.setBounds(300, 30, 300, 20);// Ecuacion encontrada
		lblIntermedio.setBounds(400,50,130,20);//Label Intermedio
		btnIntermedio.setBounds(120,300,110,30);//Boton intermedio
		
		sInter.setBounds(310,70,370,200);//Cuadro del Codigo intemedio
		
		// Triplos
		lblTrpl.setBounds(300,300, 100, 20);
		sTrpl.setBounds(310,320, 250, 200);

		btnTriplo.setBounds(10,300, 100, 30);//Boton

		// Cuadruplos
		//lblCrpl.setBounds(300, 330, 100, 20);
		//sCrpl.setBounds(300, 350, 250, 200);

		//btnCuadruplo.setBounds(120, 300, 100, 30);

		add(btnGuardar);
		add(btnAbrir);
		add(btnLimpia);
		add(lblProg);
		add(sCod);
		add(btnScan);
		add(sTok);
		add(lblTok);
		add(btnPars);
		add(btnSem);
		add(lblTrpl);
		add(sTrpl);
		add(btnTriplo);
		//add(lblCrpl);
		add(sCrpl);
		add(btnCuadruplo);
		add(lblEcu);
		add(lblIntermedio);
		add(btnIntermedio);
		add(sInter);

		Escuchadores();
		deshabilita();

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void deshabilita() {
		btnScan.setEnabled(false);
		btnPars.setEnabled(false);
		btnSem.setEnabled(false);
		btnGuardar.setEnabled(false);
		btnTriplo.setEnabled(false);
		btnCuadruplo.setEnabled(false);
		btnIntermedio.setEnabled(false);
	}

	public void Escuchadores() {
		codigo.addKeyListener(this);

		btnAbrir.addActionListener(this);
		btnScan.addActionListener(this);
		btnPars.addActionListener(this);
		btnSem.addActionListener(this);
		btnTriplo.addActionListener(this);
		btnCuadruplo.addActionListener(this);
		btnGuardar.addActionListener(this);
		btnLimpia.addActionListener(this);
		btnIntermedio.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btnAbrir) {
			if (archivoSeleccionado.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)
				return;
			else {
				// System.out.print(archivo.getAbsolutePath());
				// new Analizador(archivo.getAbsolutePath()+"");
				// System.out.print(archivo.getName().toString());
				archivo = archivoSeleccionado.getSelectedFile();
				abrir();
				limpiarTabla();
				btnScan.setEnabled(true);
				btnGuardar.setEnabled(true);
				return;
			}
		}
		if (evt.getSource() == btnScan) {
			limpiarTabla();
			lex = new Lexico(archivo.getAbsolutePath() + "");
			if (!lex.error)// Si no ocurre ningun error lexico llena la tabla
			{
				btnScan.setEnabled(false);
				llenarTabla();
				btnPars.setEnabled(true);
				btnTriplo.setEnabled(true);
				btnCuadruplo.setEnabled(true);
				lblEcu.setText("Ecuacion: " + lex.ecuacion);
			}
			return;
		}
		if (evt.getSource() == btnPars) {
			sin = new Sintactico(lex.PalabrasAnalizadas);
			if (!sin.ErrorS) {
				btnPars.setEnabled(false);
				btnSem.setEnabled(true);
			}
			return;
		}
		if (evt.getSource() == btnSem) {
			sem = new Semantico(sin.declaradas, sin.asignadas);
			if(!sem.ErrorSem){
				btnSem.setEnabled(false);
				btnIntermedio.setEnabled(true);
			}
			return;
		}
		if(evt.getSource()==btnIntermedio){
			Interm = new Intermedio(sin.TablaSimbolos);
			Inter.setText(Interm.Ensamblador);
			return;
		}
		if (evt.getSource() == btnTriplo) {
			limpiarTablaTriplo();
			if (lex.ecuacion != "") {
				llenarTablaTriplo(lex.ecuacion);
			}
			return;
		}
		if (evt.getSource() == btnCuadruplo) {
			limpiarTablaCuadruplo();
			if (lex.ecuacion != "") {
				llenarTablaCuadruplo(lex.ecuacion);
			}
			return;
		}
		if (evt.getSource() == btnGuardar) {
			guardar();
			return;
		}
		if (evt.getSource() == btnLimpia) {
			btnGuardar.setEnabled(false);
			btnScan.setEnabled(false);
			btnPars.setEnabled(false);
			btnSem.setEnabled(false);
			btnTriplo.setEnabled(false);
			btnCuadruplo.setEnabled(false);
			btnIntermedio.setEnabled(false);
			codigo.setText("");
			lblEcu.setText("Ecuacion: ");
			limpiarTabla();
			limpiarTablaTriplo();
			limpiarTablaCuadruplo();
			
			return;
		}
	}

	public boolean abrir() {
		String texto = "", linea;
		try {
			FileReader fr = new FileReader(archivo);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			while ((linea = br.readLine()) != null)
				texto += linea + "\n";
			codigo.setText(texto);// llena el TextArea de Codigo
			return true;
		} catch (Exception e) {
			archivo = null;
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
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se ha podido modificar el archivo", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public void llenarTabla() {
		for (int i = 0; i < lex.PalabrasAnalizadas.size(); i++)
			modelo.insertRow(modelo.getRowCount(),
					new Object[] { lex.PalabrasAnalizadas.get(i).getTipo(), lex.PalabrasAnalizadas.get(i).getValor() });
	}

	public void llenarTablaTriplo(String ecuacion) {
		trpl.resultado(ecuacion);
		ArrayList<String> niveles = trpl.niveles;

		for (int i = 0; i < niveles.size(); i++) {
			String[] pos = niveles.get(i).split(" ");
			String pos2 = "";
			if (pos.length > 2) {
				pos2 = pos[2];
			}

			tbTriplo.insertRow(tbTriplo.getRowCount(), new Object[] { "(" + i + ")", pos[0], pos[1], pos2 });
		}
	}

	public void llenarTablaCuadruplo(String ecuacion) {
		crpl.resultado(ecuacion);
		ArrayList<String> niveles = crpl.niveles;

		for (int i = 0; i < niveles.size(); i++) {
			String[] pos = niveles.get(i).split(" ");
			String pos2 = "";
			if (pos.length > 3) {
				pos2 = pos[2];
			}
			tbCuadruplo.insertRow(tbCuadruplo.getRowCount(), new Object[] { (i + 1), pos[0], pos[1], pos[2], pos[3] });
		}
	}

	public void limpiarTabla() {
		while (tabla.getRowCount() != 0) {
			((DefaultTableModel) tabla.getModel()).removeRow(0);
		}
	}

	public void limpiarTablaTriplo() {
		while (tabla1.getRowCount() != 0) {
			((DefaultTableModel) tabla1.getModel()).removeRow(0);
		}
	}

	public void limpiarTablaCuadruplo() {
		while (tabla2.getRowCount() != 0) {
			((DefaultTableModel) tabla2.getModel()).removeRow(0);
		}
	}

	public void keyPressed(KeyEvent evt) {// Presiona una tecla
	}

	public void keyReleased(KeyEvent evt) {// Escrita
	}

	public void keyTyped(KeyEvent evt) {// suelta
		if (codigo.getText().length() != 0)
			btnScan.setEnabled(true);
		else
			btnScan.setEnabled(false);
		// btnPars.setEnabled(false);
	}

	public static void main(String[] args) {
		new Formulario();
	}
}

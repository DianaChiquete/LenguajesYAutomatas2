package proyectoaut;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.*;
public class Vista extends JFrame implements ActionListener,MenuListener{
	JMenuBar MenuPrincipal;
	JMenu iniciar,Guardar;
	JRadioButton crear, abrir;
	JDialog principal;
	JLabel eti;
	JButton aceptar,analizar;
	JFileChooser FuncionesArchivos;
	File archivo;
	JTextArea Texto;
	JList<String> tokens;
	JTabbedPane documentos,consola;
	String [] titulos ={"Tipo","Nombre","Valor"};
	DefaultTableModel modelo = new DefaultTableModel(new Object[0][0],titulos);
	JTable tablaDatos = new JTable(modelo);
	public Vista() {
		inicializaciones();
	}
	public void inicializaciones() {
		MenuPrincipal=new JMenuBar();
		iniciar=new JMenu("Iniciar Analisis");
		Guardar=new JMenu("Guardar");
		FuncionesArchivos= new JFileChooser("Guardar");
		tokens=new JList<String>();
		documentos = new JTabbedPane();
		consola = new JTabbedPane();
		Texto = new JTextArea();
		eti=new JLabel("Ingrese la opcion que desea realizar:");
		crear=new JRadioButton("Crear el archivo",false);
		abrir=new JRadioButton("Abrir archivo seleccionado",false);
		analizar=new JButton("Analizar");
		aceptar=new JButton("Aceptar");
		modalPrincipal();
	}
	public void modalPrincipal() {
		principal=new JDialog();
		principal.setModal(true);
		principal.setTitle("Analizador");
		principal.setSize(300, 180);
		principal.setLayout(null);
		principal.setLocationRelativeTo(null);
		eti.setBounds(40, 10,250, 15);
		crear.setBounds(70, 35,250, 15);
		abrir.setBounds(70, 60,250, 15);
		aceptar.setBounds(80,85,110, 30);
		principal.add(eti);
		principal.add(crear);
		principal.add(abrir);
		principal.add(aceptar);
		crear.addActionListener(this);
		abrir.addActionListener(this);
		aceptar.addActionListener(this);
		principal.setVisible(true);
	}
	private void hazInterfaz(String titulo) {
		setTitle("Analizador sint�ctico :v");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(MenuPrincipal);
		MenuPrincipal.add(iniciar);
		setLayout(new GridLayout(2,0));
		setSize(600,450);
		setLocationRelativeTo(null);
		Texto.setFont(new Font("Arial", Font.PLAIN, 12));
		documentos.addTab(titulo, new JScrollPane(Texto));
		documentos.setToolTipText("Aqui se muestra el codigo");
		add(documentos);
		consola.addTab("Consola",new JScrollPane(tokens));
		consola.addTab("Tabla",new JScrollPane(tablaDatos));
		add(consola);
	}
	public void hazEscuchas() {
		iniciar.addMenuListener(this);
		Guardar.addMenuListener(this);
	}
	boolean ban=false;
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==crear) {
			abrir.setSelected(false);
		}
		if(e.getSource()==abrir) {
			crear.setSelected(false);
		}
		if(e.getSource()==aceptar) {
			if(abrir.isSelected()) {
				FuncionesArchivos.setDialogTitle("Abrir");
				FuncionesArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(FuncionesArchivos.showOpenDialog(this)==JFileChooser.CANCEL_OPTION) 
					return;
				archivo=FuncionesArchivos.getSelectedFile();
				abrir();
				principal.dispose();
				hazInterfaz(archivo.getName());
				setVisible(true);
				hazEscuchas();
				ban=true;
				return;
			}
			principal.dispose();
			hazInterfaz("Nuevo");
			MenuPrincipal.add(Guardar);
			setVisible(true);
			hazEscuchas();
		}
	}
	public boolean guardar() {
		try {
			if(archivo==null) {
				FuncionesArchivos.setDialogTitle("Guardar como");
				FuncionesArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(FuncionesArchivos.showSaveDialog(this)==JFileChooser.CANCEL_OPTION) 
					return false;
				ban=true;
				archivo=FuncionesArchivos.getSelectedFile();
				documentos.setTitleAt(0, archivo.getName());
			}
			FileWriter fw = new FileWriter(archivo);
			BufferedWriter bf = new BufferedWriter(fw);
			bf.write(Texto.getText());
			bf.close();
			fw.close();
		}catch (Exception e) {
			System.out.println("Ha ocurrido un problema por favor verifiquelo");
			return false;
		}
		return true;
	}
	public boolean abrir() {
		String texto="",linea;
		try {
			FileReader fr = new FileReader(archivo) ; 
			BufferedReader br= new BufferedReader(fr);
			while((linea=br.readLine())!=null) 
				texto+=linea+"\n";
			Texto.setText(texto);
			return true;
		}catch (Exception e) {
			archivo=null;
			JOptionPane.showMessageDialog(null, "El archivo no es de tipo texto", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	public void menuSelected(MenuEvent Evt) {
		if(Evt.getSource()==iniciar) {
			if(!ban) {
				JOptionPane.showMessageDialog(null, "Guarde el archivo primero antes de analizarlo", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			Analizador analisador = new Analizador(archivo.getAbsolutePath());
			tokens.setListData(analisador.getmistokens().toArray( new String [0]));
			modelo = new DefaultTableModel(new Object[0][0],titulos);
			tablaDatos.setModel(modelo);
			for (int i = analisador.getIdenti().size()-1; i >=0; i--) {
				Identificador id = analisador.getIdenti().get(i);
				if(!id.tipo.equals("")) {
					Object datostabla[]= {id.tipo,id.nombre,id.valor};
					modelo.addRow(datostabla);
				}
			}
			return;
		}
		if(Evt.getSource()==Guardar) {
			guardar();
		}
	}
	public void menuCanceled(MenuEvent e) {
		
	}
	public void menuDeselected(MenuEvent e) {
	}
}

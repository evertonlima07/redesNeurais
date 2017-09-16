import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DiagnosticoRapido extends JFrame {

	private JPanel contentPane;
	private JLabel jlabelLogo, epoca,erro;
	private JTextField numeroepocas;
	private JTextField taxaerro;
	
  
   ImageIcon fundo = new ImageIcon(getClass().getResource("LogoMamografiapng.png"));
 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DiagnosticoRapido frame = new DiagnosticoRapido();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DiagnosticoRapido() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("mama.png"));
		setTitle("DiagnosticoRapido");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,720);
		contentPane = new JPanel();
		// Entradas dos numeros de Epocas e da taxa de rro
		numeroepocas = new JTextField();
		contentPane.add(numeroepocas);
		numeroepocas.setBounds(650, 40, 60, 30);
		epoca = new JLabel("Epocas:");
		contentPane.add(epoca);
		epoca.setBounds(600, 40, 60, 30);
		
	 //___________________________________________________________________
		taxaerro = new JTextField();
		contentPane.add(taxaerro);
		taxaerro.setBounds(800, 40, 60, 30);
		erro = new JLabel("TaxaErro:");
		contentPane.add(erro);
		erro.setBounds(742, 40, 60, 30);
		//-------------------------------------------------------------
		
		jlabelLogo = new JLabel(fundo);
		jlabelLogo.setBounds(30, 1, 500, 250);
		contentPane.add(jlabelLogo);
		
	   setContentPane(contentPane);
		
	    setLayout(null);
		JTextArea textArea = new JTextArea();
		contentPane.add(textArea);
		textArea.setBounds(600, 170, 280, 500);
		
		
		JButton diagnostico = new JButton("Obter Diagn�stico");
		contentPane.add(diagnostico);
		diagnostico.setBounds(105, 500, 180, 28);
		JButton treinar = new JButton("Treinar");
		contentPane.add(treinar);
		treinar.setBounds(700, 100, 75, 30);
			
			JLabel birads = new JLabel ("Bi-Rads:");
			contentPane.add(birads);
			birads.setBounds(60, 300, 150, 30);
		// Combo Box para o campo bi rads
		  JComboBox<String> combo_Birads = new JComboBox<String>();
		  contentPane.add(combo_Birads);
		  combo_Birads.setBounds(120, 300, 150, 30);
		  combo_Birads.addItem("1");
		  combo_Birads.addItem("2");
		  combo_Birads.addItem("3");
		  combo_Birads.addItem("4");
		  combo_Birads.addItem("5");
		  // Combo para campo forma
		  JLabel forma = new JLabel ("Forma:");
			contentPane.add(forma);
			forma.setBounds(60, 350, 150, 30);
		  JComboBox<String> combo_forma = new JComboBox<String>();
		  contentPane.add(combo_forma);
		  combo_forma.setBounds(120, 350, 150, 30);
		  combo_forma.addItem("1- Redonda");
		  combo_forma.addItem("2- Oval");
		  combo_forma.addItem("3- Lobular");
		  combo_forma.addItem("4- Irregular");
		  // combo para margem
		  JLabel margem = new JLabel ("Margem:");
				contentPane.add(margem);
				margem.setBounds(60, 400, 150, 30);
		  JComboBox<String> combo_margem = new JComboBox<String>();
		  contentPane.add(combo_margem);
		  combo_margem.setBounds(120, 400, 150, 30);
		  combo_margem.addItem("1- Circuscrito");
		  combo_margem.addItem("2- Microlobulado");
		  combo_margem.addItem("3- Obscurecido");
		  combo_margem.addItem("4- Mal definido");
		  combo_margem.addItem("5- Espiculados");
		  //combo para densidade
		  JLabel densidade = new JLabel ("Densidade:");
			contentPane.add(densidade);
			densidade.setBounds(55, 450, 150, 30);
	  JComboBox<String> combo_densidade = new JComboBox<String>();
	  contentPane.add(combo_densidade);
	  combo_densidade.setBounds(120, 450, 150, 30);
	  combo_densidade.addItem("1- Alta");
	  combo_densidade.addItem("2- Baixa");
	  combo_densidade.addItem("3- Iso");
	  combo_densidade.addItem("4- Contendo gordura");
	 
		  
		 
		
		JLabel lblResultadoTreinamento = new JLabel("Log de Treinamento:");
		contentPane.add(lblResultadoTreinamento);
		lblResultadoTreinamento.setBounds(600, 100, 200, 100);
		contentPane.setBackground(new java.awt.Color(255,217,239));
		
		 
		       
		    
	}


}


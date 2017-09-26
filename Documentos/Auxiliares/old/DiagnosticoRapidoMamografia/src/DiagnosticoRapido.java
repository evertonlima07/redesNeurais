import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;

public class DiagnosticoRapido extends JFrame {

	private JPanel contentPane;
	private JLabel jlabelLogo;
	private JTextField numeroepocas;
	private JTextField taxaerro;
	private JScrollPane scroll;
	private JMenuBar menu;
	private JMenu menu1;
	private JMenu menu2;
	private JMenu menu3;
	private JMenuItem item1;
	private JMenuItem itemconftaxa;
	private JMenuItem itemconferro;
	private JMenuItem item2;
	private JMenuItem nepocas;
	private JButton limpar;


	ImageIcon fundo = new ImageIcon(getClass().getResource("mama.png"));
	int numeroNeuroniosCamadaIntermediaria = 12;
	int numeroNeuroniosEntrada = 5;

	// Criada a Rede Neural
	RedeNeural rede = new RedeNeural(numeroNeuroniosCamadaIntermediaria, numeroNeuroniosEntrada);

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
		setIconImage(Toolkit.getDefaultToolkit().getImage("src//icon.png"));
		setTitle("DiagnosticoRapido");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 730);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		// Entradas dos numeros de Epocas e da taxa de rro
		// numeroepocas = new JTextField();
		// contentPane.add(numeroepocas);
		// numeroepocas.setBounds(650, 40, 60, 30);
		// epoca = new JLabel("Epocas:");
		// contentPane.add(epoca);
		// epoca.setBounds(600, 40, 60, 30);

		// painel = new Panel();
		// add(painel);
		// painel.add(contentPane);
		// contentPane.add(new JLabel(new
		// ImageIcon(getClass().getResource("mama.png"))));

		// ___________________________________________________________________
		// taxaerro = new JTextField();
		// contentPane.add(taxaerro);
		// taxaerro.setBounds(800, 40, 60, 30);
		// erro = new JLabel("TaxaErro:");
		// contentPane.add(erro);
		// erro.setBounds(742, 40, 60, 30);
		// -------------------------------------------------------------
		menu = new JMenuBar();
		menu1 = new JMenu("Opções");
		menu2 = new JMenu("Sobre");
		menu3 = new JMenu("Sair");
		item1 = new JMenuItem("Exit");
		item2 = new JMenuItem("Desenvolvedores");
		itemconftaxa = new JMenuItem("Configurar Taxa de Aprendizado");
		itemconferro = new JMenuItem("Configurar Taxa de Erro");
		nepocas = new JMenuItem("Configurar Numero de Epocas");
		setJMenuBar(menu);
		menu1.add(nepocas);
		menu.add(menu1);
		menu.add(menu2);
		menu.add(menu3);
		menu2.add(item2);
		menu3.add(item1);
		menu1.add(itemconftaxa);
		menu1.add(itemconferro);
		item1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		
		item2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 JOptionPane.showMessageDialog(null,

						    "                        Universidade Federal de Sergipe\n                     "

                        + "          Sistemas de Informação\n"

                        + "                        Disciplina: Inteligência Artificial\n"

                        + "                  Doscente: Dr. Alcides Xavier Benicasa\n"

                        + "  Discentes: Amanda Rezende, Everton Lima, Layla Joana\n"

                        + "                                              2017.1");

	            }
			
			
		});
		itemconftaxa.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String taxaapren = JOptionPane.showInputDialog("Informe a Taxa de Aprendizado!?");
				rede.TAXA_APRENDIZADO = Double.parseDouble(taxaapren);
			}
			
		});
		
		itemconferro.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String taxaerro = JOptionPane.showInputDialog("Informe a Taxa de Erro!?");
				rede.erro = Double.parseDouble(taxaerro);
			}
			
		});
		nepocas.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String epoca = JOptionPane.showInputDialog("Informe o numero de Epocas!?");
				rede.numeroepocas = Integer.parseInt(epoca);
			}
			
		});
		
		
		

		jlabelLogo = new JLabel(fundo);
		jlabelLogo.setBounds(0, 0, 900, 720);
		contentPane.add(jlabelLogo);

		setContentPane(contentPane);

		setLayout(null);
		JTextArea textArea = new JTextArea();
		contentPane.add(textArea);
		textArea.setBounds(560, 200, 230, 450);
		JScrollPane scr = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scr.setBounds(560, 200, 230, 450);// You have to set bounds for all the
											// controls and containers incas eof
											// null layout
		contentPane.add(scr);// Add you scroll pane to container

		JLabel birads = new JLabel("Bi-Rads:");
		contentPane.add(birads);
		birads.setBounds(225, 300, 150, 30);
		// Combo Box para o campo bi rads
		JComboBox<String> combo_Birads = new JComboBox<String>();
		contentPane.add(combo_Birads);
		combo_Birads.setBounds(290, 300, 150, 30);
		combo_Birads.addItem("1");
		combo_Birads.addItem("2");
		combo_Birads.addItem("3");
		combo_Birads.addItem("4");
		combo_Birads.addItem("5");
		// Combo para campo forma
		JLabel forma = new JLabel("Forma:");
		contentPane.add(forma);
		forma.setBounds(225, 350, 150, 30);
		JComboBox<String> combo_forma = new JComboBox<String>();
		contentPane.add(combo_forma);
		combo_forma.setBounds(290, 350, 150, 30);
		combo_forma.addItem("1- Redonda");
		combo_forma.addItem("2- Oval");
		combo_forma.addItem("3- Lobular");
		combo_forma.addItem("4- Irregular");
		// combo para margem
		JLabel margem = new JLabel("Margem:");
		contentPane.add(margem);
		margem.setBounds(225, 400, 150, 30);
		JComboBox<String> combo_margem = new JComboBox<String>();
		contentPane.add(combo_margem);
		combo_margem.setBounds(290, 400, 150, 30);
		combo_margem.addItem("1- Circuscrito");
		combo_margem.addItem("2- Microlobulado");
		combo_margem.addItem("3- Obscurecido");
		combo_margem.addItem("4- Mal definido");
		combo_margem.addItem("5- Espiculados");
		// combo para densidade
		JLabel densidade = new JLabel("Densidade:");
		contentPane.add(densidade);
		densidade.setBounds(225, 450, 150, 30);
		JComboBox<String> combo_densidade = new JComboBox<String>();
		contentPane.add(combo_densidade);
		combo_densidade.setBounds(290, 450, 150, 30);
		combo_densidade.addItem("1- Alta");
		combo_densidade.addItem("2- Baixa");
		combo_densidade.addItem("3- Iso");
		combo_densidade.addItem("4- Contendo gordura");

		JLabel lblResultadoTreinamento = new JLabel("Log de Treinamento:");
		contentPane.add(lblResultadoTreinamento);
		lblResultadoTreinamento.setBounds(540, 130, 200, 100);
		contentPane.setBackground(new java.awt.Color(255, 255, 255));



		JButton diagnostico = new JButton("Obter Diagnóstico");
		contentPane.add(diagnostico);
		diagnostico.setBounds(290, 500, 150, 28);
		diagnostico.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (rede.isTreinou()) {
					// MANDAR CLASSIFICAR
					int combo1 = combo_Birads.getSelectedIndex();
					int combo2 = combo_forma.getSelectedIndex();
					int combo3 = combo_margem.getSelectedIndex();
					int combo4 = combo_densidade.getSelectedIndex();
					String result = rede.classificar(new double[] { combo1, combo2, combo3, combo4, 1 }); // 0
					JOptionPane.showMessageDialog(null, result);
				} else {
					/// COLOCAR MNESAGEM QUE PRECISAR TREINAR 1º
					JOptionPane.showMessageDialog(null, "Base de dados sem treinar!");
				}
			}
		});
        limpar = new JButton("Limpar");
        contentPane.add(limpar);
        limpar.setBounds(670, 135, 75, 30);
        limpar.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 textArea.setText("");
				 
			}
		});

		JButton treinar = new JButton("Treinar");
		contentPane.add(treinar);
		treinar.setBounds(590, 135, 75, 30);
		//treinar.setBackground(new java.awt.Color(253, 153, 203));
		treinar.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Defenido matriz com os dados do arquivo "mamografia.csv"
				double[][] conjuntoTreinamento = null;
				try {
					conjuntoTreinamento = rede.LerConjuntoTreinamento();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// Defenido vetor com os dados do arquivo "resultados.csv"
				double[] valoresEsperados = null;
				try {
					valoresEsperados = rede.LerValoresEsperados();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Fazendo o treinamento da Rede Neural

				///System.out.println("Treinando...");
				String result = rede.treinar(conjuntoTreinamento, valoresEsperados);

				textArea.setText(result);
				// textArea.setText(erro);

			}
		});

	}

}

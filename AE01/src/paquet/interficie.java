package paquet;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class interficie extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textDirectori;
    private JTextArea txtArea;
    private JRadioButton rdbtnAscendent;
    private JComboBox<String> comboBoxCriteri;
    private JTextField textStringABuscar;
    private JButton btnBuscar;
    private JTextArea txtResultats;

    /**
     * Este és el main que s'encarrega de llançar la interfície grafica
     * @param args Els arguments de la línia de comandos
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    interficie frame = new interficie();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Son tots els elements que es creen en la interfície grafica.
     */
    public interficie() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1054, 767);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textDirectori = new JTextField();
        textDirectori.setBounds(40, 51, 339, 19);
        contentPane.add(textDirectori);
        textDirectori.setColumns(10);

        rdbtnAscendent = new JRadioButton("Ascendent");
        rdbtnAscendent.setSelected(true);
        rdbtnAscendent.setBounds(177, 99, 100, 25);
        contentPane.add(rdbtnAscendent);

        JRadioButton rdbtnDescendent = new JRadioButton("Descendent");
        rdbtnDescendent.setBounds(279, 99, 100, 25);
        contentPane.add(rdbtnDescendent);

        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnAscendent);
        group.add(rdbtnDescendent);

        comboBoxCriteri = new JComboBox<String>();
        comboBoxCriteri.setModel(new DefaultComboBoxModel<String>(new String[] { "Nom", "Grandària", "Data de Modificació" }));
        comboBoxCriteri.setSelectedIndex(0);
        comboBoxCriteri.setBounds(10, 99, 150, 25);
        contentPane.add(comboBoxCriteri);
        

        ImageIcon icon = new ImageIcon("c:\\Users\\Alberto\\eclipse-workspace\\AE01\\carpeta.png");
        Image img = icon.getImage();
        Image newImage = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon iconMod = new ImageIcon(newImage);
        JButton btnSeleccionarDirectorio = new JButton();
        btnSeleccionarDirectorio.setIcon(iconMod);
        btnSeleccionarDirectorio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    textDirectori.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
        btnSeleccionarDirectorio.setFont(new Font("Tahoma", Font.PLAIN, 19));
        btnSeleccionarDirectorio.setBounds(10, 50, 20, 20);
        contentPane.add(btnSeleccionarDirectorio);

        JButton btnFiltroTxt = new JButton("Filtrar");
        btnFiltroTxt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filtrarArchivosTxt();
            }
        });
        btnFiltroTxt.setFont(new Font("Tahoma", Font.PLAIN, 19));
        btnFiltroTxt.setBounds(10, 159, 132, 33);
        contentPane.add(btnFiltroTxt);

        txtArea = new JTextArea();
        txtArea.setBounds(405, 45, 625, 675);
        contentPane.add(txtArea);

        textStringABuscar = new JTextField();
        textStringABuscar.setBounds(10, 205, 371, 25);
        contentPane.add(textStringABuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarCoincidencias();
            }
        });
        btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 19));
        btnBuscar.setBounds(10, 245, 100, 33);
        contentPane.add(btnBuscar);

        txtResultats = new JTextArea();
        txtResultats.setBounds(10, 301, 371, 283);
        contentPane.add(txtResultats);

        JButton btnSeleccionarFitxers = new JButton("Seleccionar Fitxers a Fusionar");
        btnSeleccionarFitxers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seleccionarFitxersAFusionar();
            }
        });
        btnSeleccionarFitxers.setFont(new Font("Tahoma", Font.PLAIN, 19));
        btnSeleccionarFitxers.setBounds(33, 622, 300, 33);
        contentPane.add(btnSeleccionarFitxers);
    }

    /**
     * Este mètode filtra i mostra informació sobre arxius amb extensió .txt en el directori especificat.
     * La informació inclou el nom, grandària, extensió i data d'última modificació dels arxius.
     */
    private void filtrarArchivosTxt() {
        File directorio = new File(textDirectori.getText());
        File[] listaArchivos = directorio.listFiles(file -> file.getName().endsWith(".txt"));

        if (listaArchivos != null) {
            String selectedCriterion = (String) comboBoxCriteri.getSelectedItem();

            ArrayList<File> listaArchivosArrayList = new ArrayList<>(Arrays.asList(listaArchivos));
            ordenarArchivos(listaArchivosArrayList, selectedCriterion);

            StringBuilder archivosTexto = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            for (File archivo : listaArchivosArrayList) {
                archivosTexto.append("Nom: " + archivo.getName()).append(" | ");
                archivosTexto.append("Tamany: " + archivo.length()).append(" Bytes | ");
                archivosTexto.append("Extensió: " + archivo.getName().substring(archivo.getName().lastIndexOf("."))).append(" | ");
                archivosTexto.append("Última modificació: " + dateFormat.format(new Date(archivo.lastModified()))).append("\n\n");
            }

            txtArea.setText(archivosTexto.toString());
        } else {
            txtArea.setText("No se encontraron archivos con la extensión .txt en el directorio indicado");
        }
    }

    /**
     * Este mètode busca coincidències d'un text específic en arxius amb extensió .txt en el directori indicat.
     * Si el camp de text 'stringABuscar' està buit, mostra una alerta, mostra els resultats en 'txtResultats'.
     */
    private void buscarCoincidencias() {
        String directoriPath = textDirectori.getText();
        String stringABuscar = textStringABuscar.getText();
        File directori = new File(directoriPath);
        File[] listaArchivos = directori.listFiles(file -> file.getName().endsWith(".txt"));
        if (textStringABuscar.getText().equals("")) {
        	 JOptionPane.showMessageDialog(null, "NO puede estar vacio.", "Alerta", JOptionPane.WARNING_MESSAGE);
        } else {
        	if (listaArchivos != null) {
                StringBuilder resultats = new StringBuilder();

                for (File archivo : listaArchivos) {
                    int coincidencies = comptarCoincidencies(archivo, stringABuscar);
                    resultats.append("Fitxer: ").append(archivo.getName()).append(" -> ").append(coincidencies).append(" coincidències\n");
                }

                txtResultats.setText(resultats.toString());
            } else {
                txtResultats.setText("No s'han trobat fitxers amb l'extensió .txt al directori indicat.");
            }
        }
        
    }

    /**
     * Este mètode compta el nombre de coincidències d'una cadena específica en un arxiu.
     * 
     * @param fitxer L'arxiu en el qual es buscaran les coincidències.
     * @param stringABuscar La cadena que es busca en l'arxiu.
     * @return El nombre de coincidències de la cadena en l'arxiu.
     */
    private int comptarCoincidencies(File fitxer, String stringABuscar) {
        int coincidencies = 0;

        try {
            Scanner scanner = new Scanner(fitxer);

            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                int index = linia.indexOf(stringABuscar);
                while (index != -1) {
                    coincidencies++;
                    index = linia.indexOf(stringABuscar, index + 1);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return coincidencies;
    }
    /**
     * Este mètode ordena una llista d'arxius segons el criteri seleccionat.
     * 
     * @param listaArchivos La llista d'arxius que s'ordenarà.
     * @param selectedCriterion El criteri d'ordenació seleccionat "Nom", "Grandària" o "Data de Modificació".
     */
    private void ordenarArchivos(ArrayList<File> listaArchivos, String selectedCriterion) {
        if ("Nom".equals(selectedCriterion)) {
            listaArchivos.sort((file1, file2) -> file1.getName().compareTo(file2.getName()));
        } else if ("Grandària".equals(selectedCriterion)) {
            listaArchivos.sort((file1, file2) -> Long.compare(file1.length(), file2.length()));
        } else if ("Data de Modificació".equals(selectedCriterion)) {
            listaArchivos.sort((file1, file2) -> Long.compare(file1.lastModified(), file2.lastModified()));
        }

        if (!rdbtnAscendent.isSelected()) {
            ArrayList<File> reversedList = new ArrayList<>(listaArchivos);
            Collections.reverse(reversedList);
            listaArchivos.clear();
            listaArchivos.addAll(reversedList);
        }
    }

    /**
     * Este mètode permet a l'usuari seleccionar múltiples arxius per a fusionar-los.
     * Utilitza un explorador d'arxius i permet a l'usuari agregar
     * més arxius a la selecció. Els arxius seleccionats s'emmagatzemen en un array.
     */
    private void seleccionarFitxersAFusionar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        File[] selectedFiles = null;

        while (true) {
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                if (selectedFiles == null) {
                    selectedFiles = fileChooser.getSelectedFiles();
                } else {
                    File[] newSelectedFiles = fileChooser.getSelectedFiles();
                    File[] combinedFiles = new File[selectedFiles.length + newSelectedFiles.length];
                    System.arraycopy(selectedFiles, 0, combinedFiles, 0, selectedFiles.length);
                    System.arraycopy(newSelectedFiles, 0, combinedFiles, selectedFiles.length, newSelectedFiles.length);
                    selectedFiles = combinedFiles;
                }
                JOptionPane.showMessageDialog(this, "Selecciona más archivos o cancela para fusionarlos.");
            } else {
                break;
            }
        }

        if (selectedFiles != null && selectedFiles.length > 1) {
            String nomFitxerNou = JOptionPane.showInputDialog(this, "Introdueix el nom del fitxer nou:");

            if (nomFitxerNou != null && !nomFitxerNou.isEmpty()) {
                File directori = new File(textDirectori.getText());
                File fitxerNou = new File(directori, nomFitxerNou + ".txt");

                if (fitxerNou.exists()) {
                    int confirmacio = JOptionPane.showConfirmDialog(this, "El fitxer ja existeix. Vols sobreescriure'l?");
                    if (confirmacio != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try (FileOutputStream fos = new FileOutputStream(fitxerNou);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                    for (File selectedFile : selectedFiles) {
                        try (FileInputStream fis = new FileInputStream(selectedFile);
                             BufferedInputStream bis = new BufferedInputStream(fis)) {

                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = bis.read(buffer)) != -1) {
                            	bos.write(buffer, 0, bytesRead); 
                            	bos.write(" ".getBytes()); 
                            }
                        }                 
                    }

                    JOptionPane.showMessageDialog(this, "Fitxers fusionats amb èxit.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error en fusionar els fitxers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }



}


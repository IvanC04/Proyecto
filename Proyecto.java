import javax.swing.*;  
import java.awt.*;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.*;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Map;  

public class Main {  

    private static Map<String, ArrayList<String>> listas = new HashMap<>();  
  
    private static String listaActual = null;  

    public static void main(String[] args) {  
    
        JFrame frame = new JFrame("Reproductor de Música");  
        frame.setSize(600, 400);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setLayout(new BorderLayout());  

   
        JPanel listasPanel = new JPanel();  
        listasPanel.setLayout(new FlowLayout());  

        JLabel listasLabel = new JLabel("Listas:");  
        JComboBox<String> listasComboBox = new JComboBox<>();  
        JButton crearListaButton = new JButton("Nueva Lista");  

    
        listasPanel.add(listasLabel);  
        listasPanel.add(listasComboBox);  
        listasPanel.add(crearListaButton);  

        DefaultListModel<String> listModel = new DefaultListModel<>();  
        JList<String> artistList = new JList<>(listModel);  
        JScrollPane scrollPane = new JScrollPane(artistList);  


        JPanel controlPanel = new JPanel();  
        controlPanel.setLayout(new FlowLayout()); 

        JTextField addField = new JTextField(15);  
        JButton addButton = new JButton("Agregar Artista");  
        JButton loadButton = new JButton("Cargar desde archivo");  
        JButton saveButton = new JButton("Guardar lista");  

    
        controlPanel.add(new JLabel("Nuevo artista:"));  
        controlPanel.add(addField);  
        controlPanel.add(addButton);  
        controlPanel.add(loadButton);  
        controlPanel.add(saveButton);  
 
        crearListaButton.addActionListener(new ActionListener() {  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                String nombreLista = JOptionPane.showInputDialog(frame, "Ingresa el nombre de la nueva lista:");  
                if (nombreLista != null && !nombreLista.trim().isEmpty()) {  
                    listas.put(nombreLista, new ArrayList<>());  
                    listasComboBox.addItem(nombreLista);  
                    listasComboBox.setSelectedItem(nombreLista);  
                    listaActual = nombreLista;  
                    listModel.clear();  
                    JOptionPane.showMessageDialog(frame, "Nueva lista creada: " + nombreLista);  
                }  
            }  
        });  

        listasComboBox.addActionListener(new ActionListener() {  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                listaActual = (String) listasComboBox.getSelectedItem();  
                ArrayList<String> artistas = listas.get(listaActual);  
                listModel.clear();  
                for (String artista : artistas) {  
                    listModel.addElement(artista);  
                }  
            }  
        });  


        addButton.addActionListener(new ActionListener() {  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                String nuevoArtista = addField.getText();  
                if (nuevoArtista.isEmpty()) {  
                    JOptionPane.showMessageDialog(frame, "Por favor ingresa el nombre de un artista.");  
                    return;  
                }  
                if (listaActual == null) {  
                    JOptionPane.showMessageDialog(frame, "Debes seleccionar o crear una lista primero.");  
                    return;  
                }  
                ArrayList<String> artistas = listas.get(listaActual);  
                artistas.add(nuevoArtista);  
                listModel.addElement(nuevoArtista);  
                addField.setText("");  
                JOptionPane.showMessageDialog(frame, "Artista agregado: " + nuevoArtista);  
            }  
        });  


        saveButton.addActionListener(new ActionListener() {  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                if (listaActual == null) {  
                    JOptionPane.showMessageDialog(frame, "Debes seleccionar una lista primero.");  
                    return;  
                }  
                ArrayList<String> artistas = listas.get(listaActual);  
                if (artistas.isEmpty()) {  
                    JOptionPane.showMessageDialog(frame, "No hay artistas en la lista " + listaActual + " para guardar.");  
                    return;  
                }  
                JFileChooser fileChooser = new JFileChooser();  
                int returnValue = fileChooser.showSaveDialog(frame);  
                if (returnValue == JFileChooser.APPROVE_OPTION) {  
                    File file = fileChooser.getSelectedFile();  
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {  
                        bw.write("Lista: " + listaActual);  
                        bw.newLine();  
                        for (String artista : artistas) {  
                            bw.write(artista);  
                            bw.newLine();  
                        }  
                        JOptionPane.showMessageDialog(frame, "Artistas de la lista " + listaActual + " guardados en el archivo.");  
                    } catch (IOException ex) {  
                        JOptionPane.showMessageDialog(frame, "Error al guardar el archivo: " + ex.getMessage());  
                    }  
                }  
            }  
        });  

        frame.add(listasPanel, BorderLayout.NORTH);  
        frame.add(scrollPane, BorderLayout.CENTER);  
        frame.add(controlPanel, BorderLayout.SOUTH); 
 
        frame.setVisible(true);  
    }  
}
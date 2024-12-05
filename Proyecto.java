import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class Proyecto {

   
    static class Artista {
        String nombre;
        String genero;
        int añoFormacion;
        ArrayList<Cancion> canciones;

        public Artista(String nombre) {
            this.nombre = nombre;
            this.genero = "";
            this.añoFormacion = 0;
            this.canciones = new ArrayList<>();
        }

        @Override
        public String toString() {
            return nombre + (genero.isEmpty() ? "" : " (" + genero + ")");
        }
    }


    static class Cancion {
        String titulo;
        String genero;
        String fechaLanzamiento;

        public Cancion(String titulo, String genero, String fechaLanzamiento) {
            this.titulo = titulo;
            this.genero = genero;
            this.fechaLanzamiento = fechaLanzamiento;
        }

        @Override
        public String toString() {
            return titulo + " (" + genero + ", " + fechaLanzamiento + ")";
        }
    }

    private static Map<String, ArrayList<Artista>> listas = new HashMap<>();
    private static String listaActual = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reproductor de Música");
        frame.setSize(800, 600);
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


        DefaultListModel<Artista> artistListModel = new DefaultListModel<>();
        JList<Artista> artistList = new JList<>(artistListModel);
        JScrollPane artistScrollPane = new JScrollPane(artistList);


        DefaultListModel<Cancion> cancionListModel = new DefaultListModel<>();
        JList<Cancion> cancionList = new JList<>(cancionListModel);
        JScrollPane cancionScrollPane = new JScrollPane(cancionList);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JTextField addField = new JTextField(15);
        JButton addButton = new JButton("Agregar Artista");
        JButton editInfoButton = new JButton("Editar Información Artista");
        JButton addCancionButton = new JButton("Agregar Canción");

        controlPanel.add(new JLabel("Nuevo artista:"));
        controlPanel.add(addField);
        controlPanel.add(addButton);
        controlPanel.add(editInfoButton);
        controlPanel.add(addCancionButton);


        crearListaButton.addActionListener(e -> {
            String nombreLista = JOptionPane.showInputDialog(frame, "Ingresa el nombre de la nueva lista:");
            if (nombreLista != null && !nombreLista.trim().isEmpty()) {
                listas.put(nombreLista, new ArrayList<>());
                listasComboBox.addItem(nombreLista);
                listasComboBox.setSelectedItem(nombreLista);
                listaActual = nombreLista;
                artistListModel.clear();
                cancionListModel.clear();
                JOptionPane.showMessageDialog(frame, "Nueva lista creada: " + nombreLista);
            }
        });


        listasComboBox.addActionListener(e -> {
            listaActual = (String) listasComboBox.getSelectedItem();
            ArrayList<Artista> artistas = listas.get(listaActual);
            artistListModel.clear();
            cancionListModel.clear();
            if (artistas != null) {
                for (Artista artista : artistas) {
                    artistListModel.addElement(artista);
                }
            }
        });


        addButton.addActionListener(e -> {
            String nuevoArtista = addField.getText();
            if (nuevoArtista.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor ingresa el nombre de un artista.");
                return;
            }
            if (listaActual == null) {
                JOptionPane.showMessageDialog(frame, "Debes seleccionar o crear una lista primero.");
                return;
            }
            Artista artista = new Artista(nuevoArtista);
            listas.get(listaActual).add(artista);
            artistListModel.addElement(artista);
            addField.setText("");
            JOptionPane.showMessageDialog(frame, "Artista agregado: " + nuevoArtista);
        });


        editInfoButton.addActionListener(e -> {
            Artista artistaSeleccionado = artistList.getSelectedValue();
            if (artistaSeleccionado == null) {
                JOptionPane.showMessageDialog(frame, "Selecciona un artista primero.");
                return;
            }

            JTextField generoField = new JTextField(artistaSeleccionado.genero);
            JTextField añoField = new JTextField(String.valueOf(artistaSeleccionado.añoFormacion));

            JPanel infoPanel = new JPanel(new GridLayout(0, 2));
            infoPanel.add(new JLabel("Género del artista:"));
            infoPanel.add(generoField);
            infoPanel.add(new JLabel("Año de formación:"));
            infoPanel.add(añoField);

            int result = JOptionPane.showConfirmDialog(frame, infoPanel, "Editar Información del Artista", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                artistaSeleccionado.genero = generoField.getText();
                artistaSeleccionado.añoFormacion = Integer.parseInt(añoField.getText());
                artistList.repaint();
                JOptionPane.showMessageDialog(frame, "Información del artista actualizada.");
            }
        });


        addCancionButton.addActionListener(e -> {
            Artista artistaSeleccionado = artistList.getSelectedValue();
            if (artistaSeleccionado == null) {
                JOptionPane.showMessageDialog(frame, "Selecciona un artista primero.");
                return;
            }

            JTextField tituloField = new JTextField();
            JTextField generoField = new JTextField();
            JTextField fechaField = new JTextField();

            JPanel cancionPanel = new JPanel(new GridLayout(0, 2));
            cancionPanel.add(new JLabel("Título de la canción:"));
            cancionPanel.add(tituloField);
            cancionPanel.add(new JLabel("Género de la canción:"));
            cancionPanel.add(generoField);
            cancionPanel.add(new JLabel("Fecha de lanzamiento:"));
            cancionPanel.add(fechaField);

            int result = JOptionPane.showConfirmDialog(frame, cancionPanel, "Agregar Canción", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String titulo = tituloField.getText();
                String genero = generoField.getText();
                String fechaLanzamiento = fechaField.getText();

                if (titulo.isEmpty() || genero.isEmpty() || fechaLanzamiento.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Por favor completa todos los campos.");
                    return;
                }

                Cancion nuevaCancion = new Cancion(titulo, genero, fechaLanzamiento);
                artistaSeleccionado.canciones.add(nuevaCancion);
                cancionListModel.addElement(nuevaCancion);
                JOptionPane.showMessageDialog(frame, "Canción agregada: " + titulo);
            }
        });

        frame.add(listasPanel, BorderLayout.NORTH);
        frame.add(artistScrollPane, BorderLayout.WEST);
        frame.add(cancionScrollPane, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}

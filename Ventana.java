import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.Year;
import java.util.ArrayList;

public class Ventana {
    private JPanel principal;
    private JTabbedPane tabbedPane1;
    private JTextField txtIsbn;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JComboBox<String> cboCategoria;
    private JSpinner spiAnio;
    private JButton btnRegistrar;
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JButton btnBuscarTexto;
    private JTextField txtBuscarTexto;
    private JLabel lblCoincidencias;
    private JList<Libro> lstListar;
    private JButton EDITARButton;
    private JLabel lblMeGusta;
    private JButton MEGUSTAButton;
    private JLabel lblTotalLibros;
    private JList<Libro> lstRanking;
    private JButton btnActualizarRanking;

    private final Lista biblioteca = new Lista();
    private TipoBusqueda ultimaBusqueda = TipoBusqueda.NINGUNA;
    private String ultimoTextoBuscado;

    private enum TipoBusqueda {
        NINGUNA, CODIGO, TEXTO
    }

    private static final String[] GENEROS = {
            "Programación", "Bases de datos", "Redes", "Seguridad",
            "Sistemas", "Software", "IA", "Gestión"
    };

    public Ventana() {
        cboCategoria.setModel(new DefaultComboBoxModel<>(GENEROS));
        int anioActual = Year.now().getValue();
        spiAnio.setModel(new SpinnerNumberModel(anioActual, 1, anioActual, 1));
        txtBuscarCodigo.setText("");

        lstListar.setModel(new DefaultListModel<>());
        lstRanking.setModel(new DefaultListModel<>());
        EDITARButton.setEnabled(false);
        MEGUSTAButton.setEnabled(false);

        actualizarTotalLibros();
        actualizarEtiquetaMeGusta(null);

        btnRegistrar.addActionListener(e -> registrarLibro());
        btnBuscarCodigo.addActionListener(e -> buscarPorCodigo());
        btnBuscarTexto.addActionListener(e -> buscarPorTexto());
        EDITARButton.addActionListener(e -> editarLibroSeleccionado());
        MEGUSTAButton.addActionListener(e -> darMeGustaAlSeleccionado());
        btnActualizarRanking.addActionListener(e -> actualizarRanking());

        lstListar.addListSelectionListener(this::cambioSeleccion);
        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedIndex() == 2) {
                actualizarRanking();
            }
        });

        actualizarRanking();
    }

    private void registrarLibro() {
        try {
            String textoCodigo = txtIsbn.getText().trim();
            if (textoCodigo.isEmpty()) {
                throw new IllegalArgumentException("El código es obligatorio.");
            }

            int codigo = Integer.parseInt(textoCodigo);
            Object seleccion = cboCategoria.getSelectedItem();
            String genero = seleccion == null ? "" : seleccion.toString();
            int anio = ((Number) spiAnio.getValue()).intValue();

            biblioteca.registrar(
                    codigo,
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    genero,
                    anio
            );
            actualizarTotalLibros();
            actualizarRanking();
            JOptionPane.showMessageDialog(principal, "Libro registrado.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(principal,
                    "El código debe ser un número entero.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(principal, ex.getMessage(),
                    "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarPorCodigo() {
        String textoCodigo = txtBuscarCodigo.getText().trim();
        if (textoCodigo.isEmpty()) {
            limpiarResultados();
            JOptionPane.showMessageDialog(principal, "Ingresa un código para buscar.",
                    "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int codigo = Integer.parseInt(textoCodigo);
            ultimaBusqueda = TipoBusqueda.CODIGO;
            ultimoTextoBuscado = textoCodigo;
            ArrayList<Libro> resultados = new ArrayList<>();
            int indice = biblioteca.binario(codigo);
            if (indice >= 0) {
                resultados.add(biblioteca.obtener().get(indice));
            }
            mostrarResultados(resultados, null);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(principal,
                        "No se encontró un libro con el código " + codigo + ".",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            limpiarResultados();
            JOptionPane.showMessageDialog(principal,
                    "El código debe ser un número entero.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarPorTexto() {
        String texto = txtBuscarTexto.getText();
        try {
            ArrayList<Libro> resultados = biblioteca.buscarCoincidencias(texto);
            ultimaBusqueda = TipoBusqueda.TEXTO;
            ultimoTextoBuscado = texto;
            mostrarResultados(resultados, null);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(principal,
                        "No se encontraron coincidencias.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            ultimaBusqueda = TipoBusqueda.NINGUNA;
            ultimoTextoBuscado = null;
            limpiarResultados();
            JOptionPane.showMessageDialog(principal, ex.getMessage(),
                    "Búsqueda inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarResultados(ArrayList<Libro> libros, Integer codigoSeleccionado) {
        DefaultListModel<Libro> modelo = new DefaultListModel<>();
        for (Libro libro : libros) {
            modelo.addElement(libro);
        }
        lstListar.setModel(modelo);
        lblCoincidencias.setText("Coincidencias: " + modelo.size());
        lstListar.clearSelection();

        if (codigoSeleccionado != null) {
            for (int i = 0; i < modelo.size(); i++) {
                if (modelo.get(i).getCodigo() == codigoSeleccionado) {
                    lstListar.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (lstListar.getSelectedIndex() < 0) {
            actualizarEtiquetaMeGusta(null);
            EDITARButton.setEnabled(false);
            MEGUSTAButton.setEnabled(false);
        }
    }

    private void limpiarResultados() {
        mostrarResultados(new ArrayList<>(), null);
    }

    private void cambioSeleccion(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting()) {
            Libro seleccionado = lstListar.getSelectedValue();
            actualizarEtiquetaMeGusta(seleccionado);
            EDITARButton.setEnabled(seleccionado != null);
            MEGUSTAButton.setEnabled(seleccionado != null);
        }
    }

    private void actualizarEtiquetaMeGusta(Libro libro) {
        lblMeGusta.setText(libro == null
                ? "Me gusta: 0"
                : "Me gusta: " + libro.getMeGusta());
    }

    private void editarLibroSeleccionado() {
        Libro seleccionado = lstListar.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(principal, "Selecciona un libro para editar.",
                    "Sin selección", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Window ventana = SwingUtilities.getWindowAncestor(principal);
        Frame propietario = ventana instanceof Frame ? (Frame) ventana : null;
        JDialog dialogo = new JDialog(propietario, "Editar libro", true);
        JTextField campoTitulo = new JTextField(seleccionado.getTitulo(), 20);
        JTextField campoAutor = new JTextField(seleccionado.getAutor(), 20);
        JComboBox<String> comboGenero = new JComboBox<>(GENEROS);
        comboGenero.setSelectedItem(seleccionado.getGenero());
        JSpinner selectorAnio = new JSpinner(new SpinnerNumberModel(
                seleccionado.getAnio(), 1, Year.now().getValue(), 1));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Código:"));
        formulario.add(new JLabel(String.valueOf(seleccionado.getCodigo())));
        formulario.add(new JLabel("Título:"));
        formulario.add(campoTitulo);
        formulario.add(new JLabel("Autor:"));
        formulario.add(campoAutor);
        formulario.add(new JLabel("Género:"));
        formulario.add(comboGenero);
        formulario.add(new JLabel("Año:"));
        formulario.add(selectorAnio);

        JButton botonGuardar = new JButton("Guardar cambios");
        JButton botonCancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(botonGuardar);
        botones.add(botonCancelar);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        dialogo.setContentPane(contenido);
        dialogo.getRootPane().setDefaultButton(botonGuardar);
        dialogo.pack();
        dialogo.setLocationRelativeTo(principal);

        botonGuardar.addActionListener(e -> {
            try {
                Object generoSeleccionado = comboGenero.getSelectedItem();
                biblioteca.editar(
                        seleccionado.getCodigo(),
                        campoTitulo.getText(),
                        campoAutor.getText(),
                        generoSeleccionado == null ? "" : generoSeleccionado.toString(),
                        ((Number) selectorAnio.getValue()).intValue()
                );
                dialogo.dispose();
                refrescarBusqueda(seleccionado.getCodigo());
                actualizarRanking();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, ex.getMessage(),
                        "No se pudo guardar", JOptionPane.WARNING_MESSAGE);
            }
        });
        botonCancelar.addActionListener(e -> dialogo.dispose());
        dialogo.setVisible(true);
    }

    private void refrescarBusqueda(int codigoSeleccionado) {
        if (ultimaBusqueda == TipoBusqueda.CODIGO) {
            buscarResultadosPorCodigo(Integer.parseInt(ultimoTextoBuscado),
                    codigoSeleccionado);
        } else if (ultimaBusqueda == TipoBusqueda.TEXTO) {
            try {
                ArrayList<Libro> resultados =
                        biblioteca.buscarCoincidencias(ultimoTextoBuscado);
                mostrarResultados(resultados, codigoSeleccionado);
            } catch (IllegalArgumentException ex) {
                ultimaBusqueda = TipoBusqueda.NINGUNA;
                ultimoTextoBuscado = null;
                limpiarResultados();
            }
        }
    }

    private void buscarResultadosPorCodigo(int codigo, int codigoSeleccionado) {
        ArrayList<Libro> resultados = new ArrayList<>();
        int indice = biblioteca.binario(codigo);
        if (indice >= 0) {
            resultados.add(biblioteca.obtener().get(indice));
        }
        mostrarResultados(resultados, codigoSeleccionado);
    }

    private void darMeGustaAlSeleccionado() {
        Libro seleccionado = lstListar.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(principal, "Selecciona un libro primero.",
                    "Sin selección", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int codigo = seleccionado.getCodigo();
            biblioteca.darMeGusta(codigo);
            refrescarBusqueda(codigo);
            actualizarRanking();
        } catch (Exception ex) {
            limpiarResultados();
            JOptionPane.showMessageDialog(principal, ex.getMessage(),
                    "No se pudo actualizar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarTotalLibros() {
        lblTotalLibros.setText("Total de libros: " + biblioteca.obtener().size());
    }

    private void actualizarRanking() {
        DefaultListModel<Libro> modelo = new DefaultListModel<>();
        for (Libro libro : biblioteca.obtenerRanking()) {
            modelo.addElement(libro);
        }
        lstRanking.setModel(modelo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BiblioLikes");
            frame.setContentPane(new Ventana().principal);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

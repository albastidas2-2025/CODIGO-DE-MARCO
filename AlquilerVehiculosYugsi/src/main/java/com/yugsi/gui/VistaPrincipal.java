package com.yugsi.gui;

import com.yugsi.db.ConexionMongo;
import com.yugsi.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaPrincipal extends JFrame {
    private Agencia agencia;
    private ConexionMongo conexionMongo;

    // Componentes de la interfaz
    private JTextField txtDni, txtNombre, txtLicencia, txtTelefono, txtDias;
    private JComboBox<Vehiculo> comboVehiculos;
    private JTextArea txtInfoVehiculo;
    private JButton btnRegistrarAlquiler, btnLimpiar, btnMostrarInfo;
    private JLabel lblEstado;

    public VistaPrincipal() {

        this.agencia = new Agencia("DevRentalYugsi");

        // Inicializar conexión a MongoDB
        this.conexionMongo = new ConexionMongo();

        // Configurar ventana
        setTitle("DevRental - Sistema de Alquiler de Vehículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);

        // Inicializar componentes
        initComponents();

        // Organizar interfaz
        organizeComponents();

        // Cargar vehículos disponibles
        cargarVehiculosDisponibles();
    }

    private void initComponents() {
        // Campos para datos del cliente
        txtDni = new JTextField(15);
        txtNombre = new JTextField(15);
        txtLicencia = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtDias = new JTextField(5);

        // ComboBox para vehículos
        comboVehiculos = new JComboBox<>();
        comboVehiculos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Vehiculo) {
                    Vehiculo v = (Vehiculo) value;
                    String tipo = v instanceof Automovil ? "Auto" : "Moto";
                    value = String.format("%s %s (%s) - $%.2f/día",
                            v.getMarca(), v.getModelo(), tipo, v.getPrecioPorDia());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Área de información
        txtInfoVehiculo = new JTextArea(8, 40);
        txtInfoVehiculo.setEditable(false);
        txtInfoVehiculo.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Botones principales
        btnRegistrarAlquiler = new JButton("REGISTRAR ALQUILER");
        btnRegistrarAlquiler.setBackground(new Color(0, 120, 0));
        btnRegistrarAlquiler.setForeground(Color.BLACK);
        btnRegistrarAlquiler.setFont(new Font("Arial", Font.BOLD, 14));

        btnLimpiar = new JButton("Limpiar Formulario");
        btnMostrarInfo = new JButton("Ver Detalles");

        // Etiqueta de estado
        lblEstado = new JLabel(" Listo para registrar alquiler ");
        lblEstado.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(240, 240, 240));

        // Configurar eventos
        btnRegistrarAlquiler.addActionListener(e -> registrarAlquiler());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnMostrarInfo.addActionListener(e -> mostrarInformacionVehiculo());
        comboVehiculos.addActionListener(e -> mostrarInformacionVehiculo());
    }

    private void organizeComponents() {
        // Usar BorderLayout con paneles organizados
        setLayout(new BorderLayout(10, 10));

        // Panel norte: Título
        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel(" REGISTRO DE ALQUILER-DEVRENTAL YUGSI");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel centro: Contenido principal
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel izquierdo: Datos del cliente
        JPanel panelCliente = new JPanel(new GridLayout(5, 2, 5, 10));
        panelCliente.setBorder(BorderFactory.createTitledBorder("👤 DATOS DEL CLIENTE"));
        panelCliente.add(new JLabel("DNI:"));
        panelCliente.add(txtDni);
        panelCliente.add(new JLabel("Nombre Completo:"));
        panelCliente.add(txtNombre);
        panelCliente.add(new JLabel("Licencia de Conducir:"));
        panelCliente.add(txtLicencia);
        panelCliente.add(new JLabel("Teléfono:"));
        panelCliente.add(txtTelefono);
        panelCliente.add(new JLabel("Días de Alquiler:"));
        panelCliente.add(txtDias);

        // Panel derecho: Selección de vehículo
        JPanel panelVehiculo = new JPanel(new BorderLayout(5, 5));
        panelVehiculo.setBorder(BorderFactory.createTitledBorder("SELECCIÓN DE VEHÍCULO"));

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSeleccion.add(new JLabel("Vehículo Disponible:"));
        panelSeleccion.add(comboVehiculos);
        panelSeleccion.add(btnMostrarInfo);

        JScrollPane scrollInfo = new JScrollPane(txtInfoVehiculo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Detalles del Vehículo"));

        panelVehiculo.add(panelSeleccion, BorderLayout.NORTH);
        panelVehiculo.add(scrollInfo, BorderLayout.CENTER);

        // Combinar paneles izquierdo y derecho
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCliente, panelVehiculo);
        splitPane.setResizeWeight(0.4);
        panelCentro.add(splitPane, BorderLayout.CENTER);

        // Panel sur: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnRegistrarAlquiler);
        panelBotones.add(btnLimpiar);

        panelCentro.add(panelBotones, BorderLayout.SOUTH);

        add(panelCentro, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    private void cargarVehiculosDisponibles() {
        // Limpiar comboBox
        comboVehiculos.removeAllItems();

        // Cargar solo vehículos disponibles
        java.util.List<Vehiculo> disponibles = agencia.getVehiculosDisponibles();

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos disponibles en este momento",
                    "Inventario Vacío", JOptionPane.WARNING_MESSAGE);
        } else {
            disponibles.forEach(v -> comboVehiculos.addItem(v));
            mostrarInformacionVehiculo();
        }
    }

    private void mostrarInformacionVehiculo() {
        Vehiculo seleccionado = (Vehiculo) comboVehiculos.getSelectedItem();
        if (seleccionado != null) {
            txtInfoVehiculo.setText(seleccionado.getInfoDetallada());

            // Calcular costo si hay días ingresados
            try {
                String diasTexto = txtDias.getText().trim();
                if (!diasTexto.isEmpty()) {
                    int dias = Integer.parseInt(diasTexto);
                    double costo = seleccionado.calcularCostoTotal(dias);
                    txtInfoVehiculo.append("\n\n══════════════════════════════════════\n");
                    txtInfoVehiculo.append(String.format("COSTO ESTIMADO PARA %d DÍAS: $%.2f", dias, costo));
                }
            } catch (NumberFormatException e) {
                // Ignorar si no es número válido
            }
        }
    }

    // ⚡ MÉTODO CRÍTICO: Registrar alquiler y guardar en MongoDB
    private void registrarAlquiler() {
        try {
            // 1. VALIDAR DATOS DEL CLIENTE
            if (txtDni.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "DNI y Nombre son campos obligatorios",
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);
                txtDni.requestFocus();
                return;
            }

            // 2. VALIDAR DÍAS DE ALQUILER
            int dias;
            try {
                dias = Integer.parseInt(txtDias.getText().trim());
                if (dias <= 0 || dias > 365) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese un número válido de días (1-365)",
                        "Error en Días", JOptionPane.ERROR_MESSAGE);
                txtDias.requestFocus();
                txtDias.selectAll();
                return;
            }

            // 3. OBTENER VEHÍCULO SELECCIONADO
            Vehiculo vehiculo = (Vehiculo) comboVehiculos.getSelectedItem();
            if (vehiculo == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un vehículo",
                        "Error de Selección", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. CREAR OBJETO CLIENTE
            Cliente cliente = new Cliente(
                    txtDni.getText().trim(),
                    txtNombre.getText().trim(),
                    txtLicencia.getText().trim(),
                    txtTelefono.getText().trim()
            );

            // 5. CREAR Y REGISTRAR ALQUILER EN LA AGENCIA
            Alquiler alquiler = agencia.registrarAlquiler(cliente, vehiculo, dias);

            // 6. GUARDAR EN MONGODB ATLAS
            boolean guardadoEnMongo = conexionMongo.guardarAlquiler(alquiler);

            // 7. MOSTRAR CONFIRMACIÓN
            String mensajeExito = String.format(
                    "✅ ALQUILER REGISTRADO EXITOSAMENTE\n\n" +
                            "ID de Alquiler: %s\n" +
                            "Cliente: %s\n" +
                            "Teléfono: %s\n" +
                            "Vehículo: %s %s\n" +
                            "Matrícula: %s\n" +
                            "Días: %d\n" +
                            "Costo Total: $%.2f\n" +
                            "Guardado en MongoDB: %s\n\n" +
                            "El vehículo ya no está disponible para nuevos alquileres.",
                    alquiler.getIdAlquiler(),
                    cliente.getNombre(),
                    cliente.getTelefono(),
                    vehiculo.getMarca(),
                    vehiculo.getModelo(),
                    vehiculo.getMatricula(),
                    dias,
                    alquiler.getCostoTotal(),
                    guardadoEnMongo ? " SÍ" : " NO (pero alquiler registrado localmente)"
            );

            JOptionPane.showMessageDialog(this, mensajeExito,
                    "Confirmación de Alquiler", JOptionPane.INFORMATION_MESSAGE);

            // 8. ACTUALIZAR INTERFAZ
            lblEstado.setText(" Alquiler registrado: " + alquiler.getIdAlquiler() + " - " + new java.util.Date());
            cargarVehiculosDisponibles(); // Actualizar lista (este vehículo ya no aparece)

        } catch (Exception e) {
            // 9. MANEJAR ERRORES
            String mensajeError = " ERROR AL REGISTRAR ALQUILER\n\n";
            mensajeError += "Mensaje: " + e.getMessage() + "\n";
            mensajeError += "Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "Desconocida");

            JOptionPane.showMessageDialog(this, mensajeError,
                    "Error Crítico", JOptionPane.ERROR_MESSAGE);

            lblEstado.setText(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtDni.setText("");
        txtNombre.setText("");
        txtLicencia.setText("");
        txtTelefono.setText("");
        txtDias.setText("");
        lblEstado.setText(" Formulario limpiado - Listo para nuevo registro ");
        txtDni.requestFocus();
    }

    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ejecutar en Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            VistaPrincipal ventana = new VistaPrincipal();
            ventana.setVisible(true);
        });
    }
}
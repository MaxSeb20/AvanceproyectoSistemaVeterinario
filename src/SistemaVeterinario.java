import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class SistemaVeterinario extends Application {
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Cita> listaCitas = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // TabPane para dividir las secciones
        TabPane tabPane = new TabPane();

        // Pestaña Gestión de Clientes y Mascotas
        Tab tabClientes = new Tab("Gestión de Clientes y Mascotas");
        tabClientes.setContent(crearGestionClientes());
        tabClientes.setClosable(false);

        // Pestaña Gestión de Citas
        Tab tabCitas = new Tab("Gestión de Citas");
        tabCitas.setContent(crearGestionCitas());
        tabCitas.setClosable(false);

        tabPane.getTabs().addAll(tabClientes, tabCitas);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setTitle("Sistema Veterinario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearGestionClientes() {
        // Campos para cliente
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        Label lblDireccion = new Label("Dirección:");
        TextField txtDireccion = new TextField();

        // Campos para agregar mascota
        Label lblMascotaNombre = new Label("Nombre de la Mascota:");
        TextField txtMascotaNombre = new TextField();
        Label lblMascotaEspecie = new Label("Especie de la Mascota:");
        TextField txtMascotaEspecie = new TextField();
        Button btnAgregarMascota = new Button("Agregar Mascota");

        ObservableList<Mascota> listaMascotas = FXCollections.observableArrayList();
        ListView<Mascota> listaMascotasView = new ListView<>(listaMascotas);

        // Botón para agregar una mascota
        btnAgregarMascota.setOnAction(e -> {
            String mascotaNombre = txtMascotaNombre.getText();
            String mascotaEspecie = txtMascotaEspecie.getText();
            if (!mascotaNombre.isEmpty() && !mascotaEspecie.isEmpty()) {
                Mascota nuevaMascota = new Mascota(mascotaNombre, mascotaEspecie);
                listaMascotas.add(nuevaMascota);
                txtMascotaNombre.clear();
                txtMascotaEspecie.clear();
            } else {
                mostrarAlerta("Error", "Debe completar todos los campos de la mascota");
            }
        });

        Button btnAgregarCliente = new Button("Agregar Cliente");
        Button btnEliminarCliente = new Button("Eliminar Cliente");
        Button btnEditarCliente = new Button("Editar Cliente");

        // Tabla de clientes
        TableView<Cliente> tablaClientes = new TableView<>();
        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        tablaClientes.getColumns().addAll(colNombre, colTelefono, colDireccion);
        tablaClientes.setItems(listaClientes);

        // Botón agregar cliente funcionalidad
        btnAgregarCliente.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();
            if (!nombre.isEmpty() && !telefono.isEmpty() && !direccion.isEmpty()) {
                Cliente cliente = new Cliente(nombre, telefono, direccion);
                cliente.getMascotas().addAll(listaMascotas);
                listaClientes.add(cliente);
                txtNombre.clear();
                txtTelefono.clear();
                txtDireccion.clear();
                listaMascotas.clear();
            } else {
                mostrarAlerta("Error", "Todos los campos deben ser completados");
            }
        });

        // Botón eliminar cliente funcionalidad
        btnEliminarCliente.setOnAction(e -> {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                listaClientes.remove(clienteSeleccionado);
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para eliminar");
            }
        });

        // Botón editar cliente funcionalidad
        btnEditarCliente.setOnAction(e -> {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                txtNombre.setText(clienteSeleccionado.getNombre());
                txtTelefono.setText(clienteSeleccionado.getTelefono());
                txtDireccion.setText(clienteSeleccionado.getDireccion());
                listaMascotas.setAll(clienteSeleccionado.getMascotas());
                listaClientes.remove(clienteSeleccionado);
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para editar");
            }
        });

        // Layout
        VBox formularioCliente = new VBox(10, lblNombre, txtNombre, lblTelefono, txtTelefono, lblDireccion, txtDireccion);
        VBox formularioMascota = new VBox(10, lblMascotaNombre, txtMascotaNombre, lblMascotaEspecie, txtMascotaEspecie, btnAgregarMascota, listaMascotasView);
        VBox botonesCliente = new VBox(10, btnAgregarCliente, btnEditarCliente);
        VBox tablaClientesBox = new VBox(10, tablaClientes, btnEliminarCliente);
        HBox layout = new HBox(20, formularioCliente, formularioMascota, botonesCliente, tablaClientesBox);
        return new VBox(layout);
    }

    private VBox crearGestionCitas() {
        // Campos para cita
        Label lblCliente = new Label("Cliente:");
        ComboBox<Cliente> comboCliente = new ComboBox<>(listaClientes);
        Label lblMascota = new Label("Mascota:");
        ComboBox<Mascota> comboMascota = new ComboBox<>();
        Label lblFecha = new Label("Fecha:");
        DatePicker datePickerFecha = new DatePicker();
        Label lblHora = new Label("Hora:");
        Spinner<LocalTime> spinnerHora = new Spinner<>();
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public void decrement(int steps) {
                if (getValue() == null) {
                    setValue(LocalTime.of(8, 0));
                } else {
                    setValue(getValue().minusMinutes(steps * 15));
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue() == null) {
                    setValue(LocalTime.of(8, 0));
                } else {
                    setValue(getValue().plusMinutes(steps * 15));
                }
            }
        };
        valueFactory.setValue(LocalTime.of(8, 0));
        spinnerHora.setValueFactory(valueFactory);
        spinnerHora.setEditable(true);
        spinnerHora.getEditor().setTextFormatter(new TextFormatter<>(new StringConverter<LocalTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime time) {
                return time != null ? time.format(formatter) : "";
            }

            @Override
            public LocalTime fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalTime.parse(string, formatter) : null;
            }
        }));

        Label lblMotivo = new Label("Motivo:");
        TextField txtMotivo = new TextField();

        Button btnAgendar = new Button("Agendar Cita");
        Button btnEliminar = new Button("Eliminar Cita");
        Button btnEditar = new Button("Editar Cita");

        // Tabla de citas
        TableView<Cita> tablaCitas = new TableView<>();
        TableColumn<Cita, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCliente().getNombre()));
        TableColumn<Cita, String> colMascota = new TableColumn<>("Mascota");
        colMascota.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMascota().getNombre()));
        TableColumn<Cita, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFecha()));
        TableColumn<Cita, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHora()));
        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMotivo()));
        tablaCitas.getColumns().addAll(colCliente, colMascota, colFecha, colHora, colMotivo);
        tablaCitas.setItems(listaCitas);

        tablaCitas.setPrefHeight(400);  // Ajustar la altura de la tabla para que llene más espacio
        tablaCitas.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(tablaCitas, Priority.ALWAYS);

        // Actualizar mascotas según el cliente seleccionado
        comboCliente.setOnAction(e -> {
            Cliente clienteSeleccionado = comboCliente.getValue();
            if (clienteSeleccionado != null) {
                comboMascota.setItems(FXCollections.observableArrayList(clienteSeleccionado.getMascotas()));
            } else {
                comboMascota.setItems(FXCollections.observableArrayList());
            }
        });

        // Botón agendar funcionalidad
        btnAgendar.setOnAction(e -> {
            if (comboCliente.getValue() != null && comboMascota.getValue() != null && datePickerFecha.getValue() != null && spinnerHora.getValue() != null) {
                Cliente cliente = comboCliente.getValue();
                Mascota mascota = comboMascota.getValue();
                String motivo = txtMotivo.getText();
                LocalDate fecha = datePickerFecha.getValue();
                LocalTime hora = spinnerHora.getValue();
                String horaString = DateTimeFormatter.ofPattern("HH:mm").format(hora);
                listaCitas.add(new Cita(cliente, mascota, fecha.toString(), horaString, motivo));

                // Ordenar la lista de citas por fecha y hora
                listaCitas.sort(Comparator.comparing((Cita c) -> LocalDate.parse(c.getFecha()))
                        .thenComparing(c -> LocalTime.parse(c.getHora())));

                // Limpiar los campos
                comboCliente.setValue(null);
                comboMascota.setItems(FXCollections.observableArrayList());
                comboMascota.setValue(null);
                datePickerFecha.setValue(null);
                spinnerHora.getValueFactory().setValue(LocalTime.of(8, 0));
                txtMotivo.clear();
            } else {
                mostrarAlerta("Error", "Todos los campos deben ser completados");
            }
        });

        // Botón eliminar funcionalidad
        btnEliminar.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                listaCitas.remove(citaSeleccionada);
            } else {
                mostrarAlerta("Error", "Debe seleccionar una cita para eliminar");
            }
        });

        // Botón editar funcionalidad
        btnEditar.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                comboCliente.setValue(citaSeleccionada.getCliente());
                comboMascota.setValue(citaSeleccionada.getMascota());
                datePickerFecha.setValue(LocalDate.parse(citaSeleccionada.getFecha()));
                spinnerHora.getValueFactory().setValue(LocalTime.parse(citaSeleccionada.getHora()));
                txtMotivo.setText(citaSeleccionada.getMotivo());
                listaCitas.remove(citaSeleccionada);
            } else {
                mostrarAlerta("Error", "Debe seleccionar una cita para editar");
            }
        });

        // Layout
        VBox formulario = new VBox(10, lblCliente, comboCliente, lblMascota, comboMascota, lblFecha, datePickerFecha, lblHora, spinnerHora, lblMotivo, txtMotivo, btnAgendar, btnEditar);
        VBox tabla = new VBox(10, tablaCitas, btnEliminar);
        HBox layout = new HBox(20, formulario, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return new VBox(layout);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

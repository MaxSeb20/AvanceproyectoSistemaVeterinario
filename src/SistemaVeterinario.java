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

        // Pestaña de Bienvenida
        Tab tabBienvenida = new Tab("Bienvenida");
        tabBienvenida.setContent(crearPaginaBienvenida(tabPane));
        tabBienvenida.setClosable(false);

        // Pestaña Gestión de Clientes y Mascotas
        Tab tabClientes = new Tab("Gestión de Clientes y Mascotas");
        tabClientes.setContent(crearGestionClientes());
        tabClientes.setClosable(false);

        // Pestaña Gestión de Citas
        Tab tabCitas = new Tab("Gestión de Citas");
        tabCitas.setContent(crearGestionCitas());
        tabCitas.setClosable(false);

        tabPane.getTabs().addAll(tabBienvenida, tabClientes, tabCitas);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setTitle("Sistema Veterinario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearPaginaBienvenida(TabPane tabPane) {
        Label lblTitulo = new Label("Bienvenido al Sistema Veterinario");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label lblDescripcion = new Label("Este sistema le permite gestionar clientes, mascotas y citas para su clínica veterinaria.\n"
                + "Utilice las pestañas de la parte superior para navegar entre las diferentes secciones del sistema.\n\n"
                + "- En la pestaña 'Gestión de Clientes y Mascotas', puede agregar, editar y eliminar clientes y sus mascotas.\n"
                + "- En la pestaña 'Gestión de Citas', puede agendar, editar y eliminar citas para las mascotas.");

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setOnAction(e -> tabPane.getSelectionModel().select(1));

        VBox layoutBienvenida = new VBox(20, lblTitulo, lblDescripcion, btnContinuar);
        layoutBienvenida.setStyle("-fx-alignment: center; -fx-padding: 20;");
        return layoutBienvenida;
    }

    private VBox crearGestionClientes() {
        // Campos para cliente
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        Label lblDirección = new Label("Dirección:");
        TextField txtDireccion = new TextField();

        // Campos para mascota
        Label lblNombreMascota = new Label("Nombre de la Mascota:");
        TextField txtNombreMascota = new TextField();
        Label lblEspecieMascota = new Label("Especie:");
        TextField txtEspecieMascota = new TextField();

        Button btnAgregarCliente = new Button("Agregar Cliente");
        Button btnAgregarMascota = new Button("Agregar Mascota");
        Button btnEliminarCliente = new Button("Eliminar Cliente");
        Button btnEditarCliente = new Button("Editar Cliente");

        // Tabla de clientes
        TableView<Cliente> tablaClientes = new TableView<>();
        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        TableColumn<Cliente, String> colDirección = new TableColumn<>("Dirección");
        colDirección.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        tablaClientes.getColumns().addAll(colNombre, colTelefono, colDirección);
        tablaClientes.setItems(listaClientes);

        // Botón agregar cliente funcionalidad
        btnAgregarCliente.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();
            if (!nombre.isEmpty() && !telefono.isEmpty() && !direccion.isEmpty()) {
                Cliente cliente = new Cliente(nombre, telefono, direccion);
                listaClientes.add(cliente);
                txtNombre.clear();
                txtTelefono.clear();
                txtDireccion.clear();
            } else {
                mostrarAlerta("Error", "Todos los campos deben ser completados");
            }
        });

        // Botón agregar mascota funcionalidad
        btnAgregarMascota.setOnAction(e -> {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            String nombreMascota = txtNombreMascota.getText();
            String especieMascota = txtEspecieMascota.getText();
            if (clienteSeleccionado != null && !nombreMascota.isEmpty() && !especieMascota.isEmpty()) {
                Mascota mascota = new Mascota(nombreMascota, especieMascota);
                clienteSeleccionado.agregarMascota(mascota);
                txtNombreMascota.clear();
                txtEspecieMascota.clear();
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente y completar los campos de la mascota");
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
                listaClientes.remove(clienteSeleccionado);
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para editar");
            }
        });

        // Layout
        VBox formularioCliente = new VBox(10, lblNombre, txtNombre, lblTelefono, txtTelefono, lblDirección, txtDireccion, btnAgregarCliente, btnEditarCliente);
        VBox formularioMascota = new VBox(10, lblNombreMascota, txtNombreMascota, lblEspecieMascota, txtEspecieMascota, btnAgregarMascota);
        VBox tabla = new VBox(10, tablaClientes, btnEliminarCliente);
        HBox layout = new HBox(20, formularioCliente, formularioMascota, tabla);
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

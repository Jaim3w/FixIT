Create table Taller(
Codigo_Taller VARCHAR2(10) primary key,
Nombre_Dueno VARCHAR2(50) NOT NULL,
Apellido_Dueno VARCHAR2(50) NOT NULL,
CorreoElectronico VARCHAR2(50) UNIQUE,
Contrasena VARCHAR2(250) NOT NULL,
Telefono VARCHAR2(15) NOT NULL,
Direccion VARCHAR2(100) NOT NULL
);

Create table Rol(
UUID_rol VARCHAR2(50) primary key,
Nombre VARCHAR2(20) NOT NULL
);


Create table Usuario(
UUID_usuario VARCHAR2(50) PRIMARY KEY,
UUID_rol VARCHAR2(50) NOT NULL,
CorreoElectronico VARCHAR2(50) UNIQUE,
Contrasena VARCHAR2(250) NOT NULL,

CONSTRAINT fk_rol FOREIGN KEY (UUID_rol) REFERENCES Rol(UUID_rol)
);

CREATE TABLE CategoriaItem (
UUID_item VARCHAR2(50) PRIMARY KEY,
Nombre VARCHAR2(50) NOT NULL
);


Create table EstadoAsignarOrden(
UUID_estado Varchar2(50) PRIMARY KEY,
Nombre VARCHAR2(20) NOT NULL
);

Create table Cliente (
Dui_cliente VARCHAR2(10) PRIMARY KEY,
Nombre VARCHAR2(50) NOT NULL,
Apellido VARCHAR2(50) NOT NULL,
Usuario Varchar2(30) NOT NULL,
Contrasena VARCHAR2(250) NOT NULL,
Correo_Electronico VARCHAR2(50) UNIQUE,
Telefono VARCHAR2(8) NOT NULL
);

Create table Marca(
UUID_marca VARCHAR2(50) PRIMARY KEY,
Nombre VARCHAR2(50) NOT NULL,
Descripcion VARCHAR2(200) NOT NULL
);

Create table Proveedor(
Dui_proveedor VARCHAR2(10) PRIMARY KEY,
Nombre VARCHAR2(50) NOT NULL,
Apellido VARCHAR2(50) NOT NULL,
Telefono VARCHAR2(9) NOT NULL,
Correo_Electronico VARCHAR2(50) UNIQUE,
Direccion VARCHAR2(200) NOT NULL
);

Create table Servicio(
UUID_servicio VARCHAR2(50) PRIMARY KEY,
Nombre VARCHAR2(50) NOT NULL,
Descripcion VARCHAR2(50) NOT NULL,
Precio NUMBER(10, 2) CHECK (Precio > 0) NOT NULL
);


Create table ProductoRepuesto(
UUID_productoRepuesto VARCHAR2(50) PRIMARY KEY,
UUID_item VARCHAR2(50),
Nombre VARCHAR2(50) NOT NULL,
ImagenProductoRepuesto VARCHAR2(255),
Precio NUMBER(10, 2) CHECK (Precio > 0) NOT NULL,

CONSTRAINT fk_item FOREIGN KEY (UUID_item) REFERENCES CategoriaItem(UUID_item)
);


Create table Modelo(
UUID_modelo VARCHAR2(50)PRIMARY KEY,
UUID_marca VARCHAR2(50) NOT NULL,
Nombre VARCHAR2(20) NOT NULL,

CONSTRAINT fk_marca FOREIGN KEY (UUID_marca) REFERENCES Marca(UUID_marca)
);


Create table Empleado(
Dui_empleado VARCHAR2(10) PRIMARY KEY,
UUID_usuario VARCHAR2(50) NOT NULL,
Nombre VARCHAR2(100) NOT NULL,
Apellido VARCHAR2(100) NOT NULL,
ImagenEmpleado VARCHAR2(255),
FechaNacimiento VARCHAR(200) NOT NULL,
Telefono VARCHAR2(9) NOT NULL,

CONSTRAINT fk_usuario FOREIGN KEY (UUID_usuario) REFERENCES Usuario(UUID_usuario)
);

Create table Carro(
Placa_carro VARCHAR2(8) PRIMARY KEY,
Dui_cliente VARCHAR2(10) NOT NULL,
UUID_modelo VARCHAR2(50) NOT NULL,
Color VARCHAR2(15) NOT NULL,
Año Varchar2(5) NOT NULL,
ImagenCarro VARCHAR2(255),
FechaRegistro VARCHAR2(200) NOT NULL,
Descripcion VARCHAR2(200) NOT NULL,

CONSTRAINT fk_cliente FOREIGN KEY (Dui_cliente) REFERENCES Cliente(Dui_cliente),
CONSTRAINT fk_modelo FOREIGN KEY (UUID_modelo) REFERENCES Modelo(UUID_modelo)
);

Create table HistorialCarro(
UUID_historialCarro Varchar2(50) PRIMARY KEY,
Placa_carro VARCHAR2(8) NOT NULL,
Descripcion VARCHAR2(150) NOT NULL,

CONSTRAINT fk_carro FOREIGN KEY (Placa_carro) REFERENCES Carro(Placa_carro)
);

Create table Cita (
UUID_cita Varchar2(50) PRIMARY KEY,
Dui_cliente VARCHAR2(10) NOT NULL,
Dui_empleado VARCHAR2(10) NOT NULL,
Fecha_cita VARCHAR2(200) NOT NULL,
Descripcion VARCHAR2(250) NOT NULL,

CONSTRAINT fk_c_empleado FOREIGN KEY (Dui_empleado) REFERENCES Empleado(Dui_empleado),
CONSTRAINT fk_c_cliente FOREIGN KEY (Dui_cliente) REFERENCES Cliente(Dui_cliente)
);


Create table AsignarOrden(
UUID_AsignarOrden Varchar2(50) PRIMARY KEY,
UUID_Cita Varchar2(50) NOT NULL,
UUID_servicio Varchar2(50) NOT NULL,
UUID_estado Varchar2(50) NOT NULL,
FechaAsignacion VARCHAR2(200) NOT NULL,
FechaFinalizacion VARCHAR2(200) NOT NULL,
Descripcion VARCHAR2(150) NOT NULL,

CONSTRAINT fk_cita_tarea FOREIGN KEY (UUID_Cita) REFERENCES Cita(UUID_Cita),
CONSTRAINT fk_servicio_tarea FOREIGN KEY (UUID_servicio) REFERENCES Servicio(UUID_servicio),
CONSTRAINT fk_estado FOREIGN KEY (UUID_estado) REFERENCES EstadoAsignarOrden(UUID_estado)
);


Create table Factura(
UUID_factura VARCHAR2(50) PRIMARY KEY,
FechaEmision VARCHAR2(200) NOT NULL,
FechaVencimiento VARCHAR2(200) NOT NULL
);

Create table DetalleFactura(
UUID_DetalleFactura VARCHAR2(50) PRIMARY KEY,
UUID_factura VARCHAR2(50) NOT NULL,
UUID_productoRepuesto VARCHAR2(50) NOT NULL,
UUID_AsignarOrden VARCHAR2(50) NOT NULL,

CONSTRAINT fk_detalle_factura FOREIGN KEY (UUID_factura) REFERENCES Factura(UUID_factura),
CONSTRAINT fk_detalle_item FOREIGN KEY (UUID_productoRepuesto) REFERENCES ProductoRepuesto(UUID_productoRepuesto),
CONSTRAINT fk_detalle_tarea FOREIGN KEY (UUID_AsignarOrden) REFERENCES AsignarOrden(UUID_AsignarOrden)
);


Create table DetalleProveedor(
UUID_DetalleProveedor VARCHAR2(50) NOT NULL,
UUID_productoRepuesto VARCHAR2(50) NOT NULL,
Dui_proveedor VARCHAR2(10) NOT NULL,
Cantidad NUMBER(10, 2) NOT NULL CHECK (Cantidad > 0) ,
FechaSuministro VARCHAR2(200) NOT NULL,

CONSTRAINT fk_producto_proveedor FOREIGN KEY (UUID_productoRepuesto) REFERENCES ProductoRepuesto(UUID_productoRepuesto),
CONSTRAINT fk_proveedor_producto FOREIGN KEY (Dui_proveedor) REFERENCES Proveedor(Dui_proveedor)
);





--------------------------------------------------------------------------------------------------------------


INSERT INTO Taller (Codigo_Taller, Nombre_Dueno, Apellido_Dueno, CorreoElectronico, Contrasena, Telefono, Direccion)
VALUES ('TallerDisp', 'Fernando', 'Merino', 'fernando.merino@gmail.com', 'merino12', '23473411', 'Mexicanos Calle Sur');


INSERT ALL
 INTO Rol (UUID_rol, Nombre) VALUES (SYS_GUID(), 'Administrador')
 INTO Rol (UUID_rol, Nombre) VALUES (SYS_GUID(), 'Empleado')
SELECT * FROM dual;

INSERT ALL
 INTO Usuario (UUID_usuario, UUID_rol, CorreoElectronico, Contrasena) 
 VALUES (SYS_GUID(),'8C988FE15F3B4C8BA57590B412F5AB6A', 'mario.garcia@gmail.com', 'mario123')
 INTO Usuario (UUID_usuario, UUID_rol, CorreoElectronico, Contrasena)
 VALUES (SYS_GUID(), 'AC91F66EE9744D1C95854429D110E268', 'rebeca.zelaya@gmail.com', 'rebe1234')
 INTO Usuario (UUID_usuario, UUID_rol, CorreoElectronico, Contrasena) 
 VALUES (SYS_GUID(), 'AC91F66EE9744D1C95854429D110E268', 'fatima.juarez@gmail.com', 'fati1234')
 INTO Usuario (UUID_usuario, UUID_rol, CorreoElectronico, Contrasena) 
 VALUES (SYS_GUID(), 'AC91F66EE9744D1C95854429D110E268', 'juan.lopez@gmail.com', 'juan1234')
 INTO Usuario (UUID_usuario, UUID_rol, CorreoElectronico, Contrasena) 
 VALUES (SYS_GUID(), 'AC91F66EE9744D1C95854429D110E268', 'maria.dominguez@gmail.com', 'maria123')
SELECT * FROM dual;

INSERT INTO CategoriaItem (UUID_item, Nombre) VALUES (SYS_GUID(), 'Producto');
INSERT INTO CategoriaItem (UUID_item, Nombre) VALUES (SYS_GUID(), 'Repuesto');


INSERT ALL
 INTO EstadoAsignarOrden (UUID_estado, Nombre)
 VALUES (SYS_GUID(), 'Sin comenzar')
 INTO EstadoAsignarOrden (UUID_estado, Nombre) 
 VALUES (SYS_GUID(), 'En proceso')
 INTO EstadoAsignarOrden (UUID_estado, Nombre) 
 VALUES (SYS_GUID(), 'Terminado')
SELECT * FROM dual;


INSERT ALL
 INTO Cliente (Dui_cliente, Nombre, Apellido, Usuario, Contrasena, Correo_Electronico, Telefono)
 VALUES ('345723699', 'Carlos', 'Goncho', 'C.Goncho', 'goncho123', 'goncho@gmail.com', '34598115')
 INTO Cliente (Dui_cliente, Nombre, Apellido, Usuario, Contrasena, Correo_Electronico, Telefono)
 VALUES ('091345821', 'Lautaro', 'Martin', 'L.Martin', 'lauti123', 'martin@gmail.com', '12056923')
 INTO Cliente (Dui_cliente, Nombre, Apellido, Usuario, Contrasena, Correo_Electronico, Telefono)
 VALUES ('456789012', 'Lionel', 'Messi', 'L.Messi', 'messi123', 'messi@gmail.com', '10493682')
 INTO Cliente (Dui_cliente, Nombre, Apellido, Usuario, Contrasena, Correo_Electronico, Telefono)
 VALUES ('218957987', 'Paulo', 'Dybala', 'P.dybala', 'dybala123', 'dybala@gmail.com', '23489679')
 INTO Cliente (Dui_cliente, Nombre, Apellido, Usuario, Contrasena, Correo_Electronico, Telefono)
 VALUES ('092438653', 'Luis', 'De la Fuente', 'L.DelaFuente', 'fuente123', 'luis@gmail.com', '93082462')
SELECT * FROM dual;


INSERT ALL
 INTO Marca (UUID_marca, Nombre, Descripcion) 
 VALUES (SYS_GUID(), 'Toyota', 'Marca japonesa conocida por su calidad en carros')
 INTO Marca (UUID_marca, Nombre, Descripcion) 
 VALUES (SYS_GUID(), 'Honda', 'Marca japonesa destacada por sus motores de buena calidad.')
 INTO Marca (UUID_marca, Nombre, Descripcion) 
 VALUES (SYS_GUID(), 'Ford', 'Marca americana reconocida por sus camionetas robustas')
 INTO Marca (UUID_marca, Nombre, Descripcion) 
 VALUES (SYS_GUID(), 'Chevrolet', 'Marca americana que ofrece una amplia variedad de vehículos.')
 INTO Marca (UUID_marca, Nombre, Descripcion) 
 VALUES (SYS_GUID(), 'Volkswagen', 'Marca alemana conocida por su ingeniería y modelos como el Golf y el Beetle.')
SELECT * FROM dual;



INSERT ALL
    INTO Proveedor (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) 
    VALUES ('256192789', 'Carlos', 'Alcaraz', '12835689', 'carlos.alcaraz@gmail.com', 'Bulevar Los Proceres')
    INTO Proveedor (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) 
    VALUES ('568355741', 'Laura', 'Bonilla', '01923578', 'laura.bonilla@gmail.com', 'Cabañas avenida sur')
    INTO Proveedor (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) 
    VALUES ('237724542', 'Josefin', 'Martinez', '12938056', 'josefin.martinez@gmail.com', 'La Libertad avenida norte')
    INTO Proveedor (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) 
    VALUES ('682425140', 'David', 'Lopez', '24572313', 'david.lopez@gmail.com', 'Calle San francisco')
    INTO Proveedor (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) 
    VALUES ('345832463', 'Luis', 'Enrique', '23388572', 'luis.enrique@gmail.com', 'Las Arboledas')
SELECT * FROM dual;



INSERT ALL
    INTO Servicio (UUID_servicio, Nombre, Descripcion, Precio) 
    VALUES (SYS_GUID(), 'Cambio de Aceite', 'Cambio de aceite de motor', 25.99)
    INTO Servicio (UUID_servicio, Nombre, Descripcion, Precio) 
    VALUES (SYS_GUID(), 'Alineación y Balanceo', 'Alineación y balanceo de ruedas', 47.99)
    INTO Servicio (UUID_servicio, Nombre, Descripcion, Precio) 
    VALUES (SYS_GUID(), 'Revisión de Frenos', 'Inspección y ajuste de frenos', 29.99)
    INTO Servicio (UUID_servicio, Nombre, Descripcion, Precio) 
    VALUES (SYS_GUID(), 'Cambio de Batería', 'Reemplazo de batería del vehículo', 79.99)
    INTO Servicio (UUID_servicio, Nombre, Descripcion, Precio) 
    VALUES (SYS_GUID(), 'Diagnóstico Completo', 'Diagnóstico completo del vehículo', 109.99)
SELECT * FROM dual;

INSERT ALL
 INTO ProductoRepuesto (UUID_productoRepuesto, UUID_item, Nombre, ImagenProductoRepuesto, Precio) 
 VALUES (SYS_GUID(), 'B04BEC98C3B843E6B41F0238396F5343' ,'Aceite de motor', EMPTY_BLOB(), 6.99)
 INTO ProductoRepuesto (UUID_productoRepuesto, UUID_item, Nombre, ImagenProductoRepuesto, Precio) 
 VALUES (SYS_GUID(), 'C2F90A4E05084AD487B9F88F6B57B312', 'Filtro de aire', EMPTY_BLOB(), 10.99)
 INTO ProductoRepuesto (UUID_productoRepuesto, UUID_item, Nombre, ImagenProductoRepuesto, Precio) 
 VALUES (SYS_GUID(), 'C2F90A4E05084AD487B9F88F6B57B312', 'Batería de coche', EMPTY_BLOB(), 70.99)
 INTO ProductoRepuesto (UUID_productoRepuesto, UUID_item,Nombre, ImagenProductoRepuesto, Precio) 
 VALUES (SYS_GUID(), 'C2F90A4E05084AD487B9F88F6B57B312', 'Pastillas de freno', EMPTY_BLOB(), 20.99)
 INTO ProductoRepuesto (UUID_productoRepuesto, UUID_item, Nombre, ImagenProductoRepuesto, Precio) 
 VALUES (SYS_GUID(), 'C2F90A4E05084AD487B9F88F6B57B312', 'Neumático', EMPTY_BLOB(), 50.99)
SELECT * FROM dual;


INSERT ALL
 INTO Modelo (UUID_modelo, UUID_marca, Nombre) 
 VALUES (SYS_GUID(), '14D6304D5B1D43C6BEE2CA15C36448A6', 'Sedán')
 INTO Modelo (UUID_modelo, UUID_marca, Nombre) 
 VALUES (SYS_GUID(), '1C09E0123A584FED9E12AAAC9A125FFF', 'CR-V Hybrid')
 INTO Modelo (UUID_modelo, UUID_marca, Nombre) 
 VALUES (SYS_GUID(), '695892BB8C4642D98BB5F91D122DFA52', 'EDGE ST')
 INTO Modelo (UUID_modelo, UUID_marca, Nombre) 
 VALUES (SYS_GUID(), '02D1B3DB8D2C450D8A014DC0C9371D6A', 'Montana')
 INTO Modelo (UUID_modelo, UUID_marca, Nombre) 
 VALUES (SYS_GUID(), 'E968636287FE4F3DB3A70517738E54F3', 'Nuevo Taigun')
SELECT * FROM dual;


INSERT ALL
 INTO Empleado (Dui_empleado, UUID_usuario, Nombre, Apellido, ImagenEmpleado, FechaNacimiento, Telefono)
 VALUES ('43862360', '2BA76E9BDDF74BF7AC930AC6082C7A67', 'Mario', 'Garcia', EMPTY_BLOB(), '2005-05-15', '83462396')
 INTO Empleado (Dui_empleado, UUID_usuario, Nombre, Apellido, ImagenEmpleado, FechaNacimiento, Telefono)
 VALUES ('234734141', 'A4E2635F8EB64B6BBE1C8F441AA58F8E', 'Rebeca', 'Zelaya', EMPTY_BLOB(), '1985-08-20', '28976122')
 INTO Empleado (Dui_empleado, UUID_usuario, Nombre, Apellido, ImagenEmpleado, FechaNacimiento, Telefono)
 VALUES ('527223462', '42269F47C50F437DA7D8B5D5DB5049F6', 'Fatima', 'Juarez', EMPTY_BLOB(), '1992-03-10', '09348267')
 INTO Empleado (Dui_empleado, UUID_usuario, Nombre, Apellido, ImagenEmpleado, FechaNacimiento, Telefono)
 VALUES ('345834343', '47A40EB83E5D49F9B3B2EBE377269BCE', 'Juan', 'Lopez', EMPTY_BLOB(), '1988-11-25', '90283167')
 INTO Empleado (Dui_empleado, UUID_usuario, Nombre, Apellido, ImagenEmpleado, FechaNacimiento, Telefono)
 VALUES ('534233464', 'D84AADD592E54AA4B8E305AE026F0793', 'Maria', 'Dominguez', EMPTY_BLOB(), '1995-07-03', '29086126')
SELECT * FROM dual;


INSERT ALL
 INTO Carro (Placa_carro, Dui_cliente, UUID_modelo, Color, Año, ImagenCarro, FechaRegistro, Descripcion)
 VALUES ('ABC1234', '345723699', '5AC3F453E0D449F485E08B85886899CE', 'Gris', '2020', EMPTY_BLOB(), '29/06/2024', 'Carro deportivo')
 INTO Carro (Placa_carro, Dui_cliente, UUID_modelo, Color, Año, ImagenCarro, FechaRegistro, Descripcion)
 VALUES ('XYZ5678', '091345821', '035D68B90E734F20810C7A738ED98E19', 'Negro', '2019', EMPTY_BLOB(), '29/06/2024', 'Carro familiar')
 INTO Carro (Placa_carro, Dui_cliente, UUID_modelo, Color, Año, ImagenCarro, FechaRegistro, Descripcion)
 VALUES ('DEF9012', '456789012', '2249C03090CC498A8BCA5DAFC699D167', 'Blanco', '2021', EMPTY_BLOB(), '29/06/2024', 'Carro urbano')
 INTO Carro (Placa_carro, Dui_cliente, UUID_modelo, Color, Año, ImagenCarro, FechaRegistro, Descripcion)
 VALUES ('GHI3456', '218957987', '4430BC33DB21400D8C8229C31DFB0E37', 'Azul', '2017', EMPTY_BLOB(), '29/06/2024', 'Carro SUV')
 INTO Carro (Placa_carro, Dui_cliente, UUID_modelo, Color, Año, ImagenCarro, FechaRegistro, Descripcion)
 VALUES ('JKL7890', '092438653', 'AE248E480F9344A5B381815593268776', 'Rojo', '2020', EMPTY_BLOB(), '29/06/2024', 'Carro eléctrico')
SELECT * FROM dual;



INSERT ALL
  INTO HistorialCarro (UUID_historialCarro, Placa_carro, Descripcion)
  VALUES (SYS_GUID(), 'ABC1234', 'Revisión de motor')
  INTO HistorialCarro (UUID_historialCarro, Placa_carro, Descripcion)
  VALUES (SYS_GUID(), 'XYZ5678', 'Cambio de aceite')
  INTO HistorialCarro (UUID_historialCarro, Placa_carro, Descripcion)
  VALUES (SYS_GUID(), 'DEF9012', 'Alineación y balanceo')
  INTO HistorialCarro (UUID_historialCarro, Placa_carro, Descripcion)
  VALUES (SYS_GUID(), 'GHI3456', 'Reparación de frenos')
  INTO HistorialCarro (UUID_historialCarro, Placa_carro, Descripcion)
  VALUES (SYS_GUID(), 'JKL7890', 'Cambio de llantas')
SELECT * FROM dual;




INSERT ALL
  INTO Cita (UUID_cita, Dui_Cliente, Dui_empleado , Fecha_cita, Descripcion)
  VALUES (SYS_GUID(), '345723699', '43862360' , '01/07/2024', 'Primera cita del taller')
  INTO Cita (UUID_cita,  Dui_Cliente, Dui_empleado , Fecha_cita, Descripcion)
  VALUES (SYS_GUID(), '091345821', '234734141', '02/07/2024', 'Segunda cita del taller')
  INTO Cita (UUID_cita,  Dui_Cliente, Dui_empleado ,Fecha_cita, Descripcion)
  VALUES (SYS_GUID(), '456789012', '527223462', '03/07/2024', 'Tercera cita del taller')
  INTO Cita (UUID_cita,  Dui_Cliente, Dui_empleado ,Fecha_cita, Descripcion)
  VALUES (SYS_GUID(), '218957987', '345834343', '04/07/2024', 'Cuarta cita del taller')
  INTO Cita (UUID_cita,  Dui_Cliente, Dui_empleado ,Fecha_cita, Descripcion)
  VALUES (SYS_GUID(), '092438653', '534233464', '02/07/2024', 'Quinta cita del taller')
SELECT * FROM dual;



INSERT ALL
  INTO AsignarOrden(UUID_AsignarOrden, UUID_cita, UUID_servicio, UUID_estado, FechaAsignacion, FechaFinalizacion, Descripcion)
  VALUES (SYS_GUID(), '451ADBA5DF3042B4B189BA2D533DAE97', '3B4A4F55A5A343AFAAEBFFCFBF300F3A', '6055DB51B4454DA9BAD9EB84398F5C98', '29/06/2024', '30/06/2024', 'Orden de mantenimiento preventivo')
  INTO AsignarOrden (UUID_AsignarOrden, UUID_cita, UUID_servicio, UUID_estado, FechaAsignacion, FechaFinalizacion, Descripcion)
  VALUES (SYS_GUID(), '6D4F23888DB44DACBFEB97A5625AC5FF', '38DE503ABB04450DBAA717B45474381E','6055DB51B4454DA9BAD9EB84398F5C98', '29/06/2024', '01/07/2024', 'Cambio de aceite')
  INTO AsignarOrden (UUID_AsignarOrden, UUID_cita, UUID_servicio, UUID_estado, FechaAsignacion, FechaFinalizacion, Descripcion)
  VALUES (SYS_GUID(), 'E1AEA8D493CF45FEAC0BBCD8045605DE', '16329AF7F9244A20931DCAFB816E4BBD','6055DB51B4454DA9BAD9EB84398F5C98', '29/06/2024', '02/07/2024', 'Alineación y balanceo')
  INTO AsignarOrden (UUID_AsignarOrden, UUID_cita, UUID_servicio, UUID_estado, FechaAsignacion, FechaFinalizacion, Descripcion)
  VALUES (SYS_GUID(), '2074B8730C2E4244999C2C9AA26B66B0', '37D3BABB7FD4476E834C2A9171A52278','6055DB51B4454DA9BAD9EB84398F5C98' ,'29/06/2024', '03/07/2024', 'Reparación de frenos')
  INTO AsignarOrden (UUID_AsignarOrden, UUID_cita, UUID_servicio, UUID_estado, FechaAsignacion, FechaFinalizacion, Descripcion)
  VALUES (SYS_GUID(), '1E4479A052024769B16E8A27235EA70B', 'B1DE0102888B4D92BE55FD4DB9D90D4B','49634202891C444A98EC020B369389B7','29/06/2024', '04/07/2024', 'Cambio de llantas')
SELECT * FROM dual;



select * from ProductoRepuesto;

SELECT  CategoriaItem.Nombre AS "Categoria", ProductoRepuesto.Nombre, 
ProductoRepuesto.ImagenProductoRepuesto AS Imagen, ProductoRepuesto.Precio
FROM ProductoRepuesto
INNER JOIN CategoriaItem
ON ProductoRepuesto.UUID_item = CategoriaItem.UUID_item  ; 

Select Empleado.Nombre AS "Tarea asignada a:", Cliente.Dui_cliente AS "Cliente", Modelo.Nombre "Modelo de carro a revisar", Servicio.Nombre AS "Servicio a realizar:", 
EstadoAsignarOrden.Nombre As "Estado de la tarea:", AsignarOrden.FechaAsignacion AS "Fecha de asignacion", 
AsignarOrden.FechaFinalizacion AS "Fecha de finalizacion", AsignarOrden.Descripcion
FROM AsignarOrden 
INNER JOIN Cita ON AsignarOrden.UUID_cita = Cita.UUID_cita      
INNER JOIN Servicio ON AsignarOrden.UUID_servicio = Servicio.UUID_servicio
INNER JOIN EstadoAsignarOrden ON AsignarOrden.UUID_estado = EstadoAsignarOrden.UUID_estado
INNER JOIN Empleado ON Cita.Dui_Empleado = Empleado.Dui_Empleado
INNER JOIN Cliente ON Cita.Dui_cliente = Cliente.Dui_cliente
INNER JOIN Carro ON Cliente.Dui_cliente = Carro.Dui_cliente
INNER JOIN Modelo ON Carro.UUID_modelo = Modelo.UUID_modelo

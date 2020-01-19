# Call Center

## Consigna

Existe un call center donde hay 3 tipos de empleados: operador, supervisor
y director. El proceso de la atención de una llamada telefónica en primera
instancia debe ser atendida por un operador, si no hay ninguno libre debe
ser atendida por un supervisor, y de no haber tampoco supervisores libres
debe ser atendida por un director.


## Requerimientos

- Debe existir una clase Dispatcher encargada de manejar las
  llamadas, y debe contener el método dispatchCall para que las
  asigne a los empleados disponibles.
- El método dispatchCall puede invocarse por varios hilos al mismo
  tiempo.
- La clase Dispatcher debe tener la capacidad de poder procesar 10
  llamadas al mismo tiempo (de modo concurrente).
- Cada llamada puede durar un tiempo aleatorio entre 5 y 10
  segundos.
- Debe tener un test unitario donde lleguen 10 llamadas.


## Extras/Plus

- Dar alguna solución sobre qué pasa con una llamada cuando no hay
  ningún empleado libre.
- Dar alguna solución sobre qué pasa con una llamada cuando entran
  más de 10 llamadas concurrentes.
- Agregar los tests unitarios que se crean convenientes.
- Agregar documentación de código



# Ejecución 

Al ejecutar la aplicación se pedirá introducir un valor que representa la cantidad de llamadas que serán atendidas, al ingresar este valor se crearán n llamadas que serán procesadas por el Dispatcher. Para salir de la aplicación se debe ingresar "exit" en la consola. Los logs durante la ejecución se mantuvieron activos para apreciar de una mejor manera el funcionamiento interno de la aplicación.

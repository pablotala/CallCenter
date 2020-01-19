package com.almundo.callcenter;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import cam.almundo.modelo.Dispatcher;
import cam.almundo.modelo.Empleado;
import cam.almundo.modelo.TipoEmpleado;
import static org.junit.Assert.assertEquals;



public class AppTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AppTest.class);
    
    private Dispatcher dispatcher;
    private List<Empleado> empleados;
    
    @Before
    public void initializeValues() {
    	empleados = new ArrayList<>();
    	dispatcher = new Dispatcher();
    }
    
    /**
     * Procesa 10 llamdos con 10 empleados
     */
    @Test
    public void numeroDeLlamadasEntrantesIgualanLlamadasAtendidas() {
        logger.info("##########Testing numeroDeLlamadasEntrantesIgualanLlamadasAtendidas");
        //setea numero de empleados
        int llamadasAProcesar = 10;
        int numeroOperadores = 6;
        int numeroSupersvisores = 3;
        int numeroDirectores = 1;
        dispatcher = new Dispatcher(10,10);

        //agrega empleados
        IntStream.range(0, numeroOperadores).forEach(i -> empleados.add(new Empleado(i + "Op", TipoEmpleado.OPERADOR,true)));
        IntStream.range(0, numeroSupersvisores).forEach(i -> empleados.add(new Empleado(i + "Sp", TipoEmpleado.SUPERVISOR,true)));
        IntStream.range(0, numeroDirectores).forEach(i -> empleados.add(new Empleado(i + "Dr", TipoEmpleado.DIRECTOR,true)));

        //Setea la lista de empleados
        dispatcher.setEmpleados(empleados);

        //Procesa las llamadas
        IntStream.range(0, llamadasAProcesar).forEach(i -> dispatcher.dispatchCall(UUID.randomUUID().toString().substring(0, 8)));

        dispatcher.stopExecutor();
        dispatcher.esperarFin();
        
        assertEquals("Llamadas procesadas", llamadasAProcesar, dispatcher.getLlamadasFinalizadas());
        logger.info("******Llamadas procesadas: {}", dispatcher.getLlamadasFinalizadas());
    }
    
    
    /**
     * Devuelve empelados siempre en orden acorde a disponibilidad OP -> SP -> DR
     */
    @Test
    public void empleadoDisponibleEnOrden() {
        logger.info("##########Testing empleadoDisponibleEnOrden");

        //seteo el numero de cada tipo de empleados
        int numeroOperadores = 3;
        int numeroSupersvisores = 2;
        int numeroDirectores = 1;
        Optional<Empleado> empleadoDisponible;

        //Agrego empleados de forma variada
        IntStream.range(0, numeroSupersvisores).forEach(i -> empleados.add(new Empleado(i + "Sp", TipoEmpleado.SUPERVISOR,true)));
        IntStream.range(0, numeroDirectores).forEach(i -> empleados.add(new Empleado(i + "Dr", TipoEmpleado.DIRECTOR,true)));
        IntStream.range(0, numeroOperadores).forEach(i -> empleados.add(new Empleado(i + "Op", TipoEmpleado.OPERADOR,true)));

        //Seteo la lista de empleados
        dispatcher.setEmpleados(empleados);

        empleadoDisponible = dispatcher.encontrarEmpleadoDisponible();

        //Supervisores y directores ocuapados, debe traer operador
        empleadoDisponible.ifPresent(empleado -> {
            assertEquals("Operador Disponible", TipoEmpleado.OPERADOR, empleado.getType());
            logger.info("******Tipo Empleado: {}", empleado.getType());
        });

        //limpia los empleados
        empleados.clear();

        //Agrego empleados de forma variada
        IntStream.range(0, numeroDirectores).forEach(i -> empleados.add(new Empleado(i + "Dr", TipoEmpleado.DIRECTOR,true)));
        IntStream.range(0, numeroOperadores).forEach(i -> empleados.add(new Empleado(i + "Op", TipoEmpleado.OPERADOR, false)));
        IntStream.range(0, numeroSupersvisores).forEach(i -> empleados.add(new Empleado(i + "Sp", TipoEmpleado.SUPERVISOR,true)));

        //Seteo la lista de empleados
        dispatcher.setEmpleados(empleados);

        empleadoDisponible = dispatcher.encontrarEmpleadoDisponible();

        //Operadores y directores ocuapados, debe traer supervisor
        empleadoDisponible.ifPresent(empleado -> {
            assertEquals("Supervisor Disponible", TipoEmpleado.SUPERVISOR, empleado.getType());
            logger.info("******Tipo Empleado: {}", empleado.getType());
        });
        //limpia los empleados
        empleados.clear();

        //Agrego empleados de forma variada
        IntStream.range(0, numeroSupersvisores).forEach(i -> empleados.add(new Empleado(i + "Sp", TipoEmpleado.SUPERVISOR, false)));
        IntStream.range(0, numeroDirectores).forEach(i -> empleados.add(new Empleado(i + "Dr", TipoEmpleado.DIRECTOR,true)));
        IntStream.range(0, numeroOperadores).forEach(i -> empleados.add(new Empleado(i + "Op", TipoEmpleado.OPERADOR, false)));

        //Seteo la lista de empleados
        dispatcher.setEmpleados(empleados);

        empleadoDisponible = dispatcher.encontrarEmpleadoDisponible();

        //Operadores y supervisores ocuapados, debe traer director
        empleadoDisponible.ifPresent(empleado -> {
            assertEquals("Director Disponble", TipoEmpleado.DIRECTOR, empleado.getType());
            logger.info("******Tipo Empleado: {}", empleado.getType());
        });
    }
	
}

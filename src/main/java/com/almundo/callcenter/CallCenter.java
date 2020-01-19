package com.almundo.callcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.IntStream;

import com.almundo.utils.Constantes;

import cam.almundo.modelo.Dispatcher;
import cam.almundo.modelo.Empleado;
import cam.almundo.modelo.TipoEmpleado;


public class CallCenter 
{
    public static void main( String[] args )
    {
    	//Inicializa el dispatcher y la lista de empleados que se van a utilizar
    	Dispatcher dispatcher = new Dispatcher();
        List<Empleado> empleados = new ArrayList<Empleado>();
        
        //Agrego empleados
        IntStream.range(0, Constantes.OPERADORES).forEach(i -> empleados.add(new Empleado(i + "Op", TipoEmpleado.OPERADOR,true)));
        IntStream.range(0, Constantes.SUPERVISORES).forEach(i -> empleados.add(new Empleado(i + "Sp", TipoEmpleado.SUPERVISOR,true)));
        IntStream.range(0, Constantes.DIRECTORES).forEach(i -> empleados.add(new Empleado(i + "Dr", TipoEmpleado.DIRECTOR,true)));
        
        //Seteo la lista de empleados
        dispatcher.setEmpleados(empleados);

        Scanner in = new Scanner(System.in);
        String inputValor;

        int callParaProcesar;
        
        //Lee del input la cantidad de llamadas a procesar hasta ingresar "salir"
        do {
            System.out.println("Llamadas para procesar ('salir' para finalizar) : ");
            inputValor = in.nextLine();
            try {
            	callParaProcesar = Integer.parseInt(inputValor);
                IntStream.range(0, callParaProcesar).forEach(i -> dispatcher.dispatchCall(UUID.randomUUID().toString().substring(0, 8)));

            } catch (NumberFormatException e) {

            }
        } while (inputValor.compareToIgnoreCase("salir") != 0);
        
        //Para el executor y todos los threads que maneja
        dispatcher.stopExecutor();

    }
    
    
}

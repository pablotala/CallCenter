package cam.almundo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.utils.Constantes;

public class Dispatcher {
	
private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

/**
 * Lista de empleados 
 */
private List<Empleado> empleados;

/**
 * El ejecutor para los threads
 */
private ExecutorService executor;

/**
 * Cola para agregar llamados en FIFO
 */
private ConcurrentLinkedQueue<String> calls;

/**
 * Semaforo/contador usado para controlar la cantidad de empleados que estan libres
 */
private Semaphore semaphore;

/**
 * Contador de llamadas
 */
private int llamadasFinalizadas;
	
	public Dispatcher() {
        setEmpleados(new ArrayList<>());
        //creo un pool de thread con la cantidad de llamadas simultaneas
        executor = Executors.newFixedThreadPool(Constantes.MAX_LLAMADAS_SIMULTANEAS);
        calls = new ConcurrentLinkedQueue<>();
        //instancia un semaforo con la cantidad de empleados disponiblñes
        semaphore = new Semaphore(Constantes.DIRECTORES + Constantes.OPERADORES + Constantes.SUPERVISORES, true);
        llamadasFinalizadas = 0;
    }
	
	
	public Dispatcher(int empleadosMax,int llamadasSimultaneas) {
        setEmpleados(new ArrayList<>());
        //creo un pool de thread con la cantidad de llamadas simultaneas
        executor = Executors.newFixedThreadPool(llamadasSimultaneas);
        calls = new ConcurrentLinkedQueue<>();
        //instancia un semaforo con la cantidad de empleados disponiblñes
        semaphore = new Semaphore(empleadosMax);
        llamadasFinalizadas = 0;
    }
	
	/**
     * Dirige las llamadas entrantes, por cada llamada agrega el id a la cola de llamadas y empieaz el proceso para atender a cada una usando un thread
     * @param callId el id de una nueva llamada
	 * @return 
     */
    public void dispatchCall(String callId) {
    	logger.info("========New call: {}", callId);
    	//Agrega la llamada a la cola
        calls.add(callId);
        executor.submit(() -> {
            try {
            	semaphore.acquire();
            Optional<Empleado> empleadoDisponible;
            //busca por un empleado disponible
            empleadoDisponible = encontrarEmpleadoDisponible();
            //Crea una nueva llamada sacando de la cola la primera llamada y el empleado disponible
            Call call = new Call(calls.poll(), empleadoDisponible.get());
            //Procesa la llamada
            call.answerCall();
            semaphore.release();
            llamadasFinalizadas++;
            
            }catch (Exception  e) {
                Thread.currentThread().interrupt();
            }
        });
        
    }
    
  
    
    /**
     * BUsca un empleado disponible en orden, OPERADOR -> SUPERVISOR -> DIRECTOR
     *
     * @return Un empleado disponible
     */
    public synchronized Optional<Empleado> encontrarEmpleadoDisponible() {
        Optional<Empleado> result = empleados.stream()
                .filter(empleado -> empleado.isLibre() && empleado.getType() == TipoEmpleado.OPERADOR)
                .findFirst();
        if (!result.isPresent()) {
            result = empleados.stream()
                    .filter(empleado -> empleado.isLibre() && empleado.getType() == TipoEmpleado.SUPERVISOR)
                    .findFirst();
            if (!result.isPresent()) {
                result = empleados.stream()
                        .filter(empleado -> empleado.isLibre() && empleado.getType() == TipoEmpleado.DIRECTOR)
                        .findFirst();
            }
        }
        result.ifPresent(empleado -> empleado.setLibre(false));
        return result;
    }
    
    /**
     * Esperara a que termine el executor
     */
    public void esperarFin() {
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            logger.error("Interrupted Exception",e);
            Thread.currentThread().interrupt();
        }
    }


	public List<Empleado> getEmpleados() {
		return empleados;
	}


	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	
    public void stopExecutor() {
        executor.shutdown();
    }

	public int getLlamadasFinalizadas() {
		return llamadasFinalizadas;
	}

	public void setLlamadasFinalizadas(int llamadasFinalizadas) {
		this.llamadasFinalizadas = llamadasFinalizadas;
	}

	


	


}

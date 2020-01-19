package cam.almundo.modelo;

import java.util.OptionalInt;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.utils.Constantes;

public class Call {
	
    private static final Logger logger = LoggerFactory.getLogger(Call.class);

	
	private int duracion;
    private String callId;
    private Empleado empleado;
    
    public Call(String callId, Empleado empleado) {
        this.duracion = getCallDuracion();
        this.callId = callId;
        this.setEmpleado(empleado);
    }
    
    /**
     * Calcula una duracion random para cada llamada
     *
     * @return el valor calculado
     */
    private int getCallDuracion() {
        int minCallDuracion = Constantes.LLAMADA_MIN_TIME;
        int maxCallDuracion = Constantes.LLAMADA_MAX_TIME;
        OptionalInt callDuracion;
        do {
        	callDuracion = new Random().ints(minCallDuracion, (maxCallDuracion + 1)).limit(1).findFirst();
        } while (!callDuracion.isPresent());
        return callDuracion.getAsInt();
    }
    
    
    /**
     * Procesa el llamado, espera la duracion del mismo apra avisar que finalizo y por ultimo libera el empleado
     */
    public void answerCall() {
        try {
            logger.info("++++++++Atendiendo llamado: {} Empleado: {}", callId, empleado.getId());
            TimeUnit.SECONDS.sleep(duracion);
            logger.info("--------Llamado finalizado: {} Empleado: {} duracion: {}", callId, empleado.getId(), duracion);
            empleado.setLibre(true);
        } catch (InterruptedException e) {
            logger.error("Interrupted Exception",e);
            Thread.currentThread().interrupt();
        }
    }
    
    
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}



}

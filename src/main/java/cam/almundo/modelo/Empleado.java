package cam.almundo.modelo;


public class Empleado {
	
	private String id;
    private TipoEmpleado tipo;
    private boolean libre;
    
    public Empleado(String id, TipoEmpleado tipo, boolean libre) {
        this.id = id;
        this.tipo = tipo;
        this.libre = libre;
    }
    
    
	public Empleado(String id, TipoEmpleado tipo) {
        this.id = id;
        this.tipo = tipo;
    }


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TipoEmpleado getType() {
		return tipo;
	}
	public void setType(TipoEmpleado tipo) {
		this.tipo = tipo;
	}
	public boolean isLibre() {
		return libre;
	}
	public void setLibre(boolean libre) {
		this.libre = libre;
	}


}

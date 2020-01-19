package cam.almundo.modelo;


public enum TipoEmpleado {
	OPERADOR("Operador"),
    SUPERVISOR("Supervisor"),
    DIRECTOR("Director");

    private String tipo;

    TipoEmpleado(String tipo) {
        this.tipo = tipo;
    }

    public String tipo() {
        return tipo;
    }
	
}





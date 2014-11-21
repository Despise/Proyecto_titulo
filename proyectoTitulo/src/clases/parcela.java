package clases;

public class parcela {

	private String id;
	private String nombre;
	private String descripcion;
	private String precio;
	private String direccion;
	private String tipoEstado;
	

	public parcela() {
		super();
		// TODO Auto-generated constructor stub
	}

	public parcela(String id, String nombre, String descripcion, String precio,
			String direccion, String tipoEstado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.direccion = direccion;
		this.tipoEstado = tipoEstado;
	}
	public parcela(String id, String nombre, String tipoEstado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipoEstado = tipoEstado;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTipoEstado() {
		return tipoEstado;
	}
	public void setTipoEstado(String tipoEstado) {
		this.tipoEstado = tipoEstado;
	}
	
	@Override
	public String toString() {
		return this.nombre +" "+ this.id;
	}
	/*
	public String toString() {
		return "parcela [id=" + id + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", precio=" + precio + ", direccion="
				+ direccion + ", tipoEstado=" + tipoEstado + "]";
	}
	*/
}

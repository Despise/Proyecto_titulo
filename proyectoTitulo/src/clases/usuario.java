package clases;

public class usuario {

	private int id;
	private String nick;
	private String nombre;
	private String apellido;
	private String correo;
	private String pass;
	private int tipoPerfil;
	
	public usuario() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(int tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	@Override
	public String toString() {
		return "usuario [id=" + id + ", nick=" + nick + ", nombre=" + nombre
				+ ", apellido=" + apellido + ", correo=" + correo + ", pass="
				+ pass + ", tipoPerfil=" + tipoPerfil + "]";
	}
	
	
	
}

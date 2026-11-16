public class Libro {
    private final int codigo;
    private String titulo;
    private String autor;
    private String genero;
    private int anio;
    private int meGusta;

    public Libro(int codigo, String titulo, String autor, String genero, int anio, int meGusta) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anio = anio;
        this.meGusta = Math.max(0, meGusta);
    }

    // Constructor de 5 parámetros para nuevos registros (meGusta inicia en 0)
    public Libro(int codigo, String titulo, String autor, String genero, int anio) {
        this(codigo, titulo, autor, genero, anio, 0);
    }

    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getGenero() { return genero; }
    public int getAnio() { return anio; }
    public int getMeGusta() { return meGusta; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setAnio(int anio) { this.anio = anio; }

    public void incrementarMeGusta() {
        meGusta++;
    }

    @Override
    public String toString() {
        return codigo + " | " + titulo + " | " + autor + " | Me gusta: " + meGusta;
    }
}
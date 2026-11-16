import java.util.ArrayList;

public class Lista {
    private final ArrayList<Libro> biblioteca;

    public Lista(){
        biblioteca=new ArrayList<>();
    }

    public void agregar(Libro dato) throws Exception {
        if (unico(dato)){
            biblioteca.add(dato);
            ordenar();
        }else
            throw new Exception("Valor duplicado");
    }

    public boolean unico(Libro aux){

        for (Libro l : biblioteca)
            if (l.getCodigo()==aux.getCodigo())
                return false;
        return true;
    }


    public void ordenar(){
        Libro aux;
        for(int i =0; i<biblioteca.size()-1; i++)
            for (int j=i+1; j<biblioteca.size(); j++)
                if (biblioteca.get(i).getCodigo()>
                        biblioteca.get(j).getCodigo()){
                    aux=biblioteca.get(i);
                    biblioteca.set(i,biblioteca.get(j));
                    biblioteca.set(j,aux);
                }

    }

    public ArrayList<Libro> buscar(String texto){
        ArrayList<Libro> resultado=new ArrayList<>();

        for(Libro aux:biblioteca){
            if(aux.getTitulo().contains(texto)){
                resultado.add(aux);
            }
        }
        return resultado;
    }

    public int binario(int codigo){
        if (codigo<1)
            return -1;
        int tam=biblioteca.size()-1;
        if(tam<0)
            return -1;
        if(codigo<biblioteca.get(0).getCodigo()
                ||codigo>biblioteca.get(tam).getCodigo())
            return -1;
        int i=0;
        int s=tam;
        int c;
        while(i<=s){
            c=(i+s)/2;
            int valor=biblioteca.get(c).getCodigo();
            if(codigo==valor){
                return c;
            }else if(codigo>valor){
                i=c+1;
            }else{
                s=c-1;
            }
        }
        return -1;

    }
    public void validarEdicion(String titulo, String autor, String genero, int anio) throws Exception {
        if (titulo == null || titulo.isEmpty()) {
            throw new Exception("El título no puede estar vacío.");
        }
        if (autor == null || autor.isEmpty()) {
            throw new Exception("El autor no puede estar vacío.");
        }
        if (genero == null || genero.isEmpty()) {
            throw new Exception("El género no puede estar vacío.");
        }
        if (anio < 0) {
            throw new Exception("El año de publicación no puede ser negativo.");
        }
    }

    public Libro editar(int codigo, String titulo, String autor, String genero, int anio) throws Exception {
        int post = binario(codigo);
        if (post == -1)
            throw new Exception("Libro no encontrado en el codigo: " + codigo);
        validarEdicion(titulo, autor, genero, anio);
        Libro libro=biblioteca.get(post);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setGenero(genero);
        libro.setAnio(anio);
        return libro;
    }


    public void darMeGusta(int codigo) {
        int indice = buscarIndicePorCodigo(codigo);   // la binaria de P4
        if (indice == -1) {
            throw new IllegalArgumentException("No existe un libro con el código " + codigo);
        }
        libros.get(indice).incrementarMeGusta();
    }

    public ArrayList<Libro> obtenerRanking() {
        ArrayList<Libro> ranking = new ArrayList<>(libros);   // copia: mismos objetos, no toca la principal
        int n = ranking.size();
        for (int i = 0; i < n - 1; i++) {
            boolean huboCambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (ranking.get(j).getMeGusta() < ranking.get(j + 1).getMeGusta()) {  // < estricto
                    Libro temp = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, temp);
                    huboCambio = true;
                }
            }
            if (!huboCambio) break;
        }
        return ranking;
    }
    public ArrayList<Libro> obtener(){
        return biblioteca;
    }

}

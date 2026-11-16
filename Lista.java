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

    public ArrayList<Libro> obtener(){
        return biblioteca;
    }

}

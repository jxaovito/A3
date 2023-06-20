import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) {
//        new Tela();

        List<Integer> lista;

        lista = new ArrayList<>();

        lista.add(1);
        lista.add(2);
        lista.add(5);

        print(lista);

        }
        private static void print(List<Integer> item){
        for(int i = 0; i < item.size(); i++){
            System.out.println(item.get(i));
        }
            Aluno i = new Aluno("Jose Francisco", "123.456.789-00", "d", "dd@", "47", "4");
            System.out.println("Veja como os atributos foram preenchidos\n\nNome: " + i.getNomePessoa());
            System.out.println("CPF: " + i.getCpfPessoa());

            Tela1 primeiratela = new Tela1(null);
        }




    }

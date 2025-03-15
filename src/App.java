import java.util.Collections; 
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        String caminhoArquivo = "ordExt_teste.txt";
        Leitor leitor = new Leitor(caminhoArquivo);
        leitor.lerArquivo();


        List<Double> dados = leitor.carregarDadosOrdenados();
        BTree arvoreB = new BTree(3);
        arvoreB.carregamentoEmMassa(dados);

        testarOperacoes(arvoreB, dados);
    }

    private static void testarOperacoes(BTree arvoreB, List<Double> dados) {
        // Teste de busca
        double valorBusca = 100.5;
        System.out.println("Busca " + valorBusca + ": " + arvoreB.buscar(valorBusca));

        // Teste de inserção
        double valorInsercao = 200.7;
        arvoreB.inserir(valorInsercao);
        int posicao = Collections.binarySearch(dados, valorInsercao);
        if (posicao < 0) posicao = -posicao - 1;
        dados.add(posicao, valorInsercao);
        System.out.println("Inserção " + valorInsercao + " confirmada: " + arvoreB.buscar(valorInsercao));

        // Teste de remoção
        double valorRemocao = 50.3;
        arvoreB.remover(valorRemocao);
        posicao = Collections.binarySearch(dados, valorRemocao);
        if (posicao >= 0) dados.remove(posicao);
        System.out.println("Remoção " + valorRemocao + " confirmada: " + !arvoreB.buscar(valorRemocao));
    }
}
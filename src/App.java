import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class App {
    public static void main(String[] args) {
        long inicio = System.nanoTime(); // Início da medição do tempo

        try {
            String caminhoArquivo = "ordExt_teste.txt";
            Leitor leitor = new Leitor(caminhoArquivo);
            leitor.lerArquivo();

            List<Double> dados = leitor.carregarDadosOrdenados();
            BTree arvoreB = new BTree(3);
            arvoreB.carregamentoEmMassa(dados);

            System.out.println("=== Estrutura da Árvore B ===");
            arvoreB.imprimirArvore();

            testarOperacoes(arvoreB, dados);
        } catch (IOException e) {
            System.err.println("Erro ao manipular arquivos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro na árvore B: " + e.getMessage());
        }

        long fim = System.nanoTime(); // Fim da medição do tempo
        double tempoExecucao = (fim - inicio) / 1_000_000_000.0; // Converter para segundos
        System.out.printf("Tempo de execução: %.4f segundos%n", tempoExecucao);
    }

    private static void testarOperacoes(BTree arvoreB, List<Double> dados) {
        // Teste de inserção
        double valorInsercao = 200.7;
        arvoreB.inserir(valorInsercao);
        int posicao = Collections.binarySearch(dados, valorInsercao);
        if (posicao < 0) posicao = -posicao - 1;
        dados.add(posicao, valorInsercao);
        System.out.println("Inserção " + valorInsercao + " confirmada: " + arvoreB.buscar(valorInsercao));

        // Teste de busca
        double valorBusca = 200.7;
        System.out.println("Busca " + valorBusca + ": " + arvoreB.buscar(valorBusca));

        // Teste de remoção
        double valorRemocao = 50.3;
        arvoreB.remover(valorRemocao);
        posicao = Collections.binarySearch(dados, valorRemocao);
        if (posicao >= 0) dados.remove(posicao);
        System.out.println("Remoção " + valorRemocao + " confirmada: " + !arvoreB.buscar(valorRemocao));

        // Testes adicionais
        testarCasosExtremos(arvoreB, dados);
    }

    private static void testarCasosExtremos(BTree arvoreB, List<Double> dados) {
        // Teste com valor mínimo
        double valorMinimo = Double.MIN_VALUE;
        arvoreB.inserir(valorMinimo);
        System.out.println("Inserção " + valorMinimo + " confirmada: " + arvoreB.buscar(valorMinimo));

        // Teste com valor máximo
        double valorMaximo = Double.MAX_VALUE;
        arvoreB.inserir(valorMaximo);
        System.out.println("Inserção " + valorMaximo + " confirmada: " + arvoreB.buscar(valorMaximo));

        // Teste com valor duplicado
        double valorDuplicado = 200.7;
        arvoreB.inserir(valorDuplicado);
        System.out.println("Inserção duplicada " + valorDuplicado + " confirmada: " + arvoreB.buscar(valorDuplicado));
    }
}
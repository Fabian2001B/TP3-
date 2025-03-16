import java.util.List;
import java.util.ArrayList;

public class BTree {
    private int ordem;
    private BNo raiz;

    public BTree(int ordem) {
        if (ordem < 2) {
            throw new IllegalArgumentException("A ordem da árvore B deve ser maior ou igual a 2.");
        }
        this.ordem = ordem;
        this.raiz = new BNo(true);
    }

    private class BNo {
        List<Double> chaves;
        List<BNo> filhos;
        boolean folha;

        BNo(boolean folha) {
            this.folha = folha;
            this.chaves = new ArrayList<>();
            this.filhos = new ArrayList<>();
        }
    }

    /**
     * Carrega uma lista de chaves ordenadas na árvore B de forma eficiente.
     *
     * @param listaOrdenada Lista de chaves já ordenadas.
     */
    public void carregamentoEmMassa(List<Double> listaOrdenada) {
        System.out.println("Total de chaves a serem inseridas: " + listaOrdenada.size());

        // Constrói a árvore B diretamente a partir da lista ordenada
        raiz = construirArvore(listaOrdenada, 0, listaOrdenada.size() - 1, true);
        System.out.println("Carregamento em massa concluído.");
    }

    /**
     * Constrói a árvore B recursivamente a partir de uma lista ordenada.
     *
     * @param listaOrdenada Lista de chaves já ordenadas.
     * @param inicio        Índice inicial da sublista.
     * @param fim           Índice final da sublista.
     * @param folha         Indica se o nó atual é uma folha.
     * @return Nó raiz da subárvore construída.
     */
    private BNo construirArvore(List<Double> listaOrdenada, int inicio, int fim, boolean folha) {
        if (inicio > fim) {
            return null;
        }

        // Criar um novo nó
        BNo no = new BNo(folha);

        // Se for um nó folha, preencher com as chaves diretamente
        if (folha) {
            no.chaves.addAll(listaOrdenada.subList(inicio, fim + 1));
        } else {
            // Se for um nó interno, dividir a lista em partes iguais e criar filhos recursivamente
            int tamanho = fim - inicio + 1;
            int chavesPorNo = 2 * ordem - 1; // Capacidade máxima de chaves por nó
            int numFilhos = (tamanho + chavesPorNo - 1) / chavesPorNo; // Número de filhos necessários

            // Dividir a lista em partes iguais para cada filho
            for (int i = 0; i < numFilhos; i++) {
                int inicioFilho = inicio + i * chavesPorNo;
                int fimFilho = Math.min(inicioFilho + chavesPorNo - 1, fim);

                // Adicionar a chave mediana ao nó atual
                if (i > 0) {
                    no.chaves.add(listaOrdenada.get(inicioFilho - 1));
                }

                // Criar o filho recursivamente
                BNo filho = construirArvore(listaOrdenada, inicioFilho, fimFilho, i == numFilhos - 1);
                no.filhos.add(filho);
            }
        }

        return no;
    }

    // Métodos de busca, inserção, remoção e impressão (mantidos da versão anterior)
    public boolean buscar(double chave) {
        return buscar(raiz, chave);
    }

    private boolean buscar(BNo no, double chave) {
        int i = 0;
        while (i < no.chaves.size() && chave > no.chaves.get(i)) i++;
        if (i < no.chaves.size() && chave == no.chaves.get(i)) return true;
        if (no.folha) return false;
        return buscar(no.filhos.get(i), chave);
    }

    public void inserir(double chave) {
        BNo noRaiz = raiz;
        if (noRaiz.chaves.size() == 2 * ordem - 1) {
            BNo novoNoRaiz = new BNo(false);
            raiz = novoNoRaiz;
            novoNoRaiz.filhos.add(noRaiz);
            dividirFilho(novoNoRaiz, 0);
            inserirNaoCheio(novoNoRaiz, chave);
        } else {
            inserirNaoCheio(noRaiz, chave);
        }
    }

    private void inserirNaoCheio(BNo no, double chave) {
        int i = no.chaves.size() - 1;
        if (no.folha) {
            while (i >= 0 && chave < no.chaves.get(i)) i--;
            no.chaves.add(i + 1, chave);
        } else {
            while (i >= 0 && chave < no.chaves.get(i)) i--;
            i++;
            BNo filho = no.filhos.get(i);
            if (filho.chaves.size() == 2 * ordem - 1) {
                dividirFilho(no, i);
                if (chave > no.chaves.get(i)) i++;
            }
            inserirNaoCheio(no.filhos.get(i), chave);
        }
    }

    private void dividirFilho(BNo pai, int indice) {
        BNo filho = pai.filhos.get(indice);
        BNo novoFilho = new BNo(filho.folha);
        pai.chaves.add(indice, filho.chaves.get(ordem - 1));
        pai.filhos.add(indice + 1, novoFilho);

        novoFilho.chaves.addAll(filho.chaves.subList(ordem, 2 * ordem - 1));
        filho.chaves.subList(ordem - 1, 2 * ordem - 1).clear();

        if (!filho.folha) {
            novoFilho.filhos.addAll(filho.filhos.subList(ordem, 2 * ordem));
            filho.filhos.subList(ordem, 2 * ordem).clear();
        }
    }

    public void remover(double chave) {
        remover(raiz, chave);
        if (raiz.chaves.isEmpty() && !raiz.folha) {
            raiz = raiz.filhos.get(0);
        }
    }

    private void remover(BNo no, double chave) {
        int indice = encontrarIndice(no, chave);

        if (indice < no.chaves.size() && no.chaves.get(indice) == chave) {
            if (no.folha) {
                no.chaves.remove(indice);
            } else {
                BNo filhoEsquerdo = no.filhos.get(indice);
                BNo filhoDireito = no.filhos.get(indice + 1);

                if (filhoEsquerdo.chaves.size() >= ordem) {
                    double predecessor = encontrarPredecessor(filhoEsquerdo);
                    no.chaves.set(indice, predecessor);
                    remover(filhoEsquerdo, predecessor);
                } else if (filhoDireito.chaves.size() >= ordem) {
                    double successor = encontrarSuccessor(filhoDireito);
                    no.chaves.set(indice, successor);
                    remover(filhoDireito, successor);
                } else {
                    merge(no, indice);
                    remover(filhoEsquerdo, chave);
                }
            }
        } else {
            if (no.folha) return;

            BNo filho = no.filhos.get(indice);
            if (filho.chaves.size() < ordem) {
                if (indice > 0 && no.filhos.get(indice - 1).chaves.size() >= ordem) {
                    emprestarDoEsquerdo(no, indice);
                } else if (indice < no.filhos.size() - 1 && no.filhos.get(indice + 1).chaves.size() >= ordem) {
                    emprestarDoDireito(no, indice);
                } else {
                    if (indice < no.filhos.size() - 1) {
                        merge(no, indice);
                    } else {
                        merge(no, indice - 1);
                        indice--;
                    }
                }
            }
            remover(no.filhos.get(indice), chave);
        }
    }

    private int encontrarIndice(BNo no, double chave) {
        int i = 0;
        while (i < no.chaves.size() && chave > no.chaves.get(i)) i++;
        return i;
    }

    private double encontrarPredecessor(BNo no) {
        while (!no.folha) {
            no = no.filhos.get(no.filhos.size() - 1);
        }
        return no.chaves.get(no.chaves.size() - 1);
    }

    private double encontrarSuccessor(BNo no) {
        while (!no.folha) {
            no = no.filhos.get(0);
        }
        return no.chaves.get(0);
    }

    private void emprestarDoEsquerdo(BNo pai, int indice) {
        BNo filho = pai.filhos.get(indice);
        BNo irmaoEsquerdo = pai.filhos.get(indice - 1);

        filho.chaves.add(0, pai.chaves.get(indice - 1));
        pai.chaves.set(indice - 1, irmaoEsquerdo.chaves.remove(irmaoEsquerdo.chaves.size() - 1));

        if (!filho.folha) {
            filho.filhos.add(0, irmaoEsquerdo.filhos.remove(irmaoEsquerdo.filhos.size() - 1));
        }
    }

    private void emprestarDoDireito(BNo pai, int indice) {
        BNo filho = pai.filhos.get(indice);
        BNo irmaoDireito = pai.filhos.get(indice + 1);

        filho.chaves.add(pai.chaves.get(indice));
        pai.chaves.set(indice, irmaoDireito.chaves.remove(0));

        if (!filho.folha) {
            filho.filhos.add(irmaoDireito.filhos.remove(0));
        }
    }

    private void merge(BNo pai, int indice) {
        BNo esquerdo = pai.filhos.get(indice);
        BNo direito = pai.filhos.get(indice + 1);

        esquerdo.chaves.add(pai.chaves.remove(indice));
        esquerdo.chaves.addAll(direito.chaves);

        if (!esquerdo.folha) {
            esquerdo.filhos.addAll(direito.filhos);
        }
        pai.filhos.remove(indice + 1);
    }

    public void imprimirArvore() {
        imprimirNo(raiz, 0);
    }
    
    private void imprimirNo(BNo no, int nivel) {
        if (no == null) {
            System.out.println("Nível " + nivel + ": (vazio)");
            return;
        }
    
        // Indentação para representar o nível da árvore
        StringBuilder indentacao = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            indentacao.append("    "); // 4 espaços por nível
        }
    
        // Exibe as chaves do nó
        System.out.print(indentacao + "Nível " + nivel + ": [");
        for (int i = 0; i < no.chaves.size(); i++) {
            System.out.print(no.chaves.get(i));
            if (i < no.chaves.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    
        // Recursão para os filhos
        if (!no.folha) {
            for (BNo filho : no.filhos) {
                imprimirNo(filho, nivel + 1);
            }
        }
    }
}
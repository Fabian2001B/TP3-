import java.util.List;
import java.util.ArrayList;

public class BTree {
    private int ordem;
    private BNo raiz;

    public BTree(int ordem) {
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

    public void carregamentoEmMassa(List<Double> listaOrdenada) {
        List<BNo> folhas = criarFolhas(listaOrdenada);
        raiz = construirArvore(folhas);
    }

    private List<BNo> criarFolhas(List<Double> listaOrdenada) {
        List<BNo> folhas = new ArrayList<>();
        int maximo = 2 * ordem - 1;
        int i = 0;
        while (i < listaOrdenada.size()) {
            BNo folha = new BNo(true);
            int fim = Math.min(i + maximo, listaOrdenada.size());
            folha.chaves.addAll(listaOrdenada.subList(i, fim));
            folhas.add(folha);
            i = fim;
        }
        return folhas;
    }

    private BNo construirArvore(List<BNo> nos) {
        if (nos.size() == 1) return nos.get(0);

        List<BNo> pais = new ArrayList<>();
        int maximoFilhos = 2 * ordem;
        int i = 0;

        while (i < nos.size()) {
            BNo pai = new BNo(false);
            int fim = Math.min(i + maximoFilhos, nos.size());
            List<BNo> filhos = nos.subList(i, fim);

            pai.filhos.add(filhos.get(0));
            for (int j = 1; j < filhos.size(); j++) {
                pai.chaves.add(filhos.get(j).chaves.get(0));
                pai.filhos.add(filhos.get(j));
            }

            pais.add(pai);
            i = fim;
        }

        return construirArvore(pais);
    }

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
}
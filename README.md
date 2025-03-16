Relatório de Implementação da Árvore B no TP2 

Objetivo 

Este projeto teve como objetivo integrar uma árvore B ao código existente do TP2, que realiza ordenação externa. Buscamos permitir buscas, inserções e remoções eficientes em grandes conjuntos de dados, aproveitando a ordenação para construir a estrutura de forma otimizada. 

Desenvolvimento e Etapas de Implementação 

Implementação da Árvore B 

Inicialmente, estudamos a estrutura da árvore B e implementamos sua representação básica, incluindo a criação de nós e a inserção de elementos sequenciais. 

Desenvolvimento dos Métodos de Busca, Inserção e Remoção 

Criamos os métodos essenciais para manipular os dados na árvore, garantindo a manutenção das propriedades da estrutura. 

A remoção exigiu atenção especial, pois envolve redistribuição de chaves e fusão de nós em casos de underflow. 

Carregamento de Dados em Memória 

Ajustamos o método lerArquivo() para carregar os dados ordenados diretamente em memória, eliminando a necessidade de arquivos temporários intermediários. 

Implementação do Bulk Loading 

Para evitar a inserção sequencial, que seria ineficiente, desenvolvemos o método carregamentoEmMassa(), que divide a lista ordenada em folhas e constrói a árvore de forma hierárquica. 

Mudança na Estrutura de Ordenação 

Inicialmente, substituímos o Heap Sort por Arrays.sort(), esperando melhorias no tempo de execução. 

No entanto, percebemos que Arrays.sort() não apresentava melhor desempenho para nosso conjunto de dados, pois sua implementação interna não era otimizada para o tamanho e distribuição dos dados utilizados. Dessa forma, retornamos ao Heap Sort, que apresentou melhor estabilidade e eficiência em nosso caso específico. 

Verificação da Árvore B 

Para garantir a correta inserção e organização dos elementos, implementamos o método imprimirArvore(), permitindo visualizar a estrutura e identificar falhas no armazenamento. 

Estudo sobre Random Access File 

Investigamos a possibilidade de utilizar RandomAccessFile para conseguirmos implementar o Desafio do TP 3, pois pelo que vimos ele troca o armazenamento dos arquivos em si pelas suas posições na memoria. 

No entanto, devido à complexidade de implementação e ficarmos perdidos tentando implementar essa abordagem, acabou que ela não foi utilizada, mas consideramos que pode ser um caminho promissor para otimizar o acesso a grandes volumes de dados e conseguir realizar o desafio. 

Desempenho e Ajustes no VS Code 

Durante os testes, enfrentamos lentidão no VS Code ao lidar com grandes arquivos e muitas operações de depuração. 

Para contornar isso otimizamos o código para reduzir a carga de processamento durante a depuração. 

 

Decisões de Projeto 

Bulk Loading: Optamos por criar as folhas diretamente e construir a árvore de baixo para cima, pois a inserção sequencial seria ineficiente. 

Escolha do Grau t: Configuramos a árvore como BTree(3), buscando um equilíbrio entre altura e desempenho. Essa escolha foi feita com base no fato de que um grau menor resulta em mais divisões de nós, enquanto um grau muito alto pode desperdiçar espaço e aumentar o tempo de busca e tambem é o padrão usado em aula. 

Testes com Arquivo em Memória: Conseguimos subir o arquivo todo em memoria. 

 

Testes e Resultados: 

Após a execução da ordenação, os arquivos foram devidamente armazenados e utilizados para testar os métodos da árvore B. Durante a execução no terminal, nós  acompanhamos logs detalhados das operações, que foram divididas da seguinte forma: 

Ordenação Concluída: O terminal nos confirmou que os arquivos foram ordenados e armazenados corretamente, sinalizando que a etapa inicial foi bem-sucedida. 

Inserção: Observamos a exibição dos elementos sendo inseridos na árvore B, acompanhados pelos ajustes na estrutura da árvore para manter as propriedades de balanceamento. 

Busca: Realizamos algumas pesquisas na árvore, com o terminal informando se os elementos foram encontrados, o que nos ajudou a verificar a eficiência das operações de busca. 

Remoção: Testamos a exclusão de elementos, e o terminal nos mostrou como a árvore redistribuía ou fundia os nós quando necessário, mantendo a integridade da estrutura. 

Os testes confirmaram que a árvore B manteve suas propriedades estruturais e executou as operações de forma eficiente, o que validou nossa integração com o TP2, garantindo que os métodos funcionaram como esperado. 

 

Conclusão 

Dificuldades Superadas: 

Integração da ordenação externa com a árvore B. 

Implementação do bulk loading para criação eficiente da árvore. 

Desenvolvimento das operações de busca, inserção e remoção na árvore B. 

Retorno ao Heap Sort após testes com Arrays.sort(). 

Depuração e verificação da estrutura da árvore B. 

Desafios Pendentes: 

Implementar o acesso direto a blocos de disco utilizando RandomAccessFile ou outra abordagem eficiente. 

Otimizar o desempenho para grandes volumes de dados, garantindo que a estrutura seja escalável e eficiente. 

 

 

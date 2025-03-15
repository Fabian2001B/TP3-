# Ordenação Externa - Prog 4
> Este repositório contém o trabalho prático 2 da disciplina de Programação 4, ministrada pela professora Kenia Carolina.

* **Para este trabalho, nos organizamos da seguinte forma:**

1. Reunião inicial e definição da abordagem: Nos reunimos para debater ideias de implementação sólidas para o problema, discutindo as melhores soluções apresentadas. Após as discussões, decidimos seguir com a seguinte ideia:

    1.1. Dividir o arquivo em partes de 100 em 100 linhas, realizar a intercalação e, por último, remover os arquivos criados, exceto o resultado final.

2. Implementação:
Para a implementação, adotamos uma estrutura simples de leitura com um loop while, onde a cada 100 linhas (ou seja, 100 iterações), ordenamos os valores e criamos um arquivo, nomeando-os de forma incremental a partir de 0.

        2.1. Desafio na nomeação dos arquivos:
        Esta etapa se provou como o primeiro desafio no seguinte sentido: visto que os arquivos seriam nomeados incrementalmente, seria necessário usar uma outra notação para nomear os arquivos resultantes das intercalações? Pois os arquivos estariam compartilhando a mesma pasta e poderiam acabar se misturando, atrapalhando o resultado das intercalações.
        Apesar disso, concluímos que, com a abordagem adotada (criar os arquivos e depois fazer a intercalação), não haveria problema em nomeá-los da mesma forma, já que eles estariam salvos no final da lista de arquivos e seguiriam a sequência de intercalação corretamente.

        2.2. Início da intercalação:
        Com os arquivos divididos e devidamente ordenados, demos início à intercalação. A intercalação é sustentada por um loop while que roda enquanto a quantidade de arquivos for maior que 0 e o índice (variável que guarda a partir de onde iremos intercalar os próximos arquivos) for menor que a quantidade de arquivos, já que, caso fosse maior, isso indicaria que já intercalamos todos os arquivos disponíveis.

        2.3. Processo de intercalação:
        Para a intercalação, criamos um array de BufferedReaders (sugestão da professora), onde cada posição guarda um leitor para cada um dos 10 próximos arquivos. Além disso, temos um array de Doubles chamado Comparador, que é usado para salvar os valores de cada BufferedReader. Buscamos o menor número no Comparador e, ao encontrá-lo, salvamos ele em uma lista que será usada para criar o novo arquivo. Salvamos o índice do arquivo onde foi encontrado o menor número e adicionamos seu próximo valor no Comparador, repetindo o processo até que o índice volte a ser -1, ou seja, não foi alterado. Após isso, escrevemos o novo arquivo.

        2.4. Finalização e exclusão de arquivos:
        Para finalizar o código, chamamos a função deletarArquivos() para excluir todos os arquivos criados, exceto pelo último, que é o resultado da intercalação final. Este método, apesar de simples, foi complexo de implementar, pois na primeira versão do programa não tínhamos pensado em uma forma de saber quantos arquivos deveriam ser apagados, então acabávamos excluindo todos, incluindo o resultado final. Foi a partir deste problema que implementamos a variável blocos, que nos permitiu acompanhar a quantidade de arquivos criados e, assim, permitir que o código apagasse a quantidade certa de arquivos até sobrar apenas um.

## Desafios encontrados:
> Durante o trabalho, nosso grupo encontrou desafios que exigiram soluções criativas e engenhosas. Aqui estão alguns deles:

* **Leitura de múltiplos arquivos simultaneamente:**
Antes da ideia de criar um array de BufferedReaders, precisávamos de uma forma de preencher o Comparador. A ideia inicial era usar apenas um Reader que mudava a cada iteração de um while. Porém, isso nos levou a vários problemas, pois o Reader resetava a leitura a cada vez que era declarado, além de dificultar o acesso às posições de cada arquivo. Para este problema, a solução apresentada pela professora de criar um array de BufferedReaders foi suficiente.

* **Manter a sequência de leitura de arquivos para intercalação estável:**
Em diversos casos, a leitura se perdia devido à criação de novos arquivos. Os novos arquivos criavam uma assimetria na leitura da pasta, levando a null pointers, repetições e, em alguns casos, duplicatas de arquivos. Para este problema, a solução usada no método deletarArquivos() foi suficiente. A variável blocos se tornou tão útil ao código que foi promovida para um campo da classe, podendo ser acessada por todos os métodos, reestruturando o código de uma forma mais prática e sequencial, onde todos os métodos mantinham registros de suas ações através desta variável.

* **Controlar a quantidade de arquivos a serem intercalados:**
Perto do final da execução do código, o método de intercalação tentava pegar 10 arquivos para fazer a intercalação. Porém, em alguns casos, restavam menos do que 10 arquivos, desencadeando uma série de problemas. A cada solução, um novo problema aparecia: loops infinitos, null pointers, duplicatas, etc. A solução deste problema, apesar de simples, exigiu muito esforço mental. Precisávamos notar o padrão que levava a cada tipo de erro que surgia neste trecho do código e localizar o núcleo do problema, que era a contagem de arquivos. Este problema surgiu quando a variável blocos tinha acabado de ser criada, ainda como variável interna de método. Assim, não pensamos na possibilidade de usá-la como solução para este problema. Após notarmos que ela poderia ser usada como a indexadora e contadora principal da classe, conseguimos calcular a quantidade de arquivos que restavam na pasta e adaptar a criação dos Readers de acordo, resolvendo o problema.

# Desafio ainda não solucionado e sua possível solução:
> Existem alguns pontos que podem ser melhorados no código, mas que, por hora, não serão analisados. Aqui está um deles:

* **Problemas de desempenho com grandes volumes de dados:**
Devido à quantidade massiva de dados no arquivo de teste da professora, o VS Code encontrou problemas ao rodar o código, resultando em crashes e lentidão. Para contornar o problema, rodamos o código diretamente no PowerShell do Windows, o que liberou mais recursos para o programa e resolveu as interrupções no código.
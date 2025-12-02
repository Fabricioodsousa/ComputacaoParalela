Aluno: Fabrício Oliveira de Sousa - 2310346

## Como Executar o Projeto

1. Clone o repositório

git clone https://github.com/Fabricioodsousa/ComputacaoParalela.git

2. Entre na pasta do projeto

cd SEU_REPOSITORIO

3. Execute a Main

4. Resultados

Os arquivos CSV com os tempos e análises ficarão na pasta: /Resultados


## Relatório de Análise de Desempenho — Algoritmos de Busca Serial e Paralela (CPU/GPU)

# Resumo

Este trabalho apresenta a implementação, execução e análise estatística de algoritmos de busca em arquivos de texto utilizando abordagens seriais, paralelas via CPU (1, 2, 4 e 8 threads) e paralela via GPU. Os experimentos foram realizados sobre três obras literárias clássicas — Don Quixote, Dracula e Moby Dick — e registraram tempos de execução e número de ocorrências da palavra buscada.

Os resultados demonstram diferenças significativas entre os métodos, evidenciando como o desempenho é influenciado pelo nível de paralelização e pelo tamanho do arquivo analisado.

# Introdução

O objetivo deste trabalho é comparar o desempenho de diferentes métodos de busca em grandes conjuntos de dados textuais. Quatro métodos principais foram implementados:

SerialCPU: busca sequencial simples.

ParallelCPU(n): busca paralela utilizando n threads.

ParallelGPU: busca paralela utilizando processamento gráfico.

A abordagem visa compreender como o desempenho se altera conforme o nível de paralelismo aumenta e como CPUs e GPUs lidam com cargas de trabalho intensivas de busca.

# Metodologia

1. Implementação dos Algoritmos

Foram desenvolvidas implementações em Java para:

Busca sequencial (serial)

Busca paralela com variação de threads

Busca em GPU usando bibliotecas de paralelismo

2. Framework de Teste

Criou-se um framework responsável por:

Executar cada algoritmo 3 vezes por arquivo

Registrar tempos de execução

Contar ocorrências da palavra “the”

Salvar os resultados em arquivos CSV

3. Execução em Ambientes Variados

Os experimentos foram realizados variando:

Tipo de processamento (CPU serial, CPU paralela, GPU)

Número de threads no caso da CPU

Três arquivos com tamanhos diferentes

4. Registro de Dados

Todos os resultados foram salvos no formato CSV, permitindo posterior análise estatística com gráficos.

5. Análise Estatística

A análise investigou:

Tempo médio de execução

Variação e estabilidade do método

Escalabilidade conforme o número de threads

Comparação entre CPU serial, CPU paralela e GPU

Resultados e Discussão

1. Don Quixote — 2.1 MB — 188 ocorrências

A busca serial variou entre 117–212ms

O modo paralelo com 4 threads apresentou os melhores resultados: 72–119ms

O modo com 8 threads teve instabilidade e piorou em alguns casos

A GPU apresentou desempenho irregular (117–190ms), ficando próxima do desempenho serial

➡ Conclusão parcial: paralelização moderada (2–4 threads) gera melhor eficiência.

2. Dracula — 869KB — 8101 ocorrências

A busca serial foi bem rápida: 30–74ms

Com 2 threads, houve estabilidade com tempos entre 31–32ms

Com 4 e 8 threads houve variação maior e alguns picos

A GPU ficou em torno de 44–48ms, sem vantagem

➡ Conclusão parcial: arquivos médios não se beneficiam muito de alto paralelismo.

3. Moby Dick — 1.2 MB — 14715 ocorrências

A forma serial variou entre 51–128ms

O método com 2 e 4 threads apresentou os melhores tempos (41–82ms)

Paralelismo com 8 threads teve instabilidade

GPU manteve tempos entre 73–112ms

➡ Conclusão parcial: paralelizar até 4 threads melhora o desempenho; acima disso há sobrecarga.

# Conclusão

Este estudo demonstrou que:

Algoritmos paralelos podem melhorar o desempenho significativamente, mas apenas até certo ponto.

A GPU, embora poderosa, não é ideal para este tipo de workload.

O melhor custo-benefício foi obtido com ParallelCPU(2) e ParallelCPU(4).

O framework criado se mostrou eficaz para análise estatística e poderá ser reutilizado em estudos futuros.

A análise permitiu compreender como o desempenho varia conforme o tamanho do arquivo, número de threads e tipo de hardware.

# Link do Projeto no GitHub

https://github.com/Fabricioodsousa/ComputacaoParalela

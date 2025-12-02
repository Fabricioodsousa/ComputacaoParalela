# 📚 Relatório de Análise de Desempenho — Algoritmos de Busca Serial e Paralela (CPU/GPU)

**Aluno:** Fabrício Oliveira de Sousa - **Matrícula:** 2310346

## 🚀 Resumo Executivo

Este trabalho apresenta a implementação e análise estatística de **algoritmos de busca em arquivos de texto** utilizando abordagens **serial**, **paralela via CPU** (com 1, 2, 4 e 8 threads) e **paralela via GPU**.

Os experimentos foram realizados sobre três obras literárias clássicas (**Don Quixote, Drácula e Moby Dick**), registrando tempos de execução e o número de ocorrências da palavra buscada ("the").

### 🎯 Principais Conclusões

Os resultados demonstraram diferenças significativas entre os métodos, evidenciando que o desempenho ideal é alcançado com um **nível moderado de paralelização na CPU (2-4 threads)**. O uso da GPU se mostrou **irregular** e, em geral, **não superou** o desempenho otimizado da CPU para esta carga de trabalho específica.

---

## 💻 Como Executar o Projeto

Siga os passos abaixo para clonar o repositório e executar a aplicação:

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/Fabricioodsousa/ComputacaoParalela.git](https://github.com/Fabricioodsousa/ComputacaoParalela.git)
    ```

2.  **Entre na Pasta do Projeto:**
    ```bash
    cd ComputacaoParalela
    ```

3.  **Execute a Classe Principal (`Main`):**

4.  **Resultados da Execução:**
    * Os arquivos CSV contendo todos os tempos e análises de desempenho serão gerados e salvos na pasta:
        **`/Resultados`**

---

## 📖 Introdução e Objetivos

O principal objetivo deste trabalho é **comparar o desempenho** de diferentes estratégias de busca em grandes conjuntos de dados textuais.

### Métodos de Busca Implementados

| Método | Descrição |
| :--- | :--- |
| **SerialCPU** | Busca sequencial simples. |
| **ParallelCPU(n)** | Busca paralela utilizando **$n$** threads (onde $n \in \{1, 2, 4, 8\}$). |
| **ParallelGPU** | Busca paralela utilizando processamento gráfico (GPU). |

A análise visa compreender como o desempenho se altera conforme o nível de paralelismo aumenta e como as arquiteturas (CPU vs. GPU) lidam com cargas de trabalho intensivas de busca textual.

---

## 🛠️ Metodologia

### 1. Implementação

* Desenvolvimento de implementações em **Java** para busca sequencial, busca paralela com variação de threads e busca em GPU (utilizando bibliotecas de paralelismo).

### 2. Framework de Teste

Foi criado um *framework* para automação e registro dos testes, responsável por:
* Executar cada algoritmo **3 vezes** por arquivo para obter uma média estável.
* Registrar os **tempos de execução** (em milissegundos).
* Contar as ocorrências da palavra-alvo: **"the"**.
* Salvar todos os dados brutos no formato **CSV**.

### 3. Ambientes de Teste

| Tipo de Processamento | Níveis de Paralelismo |
| :--- | :--- |
| **CPU (Serial)** | 1 thread |
| **CPU (Paralela)** | 1, 2, 4 e 8 threads |
| **GPU (Paralela)** | Paralelismo massivo |

### 4. Análise Estatística

A análise focou em: **Tempo médio de execução**, **estabilidade do método (variação)** e **escalabilidade** conforme o aumento de threads e a comparação direta entre CPU e GPU.

---

## 📊 Resultados e Discussão

Os resultados de desempenho são apresentados a seguir, com ênfase na variação dos tempos de execução (mínimo-máximo).

### 1. Don Quixote (2.1 MB) — 188 Ocorrências

| Método | Variação de Tempo (ms) | Observação |
| :--- | :--- | :--- |
| **SerialCPU** | 117 – 212 ms | Linha de base. |
| **ParallelCPU(4)** | **72 – 119 ms** | **Melhor Desempenho.** |
| **ParallelCPU(8)** | 120 – 250 ms | Instabilidade e piora em alguns casos devido a sobrecarga. |
| **ParallelGPU** | 117 – 190 ms | Próxima do desempenho serial, pouca ou nenhuma vantagem. |

> 💡 **Conclusão Parcial:** A paralelização moderada (2–4 threads) é o ponto de maior eficiência para arquivos grandes.

### 2. Drácula (869 KB) — 8101 Ocorrências

| Método | Variação de Tempo (ms) | Observação |
| :--- | :--- | :--- |
| **SerialCPU** | 30 – 74 ms | Rápida devido ao tamanho médio do arquivo. |
| **ParallelCPU(2)** | **31 – 32 ms** | Excelente estabilidade e consistência. |
| **ParallelCPU(8)** | 35 – 70 ms | Maior variação e picos de lentidão. |
| **ParallelGPU** | 44 – 48 ms | Desempenho sem vantagem em relação à CPU paralela. |

> 💡 **Conclusão Parcial:** Arquivos médios tendem a se beneficiar pouco de alto paralelismo, sendo **2 threads** o ideal pela estabilidade e baixa sobrecarga.

### 3. Moby Dick (1.2 MB) — 14715 Ocorrências

| Método | Variação de Tempo (ms) | Observação |
| :--- | :--- | :--- |
| **SerialCPU** | 51 – 128 ms | Linha de base. |
| **ParallelCPU(2/4)** | **41 – 82 ms** | Melhores e mais consistentes tempos. |
| **ParallelCPU(8)** | 60 – 110 ms | Instabilidade perceptível. |
| **ParallelGPU** | 73 – 112 ms | Desempenho lento. |

> 💡 **Conclusão Parcial:** Paralelizar até **4 threads** melhora o desempenho significativamente; acima disso, o *overhead* (sobrecarga de gerenciamento de threads) começa a prejudicar.

---

## ✅ Conclusão Final

Este estudo demonstrou a importância de otimizar o nível de paralelismo de acordo com o *workload* e a arquitetura:

* **Eficiência da CPU:** Algoritmos paralelos na CPU melhoraram o desempenho, sendo **ParallelCPU(2) e ParallelCPU(4)** o ponto de **melhor custo-benefício** para a busca textual.
* **GPU para Busca Textual:** A GPU, embora poderosa em cálculos massivos e uniformes, não se mostrou ideal para esta tarefa de busca em arquivo, onde a latência e o *overhead* de transferência de dados se sobrepuseram ao ganho de paralelismo.
* **Reutilização:** O *framework* de teste desenvolvido provou ser eficaz para análise estatística e poderá ser facilmente reutilizado em futuros estudos de desempenho.

---

## 🔗 Código Fonte

O projeto completo está disponível no GitHub:

[https://github.com/Fabricioodsousa/ComputacaoParalela](https://github.com/Fabricioodsousa/ComputacaoParalela)
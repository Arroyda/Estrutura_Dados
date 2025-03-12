// GerenciadorTarefas.java
import java.util.*;

public class GerenciadorTarefas {
    // Fila para tarefas regulares (FIFO)
    private Queue<TarefaRegular> filaRegular = new LinkedList<>();

    // Pilha para tarefas prioritárias (LIFO)
    private Stack<TarefaPrioritaria> pilhaPrioritaria = new Stack<>();

    // Fila de prioridade para tarefas com prioridade numérica
    private PriorityQueue<TarefaComPrioridade> filaPrioridade = new PriorityQueue<>();

    // Método para adicionar tarefa regular
    public void adicionarTarefaRegular(String descricao) {
        filaRegular.add(new TarefaRegular(descricao));
    }

    // Método para adicionar tarefa prioritária
    public void adicionarTarefaPrioritaria(String descricao) {
        pilhaPrioritaria.push(new TarefaPrioritaria(descricao));
    }

    // Método para adicionar tarefa com prioridade numérica
    public void adicionarTarefaComPrioridade(String descricao, int prioridade) {
        filaPrioridade.add(new TarefaComPrioridade(descricao, prioridade));
    }

    // Método para processar a próxima tarefa
    public void processarProximaTarefa() {
        if (!pilhaPrioritaria.isEmpty()) {
            TarefaPrioritaria tarefa = pilhaPrioritaria.pop();
            System.out.println("Processando tarefa prioritária: " + tarefa);
        } else if (!filaPrioridade.isEmpty()) {
            TarefaComPrioridade tarefa = filaPrioridade.poll();
            System.out.println("Processando tarefa com prioridade numérica: " + tarefa);
        } else if (!filaRegular.isEmpty()) {
            TarefaRegular tarefa = filaRegular.poll();
            System.out.println("Processando tarefa regular: " + tarefa);
        } else {
            System.out.println("Não há tarefas pendentes.");
        }
    }

    // Método para exibir todas as tarefas pendentes
    public void exibirTarefasPendentes() {
        System.out.println("Tarefas Prioritárias:");
        for (TarefaPrioritaria tarefa : pilhaPrioritaria) {
            System.out.println("  " + tarefa);
        }

        System.out.println("Tarefas com Prioridade Numérica:");
        for (TarefaComPrioridade tarefa : filaPrioridade) {
            System.out.println("  " + tarefa);
        }

        System.out.println("Tarefas Regulares:");
        for (TarefaRegular tarefa : filaRegular) {
            System.out.println("  " + tarefa);
        }
    }
}
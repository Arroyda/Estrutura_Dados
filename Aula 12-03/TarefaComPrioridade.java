// TarefaComPrioridade.java
public class TarefaComPrioridade extends Tarefa implements Comparable<TarefaComPrioridade> {
    int prioridade;

    public TarefaComPrioridade(String descricao, int prioridade) {
        super(descricao); // Chama o construtor da classe base (Tarefa)
        this.prioridade = prioridade;
    }

    @Override
    public int compareTo(TarefaComPrioridade outra) {
        return Integer.compare(this.prioridade, outra.prioridade);
    }

    @Override
    public String toString() {
        return super.toString() + " [Prioridade: " + prioridade + "]";
    }
}
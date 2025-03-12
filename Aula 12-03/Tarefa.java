// Tarefa.java
import java.util.Date;

public class Tarefa {
    String descricao;
    Date dataCriacao;

    public Tarefa(String descricao) {
        this.descricao = descricao;
        this.dataCriacao = new Date(); // Data de criação é a data atual
    }

    @Override
    public String toString() {
        return descricao + " (Criada em: " + dataCriacao + ")";
    }
}
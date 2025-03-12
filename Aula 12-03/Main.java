// Main.java
public class Main {
    public static void main(String[] args) {
        GerenciadorTarefas gerenciador = new GerenciadorTarefas();

        // Adicionando tarefas
        gerenciador.adicionarTarefaRegular("Enviar relatório \n");
        gerenciador.adicionarTarefaPrioritaria("Resolver problema urgente \n");
        gerenciador.adicionarTarefaComPrioridade("Atualizar banco de dados \n", 5);
        gerenciador.adicionarTarefaComPrioridade("Corrigir erros de digitação \n", 2);
        gerenciador.adicionarTarefaRegular("Preparar apresentação \n");
        gerenciador.adicionarTarefaRegular("Preparar Café \n");

        // Processando tarefas
        gerenciador.processarProximaTarefa(); // Deve processar "Resolver problema urgente"
        gerenciador.processarProximaTarefa(); // Deve processar "Corrigir erros de digitação"
        gerenciador.processarProximaTarefa(); // Deve processar "Atualizar banco de dados"
        gerenciador.processarProximaTarefa(); // Deve processar "Enviar relatório"

        // Exibindo tarefas pendentes
        gerenciador.exibirTarefasPendentes(); // Deve exibir "Preparar apresentação" e "Preparar Café"
    }
}
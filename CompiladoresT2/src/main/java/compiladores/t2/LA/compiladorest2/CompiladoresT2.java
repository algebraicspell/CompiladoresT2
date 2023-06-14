package compiladores.t2.LA.compiladorest2;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class CompiladoresT2 {

    public static void main(String[] args) {
        String arquivoSaida = args[1];
        
        try {
            PrintWriter pw = new PrintWriter(arquivoSaida);
            try {
                CharStream cs = CharStreams.fromFileName(args[0]);
                LALexer linguagemAlg = new LALexer(cs);
                
                Token tolkien = null;
                while ((tolkien = linguagemAlg.nextToken()).getType() != Token.EOF) {
                    /* Mensagens de Erro referentes a análise léxica */
                    
                    if ("Erro".equals(LALexer.VOCABULARY.getDisplayName(tolkien.getType()))) {
                        pw.println("Linha " + tolkien.getLine() + ": " + tolkien.getText() + " - simbolo nao identificado");
                        break;
                    } else if ("ComentarioNaoFechado".equals(LALexer.VOCABULARY.getDisplayName(tolkien.getType()))) {
                        pw.println("Linha " + tolkien.getLine() + ": comentario nao fechado");
                        break;
                    } else if ("CadeiaLiteralNaoFechada".equals(LALexer.VOCABULARY.getDisplayName(tolkien.getType()))) {
                        pw.println("Linha " + tolkien.getLine() + ": cadeia literal nao fechada");
                        break;
                    } else if ("FechamentoComentarioIsolado".equals(LALexer.VOCABULARY.getDisplayName(tolkien.getType()))) {
                        pw.println("Linha " + tolkien.getLine() + ": " + tolkien.getText() + " - simbolo nao identificado");
                        break;
                    } else {
                        /* pw.println("<\'" + tolkien.getText() + "\'," + LALexer.VOCABULARY.getDisplayName(tolkien.getType()) + ">"); */
                    }
                }
                
                CommonTokenStream tolkiens = new CommonTokenStream(linguagemAlg);
                
                /* Retorna para o início da lista de tokens */
                linguagemAlg.reset();
                LAParser parser = new LAParser(tolkiens);
                parser.removeErrorListeners();
                
                /* Instancia um objeto da classe criada para customizar as mensages de erro */
                MensagensDeErro msgErro = new MensagensDeErro(pw);
                parser.addErrorListener(msgErro);
                
                /* Inicia a análise sintática */
                parser.programa();
                
                /* Mensagem padrão que deve ser escrita ao final do arquivo de saída */
                pw.println("Fim da compilacao");

                pw.close();
            } catch (IOException ex) {
            }
        } catch(FileNotFoundException fnfe) {
        }
    }
}
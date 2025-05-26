package br.com.natcorpbr.batidaPonto.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerPonto {

    private static final String LOG_FILE = "logsBP/marcacao_ponto_log.txt";

    public static void registraPonto(String mensagem) throws IOException{
        try{
            File logFile = new File(LOG_FILE);

            // Garante que a pasta "logs" existe antes de criar o arquivo
            logFile.getParentFile().mkdirs();

            if (!logFile.exists()){
                logFile.createNewFile();
            }

            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write(timestamp + " _ " + mensagem);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

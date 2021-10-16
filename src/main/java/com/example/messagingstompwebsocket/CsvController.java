package com.example.messagingstompwebsocket;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CsvController {

    private static final String PATH = "D:\\icaro\\Codes\\messaging-stomp-websocket\\";

    @RequestMapping(value="/csv/{filename}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadCSV(@PathVariable String filename) throws Exception {

        File file = new File(filename);
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(inputStream);
        file.delete();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }


    @MessageMapping("/hello")
    @SendTo("/topic/request")
    public ResponseEntity<Observer> requestCsv(HelloMessage message) throws Exception {
        List<Receivable> receivableList = new ArrayList<>();

        Receivable r1 = new Receivable();
        r1.setHash("1234");
        r1.setValor(BigDecimal.ONE);

        Receivable r2 = new Receivable();
        r2.setHash("2356");
        r2.setValor(BigDecimal.TEN);

        receivableList.add(r1);
        receivableList.add(r2);

        String filename = message.getName() + ".csv";
        ByteArrayInputStream stream = tutorialsToCSV(receivableList);
        OutputStream out = new FileOutputStream(filename);
        byte[] buf = new byte[1024];
        int len;
        while ((len = stream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        stream.close();
        out.close();

        Observer obs = new Observer();
        obs.setFile(filename);

        return ResponseEntity.ok().body(obs);
    }

    public void createCsvFile(){

    }

    public static ByteArrayInputStream tutorialsToCSV(List<Receivable> receivables) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Receivable ur : receivables) {
                List<String> data = Arrays.asList(
                        ur.getHash(),
                        String.valueOf(ur.getValor())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

}

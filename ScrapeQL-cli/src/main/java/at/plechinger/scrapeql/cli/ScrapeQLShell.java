/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.plechinger.scrapeql.cli;

import at.plechinger.scrapeql.parser.QueryParser;
import at.plechinger.scrapeql.query.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.io.CharStreams;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 * @author lukas
 */
public class ScrapeQLShell {

    private QueryParser parser = new QueryParser();

    public ScrapeQLShell(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            Query query = parser.parse(CharStreams.toString(reader));

            Map<String, Object> output = query.execute();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            String outputString = mapper.writeValueAsString(output);

            System.out.println(outputString);

        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        ScrapeQLShell shell = new ScrapeQLShell(args);
    }
}

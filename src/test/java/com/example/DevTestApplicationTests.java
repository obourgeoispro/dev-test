package com.example;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevTestApplication.class)
public class DevTestApplicationTests {

    private static final Logger LOG = LoggerFactory.getLogger(DevTestApplicationTests.class);
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    Runner runner;
    RestTemplate restTemplate = new TestRestTemplate();


    @Test
    public void contextLoads() {
    }

    @Test
    public void testRequest() throws Exception {
        Collection<City> cities = runner.doRequest(Runner.ENDPOINT_URL + "San Diego");
        assertThat(cities.size(), greaterThan(1));
    }

    @Test
    public void testSerializationToCSV() throws Exception {
        Collection<City> cities = new ArrayList<>();
        String jsonPath = applicationContext.getResource("classpath:sample.json").getFile().getPath();
        final byte[] jsonData = Files.readAllBytes(Paths.get(jsonPath));
        JsonParser jsonParser = new JsonFactory().createParser(jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        MappingIterator iterator = objectMapper.readValues(jsonParser, City[].class);
        //The parser returns an Array
        while (iterator.hasNext()) {
            City[] it = (City[]) iterator.next();
            for (int i = 0; i < it.length; i++) {
                cities.add(it[i]);
            }
        }
        runner.exportToCSV(cities);
    }
}

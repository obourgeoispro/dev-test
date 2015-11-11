package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class Runner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    public static final String ENDPOINT_URL = "http://api.goeuro.com/api/v2/position/suggest/en/";

    @Override
    public void run(String... args) throws Exception {
        if (args != null && args.length == 1) {
            processCommand(args[0].trim());
        } else {
            help(args);
        }
    }

    protected void processCommand(String cityName) {
        assert !cityName.isEmpty();
        LOG.info("Getting suggestions for {}", cityName);
        Collection<City> cities = doRequest(ENDPOINT_URL + cityName);
        exportToCSV(cities);
    }

    protected void help(String... args) {
        if (args.length == 0) {
            LOG.warn("You must provide at least one argument for CITY_NAME");
        } else if (args.length > 1) {
            LOG.warn("Only one city name is allowed: use double quotes for multi-word cities");
        }
    }

    protected Collection<City> doRequest(String url) {
        //RestTemplate with DefaultResponseErrorHandler from Spring
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<Collection<City>> typeReference = new ParameterizedTypeReference<Collection<City>>() {
        };
        ResponseEntity<Collection<City>> citiesResponse = restTemplate.exchange(url, HttpMethod.GET, null, typeReference);
        return citiesResponse.getBody();
    }

    protected void exportToCSV(Collection<City> cities) {
        CsvMapper csvMapper = new CsvMapper();
        //Use Jackson CSV dataformat for export
        CsvSchema csvSchema = csvMapper.schemaFor(CityDTO.class).withHeader().withNullValue("NULL");
        //Transfer to DTO to get only 5 fields: Jackson mixins still exports empty columns even with @JsonIgnore
        Collection<CityDTO> cityDTOs = new ArrayList<>();
        for (City city : cities) {
            CityDTO cityDTO = new CityDTO(city.getId(), city.getName(), city.getType(),
                    city.getGeoPosition().getLatitude(), city.getGeoPosition().getLongitude());
            cityDTOs.add(cityDTO);
        }
        try {
            byte[] citiesAsCSV = csvMapper.writer(csvSchema).writeValueAsBytes(cityDTOs);
            //Export file to current directory, overwrite file if exists
            Path path = Paths.get("", "csv-export.csv");
            Files.write(path, citiesAsCSV);
            LOG.info("Exported CSV file as {}", path.toAbsolutePath());
        } catch (JsonProcessingException e) {
            LOG.error("Error processing JSON", e);
        } catch (IOException e) {
            LOG.error("Could not create/write CSV file", e);
        }
    }
}
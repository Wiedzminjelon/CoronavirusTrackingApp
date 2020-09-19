package com.wiedzminjelon.coronavirustrackerapp.services;

import com.wiedzminjelon.coronavirustrackerapp.models.LocationStats;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class CoronaVirusDataService {

    private String VIRUS_DATA_URL = generatedDailyUrl();
    private List<LocationStats> allStats = new ArrayList<>();

    public String generatedDailyUrl() {
        String datePattern = "MM-dd-yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        String urlDate = dateFormatter.format(LocalDate.now().minusDays(1));
        return "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + urlDate + ".csv";
    }

    @PostConstruct
    @Scheduled(cron = "* 1 * * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> stats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province_State"));
            locationStat.setCountry(record.get("Country_Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get("Confirmed")));
            stats.add(locationStat);
        }
        this.allStats = stats;
    }
}

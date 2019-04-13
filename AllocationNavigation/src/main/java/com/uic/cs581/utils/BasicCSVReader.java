package com.uic.cs581.utils;

import com.uic.cs581.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.uic.cs581.model.Resource.EXPIRATION_TIME_MILLIS;

@Slf4j
public class BasicCSVReader {
    private static final String CSV_FILE_PATH = "./src/main/resources/";
    private static int resourceCount = 1;

    private static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static List<Resource> getResourcesFromTestData(String fileName) throws IOException, ParseException {
        log.info("Working Directory = " +
                System.getProperty("user.dir"));
        List<Resource> resources = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH + fileName), StandardCharsets.UTF_8);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                log.info("Record No - " + csvRecord.getRecordNumber());
                log.info("---------------");
                log.info(csvRecord.toString());

                Resource r = Resource.builder()
                        .dropOffLat(csvRecord.get(10))
                        .dropOffLong(csvRecord.get(9))
                        .dropOffTimeInMillis(DateUtils.parseDate(csvRecord.get(2), TIME_FORMAT).getTime())
                        .expirationTimeLeftInMillis(EXPIRATION_TIME_MILLIS)
                        .resourceId(resourceCount++)
//                        .pickUpLat()
//                        .pickUpLong()
                        .pickupTimeInMillis(DateUtils.parseDate(csvRecord.get(1), TIME_FORMAT).getTime()).build();


                log.info(r.toString());
                log.info("---------------\n\n");

            }
            return null;
        }
    }
}
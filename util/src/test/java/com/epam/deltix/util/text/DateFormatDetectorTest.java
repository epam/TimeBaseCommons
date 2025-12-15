/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.epam.deltix.util.text;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class DateFormatDetectorTest {


    @Test
    public void getDateTimeFormatStringFor() throws IOException, URISyntaxException {
        BufferedReader formatReader = getSourceReader("com/epam/deltix/util/text/dateTimeFormat.txt");
        String line = formatReader.readLine();
        while (line != null) {
            String[] split = line.split(",");
            String date = split[0];
            String time = split[1];
            String datetime = date + time;

            String datePattern = split[2];
            String timePattern = split[3];
            String datetimePattern = datePattern + timePattern;

            assertEquals(datePattern, DateFormatDetector.getDateFormatStringFor(date));
            assertEquals(timePattern, DateFormatDetector.getTimeFormatStringFor(time));
            assertEquals(datetimePattern, DateFormatDetector.getDateTimeFormatStringFor(datetime));
            line = formatReader.readLine();
        }
    }

    @Test
    public void getDateTimeFormatStringForNegativeCases() {
        String[] invalidInputs = {
                "",
                "random text",
                "2023-11-14T22:13:20.123456Z", // micro mot supported
                "2023-11-14T22:13:20.1Z", // partial millis
                "22:13:20.12Z", // partial millis
                "22:13:20.12345678Z", // partial nanos
                "22:13:20.12345678", // partial nanos
                "22:13:20.1234567890", // more than nanos
                "2023-11-14 22:13:20.123456789123", // more than nanos
//                "2023-99-99",
//                "25:61:61",
//                "2023-11-14T25:00:00",
//                "2023-11-14T22:61:00",
//                "2023-11-14T22:13:61",
//                "11/31/2023", // 31 nov not exist
//                "2023/11/14T22:13:20.123456789Z_extra",
//                "2023-11-14T22:13:20.123456789_extra",
//                "20231114T221320_extra",
//                "2023-11-14T22:13:20AMPM",
        };
        for (String invalidInput : invalidInputs) {
            assertNull(DateFormatDetector.getDateFormatStringFor(invalidInput));
            assertNull(DateFormatDetector.getTimeFormatStringFor(invalidInput));
            assertNull(DateFormatDetector.getDateTimeFormatStringFor(invalidInput));
        }
    }

    private BufferedReader getSourceReader(String res) throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource(res);
        File file = new File(Objects.requireNonNull(resource).toURI());
        return Files.newBufferedReader(file.toPath());
    }
}
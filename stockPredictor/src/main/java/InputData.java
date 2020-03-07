import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class InputData {
    static Date startDate;
    final static Logger logger = Logger.getLogger(InputData.class);
    Map<Integer, Double> dowJonesClosingMap = new HashMap<Integer, Double>();

    public InputData(String filenameDowData) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader( filenameDowData));
            int dateDifference = -1;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                String dateOfStock=data[0];
                try {
                    int prevDateDifference = dateDifference;
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(dateOfStock);
                    if (startDate == null) {
                        startDate = new Date(date1.getTime()-DateUtil.DAY_MILLISECONDS/12);
                        logger.debug("start date of dow - " + startDate.toString());

                    }
                    dateDifference = (int) ((date1.getTime()-startDate.getTime()) / (1000.0 * 60 * 60 *24));
                    dowJonesClosingMap.put(dateDifference, Double.valueOf(data[4]));
                    logger.trace("added day " + dateDifference + " for " + date1.toString() + ", "
                            + dowJonesClosingMap.get(dateDifference)
                            + " to map");
                    int dateRateOfChange = dateDifference - prevDateDifference;
                    if ((dateRateOfChange > 4) || (dateRateOfChange == 2) || dateRateOfChange < 1) {
                        logger.warn("unusual date difference gap of " + dateRateOfChange);
                    }
                }
                 catch (ParseException ex) {
                    continue;
                }


            }
            linearizeGaps(dowJonesClosingMap);

        } catch (FileNotFoundException ex) {
            logger.error("File " +  filenameDowData + " not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<Integer, Double> getDowJonesClosingMap() {
        return dowJonesClosingMap;
    }

    private void linearizeGaps(Map<Integer, Double> map) {
        int lastKnownKey = 0;
        for (int i=1; i<Collections.max(map.keySet()); i++) {
            if (map.containsKey(i)) {
                lastKnownKey = i;
                continue;
            }
            int j=0;
            while (!map.containsKey(i+j)) {
                j++;
            }
            int daysBetween = j+1;
            double slope = (map.get(i+j) - map.get(lastKnownKey))/daysBetween;
            map.put(i, slope*(i-lastKnownKey)+map.get(lastKnownKey));
            logger.trace("added day " + i + " with value " + map.get(i+j));
            lastKnownKey = i;
        }
    }
}

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class InputData {
    static Date startDate;
    final static Logger logger = Logger.getLogger(InputData.class);
    Map<Integer, Double> dowJonesClosingMap = new HashMap<Integer, Double>();

    public InputData(String filename) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1985, 1, 29);
        startDate = calendar.getTime();


        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                String dateOfStock=data[0];
                try {
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(dateOfStock);
                    int dateDifference = (int) ((date1.getTime()-startDate.getTime()) / (1000.0 * 60 * 60 *24));
                    dowJonesClosingMap.put(dateDifference, Double.valueOf(data[4]));
                }
                 catch (ParseException ex) {
                    continue;
                }

                System.out.println("Data [date= " + data[0] + " , close=" + data[4] + "]");

            }

        } catch (FileNotFoundException ex) {
            logger.error("File " + filename + " not found: " + ex.getMessage());
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

}

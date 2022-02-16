package ExcelHash;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DataDriven {

    public ArrayList<String> getData(String testCase) throws IOException {
        ArrayList<String> array = new ArrayList<>();

        FileInputStream file = new FileInputStream("Book1.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet("testdata");

        Iterator<Row> rows = sheet.iterator();//Takes all rows data
        Row firstRow = rows.next(); //Takes first row

        Iterator<Cell> cell = firstRow.cellIterator();//Takes all cell data
        int column = 0;
        while (cell.hasNext()) {
            Cell cellValue = cell.next();//Takes first cell of first row
            if (cellValue.getStringCellValue().equalsIgnoreCase("Testcases")) {
                break;
            }
            column++;
        }

        //Form 1
        /*int rowCount = sheet.getPhysicalNumberOfRows();
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
        String caseTitle;
        String cellValue;
        for (int i = 1; i < rowCount; i++) {
            caseTitle = sheet.getRow(i).getCell(column).getStringCellValue();
            if (caseTitle.equalsIgnoreCase("Purchase")) {
                for (int j = 1; j < columnCount; j++) {
                    cellValue = sheet.getRow(i).getCell(j).getStringCellValue();
                    System.out.println(cellValue);
                }
            }
        }*/

        //Form 2
        while (rows.hasNext()){
            Row newRow = rows.next();
            if(newRow.getCell(column).getStringCellValue().equalsIgnoreCase(testCase)){
                Iterator<Cell> dataInRow = newRow.cellIterator(); //Takes all row data
                while (dataInRow.hasNext()){
                    Cell checkCell = dataInRow.next();
                    if(checkCell.getCellType()== CellType.STRING){
                        array.add(checkCell.getStringCellValue());
                    }else{
                        array.add(NumberToTextConverter.toText(checkCell.getNumericCellValue()));
                    }
                }
            }
        }

        return array;
    }
}

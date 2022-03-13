package com.example.TelegramBot;

import com.example.TelegramBot.Model.ColumnReceiptDTO;
import com.example.TelegramBot.Model.ReceiptDTO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelWorker {

    public static void CreateReceipt(ReceiptDTO receiptDTO) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // создание листа с названием "Просто лист"
        HSSFSheet sheet = workbook.createSheet("Чек");

        Row rowMain = sheet.createRow(1);
        rowMain.createCell(0).setCellValue("Чек от "+ receiptDTO.getDateCreate());

        Row rowHead = sheet.createRow(2);
        rowHead.createCell(0).setCellValue("Название услуги");
        rowHead.createCell(1).setCellValue("Ед. изм");
        rowHead.createCell(2).setCellValue("Тариф");
        rowHead.createCell(3).setCellValue("Обьем");
        rowHead.createCell(4).setCellValue("Цена");
        rowHead.createCell(5).setCellValue("Перерасчет");
        rowHead.createCell(6).setCellValue("Поставщик услуг");

        List<Row> content = new ArrayList<>();
        for (int i=0;i<receiptDTO.getColumns().size();i++) {
            ColumnReceiptDTO column=receiptDTO.getColumns().get(i);
            Row row= sheet.createRow(3+i);
            row.createCell(0).setCellValue(column.getServiceDTO().getName());
            row.createCell(1).setCellValue(column.getServiceDTO().getUnit().getName());
            row.createCell(2).setCellValue(column.getServiceDTO().getRate());
            row.createCell(3).setCellValue(column.getVolume());
            row.createCell(4).setCellValue(column.getSum());
            row.createCell(5).setCellValue(column.getRecalculation());
            row.createCell(6).setCellValue(column.getProviderDTO().getName());
        }

        File file=new File("file.xls");
        file.setWritable(true);

        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
    }
}

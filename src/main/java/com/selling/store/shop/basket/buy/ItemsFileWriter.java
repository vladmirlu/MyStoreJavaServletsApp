package com.selling.store.shop.basket.buy;

import com.selling.store.entity.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * Service for managing and saving the buy related data
 */
public class ItemsFileWriter {

    /**
     * ItemsFileWriter logger.
     */
    private final Logger logger = Logger.getLogger(ItemsFileWriter.class);

    /**
     * Creates .csv-format file with real time marker, writes items data into this file
     * and sets the status-201 for this response or, sets response status 400, when FileNotFoundException is cached.
     * In case of another exceptions sets the 500 response status.
     *
     * @param items    list of the buy items
     * @param response http servlet response
     */
    void writeItemsAsCSV(List<Item> items, HttpServletResponse response) {
        logger.debug("Items to buy: " + items);
        try {
            String fileName = "order-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd; HH mm ss"));
            logger.debug("Start creating file: " + fileName);
            File file = new File("data/"
                    + fileName + "." + FilenameUtils.getExtension(fileName + ".csv"));
            logger.debug("Created file: " + file.getAbsolutePath());

            StringBuilder sb = new StringBuilder();
            items.forEach(item ->
                     sb.append('"').append(item.getName()).append('"').append(',')
                    .append('"').append(item.getCode()).append('"').append(',')
                    .append('"').append(item.getPrice()).append('"').append('\n'));
            logger.debug("Data to write into: " + sb.toString());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();
            response.setStatus(201);
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException cached: " + e.getMessage());
            response.setStatus(400);
            e.printStackTrace();
        } catch (Throwable th) {
            logger.error("Throwable cached: " + th.getMessage());
            response.setStatus(500);
            th.printStackTrace();
        }
    }
}

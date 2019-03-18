package com.selling.store.shop;

import com.opencsv.CSVReader;
import com.selling.store.entity.Item;
import com.selling.store.exception.ApplicationErrorException;
import com.selling.store.exception.ItemNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Object for items existing validation
 */
public class ItemsValidatorProvider {

    public ItemsValidatorProvider(){
        initItems();
    }

    /**
     * ItemsValidatorProvider logger.
     */
    private final Logger logger = Logger.getLogger(ItemsValidatorProvider.class);

    /**
     * List of of the Item entity
     */
    private List<Item> items;

    /**
     * Creates and init entity for reading data from file of .csv-format
     *
     * @return new CSVReader initialised with the file source
     */
    private CSVReader initCSVReader() {
        try {
            logger.debug("Creating CSVReader for file data/items.csv");
            File dir = new File("data/");
            if (!dir.exists()) Files.createDirectory(Paths.get(dir.getPath()));
            File file = new File("data/items." + FilenameUtils.getExtension("items.csv"));
            if(!file.exists()) {
                Files.createFile(Paths.get(file.getPath()));
                logger.debug("Created file: " + file.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(generateTestItems().getBytes());
                fos.close();
                logger.debug("Items data saved into file: " + file.getAbsolutePath());
            }
            return new CSVReader(new FileReader("data/items.csv"));
        } catch (FileNotFoundException f) {
            logger.error("FileNotFoundException appears: " + f.getMessage());
            f.printStackTrace();
            throw new ItemNotFoundException("Error! Could not init file data/items.csv. \nFileNotFoundException caught: " + f.getMessage());
        } catch (IOException e) {
            logger.error("IOException appears: " + e.getMessage());
            e.printStackTrace();
            throw new ApplicationErrorException("Error! Could not init file data/items.csv. \nIOException caught: " + e.getMessage());
        }
    }

    /**
     * Generate item test collection
     *
     * @return item collection as string
     */
    private String generateTestItems() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            sb.append('"').append("Item").append(i).append('"').append(',')
                    .append('"').append(random.nextInt(1000)).append('"').append(',')
                    .append('"').append(random.nextInt(200)).append("$").append('"').append('\n');
        }
        logger.debug("Test data to display: " + sb.toString());
        return sb.toString();
    }

    /**
     * Extracts the existing items from the items.csv file and fill this.items list with extracted values
     */
    void initItems() {
        items = new ArrayList<>();
        try {
            String[] line;
            CSVReader reader = initCSVReader();
            while ((line = reader.readNext()) != null) {
                items.add(new Item(line[0], line[1], line[2]));
            }
            logger.debug("Initialized items: " + items);
            System.out.println("Items refreshed!");
        } catch (IOException e) {
            logger.error("initItems() method. IOException cached: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Validates if the items.csv file contains the item codes from list
     *
     * @param itemCodes the item codes list
     * @return nonexistent item codes
     */
    public List<String> getNonexistentItemCodes(List<String> itemCodes) {
        logger.debug("Items codes to check if csv file contains them: " + itemCodes);
        List<String> existing = new ArrayList<>();
        try {
            String[] line;
            CSVReader reader = initCSVReader();
            while ((line = reader.readNext()) != null) {
                existing.add(line[1]);
            }
            logger.debug("Extracted from scv file items codes: " + existing);
        } catch (IOException e) {
            logger.error("getNonexistentItemCodes() method. IOException cached: " + e.getMessage());
            e.printStackTrace();
        }
        return itemCodes.stream().filter(code -> !existing.contains(code)).collect(Collectors.toList());
    }

    /**
     * Extracts existing items from if the items.csv file by the item codes containing list
     *
     * @param itemCodes the item codes array
     * @param response  http servlet response
     * @return items list from the items.csv file or, if items list is empty, sets response status to 400 and goes throw ItemNotFoundException
     */
    public List<Item> extractExistingItems(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<String> itemCodes = Arrays.asList(request.getParameterValues("codes[]"));
            logger.debug("Items codes to check if csv file contains them: " + itemCodes);
            List<Item> items = new ArrayList<>();
            String[] line;
            CSVReader reader = initCSVReader();
            while ((line = reader.readNext()) != null) {
                if (itemCodes.contains(line[1])) {
                    items.add(new Item(line[0], line[1], line[2]));
                }
            }
            logger.debug("Existing extracted from scv file items: " + items);
            if (items.isEmpty()) {
                logger.warn("Item list is empty though it shouldn't be!");
                response.setStatus(400);
                throw new ItemNotFoundException("Items: " + itemCodes + " not found");
            }
            return items;
        } catch (NullPointerException n){
            logger.error("extractExistingItems() method. NullPointerException cached: " + n.getMessage());
            response.setStatus(400);
            n.printStackTrace();
            throw new ItemNotFoundException(n.getMessage());
        }
        catch (IOException e) {
            logger.error("extractExistingItems() method. IOException cached: " + e.getMessage());
            response.setStatus(500);
            e.printStackTrace();
            throw new ApplicationErrorException(e.getMessage());
        }
    }

    /**
     * Returns the existing items
     */
    List<Item> getItems() {
        return items;
    }
}

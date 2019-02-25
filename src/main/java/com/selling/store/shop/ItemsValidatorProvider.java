package com.selling.store.shop;

import com.opencsv.CSVReader;
import com.selling.store.entity.Item;
import com.selling.store.exception.ApplicationErrorException;
import com.selling.store.exception.ItemNotFoundException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Object for items existing validation
 */
public class ItemsValidatorProvider {

    /**
     * ItemsValidatorProvider logger.
     */
    private final Logger logger = Logger.getLogger(ItemsValidatorProvider.class);

    /**
     * List of of the Item entity
     */
    private List<Item> items;

    /**
     * Object to get data from the application properties file
     */
    private ResourceBundle resourceBundle;

    /**
     * Build new file provider and create resource bundle from the properties file
     */
    public ItemsValidatorProvider() {
        String resourcesPath = "src/main/resources/application.properties";
        try {
            this.resourceBundle = new PropertyResourceBundle(new FileInputStream(resourcesPath));
        } catch (MissingResourceException m) {
            m.printStackTrace();
            logger.error("Resource is missing: " + resourcesPath + "MissingResourceException occur: " + m.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException occur during reading resources from the file '" + resourcesPath + "'; "+ e.getMessage());
        }
    }

    /**
     * Creates and init entity for reading data from file of .csv-format
     *
     * @return new CSVReader initialised with the file source
     */
    private CSVReader initCSVReader() throws FileNotFoundException {
        logger.debug("Creating CSVReader for file " + resourceBundle.getString("itemsData"));
        return new CSVReader(new FileReader(resourceBundle.getString("itemsData")));
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

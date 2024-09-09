package com.ciandt.vastiane.googlesheetsintegration.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {
    private static Sheets sheetsService;
    private static final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    // Constructor to initialize Google Sheets Service
    public GoogleSheetsService() throws GeneralSecurityException, IOException {
        sheetsService = getSheetsService();
    }

    // Initializes the Google Sheets API client
    private Sheets getSheetsService() throws GeneralSecurityException, IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Google Sheets Integration")
                .build();
    }

    // Method to read data from Google Sheets
    public List<List<Object>> readData(String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        return response.getValues();
    }

    // Method to append data to Google Sheets
    public void appendData(String range, List<List<Object>> values) throws IOException {
        // Create the body of the request with the values
        ValueRange body = new ValueRange().setValues(values);

        // Append data to the next available row in Google Sheets
        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    // Update data in a specific row (if needed for other methods)
    public void updateData(String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);

        sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    // Method to delete a product from Google Sheets by clearing the row
    public void deleteProductFromSheet(String productId) throws IOException {
        List<List<Object>> values = readData("Products!A:D");

        int rowIndex = -1;
        for (int i = 0; i < values.size(); i++) {
            List<Object> row = values.get(i);
            if (!row.isEmpty() && row.get(0).toString().equals(productId)) {
                rowIndex = i + 1;  // Row index in Google Sheets (1-based)
                break;
            }
        }

        if (rowIndex != -1) {
            List<List<Object>> emptyRow = Collections.singletonList(Collections.nCopies(4, ""));  // Empty values
            String deleteRange = "Products!A" + rowIndex + ":D" + rowIndex;
            updateData(deleteRange, emptyRow);  // Clear the row by replacing with empty data
        } else {
            throw new RuntimeException("Product not found in Google Sheets.");
        }
    }
}


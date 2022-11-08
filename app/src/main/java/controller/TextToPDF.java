package controller;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.ocrextracttext.MainActivity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TextToPDF {

    static MainActivity context;

    public static File ConfigPDF(String textInput) {

        Document document = new Document();

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmm");

        try {
            File pdfFile =new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS),
                    "AndroPDF_" + dateFormat.format(Calendar.getInstance().getTime()) + ".pdf"
            );
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            System.out.println();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Bitmap display the first page review of PDF
            // Open the document
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("AndroPDF / HawkStar");
            document.addCreator("standard lord.");

            Paragraph p = new Paragraph(textInput);
            document.add(p);
            return pdfFile;
//            Toast.makeText(context.getApplicationContext(),
//                    "New PDF named AndroPDF" +
//                            dateFormat.format(Calendar.getInstance().getTime()) +
//                            ".pdf successfully generated at DOWNLOADS folder", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException fe) {
            Log.e("PDFCreator", "FileNotFoundException:" + fe);
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException" + de);
        } finally {
            document.close();
        }
        return null;
    }


    public static void addMetaData(Document document) {
        document.addTitle("App screen title");
    }

    public static void addTitlePage(Document document) {
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD | Font.UNDERLINE, BaseColor.GRAY);
        Font smallFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        // Start New Paragraph
        Paragraph prHead = new Paragraph();

        // Set Font in this paragraph
        prHead.setFont(titleFont);

        // Add item into Paragraph
        prHead.add("RESUME - Name\n");

        document.newPage();
    }

}

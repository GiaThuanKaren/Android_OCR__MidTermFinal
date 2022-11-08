package com.example.ocrextracttext;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import controller.Api;
import controller.RetrofitClient;
import model.PDF;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPDFActivity extends AppCompatActivity implements OnPdfSelectListener{

    private MainAdapter adapter;
    private List<File> pdfList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpdf);

        sendNetworkRequest();

        runtimePermission();

//        //Creating a File object for directory
//        File directoryPath = new File(Environment.getExternalStorageDirectory() + "/Download");
//        //List of all files and directories
//        File[] filesList;
//        filesList = directoryPath.listFiles();
//        System.out.println("List of files and directories in the specified directory:");
//        for(File file : filesList) {
//            System.out.println("File name: "+file.getName());
//            System.out.println("File path: "+file.getAbsolutePath());
//            System.out.println("Size :"+file.getTotalSpace());
//            System.out.println(" ");
//        }
    }

    private void sendNetworkRequest() {
        Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);
        Call<PDF> call = apiInterface.getFolder("1PHWEg2sUEaSDWx4QUWEsTjzojP3E0KL1");

        call.enqueue(new Callback<PDF>() {
            @Override
            public void onResponse(Call<PDF> call, Response<PDF> response) {
//                Toast.makeText(MainActivity.this, "Call API successfully " + response.body(), Toast.LENGTH_SHORT).show();
//                PDF.dataPDF[] data = response.body().getData();
//
//                for (PDF.dataPDF item : data) {
//                    Log.e("Response link", "" + item.getWebContentLink());
//                }
            }

            @Override
            public void onFailure(Call<PDF> call, Throwable t) {
                Toast.makeText(MainPDFActivity.this, "Call API failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Call api", t.getMessage());
            }
        });
    }

    private Bitmap pdfToBitmap(File pdfFile) {
//        File pdfFile = new File("https://drive.google.com/uc?id=1MyKegj-yOwFRJd74RVQzLcwygkr5xfaD&export=download");
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if(pageCount>0){
                PdfRenderer.Page page = renderer.openPage(0);
                int width = (int) (page.getWidth());
                int height = (int) (page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }


    private void runtimePermission(){
        Dexter.withContext(MainPDFActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayPDF();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    public ArrayList<File> findPdf(File fileObj){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=fileObj.listFiles();

        for(File singleFile: files){
            if(singleFile.isDirectory()&&!singleFile.isHidden()){
                findPdf(singleFile);
            }
            else{
                if(singleFile.getName().endsWith(".pdf")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList ;
    }



    public void displayPDF(){
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        ArrayList<File> pdfList=new ArrayList<>();
        File directoryPath = new File(Environment.getExternalStorageDirectory() + "/Download");
        File pdfFile = new File("https://drive.google.com/uc?id=1MyKegj-yOwFRJd74RVQzLcwygkr5xfaD&export=download");
        pdfList=(findPdf(directoryPath));
        pdfList.add(pdfFile);
        adapter=new MainAdapter(this,pdfList,this, pdfToBitmap(pdfFile));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onPdfSelected(File file) {
        startActivity(new Intent(MainPDFActivity.this,PdfActivity.class).putExtra("path",file.getAbsolutePath()));
    }
}
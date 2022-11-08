package com.example.ocrextracttext;

import static com.google.mlkit.vision.text.TextRecognizerOptionsInterface.LATIN;
import static com.google.mlkit.vision.text.TextRecognizerOptionsInterface.LATIN_AND_CHINESE;
import static com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import adapter.TextRecognitionAdapter;
import controller.Api;
import controller.RetrofitClient;
import controller.Services;
import controller.TextToPDF;
import model.PDF;
import model.TextRecognitionList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    private NotificationManagerCompat notificationManagerCompat;
    final int CAMERA_PIC_REQUEST = 1888;
    List<TextRecognitionList> textRecognitionListList = new ArrayList<>();
    TextRecognitionAdapter textAdapter;
    RecyclerView textRecycler;
    public Bitmap bitmap,bitmap2;
    private TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    public SimpleDateFormat dayFormat = new SimpleDateFormat("EE", Locale.US);
    public SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.US);
    public SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);
    Button fab;
    private String myText;
    private Uri imageURI=null;
    AlertDialog.Builder mydialog;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getRotationCompensation(String cameraId, Activity activity, boolean isFrontFacing)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // Get the device's sensor orientation.
        CameraManager cameraManager = (CameraManager) activity.getSystemService(CAMERA_SERVICE);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
        }
        return rotationCompensation;
    }

    public void openCamera(View view) throws IOException {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Sample Description");

        imageURI=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Intent to launch camera
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageURI);

//        startActivityForResult(intent, 100);
//        startActivity(intent);
        cameraActivityResultLauncher.launch(intent);


    }


    private ActivityResultLauncher<Intent> cameraActivityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // here we will receive the image,if taken from camera
            if(result.getResultCode()==Activity.RESULT_OK){

                // image is taken from camera
                // we already have the image in imageUri using function pickImageCamera()
                Log.d("HELLO","onActivityResult: imageUri"+imageURI);

            }
            else{
                // cancelled
                Log.d("HELLO","onActivityResult:  cancelled");
                Toast.makeText(MainActivity.this,"Cancelled......",Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Bit Map Image" + resultCode);
//        Bitmap a = (Bitmap) data.getExtras().get("data");

//        imageURI= (Uri) data.getExtras().get("output");
//        Uri a = getImageUri(this,image);
//        Intent data1 = data;
//        imageURI = data1.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            recognizeTextFormImage();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (resultCode == 100) {

//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            System.out.println("Bit Map Image");
//            getImageUri(,image);
//            recognizeTextFormImage(image);

//            ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
//            imageview.setImageBitmap(image);

        }
    }
    private void DeleteKeyPreference(){

        SharedPreferences sharedPreferences = getSharedPreferences("trang_thai",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("key_user",null);
        editor.commit();
    }
    private void recognizeTextFormImage() throws IOException, CameraAccessException {
        System.out.println("Start Recoginize");
        int degree = getRotationCompensation("1",this,true);
//        InputImage inputImage = InputImage.fromFilePath(this,imageURI);

        InputImage image = InputImage.fromBitmap(bitmap, degree);

        Task<Text> result =textRecognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text visionText) {

                String resultText = visionText.getText();
                myText=resultText;

                AlertDialog.Builder mydialog=new AlertDialog.Builder(MainActivity.this);
                mydialog.setTitle("Text Recognize");
                EditText recognigeText=new EditText(MainActivity.this);
                recognigeText.setText(resultText);
                mydialog.setView(recognigeText);

                mydialog.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Resutl",resultText);
                        clipboard.setPrimaryClip(clip);
                    }
                });

                mydialog.show();
                for (Text.TextBlock block : visionText.getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCornerPoints = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for (Text.Line line : block.getLines()) {
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            Point[] elementCornerPoints = element.getCornerPoints();
                            Rect elementFrame = element.getBoundingBox();
                            for (Text.Symbol symbol : element.getSymbols()) {
                                String symbolText = symbol.getText();
                                Point[] symbolCornerPoints = symbol.getCornerPoints();
                                Rect symbolFrame = symbol.getBoundingBox();
                            }
                        }
                    }
                }
                Log.d("Result",resultText);
                if(!resultText.equals("")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("trang_thai", MODE_PRIVATE);
                    String folderid=sharedPreferences.getString("key_user", "");
                    File resut =TextToPDF.ConfigPDF(resultText);
                    boolean flag=Services.uploadFile(resut.getPath(),folderid);
                    if(flag) {
                            Toast.makeText(MainActivity.this,"Upload File Thanh Cong",Toast.LENGTH_SHORT).show();
                            PushNofitication("Upload File","Upload File Thanh Cong");
    //                        finish();
    //                        overridePendingTransition(0, 0);
    //                        startActivity(getIntent());
    //                        overridePendingTransition(0, 0);
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel1")
//                                .setSmallIcon(R.drawable.icon_pdf)
//                                .setContentTitle("My notification")
//                                .setContentText("Much longer text that cannot fit one line...")
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText("Much longer text that cannot fit one line..."))
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    }
                    else {
                        PushNofitication("Upload File","Upload File That Bai");
                        Toast.makeText(MainActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(MainActivity.this,"Can;t not detect , Please Try Again",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("Erro","onFailure: ",e);
                Toast.makeText(MainActivity.this,"Failed recognizing text due to  "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
//        String a="ljfalsjfklsjflkaj";
//
//
//        Log.d("Final",a);
//        System.out.println("fjalkfjklsaddjflasjfkl"+result.getResult().getTextBlocks());

    }

    private void sendNetworkRequest() {
        System.out.println("Response Call:");
        Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);
        SharedPreferences sharedPreferences = getSharedPreferences("trang_thai", MODE_PRIVATE);
        if (sharedPreferences.getString("folder_id", "") == "") {
            Toast.makeText(MainActivity.this,"No folderId.Plese try again later",Toast.LENGTH_SHORT).show();

        } else {
//            String folderId ="1grRCm1StMTWe34yN83bUu82MuTW-AUOT";
            String folderId= sharedPreferences.getString("folder_id","");
            System.out.println("folderId:"+folderId);
            Call<PDF> call = apiInterface.getFolder(folderId);

            call.enqueue(new Callback<PDF>() {

                @Override
                public void onResponse(Call<PDF> call, Response<PDF> response) {
//                Toast.makeText(MainActivity.this, "Call API successfully " + response.body(), Toast.LENGTH_SHORT).show();
                    System.out.println("Response 1:");
//                    System.out.println(response.body());
                    if(response.body()!=null){

                        System.out.println(response.body().text);
                        PDF.dataPDF[] arr = response.body().data;
                        System.out.println("Kết quả respone Yes");
                        for(int i =0;i<arr.length;i++){
                            System.out.println(arr[i].name);
                            TextRecognitionList text = new TextRecognitionList();
                            text.set_header(arr[i].name);
                            text.set_description("This is description for" +arr[i].name);
                            text.set_day(dayFormat.format(Calendar.getInstance().getTime()));
                            text.set_date(dateFormat.format(Calendar.getInstance().getTime()));
                            text.set_month(monthFormat.format(Calendar.getInstance().getTime()));
                            textRecognitionListList.add(text);
                        }
                        TextRecognitionList text5 = new TextRecognitionList();
                        text5.set_header("This is header");
                        text5.set_description("This is description");
                        text5.set_day(dayFormat.format(Calendar.getInstance().getTime()));
                        text5.set_date(dateFormat.format(Calendar.getInstance().getTime()));
                        text5.set_month(monthFormat.format(Calendar.getInstance().getTime()));
                        textRecognitionListList.add(text5);
//                        System.out.println(response.body());

                    }
//                    PDF.dataPDF[] data = response.body().data;
//
//                    for (int i=0;i<data.length;i++) {
//                        System.out.println("Response data" + "Hello");
//                    }
                }

                @Override
                public void onFailure(Call<PDF> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Call API failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Call api", t.getMessage());
                }
            });
        }

    }






    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, MainActivity1.class);
                Toast.makeText(this, "Logout successfull", Toast.LENGTH_LONG).show();
                System.out.println("Logout successfull");
                DeleteKeyPreference();
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void PushNofitication(String title,String msg){
        Notification notification = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.icon_pdf)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        int notificationId = 1;
        this.notificationManagerCompat.notify(notificationId, notification);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.notificationManagerCompat = NotificationManagerCompat.from(this);

        textRecycler = findViewById(R.id.textRecycler);
        textAdapter = new TextRecognitionAdapter(this, textRecognitionListList);
        textRecycler.setLayoutManager(new LinearLayoutManager(this));
        textRecycler.setAdapter(textAdapter);
        mydialog=new AlertDialog.Builder(MainActivity.this);
        System.out.println("Response end:");
        Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);
        SharedPreferences sharedPreferences = getSharedPreferences("trang_thai", MODE_PRIVATE);
        String folderId ="1grRCm1StMTWe34yN83bUu82MuTW-AUOT";
        folderId= sharedPreferences.getString("folder_id","");
        System.out.println("folderId:"+folderId);
        Call<PDF> call = apiInterface.getFolder(folderId);

        call.enqueue(new Callback<PDF>() {

            @Override
            public void onResponse(Call<PDF> call, Response<PDF> response) {
//                Toast.makeText(MainActivity.this, "Call API successfully " + response.body(), Toast.LENGTH_SHORT).show();
                System.out.println("Response 1:");
//                    System.out.println(response.body());
                if(response.body()!=null){

                    System.out.println(response.body().text);
                    PDF.dataPDF[] arr = response.body().data;
                    System.out.println("Kết quả respone Yes");
                    for(int i =0;i<arr.length;i++){
                        System.out.println(arr[i].name);
                        TextRecognitionList text = new TextRecognitionList();
                        text.set_header(arr[i].name);
                        text.set_description("This is description for" +arr[i].name);
                        text.set_day(dayFormat.format(Calendar.getInstance().getTime()));
                        text.set_date(dateFormat.format(Calendar.getInstance().getTime()));
                        text.set_month(monthFormat.format(Calendar.getInstance().getTime()));
                        textRecognitionListList.add(text);
                    }
                    textAdapter = new TextRecognitionAdapter(MainActivity.this, textRecognitionListList);
                    textRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    textRecycler.setAdapter(textAdapter);


//                        System.out.println(response.body());

                }
//                    PDF.dataPDF[] data = response.body().data;
//
//                    for (int i=0;i<data.length;i++) {
//                        System.out.println("Response data" + "Hello");
//                    }
            }

            @Override
            public void onFailure(Call<PDF> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Call API failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Call api", t.getMessage());
            }
        });

    }
}
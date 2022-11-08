package controller;

import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Services {
    private static String URL ="https://iot-service-1.vercel.app/upload";

    public static boolean uploadFile(String path,String folderid){
        System.out.println("Upload File Len Server"+folderid+" " + path);
        File file=new File(path);

        try{
            RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("tenfile",file.getName(),RequestBody.create(MediaType.parse("application/pdf"),file))
                    .addFormDataPart("key",folderid)
                    .addFormDataPart("submit","submit")
                    .build();

            Request request=new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println(response.toString());
                }
            });
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

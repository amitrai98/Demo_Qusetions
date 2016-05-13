package android.com.demo_qusetions;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static JSONObject jsonObj;
    private static boolean LOCAL_SOLAR_READY = false;
    ProgressDialog pd = null;
    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnClickMe = null;

    private static final String FILE_NAME = "data.txt";
    private static final String DUBLICATE_FILE_NAME = "duplicate.txt";
    private static final String FOLDER_NAME = "QuestionDemo";

    private List<String> list_gcm_ids = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new DataDownloader().execute();

//        boolean networkState =  hasConnection(this);

        initView();
//        Log.e(TAG, ""+networkState);
    }


    private void initView(){
        btnClickMe = (Button) findViewById(R.id.btnClickMe);
        btnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJson();
            }
        });
    }

    class DataDownloader extends AsyncTask{

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Downloading data");
            pd.setMessage("Please Wait");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            pd.show();

            try{
                for (int i = 0; i<1000 ; i++){
                    Log.e(TAG, "this is a delay loop "+ i);
                }
            }
            catch(Exception ex)
            {
                Log.e(TAG, "failed");
            }
            pd.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }


    /**
     * checks internet status on device
     * @param context
     * @return
     */
    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }



    /**
     * get mapped String
     */
    public void getJson(){

        new Thread(new Runnable() {
            public boolean LOCAL_SOLAR_PROCESSING;

            @Override
            public void run() {
                try {
                    list_gcm_ids.clear();
                    LOCAL_SOLAR_PROCESSING = true;
                    File root = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
                    File yourFile = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME+ File.separator+FILE_NAME);
                    String json_string = "";
                    if(!root.exists()){
                        root.mkdirs();
                    }

                    if(!yourFile.exists()){
                        File myfile = new File(root, FILE_NAME);
                            FileWriter fstream = new FileWriter(myfile,true);
                            BufferedWriter fbw = new BufferedWriter(fstream);
                            fbw.close();
                            Log.i(TAG, "new voice comamnd mapped");
                            getJson();
                    }
                    try {
                        InputStream fis=new FileInputStream(yourFile);
                        BufferedReader br=new BufferedReader(new InputStreamReader(fis));

                        for (String line = br.readLine(); line != null; line = br.readLine()) {
//                            System.out.println(line);

                            json_string = json_string+line;
                        }

                        Log.e("value", "" + json_string);

                        br.close();
                    }
                    catch(Exception e){
                        System.err.println("Error: Target File Cannot Be Read");
                    }

                    FileInputStream stream = new FileInputStream(yourFile);
                    String jsonStr = null;
                    try {
                        FileChannel fc = stream.getChannel();
                        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                        jsonStr = Charset.defaultCharset().decode(bb).toString();
                        if (jsonStr.length() > 0 && jsonStr.charAt(jsonStr.length()-2)==',') {
                            jsonStr = jsonStr.substring(0, jsonStr.length() - 2);
                        }
                        if (jsonStr != null)
                            jsonStr = jsonStr;
                        else
                            return ;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        stream.close();
                    }
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i=0 ; i<jsonArray.length(); i++){
                        jsonObj = jsonArray.getJSONObject(i);
                        if(jsonObj.has("GCMRegistrationID")){
                            if(!jsonObj.getString("GCMRegistrationID").equals("None")){
                                list_gcm_ids.add(jsonObj.getString("GCMRegistrationID"));
                            }
                        }
                    }

                    for (int i =0; i<list_gcm_ids.size(); i++ ){
                        for(int j = i ; j<list_gcm_ids.size() ; j++){
                            if(i!=j && list_gcm_ids.get(i).equals(list_gcm_ids.get(j))){
                                Log.e(TAG, "dublicate found for " + list_gcm_ids.get(i));
                                storeDuplicateGcm("Duplicate gcm",list_gcm_ids.get(i) );
                            }
                        }
                    }
                    Log.e(TAG, "processing done");
                    LOCAL_SOLAR_READY = true;

                } catch (Exception e) {
                    e.printStackTrace();
                    LOCAL_SOLAR_READY = false;
                }finally {
                    LOCAL_SOLAR_PROCESSING = false;
                }
            }
        }).start();

    }


    public void storeDuplicateGcm(String key, String value) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
            if (!root.exists()) {
                root.mkdirs();
            }else if(root.exists()){
                try{
                    // file exist
                    File myfile = new File(root, DUBLICATE_FILE_NAME);
                    if(myfile.exists()){
                        FileWriter fstream = new FileWriter(myfile,true);
                        BufferedWriter fbw = new BufferedWriter(fstream);
                        fbw.write("\t");
                        fbw.write("\""+key+"\""+": " + "\""+value+"\""+",");
                        fbw.newLine();
                        fbw.close();
                        Log.i(TAG, "new voice comamnd mapped");

                    }else {
                        // file not exists
                        File gpxfile = new File(root, DUBLICATE_FILE_NAME);
                        FileWriter fstream = new FileWriter(gpxfile,true);
                        BufferedWriter fbw = new BufferedWriter(fstream);
                        fbw.write("{");
                        fbw.newLine();
                        fbw.write("\t");
                        fbw.write("\"" + key + "\"" + ": " + "\"" + value + "\"" + ",");
                        fbw.newLine();
                        fbw.close();
                        Log.i(TAG, "new voice comamnd mapped");
                    }
                }catch (Exception e) {
                    Log.e(TAG, "error while adding voice command");
                }
            }else {

                File gpxfile = new File(root, DUBLICATE_FILE_NAME);
                FileWriter fstream = new FileWriter(gpxfile,true);
                BufferedWriter fbw = new BufferedWriter(fstream);
                fbw.write("\"" + key + "\"" + ": " + "\"" + value + "\"" + ",");
                fbw.newLine();
                fbw.close();
                Log.i(TAG, "new voice comamnd mapped");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package gi.hfhgit.callnotes;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vytautasdagilis on 19/11/2017.
 */

class RecordingUploader implements Runnable {
    public static final String UPLOAD_FILE_ENDPOINT = "http://localhost:8080/someEndpoint";
    private String recordUrl;

    public RecordingUploader(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public void upload() {

        uploadFile(recordUrl);
    }

    public int uploadFile(String sourceFileUri) {
        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        int serverResponseCode = 0;

        if (!sourceFile.exists()) {
            Log.e("RecordingUploader", "File does not exist : " + sourceFileUri);
            return 0;
        }
        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(UPLOAD_FILE_ENDPOINT);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            try {

                dos = new DataOutputStream(conn.getOutputStream());
            } catch (ConnectException exception) {
                Log.i("RecordingUploader", "URL: " + UPLOAD_FILE_ENDPOINT + " ConnectException: " + exception.toString());
                return 0;
            }

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            String contentDispositionHeader = "Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd;
            Log.i("RecordingUploader", "contentDispositionHeader: " + contentDispositionHeader);
            dos.writeBytes(contentDispositionHeader);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            String serverResponseMessage = conn.getResponseMessage();

            Log.i("RecordingUploader", "HTTP Response is : "
                    + serverResponseMessage + ": ");
            serverResponseCode = conn.getResponseCode();


            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e("RecordingUploader", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RecordingUploader", "Exception : "
                    + e.getMessage(), e);
        }
        return serverResponseCode;

    }

    @Override
    public void run() {
        upload();
    }
}
